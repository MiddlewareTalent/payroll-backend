package com.payroll.uk.payroll_processing.controller;


import com.payroll.uk.payroll_processing.hmrc_config.ApplicationRestrictedAccess;
import com.payroll.uk.payroll_processing.hmrc_config.OAuthTokenAccess;
import com.payroll.uk.payroll_processing.hmrc_config.UserAccessWithFrontendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api")

public class HelloWorldController {

    @Autowired
    private ApplicationRestrictedAccess applicationRestrictedAccess;
//    @Autowired
//    private UserRestrictedAccessWithPKCE userRestrictedAccessWithPKCE;

    @Autowired
    private UserAccessWithFrontendService userAccessWithFrontendService;


    private final  RestTemplate restTemplate = new RestTemplate();
    @Autowired
    private OAuthTokenAccess oAuthToken;



    private static final String BASE_URL = "https://test-api.service.hmrc.gov.uk";

    @GetMapping("/hello-world")
    public String helloWorld() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/vnd.hmrc.1.0+json");

        HttpEntity<Void> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(
                BASE_URL + "/hello/world",
                HttpMethod.GET,
                entity,
                String.class
        );

        return response.getBody();
    }

    @GetMapping("/hello-application")
    public String helloApplication() {
        String token = applicationRestrictedAccess.getAccessTokenData("hello");

        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/vnd.hmrc.1.0+json");
        headers.setBearerAuth(token);

        HttpEntity<Void> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(
                BASE_URL + "/hello/application",
                HttpMethod.GET,
                entity,
                String.class
        );

        return response.getBody();
    }


    @GetMapping("/hello-user")
    public ResponseEntity<String> helloUser(@RequestParam("scope")String scope,@RequestParam("sessionId") String sessionId) {
//        String scope = "hello";
        OAuthTokenAccess token = userAccessWithFrontendService.getToken(scope, sessionId);
        if (token == null || token.getAccessToken() == null) {
            return ResponseEntity.status(500).body("Failed to retrieve access token");
        }
        String accessToken = token.getAccessToken();

        System.out.println("Access Token: " + accessToken);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/vnd.hmrc.1.0+json");
        headers.setBearerAuth(accessToken);

        HttpEntity<Void> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(
                BASE_URL + "/hello/user",
                HttpMethod.GET,
                entity,
                String.class
        );

        return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
    }

}
