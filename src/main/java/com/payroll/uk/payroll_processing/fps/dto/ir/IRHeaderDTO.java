package com.payroll.uk.payroll_processing.fps.dto.ir;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class IRHeaderDTO {
    private List<IRKeyDTO> keys;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate periodEnd;
    private String defaultCurrency;
    private String irmark;
    private String sender;

    private PrincipalDTO principal;
    private AgentDTO agent;

    private ManifestDTO manifest;
}
