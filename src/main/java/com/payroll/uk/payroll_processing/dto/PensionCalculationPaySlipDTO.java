package com.payroll.uk.payroll_processing.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PensionCalculationPaySlipDTO {
    private Long id;
    private String employeeId;
    @Schema(defaultValue = "false")
    private boolean hasPensionEligible= false;
    private String nationalInsuranceNumber;
    private String paySlipReference;
    private BigDecimal income;
    private BigDecimal pensionContributionDeductionAmount;
    private BigDecimal employerPensionContributionAmount;
}
