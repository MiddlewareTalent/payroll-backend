package com.payroll.uk.payroll_processing.controller;

import com.payroll.uk.payroll_processing.hmrc_config.ApplicationRestrictedAccess;
import com.payroll.uk.payroll_processing.hmrc_config.OAuthTokenAccess;
import com.payroll.uk.payroll_processing.hmrc_config.UserRestrictedAccess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.net.URI;

@RestController
@RequestMapping("/oauth2.0")
public class OAuthController {

    private final UserRestrictedAccess userRestrictedAccess;
    private final ApplicationRestrictedAccess applicationRestrictedAccess;
    private final OAuthTokenAccess oAuthToken;

    @Autowired
    public OAuthController(UserRestrictedAccess userRestrictedAccess, ApplicationRestrictedAccess applicationRestrictedAccess, OAuthTokenAccess oAuthToken) {
        this.userRestrictedAccess = userRestrictedAccess;
        this.applicationRestrictedAccess = applicationRestrictedAccess;
        this.oAuthToken = oAuthToken;
    }

    @GetMapping("/login/{scope}")
    public ResponseEntity<Void> login(@PathVariable String scope) {
        System.out.println("Request started: " + System.currentTimeMillis());
        System.out.println("Received scope: " + scope);

        URI redirectUri = userRestrictedAccess.buildAuthorizationUri(scope);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(redirectUri);
        System.out.println("successfully completed login method");
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }


    /*@GetMapping("/callback")
    public OAuthTokenAccess callback(@RequestParam String code) {
        System.out.println("Received code: " + code);
        OAuthTokenAccess tokenResponse = userRestrictedAccess.handleCallback(code); // exchanges code for token
        *//*if (tokenResponse == null || !tokenResponse.containsKey("access_token")) {
            return new RedirectView("http://localhost:5173/error"); // Redirect to an error page if token is not found
        }*//*
//        String accessToken = (String) tokenResponse.get("access_token");
        return tokenResponse;

//        String redirectUrl = "http://localhost:5173/access-token/" + tokenResponse;
//
//        return new RedirectView(redirectUrl);
    }*/

    @GetMapping("/callback")
    public RedirectView callback(@RequestParam String code) {
        try {
            OAuthTokenAccess tokenResponse = userRestrictedAccess.handleCallback(code);


            if (tokenResponse != null && tokenResponse.getAccessToken() != null) {

                // Redirect to frontend with success status
                String redirectUrl = "http://localhost:5173/success?token=true";
                return new RedirectView(redirectUrl);
            } else {
                return new RedirectView("http://localhost:5173/error?token=false");
            }

        } catch (Exception e) {
            e.printStackTrace();
            return new RedirectView("http://localhost:5173/error?token=false");
        }
    }


    @GetMapping("/token-status/check")
    public ResponseEntity<Boolean> checkTokenStatus(@RequestParam(required = false) String scope) {
        if (oAuthToken == null || oAuthToken.getExpiresAt() == null) {
            return ResponseEntity.ok(false); // token not available or invalid
        }

        boolean isValid = !oAuthToken.isExpired(); // returns true if still valid
        return ResponseEntity.ok(isValid);
    }

    @GetMapping("/token-data")
    public ResponseEntity<OAuthTokenAccess> getTokenData(@RequestParam(required = false) String scope) {
        OAuthTokenAccess tokenData =userRestrictedAccess.getTokenData(scope) ;
        if (tokenData == null || tokenData.isExpired()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        return ResponseEntity.ok(tokenData);
    }



    @GetMapping("/generate-token")
    public String generateToken(@RequestParam(required = false) String scope) {
        return applicationRestrictedAccess.getAccessTokenData(scope);
    }
}

