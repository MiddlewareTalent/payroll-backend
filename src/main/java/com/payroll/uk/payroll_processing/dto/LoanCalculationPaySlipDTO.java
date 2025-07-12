package com.payroll.uk.payroll_processing.dto;

import com.payroll.uk.payroll_processing.dto.employeedto.PostGraduateLoanDTO;
import com.payroll.uk.payroll_processing.dto.employeedto.StudentLoanDTO;
import com.payroll.uk.payroll_processing.entity.employee.PostGraduateLoan;
import com.payroll.uk.payroll_processing.entity.employee.StudentLoan;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoanCalculationPaySlipDTO {
    private Long id;
    @NotBlank
    private String employeeId;
    @NotBlank
    private String nationalInsuranceNumber;

    @Schema(defaultValue = "false")
    private Boolean hasStudentLoan = false;

    @PositiveOrZero(message = "Total deduction amount in student loan must be positive or zero")
    private BigDecimal totalDeductionAmountInStudentLoan = BigDecimal.ZERO;


    private StudentLoan.StudentLoanPlan studentLoanPlanType = StudentLoan.StudentLoanPlan.NONE;

    @PositiveOrZero(message = "Total deduction amount in postgraduate loan must be positive or zero")
    private BigDecimal totalDeductionAmountInPostgraduateLoan = BigDecimal.ZERO;


    private PostGraduateLoan.PostgraduateLoanPlanType postgraduateLoanPlanType = PostGraduateLoan.PostgraduateLoanPlanType.NONE;
    @Schema(defaultValue = "false")
    private Boolean hasPostgraduateLoan = false;

    private String paySlipReference;
}
