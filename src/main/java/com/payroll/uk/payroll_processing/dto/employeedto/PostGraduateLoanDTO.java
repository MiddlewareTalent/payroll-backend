package com.payroll.uk.payroll_processing.dto.employeedto;


import com.payroll.uk.payroll_processing.entity.employee.PostGraduateLoan;
import io.swagger.v3.oas.annotations.media.Schema;
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
    private Boolean hasPostgraduateLoan = false;
    private BigDecimal monthlyDeductionAmountInPostgraduateLoan = BigDecimal.ZERO;
    private BigDecimal weeklyDeductionAmountInPostgraduateLoan = BigDecimal.ZERO;
    private BigDecimal yearlyDeductionAmountInPostgraduateLoan = BigDecimal.ZERO;
    private BigDecimal totalDeductionAmountInPostgraduateLoan = BigDecimal.ZERO;
    @Enumerated(EnumType.STRING)
    private PostGraduateLoan.PostgraduateLoanPlanType postgraduateLoanPlanType= PostGraduateLoan.PostgraduateLoanPlanType.NONE;
}
