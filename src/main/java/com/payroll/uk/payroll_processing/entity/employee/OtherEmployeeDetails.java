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

    @PositiveOrZero(message = "total taxable pay in this employment must be zero or positive")
    @Column(name = "total_taxable_pay_in_this_employment")
    private BigDecimal totalTaxablePayInThisEmployment=BigDecimal.ZERO;

    @PositiveOrZero(message = "earnings at LEL YTD must be zero or positive")
    @Column(name = "earnings_at_lel_ytd")
    private BigDecimal earningsAtLELYtd;    // AtLELYTD
    @PositiveOrZero(message = "earnings at LEL to PT YTD must be zero or positive")
    @Column(name = "earnings_lel_to_pt_ytd")
    private BigDecimal earningsLelToPtYtd;  // LELtoPTYTD
    @PositiveOrZero(message = "earnings at PT to UEL YTD must be zero or positive")
    @Column(name = "earnings_pt_to_uel_ytd")
    private BigDecimal earningsPtToUelYtd;  // PTtoUELYTD



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

    @Column(name = "total_earnings_amount_in_this_employment")
    @PositiveOrZero(message = "Total earnings amount in this employment must be zero or positive")
    private  BigDecimal totalEarningsAmountInThisEmployment= BigDecimal.ZERO;

    @Column(name = "total_tax_pay_to_date")
    @PositiveOrZero(message = "Total tax pay to date must be zero or positive")
    private BigDecimal totalTaxPayToDate = BigDecimal.ZERO;

    // Income Tax related fields
    @PositiveOrZero(message = "Income tax paid must be zero or positive")
    @Column(name ="income_tax_paid")
    private BigDecimal incomeTaxPaid = BigDecimal.ZERO;

    @PositiveOrZero(message = "Total income tax paid in company must be zero or positive")
    @Column(name = "total_income_tax_paid_in_company")
    private BigDecimal totalIncomeTaxPaidInThisEmployment = BigDecimal.ZERO;

    @PositiveOrZero(message = "number of pay periods income tax paid must be zero or positive")
    @Column(name = "number_of_pay_periods_income_tax_paid")
    private BigDecimal numberOfPayPeriodsIncomeTaxPaid = BigDecimal.ZERO;

    // National Insurance related fields
    @PositiveOrZero(message = "total employee NI contribution in company must be zero or positive")
    @Column(name="total_employee_ni_contribution_in_company")
    private BigDecimal totalEmployeeNIContributionInCompany = BigDecimal.ZERO;

    @PositiveOrZero(message = "employee NI contribution must be zero or positive")
    @Column(name = "employee_ni_contribution")
    private BigDecimal employeeNIContribution = BigDecimal.ZERO;


    @Column(name = "number_of_pay_periods_ni_contributions")
    @PositiveOrZero(message = "Number of pay periods NI contributions must be zero or positive")
    private BigDecimal numberOfPayPeriodsNIContributions = BigDecimal.ZERO;

    //Pension related fields
    @Column(name = "total_amount_pension_contribution")
    @PositiveOrZero(message = "Total amount pension contribution must be zero or positive")
    private BigDecimal totalAmountPensionContribution = BigDecimal.ZERO;

    @Column(name = "number_of_pay_periods_pension_contribution")
    @PositiveOrZero(message = "Number of pay periods for pension contribution must be zero or positive")
    private BigDecimal numberOfPayPeriodsPensionContribution = BigDecimal.ZERO;

    @Column(name = "pension_contribute_amount")
    @PositiveOrZero(message = "Pension contribute amount must be zero or positive")
    private BigDecimal pensionContributeAmount= BigDecimal.ZERO;

    @Column(name = "remaining_k_code_amount")
    @PositiveOrZero(message = "Remaining K Code Amount must be zero or positive")
    private BigDecimal remainingKCodeAmount=BigDecimal.ZERO;




    // Only called when the entity is first persisted
    public void setDefaultsIfNull() {
        if (numberOfPayPeriodsEmergencyTaxCodeUsed == null) numberOfPayPeriodsEmergencyTaxCodeUsed = BigDecimal.ZERO;
        if (totalAllowanceUsedDuringEmergencyCode == null) totalAllowanceUsedDuringEmergencyCode = BigDecimal.ZERO;
        if (usedPersonalAllowance == null) usedPersonalAllowance = BigDecimal.ZERO;
        if (totalUsedPersonalAllowance == null) totalUsedPersonalAllowance = BigDecimal.ZERO;
        if (remainingPersonalAllowance == null) remainingPersonalAllowance = BigDecimal.ZERO;
        if( totalEarningsAmountYTD == null) totalEarningsAmountYTD = BigDecimal.ZERO;

        if (incomeTaxPaid == null) incomeTaxPaid = BigDecimal.ZERO;
        if (totalIncomeTaxPaidInThisEmployment == null) totalIncomeTaxPaidInThisEmployment = BigDecimal.ZERO;
       /* if (numberOfMonthsOfIncomeTaxPaid == null) numberOfMonthsOfIncomeTaxPaid = BigDecimal.ZERO;
        if (numberOfYearsOfIncomeTaxPaid == null) numberOfYearsOfIncomeTaxPaid = BigDecimal.ZERO;
        if (numberOfWeeksOfIncomeTaxPaid == null) numberOfWeeksOfIncomeTaxPaid = BigDecimal.ZERO;*/

        if (numberOfPayPeriodsIncomeTaxPaid == null) numberOfPayPeriodsIncomeTaxPaid = BigDecimal.ZERO;

        if (totalEmployeeNIContributionInCompany == null) totalEmployeeNIContributionInCompany = BigDecimal.ZERO;
        if (employeeNIContribution == null) employeeNIContribution = BigDecimal.ZERO;
       /* if (numberOfMonthsOfNIContributions == null) numberOfMonthsOfNIContributions = BigDecimal.ZERO;
        if (numberOfWeeksOfNIContributions == null) numberOfWeeksOfNIContributions = BigDecimal.ZERO;
        if (numberOfYearsOfNIContributions == null) numberOfYearsOfNIContributions = BigDecimal.ZERO;*/
        if (numberOfPayPeriodsNIContributions == null) numberOfPayPeriodsNIContributions = BigDecimal.ZERO;

        if (totalAmountPensionContribution == null) totalAmountPensionContribution = BigDecimal.ZERO;
        if (numberOfPayPeriodsPensionContribution == null) numberOfPayPeriodsPensionContribution = BigDecimal.ZERO;
        if (pensionContributeAmount == null) pensionContributeAmount = BigDecimal.ZERO;
        if (remainingKCodeAmount == null) remainingKCodeAmount = BigDecimal.ZERO;
        if (totalEarningsAmountInThisEmployment ==null) totalEarningsAmountInThisEmployment = BigDecimal.ZERO;
        if (totalTaxPayToDate==null) totalTaxPayToDate = BigDecimal.ZERO;
        if (totalTaxablePayInThisEmployment == null) totalTaxablePayInThisEmployment = BigDecimal.ZERO;
        if (earningsAtLELYtd == null) earningsAtLELYtd = BigDecimal.ZERO;
        if (earningsLelToPtYtd == null) earningsLelToPtYtd = BigDecimal.ZERO;
        if (earningsPtToUelYtd == null) earningsPtToUelYtd = BigDecimal.ZERO;
    }








}
