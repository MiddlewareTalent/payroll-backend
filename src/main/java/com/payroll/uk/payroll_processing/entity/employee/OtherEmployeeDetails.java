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


    //Personal Allowance related fields
    @PositiveOrZero(message = "number Of PayPeriods Emergency TaxCode Used must be zero or positive")
    @Column(name = "number_of_pay_periods_emergency_tax_code_used")
    private BigDecimal numberOfPayPeriodsEmergencyTaxCodeUsed = BigDecimal.ZERO;

    @PositiveOrZero(message = "total Allowance Used During Emergency code must be zero or positive")
    @Column(name = "total_allowance_used_during_emergency_code")
    private BigDecimal totalAllowanceUsedDuringEmergencyCode = BigDecimal.ZERO;

    @PositiveOrZero(message = "Used personal allowance must be zero or positive")
    @Column(name = "used_personal_allowance")
    private BigDecimal usedPersonalAllowance = BigDecimal.ZERO;

    @PositiveOrZero(message = "total Used Personal Allowance must be zero or positive")
    @Column(name ="total_used_personal_allowance")
    private BigDecimal totalUsedPersonalAllowance = BigDecimal.ZERO;

    @PositiveOrZero(message = "remaining Personal Allowance must be zero or positive")
    @Column(name = "remaining_personal_allowance")
    private BigDecimal remainingPersonalAllowance = BigDecimal.ZERO;

    // Gross salary related fields
    @Column(name = "total_earnings_amount_ytd")
    @PositiveOrZero(message = "Total earnings amount YTD must be zero or positive")
    private BigDecimal totalEarningsAmountYTD = BigDecimal.ZERO;


    // Income Tax related fields
    @PositiveOrZero(message = "Income tax paid must be zero or positive")
    @Column(name ="income_tax_paid")
    private BigDecimal incomeTaxPaid = BigDecimal.ZERO;

    @PositiveOrZero(message = "Total income tax paid in company must be zero or positive")
    @Column(name = "total_income_tax_paid_in_company")
    private BigDecimal totalIncomeTaxPaidInCompany = BigDecimal.ZERO;

    @PositiveOrZero(message = "number of months of income tax paid must be zero or positive")
    @Column(name = "number_of_months_of_income_tax_paid")
    private BigDecimal numberOfMonthsOfIncomeTaxPaid = BigDecimal.ZERO;

    @PositiveOrZero(message = "number of years of income tax paid must be zero or positive")
    @Column(name = "number_of_years_of_income_tax_paid")
    private BigDecimal numberOfYearsOfIncomeTaxPaid = BigDecimal.ZERO;

    @PositiveOrZero(message = "number of weeks of income tax paid must be zero or positive")
    @Column(name = "number_of_weeks_of_income_tax_paid")
    private BigDecimal numberOfWeeksOfIncomeTaxPaid = BigDecimal.ZERO;

    // National Insurance related fields
    @PositiveOrZero(message = "total employee NI contribution in company must be zero or positive")
    @Column(name="total_employee_ni_contribution_in_company")
    private BigDecimal totalEmployeeNIContributionInCompany = BigDecimal.ZERO;

    @PositiveOrZero(message = "employee NI contribution must be zero or positive")
    @Column(name = "employee_ni_contribution")
    private BigDecimal employeeNIContribution = BigDecimal.ZERO;

    // Number of months, weeks, and years of NI contributions
    @PositiveOrZero(message = "number of months of NI contributions must be zero or positive")
    @Column(name = "number_of_months_of_ni_contributions")
    private BigDecimal numberOfMonthsOfNIContributions = BigDecimal.ZERO;

    @PositiveOrZero(message = "number of Weeks of NI contributions must be zero or positive")
    @Column(name = "number_of_weeks_of_ni_contributions")
    private BigDecimal numberOfWeeksOfNIContributions = BigDecimal.ZERO;

    @PositiveOrZero(message = "number of Years of NI contributions must be zero or positive")
    @Column(name = "number_of_years_of_ni_contributions")
    private BigDecimal numberOfYearsOfNIContributions = BigDecimal.ZERO;

    //Pension related fields
    @Column(name = "total_amount_pension_contribution")
    @PositiveOrZero
    private BigDecimal totalAmountPensionContribution = BigDecimal.ZERO;

    @Column(name = "number_of_pay_periods_pension_contribution")
    @PositiveOrZero
    private BigDecimal numberOfPayPeriodsPensionContribution = BigDecimal.ZERO;

    @Column(name = "pension_contribute_amount")
    private BigDecimal pensionContributeAmount= BigDecimal.ZERO;




    // Only called when the entity is first persisted
    @PrePersist
    public void setDefaultsIfNull() {
        if (numberOfPayPeriodsEmergencyTaxCodeUsed == null) numberOfPayPeriodsEmergencyTaxCodeUsed = BigDecimal.ZERO;
        if (totalAllowanceUsedDuringEmergencyCode == null) totalAllowanceUsedDuringEmergencyCode = BigDecimal.ZERO;
        if (usedPersonalAllowance == null) usedPersonalAllowance = BigDecimal.ZERO;
        if (totalUsedPersonalAllowance == null) totalUsedPersonalAllowance = BigDecimal.ZERO;
        if (remainingPersonalAllowance == null) remainingPersonalAllowance = BigDecimal.ZERO;
        if( totalEarningsAmountYTD == null) totalEarningsAmountYTD = BigDecimal.ZERO;

        if (incomeTaxPaid == null) incomeTaxPaid = BigDecimal.ZERO;
        if (totalIncomeTaxPaidInCompany == null) totalIncomeTaxPaidInCompany = BigDecimal.ZERO;
        if (numberOfMonthsOfIncomeTaxPaid == null) numberOfMonthsOfIncomeTaxPaid = BigDecimal.ZERO;
        if (numberOfYearsOfIncomeTaxPaid == null) numberOfYearsOfIncomeTaxPaid = BigDecimal.ZERO;
        if (numberOfWeeksOfIncomeTaxPaid == null) numberOfWeeksOfIncomeTaxPaid = BigDecimal.ZERO;

        if (totalEmployeeNIContributionInCompany == null) totalEmployeeNIContributionInCompany = BigDecimal.ZERO;
        if (employeeNIContribution == null) employeeNIContribution = BigDecimal.ZERO;
        if (numberOfMonthsOfNIContributions == null) numberOfMonthsOfNIContributions = BigDecimal.ZERO;
        if (numberOfWeeksOfNIContributions == null) numberOfWeeksOfNIContributions = BigDecimal.ZERO;
        if (numberOfYearsOfNIContributions == null) numberOfYearsOfNIContributions = BigDecimal.ZERO;

        if (totalAmountPensionContribution == null) totalAmountPensionContribution = BigDecimal.ZERO;
        if (numberOfPayPeriodsPensionContribution == null) numberOfPayPeriodsPensionContribution = BigDecimal.ZERO;
        if (pensionContributeAmount == null) pensionContributeAmount = BigDecimal.ZERO;
    }








}
