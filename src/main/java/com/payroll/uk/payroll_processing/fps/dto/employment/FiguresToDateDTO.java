package com.payroll.uk.payroll_processing.fps.dto.employment;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class FiguresToDateDTO {
    private BigDecimal taxablePayTD;
    private BigDecimal totalTaxTD;
    private BigDecimal studentLoansTD;
    private BigDecimal postGradLoansTD;
    private BigDecimal BenefitsTaxedViaPayrollYTD;
    private BigDecimal employeePensionContributionsPaidYTD;
    private BigDecimal employeePensionContributionsNotPaidYTD;


}
