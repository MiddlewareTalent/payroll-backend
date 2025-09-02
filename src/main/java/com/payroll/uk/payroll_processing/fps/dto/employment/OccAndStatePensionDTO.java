package com.payroll.uk.payroll_processing.fps.dto.employment;


import lombok.Data;

import java.math.BigDecimal;

@Data
public class OccAndStatePensionDTO {
    private String pensionBereaved;
    private BigDecimal pensionAmount;
}
