package com.payroll.uk.payroll_processing.fps.dto.employeeDetails;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NameDTO {
    private String employeeTtl;
    private String employeeFore;
    private String employeeSur;
    private String employeeInitials;
}
