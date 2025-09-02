package com.payroll.uk.payroll_processing.dto.customdto;

import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NIContributionDTO {
    private String niLetter; // e.g., M, A, B
    private BigDecimal lowerEarningsLimit;  // LEL
    private BigDecimal primaryThreshold;    // PT
    private BigDecimal upperEarningsLimit;  // UEL
    private BigDecimal employeeContribution;
//    private BigDecimal employerContribution; // optional


}
