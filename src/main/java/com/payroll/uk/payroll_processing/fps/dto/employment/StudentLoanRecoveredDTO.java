package com.payroll.uk.payroll_processing.fps.dto.employment;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class StudentLoanRecoveredDTO {
    private BigDecimal amount;
    private String planType;
}
