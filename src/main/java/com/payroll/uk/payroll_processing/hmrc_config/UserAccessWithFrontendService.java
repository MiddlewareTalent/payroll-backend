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
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class UserAccessWithFrontendService {


    private final RestTemplate restTemplate;
    private final HmrcAuthProperties props;

    private final Map<String, String> codeVerifierStore = new ConcurrentHashMap<>();
    // Token store: sessionId → (scope → token)
    private final Map<String, Map<String, OAuthTokenAccess>> sessionTokenStore = new ConcurrentHashMap<>();

    @Autowired
    public UserAccessWithFrontendService(RestTemplate restTemplate, HmrcAuthProperties props) {
        this.restTemplate = restTemplate;
        this.props = props;
    }

    public URI generateAuthorizationUri(String scope, String sessionId) {
        String codeVerifier = PkceUtil.generateCodeVerifier();
        String codeChallenge = PkceUtil.generateCodeChallenge(codeVerifier);
        codeVerifierStore.put(sessionId, codeVerifier);

        return URI.create(props.getAuthorizeUrl()
                + "?response_type=code"
                + "&client_id=" + props.getClientId()
                + "&redirect_uri=" + props.getRedirectUri()
                + "&scope=" + UriUtils.encode(scope, StandardCharsets.UTF_8)
                + "&code_challenge=" + codeChallenge
                + "&code_challenge_method=S256"
                + "&state=" + sessionId);
    }

    public OAuthTokenAccess executeCodeForToken(String code, String sessionId) {
        String codeVerifier = codeVerifierStore.remove(sessionId);
        if (codeVerifier == null) throw new RuntimeException("Missing code_verifier");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("code", code);
        body.add("client_id", props.getClientId());
        body.add("client_secret", props.getClientSecret());
        body.add("redirect_uri", props.getRedirectUri());
        body.add("code_verifier", codeVerifier);

        ResponseEntity<Map> response = restTemplate.postForEntity(
                props.getTokenUrl(),
                new HttpEntity<>(body, headers),
                Map.class);

        Map<String, Object> data = response.getBody();
        if (data == null) {
            throw new RuntimeException("Token exchange failed: response body is null");
        }
        String accessToken = (String) data.get("access_token");
        String refreshToken = (String) data.getOrDefault("refresh_token", null);
        String tokenType = (String) data.get("token_type");
        String scope = (String) data.get("scope");
        int expiresIn = (int) data.get("expires_in");

        OAuthTokenAccess token = new OAuthTokenAccess(
                accessToken, refreshToken, scope, tokenType,
                Instant.now().plusSeconds(expiresIn));

        sessionTokenStore
                .computeIfAbsent(sessionId, k -> new ConcurrentHashMap<>())
                .put(scope, token);
        return token;
    }

    // Step 3: Get token when calling any endpoint
    public OAuthTokenAccess getToken( String scope,String sessionId) {
        if (scope==null|| sessionId==null) {
            throw new IllegalArgumentException("Scope and sessionId must not be null");
        }
        Map<String, OAuthTokenAccess> scopedTokens = sessionTokenStore.get(sessionId);
//        System.out.println("scopedTokens: " + scopedTokens);
//        System.out.println("scope: " + scope);
//        System.out.println("sessionId: " + sessionId);
//        System.out.println(scopedTokens.containsKey(scope));
        if (scopedTokens == null || !scopedTokens.containsKey(scope)) {
            throw new RuntimeException("Token missing for scope: " + scope);
        }

        OAuthTokenAccess token = scopedTokens.get(scope);
        if (token.isExpired()) {
            clearSession(sessionId);
            throw new RuntimeException("Token expired for scope: " + scope);
        }

        return token;
    }




    public String generatedAccessToken( String scope,String sessionId) {
        Map<String, OAuthTokenAccess> scopedTokens = sessionTokenStore.get(sessionId);
        if (scopedTokens == null || !scopedTokens.containsKey(scope)) {
            throw new RuntimeException("Token missing for scope: " + scope);
        }

        OAuthTokenAccess token = scopedTokens.get(scope);
        if (token.isExpired()) {
            throw new RuntimeException("Token expired for scope: " + scope);
        }

        return token.getAccessToken();
    }


    public void clearSession(String sessionId) {
        codeVerifierStore.remove(sessionId);
        sessionTokenStore.remove(sessionId);
    }

    public void clearSession(String scope, String sessionId) {
        sessionTokenStore.remove(sessionId);
//        codeVerifierStore.remove(sessionId);
        System.out.println("Cleared token and code_verifier for scope: " + scope + ", sessionId: " + sessionId);
    }

}
