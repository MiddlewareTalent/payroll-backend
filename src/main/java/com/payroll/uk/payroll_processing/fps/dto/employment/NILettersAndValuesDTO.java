package com.payroll.uk.payroll_processing.fps.dto.employment;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class NILettersAndValuesDTO {
    private String niLetter;
    private BigDecimal grossEarningsForNICsInPd;
    private BigDecimal grossEarningsForNICsYTD;
    private BigDecimal atLELYTD;
    private BigDecimal lelToPTYTD;
    private BigDecimal ptToUELYTD;
    private BigDecimal totalEmployerNICInPd;
    private BigDecimal totalEmployerNICYTD;
    private BigDecimal employeeNIContributionsInPd;
    private BigDecimal employeeNIContributionsYTD;
}
