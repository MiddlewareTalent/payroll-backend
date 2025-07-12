package com.payroll.uk.payroll_processing.dto.employeedto;


import com.payroll.uk.payroll_processing.entity.employee.PostGraduateLoan;
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
public class PostGraduateLoanDTO {

    @Schema(defaultValue = "false")
    @Column(name = "hasPostgraduateLoan")
    private Boolean hasPostgraduateLoan = false;
    private BigDecimal numberOfPayPeriodsOfPostgraduateLoan = BigDecimal.ZERO;
    private BigDecimal deductionAmountInPostgraduateLoan = BigDecimal.ZERO;

    @Column(name = "totalDeductionAmountInPostgraduateLoan")
    private BigDecimal totalDeductionAmountInPostgraduateLoan = BigDecimal.ZERO;
    @Enumerated(EnumType.STRING)
    @Column(name = "postgraduateLoanPlanType")
    private PostGraduateLoan.PostgraduateLoanPlanType postgraduateLoanPlanType= PostGraduateLoan.PostgraduateLoanPlanType.NONE;

   /* @Column(name = "monthlyDeductionAmountInPostgraduateLoan")
    private BigDecimal monthlyDeductionAmountInPostgraduateLoan = BigDecimal.ZERO;
    @Column(name = "weeklyDeductionAmountInPostgraduateLoan")
    private BigDecimal weeklyDeductionAmountInPostgraduateLoan = BigDecimal.ZERO;
    @Column(name = "yearlyDeductionAmountInPostgraduateLoan")
    private BigDecimal yearlyDeductionAmountInPostgraduateLoan = BigDecimal.ZERO*/;
}
