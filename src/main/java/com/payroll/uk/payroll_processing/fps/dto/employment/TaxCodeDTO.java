package com.payroll.uk.payroll_processing.fps.dto.employment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaxCodeDTO {
    private String taxCode;             // Must follow HMRC pattern rules
    private Boolean basisNonCumulative; // Optional attribute (null if not used)
    private String taxRegime;           // Optional attribute (S or C)
}
