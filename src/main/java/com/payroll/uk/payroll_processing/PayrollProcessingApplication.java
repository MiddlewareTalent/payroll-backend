package com.payroll.uk.payroll_processing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@EnableWebMvc
@EnableScheduling
public class PayrollProcessingApplication {






	public static void main(String[] args) {

		 SpringApplication.run(PayrollProcessingApplication.class, args);

	}

//	public void run(String... args) throws RuntimeException{
//		BigDecimal amount = studentLoanCalculation.calculateStudentLoan(new BigDecimal("6000"),true, StudentLoan.StudentLoanPlan.STUDENT_LOAN_PLAN_2,"2025-2026","Monthly");
//		System.out.println("amount: "+amount);
//	}





}
