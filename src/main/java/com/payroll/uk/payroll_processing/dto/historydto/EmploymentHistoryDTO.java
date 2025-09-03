package com.payroll.uk.payroll_processing.dto.historydto;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.payroll.uk.payroll_processing.dto.employeedto.EmployeeAddressDTO;
import com.payroll.uk.payroll_processing.entity.NICategoryLetters;
import com.payroll.uk.payroll_processing.entity.employee.PostGraduateLoan;
import com.payroll.uk.payroll_processing.entity.employee.StudentLoan;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmploymentHistoryDTO {
    private Long id;
    private String employeeId;
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;
   /* @Column(name = "address")
    private String address;
    @Column(name = "postCode")
    private String postCode;*/

    private EmployeeAddressDTO employeeAddressDTO;
    @Column(name = "annual_income_of_employee")
    @PositiveOrZero(message = "Annual income must be zero or positive")
    private BigDecimal annualIncomeOfEmployee;
    @Column(name = "tax_code",nullable = false)
    private String taxCode;
    @Column(name = "is_emergency_code",nullable = false)
    private boolean hasEmergencyCode=false;
    @Pattern(regexp = "^[A-Z]{2}[0-9]{6}[A-Z]$",
            message = "National Insurance number must be in the format AB123456C")
    @Column(name = "national_insurance_number", unique = true,nullable = false)
    private String nationalInsuranceNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "ni_letter",nullable = false)
    private NICategoryLetters niLetter;

    @Column(name = "payroll_id", unique = true, nullable = false)
    private String payrollId;

    //Other employee details
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

    @Column(name = "total_income_tax_ytd")
    @PositiveOrZero(message = "Total tax pay to date must be zero or positive")
    private BigDecimal totalIncomeTaxYTD = BigDecimal.ZERO;

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


    //Student Loan related fields
    @Schema(defaultValue = "false")
    @Column(name = "has_student_loan",nullable = false)
    private Boolean hasStudentLoan = false;

    @Column(name = "deduction_amount_in_student_loan")
    @PositiveOrZero(message = "Deduction amount in student loan must be positive or zero")
    private BigDecimal deductionAmountInStudentLoan = BigDecimal.ZERO;

    @Column(name = "number_of_pay_periods_of_student_loan")
    @PositiveOrZero(message = "Number of pay periods of student loan must be positive or zero")
    private BigDecimal numberOfPayPeriodsOfStudentLoan = BigDecimal.ZERO;



    @Column(name = "total_deduction_amount_in_student_loan")
    @PositiveOrZero(message = "Total deduction amount in student loan must be positive or zero")
    private BigDecimal totalDeductionAmountInStudentLoan = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @Column(name = "student_loan_plan_type",nullable = false,length = 100)
    private StudentLoan.StudentLoanPlan studentLoanPlanType = StudentLoan.StudentLoanPlan.NONE;

    //Post Graduate Loan related fields
    @Schema(defaultValue = "false")
    @Column(name = "has_postgraduate_loan",nullable = false)
    private Boolean hasPostgraduateLoan = false;

    @Column(name = "number_of_pay_periods_of_postgraduate_loan")
    @PositiveOrZero(message = "Number of pay periods of postgraduate loan must be positive or zero")
    private BigDecimal numberOfPayPeriodsOfPostgraduateLoan = BigDecimal.ZERO;

    @Column(name = "deduction_amount_in_postgraduate_loan")
    @PositiveOrZero(message = "Deduction amount in postgraduate loan must be positive or zero")
    private BigDecimal deductionAmountInPostgraduateLoan = BigDecimal.ZERO;

    @Column(name = "total_deduction_amount_in_postgraduate_loan")
    @PositiveOrZero(message = "Total deduction amount in postgraduate loan must be positive or zero")
    private BigDecimal totalDeductionAmountInPostgraduateLoan = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @Column(name = "postgraduate_loan_plan_type",nullable = false)
    private PostGraduateLoan.PostgraduateLoanPlanType postgraduateLoanPlanType = PostGraduateLoan.PostgraduateLoanPlanType.NONE;

    @Column(name = "employee_id_changed",nullable = false)
    private  boolean employeeIdChanged = false;
    @Column(name = "date_of_birth_changed",nullable = false)
    private boolean dateOfBirthChanged = false;
    @Column(name = "address_changed",nullable = false)
    private boolean addressChanged = false;
    @Column(name = "post_code_changed",nullable = false)
    private boolean postCodeChanged = false;
    @Column(name = "annual_income_changed",nullable = false)
    private  boolean annualIncomeChanged = false;
    @Column(name = "tax_code_changed",nullable = false)
    private boolean taxCodeChanged = false;
    @Column(name = "national_insurance_number_changed",nullable = false)
    private boolean nationalInsuranceNumberChanged = false;
    @Column(name = "ni_letter_changed",nullable = false)
    private boolean niLetterChanged = false;
    @Column(name = "payroll_id_changed",nullable = false)
    private boolean payrollIdChanged = false;

    @Column(name = "student_loan_plan_type_changed",nullable = false)
    private boolean studentLoanPlanTypeChanged = false;
    @Column(name = "Post_graduate_loan_plan_type_changed",nullable = false)
    private boolean postgraduateLoanPlanTypeChanged = false;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;
}
