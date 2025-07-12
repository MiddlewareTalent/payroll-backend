package com.payroll.uk.payroll_processing.dto.employerdto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OtherEmployerDetailsDTO {
    private BigDecimal totalPaidGrossAmountYTD= BigDecimal.ZERO;
    private BigDecimal currentPayPeriodPaidGrossPay = BigDecimal.ZERO;
    //PAYE (income tax)
    private BigDecimal currentPayPeriodPAYE = BigDecimal.ZERO;
    private BigDecimal totalPAYEYTD;
    //Employee NI Contributions
    private BigDecimal currentPayPeriodEmployeesNI = BigDecimal.ZERO;
    private BigDecimal totalEmployeesNIYTD;
    //Employer NI Contributions
    private BigDecimal currentPayPeriodEmployersNI = BigDecimal.ZERO;
    private BigDecimal totalEmployersNIYTD;
   //Employer Pension Contributions
    private BigDecimal totalEmployerPensionContribution = BigDecimal.ZERO;
    private BigDecimal currentPayPeriodEmployerPensionContribution = BigDecimal.ZERO;
    //Employee Pension Contributions
    private BigDecimal totalEmployeePensionContribution = BigDecimal.ZERO;
    private BigDecimal currentPayPeriodEmployeePensionContribution = BigDecimal.ZERO;
}
