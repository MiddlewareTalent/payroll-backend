package com.payroll.uk.payroll_processing.hmrc_config;

import com.payroll.uk.payroll_processing.utils.PkceUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriUtils;

import java.net.URI;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class UserRestrictedAccessWithPKCE {

    private final RestTemplate restTemplate;
    private final HmrcAuthProperties props;

    // In-memory stores for demo; replace with DB/Redis in production
    private final Map<String, String> codeVerifierStore = new ConcurrentHashMap<>();
    private final Map<String, OAuthTokenAccess> tokenStore = new ConcurrentHashMap<>();

    @Autowired
    public UserRestrictedAccessWithPKCE(RestTemplate restTemplate, HmrcAuthProperties props) {
        this.restTemplate = restTemplate;
        this.props = props;
    }

    // Step 1: Build Authorization URL with PKCE
    public URI buildAuthorizationUri(String scope, String sessionId) {
        String codeVerifier = PkceUtil.generateCodeVerifier();
        String codeChallenge = PkceUtil.generateCodeChallenge(codeVerifier);
        codeVerifierStore.put(sessionId, codeVerifier);

        String url = props.getAuthorizeUrl()
                + "?response_type=code"
                + "&client_id=" + props.getClientId()
                + "&redirect_uri=" + props.getRedirectUri()
                + "&scope=" + UriUtils.encode(scope, "UTF-8")
                + "&code_challenge=" + codeChallenge
                + "&code_challenge_method=S256";

        return URI.create(url);
    }

    // Step 2: Exchange code using stored code_verifier
    public OAuthTokenAccess exchangeCodeForToken(String code, String sessionId) {
        String codeVerifier = codeVerifierStore.get(sessionId);
        if (codeVerifier == null) {
            throw new RuntimeException("Missing code_verifier for session: " + sessionId);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("code", code);
        body.add("client_id", props.getClientId());
        body.add("redirect_uri", props.getRedirectUri());
        body.add("client_secret", props.getClientSecret());
        body.add("code_verifier", codeVerifier);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(props.getTokenUrl(), request, Map.class);
        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
            throw new RuntimeException("Token exchange failed: " + response);
        }

        Map<String, Object> data = response.getBody();

        String accessToken = (String) data.get("access_token");
        String refreshToken = (String) data.getOrDefault("refresh_token", null);
        String tokenType = (String) data.get("token_type");
        String scope = (String) data.get("scope");
        Integer expiresIn = (Integer) data.get("expires_in");

        Instant expiresAt = Instant.now().plusSeconds(expiresIn != null ? expiresIn : 3600);
        OAuthTokenAccess token = new OAuthTokenAccess(accessToken, refreshToken, scope, tokenType, expiresAt);

        tokenStore.put(token.getScope(), token);
        System.out.println("Access Token: " + accessToken);
        System.out.println("Scope: " + scope);
        System.out.println(codeVerifierStore.remove(sessionId));
        String val = codeVerifierStore.remove(sessionId);
        System.out.println("Removed code_verifier for session: " + sessionId + ", value: " + val);

        return token;
    }

    // Step 3: Use stored access_token later
    public OAuthTokenAccess getToken(String scope) {
        OAuthTokenAccess token = tokenStore.get(scope);
        if (token == null || token.isExpired()) {
            throw new RuntimeException("Access token missing or expired. Re-authenticate.");
        }
        return token;
    }

    public void clearSession(String scope, String sessionId) {
        tokenStore.remove(scope);
        codeVerifierStore.remove(sessionId);
    }

}
