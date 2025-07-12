package com.payroll.uk.payroll_processing.entity;

import com.payroll.uk.payroll_processing.entity.employee.PostGraduateLoan;
import com.payroll.uk.payroll_processing.entity.employee.StudentLoan;
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
@Entity
public class LoanCalculationPaySlip {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "employee_id",nullable = false)
    private String employeeId;
    @Column(name = "national_insurance_number", nullable = false)
    private String nationalInsuranceNumber;
    @Column(name = "has_student_loan",nullable = false)
    private Boolean hasStudentLoan = false;
    @Column(name = "total_deduction_amount_in_student_loan")
    @PositiveOrZero(message = "Total deduction amount in student loan must be positive or zero")
    private BigDecimal totalDeductionAmountInStudentLoan = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @Column(name = "student_loan_plan_type",nullable = false)
    private StudentLoan.StudentLoanPlan studentLoanPlanType = StudentLoan.StudentLoanPlan.NONE;
    @Column(name = "total_deduction_amount_in_postgraduate_loan")
    @PositiveOrZero(message = "Total deduction amount in postgraduate loan must be positive or zero")
    private BigDecimal totalDeductionAmountInPostgraduateLoan = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @Column(name = "postgraduate_loan_plan_type",nullable = false)
    private PostGraduateLoan.PostgraduateLoanPlanType postgraduateLoanPlanType = PostGraduateLoan.PostgraduateLoanPlanType.NONE;
    @Schema(defaultValue = "false")
    @Column(name = "has_postgraduate_loan",nullable = false)
    private Boolean hasPostgraduateLoan = false;

    @Column(name = "pay_slip_reference", nullable = false)
    private String paySlipReference;




}
