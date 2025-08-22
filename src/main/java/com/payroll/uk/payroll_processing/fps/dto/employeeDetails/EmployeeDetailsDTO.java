package com.payroll.uk.payroll_processing.fps.dto.employeeDetails;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeDetailsDTO {
    private String employeeNINO;
    private NameDTO employeeName;
    private AddressDTO employeeAddress;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate employeeBirthDate;
    private String employeeGender;
    private String employeePassportNumber;
    private PartnerDetailsDTO employeePartnerDetails;

}
