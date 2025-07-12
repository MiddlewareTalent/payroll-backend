package com.payroll.uk.payroll_processing.controller;

import com.payroll.uk.payroll_processing.hmrc_config.OAuthTokenAccess;
import com.payroll.uk.payroll_processing.hmrc_config.UserAccessWithFrontendService;
import com.payroll.uk.payroll_processing.hmrc_config.UserRestrictedAccessWithPKCE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/oauth")
public class UserAccessWithFrontendController {
    @Autowired
    private UserAccessWithFrontendService userAccessWithFrontendService;

    // Step 1: Redirect user to HMRC with PKCE (frontend provides sessionId)
    @GetMapping("/login/{scope}")
    public ResponseEntity<Void> loginWithPkce(@PathVariable String scope,
                                              @RequestParam(value = "sessionId") String sessionId) {
        URI redirectUri = userAccessWithFrontendService.generateAuthorizationUri(scope, sessionId);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(redirectUri);
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }

    // Step 2: Handle HMRC callback and exchange code for token using state (which holds sessionId)
    @GetMapping("/callback")
    public RedirectView handleCallback(@RequestParam("code") String code,
                                       @RequestParam("state") String sessionId) {
        try {
            OAuthTokenAccess token = userAccessWithFrontendService.executeCodeForToken(code, sessionId);
            return new RedirectView("http://localhost:5173/success?token=true");
        } catch (Exception e) {
            e.printStackTrace();
            return new RedirectView("http://localhost:5173/error?token=false");
        }
    }

    // Step 3: Logout by sessionId and scope
    @GetMapping("/logout")
    public ResponseEntity<String> logout(@RequestParam String sessionId) {
        userAccessWithFrontendService.clearSession(sessionId);
        return ResponseEntity.ok("Session cleared");
    }
    @GetMapping("/token-info")
    public ResponseEntity<OAuthTokenAccess> getTokenInfo(@RequestParam String scope,@RequestParam String sessionId) {
        OAuthTokenAccess token = userAccessWithFrontendService.getToken(scope,sessionId);
        if (token == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(token);
    }
    @GetMapping("/generate-session-id")
    public ResponseEntity<String> generateSessionId() {
        List<String> data=new ArrayList<>();
        if (data.isEmpty()) {
            String sessionId= UUID.randomUUID().toString();
            data.add(sessionId);
        }

        return ResponseEntity.ok(data.get(0));
    }
}
