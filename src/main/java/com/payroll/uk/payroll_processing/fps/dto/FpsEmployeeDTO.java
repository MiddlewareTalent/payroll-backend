package com.payroll.uk.payroll_processing.fps.dto;


import com.payroll.uk.payroll_processing.fps.dto.employeeDetails.EmployeeDetailsDTO;
import com.payroll.uk.payroll_processing.fps.dto.employment.EmploymentDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FpsEmployeeDTO {
    private EmployeeDetailsDTO employeeDetails;
    private EmploymentDTO employment;
}
