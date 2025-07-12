package com.payroll.uk.payroll_processing.entity.employee;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class StudentLoan {
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
    @Column(name = "student_loan_plan_type",nullable = false)
    private StudentLoanPlan studentLoanPlanType = StudentLoanPlan.NONE;





    public enum StudentLoanPlan {

        STUDENT_LOAN_PLAN_1,
        STUDENT_LOAN_PLAN_2,
        STUDENT_LOAN_PLAN_3,
        NONE

    }

    @PrePersist
    public void setStudentLoanDefaults() {
        if (hasStudentLoan == null) hasStudentLoan = false;
        if (deductionAmountInStudentLoan == null) deductionAmountInStudentLoan = BigDecimal.ZERO;
        if (numberOfPayPeriodsOfStudentLoan == null) numberOfPayPeriodsOfStudentLoan = BigDecimal.ZERO;
        if (totalDeductionAmountInStudentLoan == null) totalDeductionAmountInStudentLoan = BigDecimal.ZERO;
        if (studentLoanPlanType == null) studentLoanPlanType = StudentLoanPlan.NONE;

       /* if (monthlyDeductionAmountInStudentLoan == null) monthlyDeductionAmountInStudentLoan = BigDecimal.ZERO;
        if (weeklyDeductionAmountInStudentLoan == null) weeklyDeductionAmountInStudentLoan = BigDecimal.ZERO;
        if (yearlyDeductionAmountInStudentLoan == null) yearlyDeductionAmountInStudentLoan = BigDecimal.ZERO;*/
    }


    //    @Column(name = "monthly_deduction_amount_in_student_loan")
//    @PositiveOrZero(message = "Monthly deduction amount in student loan must be positive or zero")
//    private BigDecimal monthlyDeductionAmountInStudentLoan = BigDecimal.ZERO;
//
//    @Column(name = "weekly_deduction_amount_in_student_loan")
//    @PositiveOrZero(message = "Weekly deduction amount in student loan must be positive or zero")
//    private BigDecimal weeklyDeductionAmountInStudentLoan = BigDecimal.ZERO;
//
//    @Column(name = "yearly_deduction_amount_in_student_loan")
//    @PositiveOrZero(message = "Yearly deduction amount in student loan must be positive or zero")
//    private BigDecimal yearlyDeductionAmountInStudentLoan = BigDecimal.ZERO;



}
