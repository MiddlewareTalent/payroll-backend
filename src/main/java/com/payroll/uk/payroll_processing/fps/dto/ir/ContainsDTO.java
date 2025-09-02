package com.payroll.uk.payroll_processing.fps.dto.ir;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContainsDTO {
    private List<ReferenceDTO> references;
}
