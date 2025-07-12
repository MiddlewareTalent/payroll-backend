package com.payroll.uk.payroll_processing.entity.employee;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.PrePersist;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostGraduateLoan {

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
    private PostgraduateLoanPlanType postgraduateLoanPlanType = PostgraduateLoanPlanType.NONE;

    public enum PostgraduateLoanPlanType {
        POSTGRADUATE_LOAN_PLAN_3,
        NONE
    }

    @PrePersist
    public void setPostgraduateDefaults() {
        if (hasPostgraduateLoan == null) {
            hasPostgraduateLoan = false;
        }
        if (totalDeductionAmountInPostgraduateLoan == null) {
            totalDeductionAmountInPostgraduateLoan = BigDecimal.ZERO;
        }
        if (postgraduateLoanPlanType == null) {
            postgraduateLoanPlanType = PostgraduateLoanPlanType.NONE;
        }
        if (deductionAmountInPostgraduateLoan == null) {
            deductionAmountInPostgraduateLoan = BigDecimal.ZERO;
        }
        if (numberOfPayPeriodsOfPostgraduateLoan == null) {
            numberOfPayPeriodsOfPostgraduateLoan = BigDecimal.ZERO;
        }

       /* if (monthlyDeductionAmountInPostgraduateLoan == null) {
            monthlyDeductionAmountInPostgraduateLoan = BigDecimal.ZERO;
        }
        if (weeklyDeductionAmountInPostgraduateLoan == null) {
            weeklyDeductionAmountInPostgraduateLoan = BigDecimal.ZERO;
        }
        if (yearlyDeductionAmountInPostgraduateLoan == null) {
            yearlyDeductionAmountInPostgraduateLoan = BigDecimal.ZERO;
        }*/
    }

    /*@Column(name = "monthly_deduction_amount_in_postgraduate_loan")
    @PositiveOrZero(message = "Monthly deduction amount in postgraduate loan must be positive or zero")
    private BigDecimal monthlyDeductionAmountInPostgraduateLoan = BigDecimal.ZERO;

    @Column(name = "weekly_deduction_amount_in_postgraduate_loan")
    @PositiveOrZero(message = "Weekly deduction amount in postgraduate loan must be positive or zero")
    private BigDecimal weeklyDeductionAmountInPostgraduateLoan = BigDecimal.ZERO;

    @Column(name = "yearly_deduction_amount_in_postgraduate_loan")
    @PositiveOrZero(message = "Yearly deduction amount in postgraduate loan must be positive or zero")
    private BigDecimal yearlyDeductionAmountInPostgraduateLoan = BigDecimal.ZERO;*/
}
