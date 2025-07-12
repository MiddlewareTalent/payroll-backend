package com.payroll.uk.payroll_processing.controller;

import com.payroll.uk.payroll_processing.hmrc_config.OAuthTokenAccess;
import com.payroll.uk.payroll_processing.hmrc_config.UserAccessWithFrontendService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/individual/employee")
@AllArgsConstructor
public class IndividualEmployeeController {

   private final UserAccessWithFrontendService userAccessWithFrontendService;
    private final RestTemplate restTemplate;
    private static final String BASE_URL = "https://test-api.service.hmrc.gov.uk";


    @GetMapping("/individual-employment-details/{utr}/annual-summary/{taxYear}")
    public ResponseEntity<String> getEmployeeAnnualSummary(
            @PathVariable("utr") String utr,
            @PathVariable("taxYear") String taxYear, @RequestParam("scope") String scope,@RequestParam("sessionId") String sessionId) {
//         String scope = "read:individual-employment";
        OAuthTokenAccess token = userAccessWithFrontendService.getToken(scope,sessionId);
        if (token == null || token.getAccessToken() == null) {
            return ResponseEntity.status(500).body("Failed to retrieve access token");
        }
        String accessToken = token.getAccessToken();
        System.out.println("Access Token: " + accessToken);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/vnd.hmrc.1.2+json");
        headers.setBearerAuth(accessToken);

        HttpEntity<Void> entity = new HttpEntity<>(headers);


        String url = BASE_URL + "/individual-employment/sa/" + utr + "/annual-summary/" + taxYear;

        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                String.class
        );

        return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
    }


}
