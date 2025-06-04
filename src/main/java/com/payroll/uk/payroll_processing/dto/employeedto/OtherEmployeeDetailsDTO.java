package com.payroll.uk.payroll_processing.dto.employeedto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OtherEmployeeDetailsDTO {
    // Personal Allowance related fields
    private BigDecimal previouslyUsedPersonalAllowance= BigDecimal.ZERO;
    private BigDecimal totalPersonalAllowanceInCompany=new BigDecimal("12570.00"); // Default value for 2025/26 tax year
    private BigDecimal usedPersonalAllowance= BigDecimal.ZERO;
    private BigDecimal totalUsedPersonalAllowance= BigDecimal.ZERO;
    private BigDecimal remainingPersonalAllowance= totalPersonalAllowanceInCompany;

    // Income Tax related fields
    private BigDecimal incomeTaxPaid;
    private BigDecimal totalIncomeTaxPaidInCompany= BigDecimal.ZERO;
    private BigDecimal numberOfMonthsOfIncomeTaxPaid= BigDecimal.ZERO;
    private BigDecimal numberOfYearsOfIncomeTaxPaid= BigDecimal.ZERO;
    private BigDecimal numberOfWeeksOfIncomeTaxPaid= BigDecimal.ZERO;

    // National Insurance related fields
    private BigDecimal totalEmployeeNIContributionInCompany= BigDecimal.ZERO;
    private BigDecimal employeeNIContribution= BigDecimal.ZERO;
    //    private int numberOfNIPaidYearsInCompany= 0;
//    private int totalNumberOfQualifyingYears= 0;
    private BigDecimal numberOfMonthsOfNIContributions= BigDecimal.ZERO;
    private BigDecimal numberOfWeeksOfNIContributions= BigDecimal.ZERO;
    private BigDecimal numberOfYearsOfNIContributions= BigDecimal.ZERO;
}
