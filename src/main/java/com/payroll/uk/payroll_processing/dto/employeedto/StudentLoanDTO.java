package com.payroll.uk.payroll_processing.dto.employeedto;

import com.payroll.uk.payroll_processing.entity.employee.StudentLoan;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentLoanDTO {
    @Schema(defaultValue = "false")
    private Boolean hasStudentLoan = false;
    private BigDecimal numberOfPayPeriodsOfStudentLoan = BigDecimal.ZERO;
    private BigDecimal deductionAmountInStudentLoan = BigDecimal.ZERO;
    private BigDecimal totalDeductionAmountInStudentLoan = BigDecimal.ZERO;
    @Enumerated(EnumType.STRING)
    @Schema(defaultValue = "NONE"  )
    private StudentLoan.StudentLoanPlan studentLoanPlanType= StudentLoan.StudentLoanPlan.NONE;

    //    @Column(name = "monthlyDeductionAmountInStudentLoan")
//    private BigDecimal monthlyDeductionAmountInStudentLoan = BigDecimal.ZERO;
//    @Column(name = "weeklyDeductionAmountInStudentLoan")
//    private BigDecimal weeklyDeductionAmountInStudentLoan = BigDecimal.ZERO;
//    @Column(name = "yearlyDeductionAmountInStudentLoan")
//    private BigDecimal yearlyDeductionAmountInStudentLoan = BigDecimal.ZERO;

}
