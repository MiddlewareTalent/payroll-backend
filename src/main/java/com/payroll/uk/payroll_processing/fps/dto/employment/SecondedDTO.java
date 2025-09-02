package com.payroll.uk.payroll_processing.fps.dto.employment;

import lombok.Data;

@Data
public class SecondedDTO {
    private String secondedStay183DaysOrMore;
    private String secondedStayLessThan183Days;
    private String secondedInOutUk;
    private String secondedEEACitizen;
    private String secondedEPM6;
}
