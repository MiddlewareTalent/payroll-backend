package com.payroll.uk.payroll_processing.fps;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployerDetailsDataDTO {
    private String empRefsOfficeNo;
    private String empRefsPayeRef;
    private String empRefsAORef;
    private String empRefsSAUTR;
    private String empRefsCOTAXREF;
}
