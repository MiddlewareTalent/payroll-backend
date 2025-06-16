package com.payroll.uk.payroll_processing.entity.employee;

import jakarta.persistence.*;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Optional;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class OtherEmployeeDetails {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;


    // Personal Allowance related fields
//    @PositiveOrZero(message = "previously used personal allowance must be zero or positive")
//    private BigDecimal previouslyUsedPersonalAllowance= BigDecimal.ZERO;
//    private BigDecimal totalPersonalAllowanceInCompany=new BigDecimal("12570.00"); // Default value for 2025/26 tax year
    @PositiveOrZero(message = "Used personal allowance must be zero or positive")
    private BigDecimal usedPersonalAllowance= BigDecimal.ZERO;
    @PositiveOrZero(message = "total Used Personal Allowance must be zero or positive")
    private BigDecimal totalUsedPersonalAllowance= BigDecimal.ZERO;
    @PositiveOrZero(message = "remaining Personal Allowance must be zero or positive")
    private BigDecimal remainingPersonalAllowance = BigDecimal.ZERO;

    // Income Tax related fields
    @PositiveOrZero(message = "Income tax paid must be zero or positive")
    private BigDecimal incomeTaxPaid;
    @PositiveOrZero(message = "Total income tax paid in company must be zero or positive")
    private BigDecimal totalIncomeTaxPaidInCompany= BigDecimal.ZERO;
    @PositiveOrZero(message = "number of months of income tax paid must be zero or positive")
    private BigDecimal numberOfMonthsOfIncomeTaxPaid= BigDecimal.ZERO;
    @PositiveOrZero(message = "number of years of income tax paid must be zero or positive")
    private BigDecimal numberOfYearsOfIncomeTaxPaid= BigDecimal.ZERO;
    @PositiveOrZero(message = "number of weeks of income tax paid must be zero or positive")
    private BigDecimal numberOfWeeksOfIncomeTaxPaid= BigDecimal.ZERO;

    // National Insurance related fields
    @PositiveOrZero(message = "total employee NI contribution in company must be zero or positive")
    private BigDecimal totalEmployeeNIContributionInCompany= BigDecimal.ZERO;
    @PositiveOrZero(message = "employee NI contribution must be zero or positive")
    private BigDecimal employeeNIContribution= BigDecimal.ZERO;
//    private int numberOfNIPaidYearsInCompany= 0;
//    private int totalNumberOfQualifyingYears= 0;
    @PositiveOrZero(message = "number of months of NI contributions must be zero or positive")
    private BigDecimal numberOfMonthsOfNIContributions= BigDecimal.ZERO;
    @PositiveOrZero(message = "number of Weeks of NI contributions must be zero or positive")
    private BigDecimal numberOfWeeksOfNIContributions= BigDecimal.ZERO;
    @PositiveOrZero(message = "number of Years of NI contributions must be zero or positive")
    private BigDecimal numberOfYearsOfNIContributions= BigDecimal.ZERO;





//    private BigDecimal calculateRemainingPersonalAllowance() {
//        BigDecimal total = Optional.ofNullable(new EmployeeDetails().getTotalPersonalAllowanceInCompany()).orElse(BigDecimal.ZERO);
//        BigDecimal used = Optional.ofNullable(new EmployeeDetails().getPreviouslyUsedPersonalAllowance()).orElse(BigDecimal.ZERO);
//        BigDecimal remaining = total.subtract(used);
//        return remaining.compareTo(BigDecimal.ZERO) < 0 ? BigDecimal.ZERO : remaining;
//    }



}
