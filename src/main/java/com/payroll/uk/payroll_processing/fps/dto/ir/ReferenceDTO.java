package com.payroll.uk.payroll_processing.fps.dto.ir;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReferenceDTO {
    private String namespace;
    private String schemaVersion;
    private String topElementName;
}
