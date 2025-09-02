package com.payroll.uk.payroll_processing.fps.dto.ir;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailDTO {
    private String email;                     // content value
    private String type;                      // attribute Type (optional)
    private Boolean preferred;                // attribute Preferred (optional)
}
