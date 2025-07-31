package com.payroll.uk.payroll_processing;

import com.payroll.uk.payroll_processing.entity.TaxThreshold;
import com.payroll.uk.payroll_processing.service.incometax.TaxCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.math.BigDecimal;

@SpringBootApplication
@EnableWebMvc
@EnableScheduling
public class PayrollProcessingApplication   {

	public static void main(String[] args) {

		 SpringApplication.run(PayrollProcessingApplication.class, args);

	}

}
