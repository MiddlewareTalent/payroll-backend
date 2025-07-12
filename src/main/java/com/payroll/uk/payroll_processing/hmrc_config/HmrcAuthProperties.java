package com.payroll.uk.payroll_processing.hmrc_config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "hmrc.auth")
public class HmrcAuthProperties {
    private String clientId;
    private String clientSecret;
    private String redirectUri;
    private String tokenUrl;
    private String authorizeUrl;
}
