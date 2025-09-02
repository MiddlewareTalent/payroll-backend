package com.payroll.uk.payroll_processing.fps.dto.employment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class FlexibleDrawdownDTO {
    private String flexiblyAccessingPensionRights;
    private String pensionDeathBenefit;
    private String seriousIllHealthLumpSum;
    private String pensionCommencementExcessLumpSum;
    private String standAloneLumpSum;
    private BigDecimal taxablePayment;
    private BigDecimal nontaxablePayment;
}
