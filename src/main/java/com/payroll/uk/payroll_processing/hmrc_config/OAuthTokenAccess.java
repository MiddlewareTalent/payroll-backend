package com.payroll.uk.payroll_processing.hmrc_config;

import jakarta.persistence.Column;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class OAuthTokenAccess {
    private String accessToken;
    private String refreshToken;
    private String scope;
    private String tokenType;
    private Instant expiresAt;

    // --- Constructors ---
    public OAuthTokenAccess() {}

    public OAuthTokenAccess(String accessToken, String refreshToken, String scope, String tokenType, Instant expiresAt) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.scope = scope;
        this.tokenType = tokenType;
        this.expiresAt = expiresAt;
    }

    // --- Getters and Setters ---
    public String getAccessToken() { return accessToken; }
    public void setAccessToken(String accessToken) { this.accessToken = accessToken; }

    public String getRefreshToken() { return refreshToken; }
    public void setRefreshToken(String refreshToken) { this.refreshToken = refreshToken; }

    public String getScope() { return scope; }
    public void setScope(String scope) { this.scope = scope; }

    public String getTokenType() { return tokenType; }
    public void setTokenType(String tokenType) { this.tokenType = tokenType; }

    public Instant getExpiresAt() { return expiresAt; }
    public void setExpiresAt(Instant expiresAt) { this.expiresAt = expiresAt; }

    public boolean isExpired() {
        return Instant.now().isAfter(this.expiresAt);
    }
}


