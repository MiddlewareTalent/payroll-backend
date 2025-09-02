package com.payroll.uk.payroll_processing.fps.dto.employment;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;

@Data
public class StarterDTO {
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate employmentStartDate;
    private String employmentStartDec;
    private String employmentStudentLoan;
    private String employmentPostgradLoan;
    private SecondedDTO employmentSeconded;
    private OccAndStatePensionDTO employmentOccPension;
    private OccAndStatePensionDTO employmentStatePension;

}
