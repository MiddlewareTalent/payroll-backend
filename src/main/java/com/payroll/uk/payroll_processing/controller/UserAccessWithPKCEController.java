package com.payroll.uk.payroll_processing.controller;

import com.payroll.uk.payroll_processing.hmrc_config.OAuthTokenAccess;
import com.payroll.uk.payroll_processing.hmrc_config.UserRestrictedAccessWithPKCE;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/oauth1.0")
public class UserAccessWithPKCEController {

    @Autowired
    private UserRestrictedAccessWithPKCE userRestrictedAccessWithPKCE;


    @GetMapping("/login/{scope}")
    public ResponseEntity<Void> loginWithPkce(@PathVariable String scope,
                                              HttpServletResponse response) {
        String sessionId = UUID.randomUUID().toString();
        Cookie cookie = new Cookie("SESSION_ID", sessionId);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);
        URI redirectUri = userRestrictedAccessWithPKCE.buildAuthorizationUri(scope, sessionId);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(redirectUri);
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }


    @GetMapping("/callback")
    public RedirectView handleCallback(@RequestParam String code,
                                       @CookieValue("SESSION_ID") String sessionId) {
        try {
            OAuthTokenAccess token = userRestrictedAccessWithPKCE.exchangeCodeForToken(code, sessionId);
            return new RedirectView("http://localhost:5173/success?token=true");
        } catch (Exception e) {
            e.printStackTrace();
            return new RedirectView("http://localhost:5173/error?token=false");
        }
    }
    @GetMapping("/logout")
    public ResponseEntity<String> logout(@RequestParam String scope, @CookieValue("SESSION_ID") String sessionId) {
        userRestrictedAccessWithPKCE.clearSession(scope, sessionId);
        return ResponseEntity.ok("Session cleared");
    }


}
