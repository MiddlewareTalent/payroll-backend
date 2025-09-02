package com.payroll.uk.payroll_processing.fps.dto.employment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrivialCommutationPaymentDTO {
    private BigDecimal amount;  // e.g., 1200.00
    private String type;        // "A", "B", or "C"
}
