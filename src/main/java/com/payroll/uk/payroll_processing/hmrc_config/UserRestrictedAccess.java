package com.payroll.uk.payroll_processing.hmrc_config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class UserRestrictedAccess {

    private final RestTemplate restTemplate;
    private final HmrcAuthProperties props;
    private final Map<String, OAuthTokenAccess> tokenStore = new ConcurrentHashMap<>();

    @Autowired
    public UserRestrictedAccess(RestTemplate restTemplate, HmrcAuthProperties props) {
        this.restTemplate = restTemplate;
        this.props = props;
    }

    public URI buildAuthorizationUri(String scope) {
        String url = props.getAuthorizeUrl()
                + "?response_type=code"
                + "&client_id=" + props.getClientId()
                + "&redirect_uri=" + props.getRedirectUri()
                + "&scope=" + scope;
        return URI.create(url);
    }

    public OAuthTokenAccess handleCallback(String code) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("code", code);
        body.add("client_id", props.getClientId());
        body.add("client_secret", props.getClientSecret());
        body.add("redirect_uri", props.getRedirectUri());

        ResponseEntity<Map> response = restTemplate.postForEntity(
                props.getTokenUrl(), new HttpEntity<>(body, headers), Map.class
        );

        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
            throw new RuntimeException("Token exchange failed: " + response);
        }

        Map<String, Object> bodyMap = response.getBody();
        String accessToken = (String) bodyMap.get("access_token");
        String refreshToken = (String) bodyMap.getOrDefault("refresh_token", null);
        String scope = (String) bodyMap.get("scope");
        String tokenType = (String) bodyMap.get("token_type");
        Integer expiresIn = (Integer) bodyMap.get("expires_in");

        Instant expiresAt = Instant.now().plusSeconds(expiresIn != null ? expiresIn : 3600);

        OAuthTokenAccess token = new OAuthTokenAccess(accessToken, refreshToken, scope, tokenType, expiresAt);
        tokenStore.put(scope, token);
        System.out.println("Token stored for scope: " + scope);
        return token;
    }

    public OAuthTokenAccess getTokenData(String scope) {
        System.out.println("Retrieving token for scope: " + scope);
        OAuthTokenAccess tokenData = tokenStore.get(scope);
        if (tokenData == null || tokenData.isExpired()) {
            throw new RuntimeException("Token is missing or expired for scope: " + scope);
        }
        return tokenData;
    }
}
