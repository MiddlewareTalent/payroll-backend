package com.payroll.uk.payroll_processing.hmrc_config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(HmrcAuthProperties.class)
public class HmrcConfig {
}