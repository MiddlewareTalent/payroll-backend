package com.payroll.uk.payroll_processing.hmrc_config;

import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
public class ApplicationRestrictedAccess {

    private final HmrcAuthProperties authProperties;

    private final RestTemplate restTemplate = new RestTemplate();
    private final Map<String, OAuthTokenAccess> tokenStore = new ConcurrentHashMap<>();

//    private final Map<String, OAuthTokenAccess> tokenCache = new ConcurrentHashMap<>();

    public String getAccessTokenData(String scope) {
        OAuthTokenAccess token = tokenStore.get(scope);
        if (token == null || token.isExpired()) {
            token = fetchNewToken(scope);
        }
        return token.getAccessToken();

    }


    private OAuthTokenAccess fetchNewToken(String scope) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "client_credentials");
        body.add("client_id", authProperties.getClientId());
        body.add("client_secret", authProperties.getClientSecret());
        body.add("scope", scope);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

        ResponseEntity<OAuthTokenAccess> response = restTemplate.postForEntity(authProperties.getTokenUrl(), request, OAuthTokenAccess.class);

        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
            throw new RuntimeException("Failed to get token: " + response);
        }


        OAuthTokenAccess storedData = toOAuthTokenAccess(response.getBody());
        tokenStore.put(storedData.getScope(), storedData);

      return storedData;
    }





    public OAuthTokenAccess toOAuthTokenAccess(OAuthTokenAccess tokenResponse) {
        String accessToken = tokenResponse.getAccessToken();
        String refreshToken = tokenResponse.getRefreshToken();
        String scope = tokenResponse.getScope();
        String tokenType = tokenResponse.getTokenType();
        Instant expiresIn = tokenResponse.getExpiresAt();
        Instant expiresAt = expiresIn != null ? expiresIn : Instant.now().plusSeconds(14400);

        return new OAuthTokenAccess(accessToken, refreshToken, scope, tokenType, expiresAt);
    }




}