package com.payroll.uk.payroll_processing.fps.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FinalSubmissionDTO {

    private String becauseSchemeCeased;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateSchemeCeased;
    private String forYear;
}
