package com.payroll.uk.payroll_processing.fps.dto.ir;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TelephoneDTO {
    private String number;                    // required
    private String extension;                 // optional
    private String type;                      // attribute Type
    private Boolean mobile;                   // attribute Mobile
    private Boolean preferred;                // attribute Preferred
}
