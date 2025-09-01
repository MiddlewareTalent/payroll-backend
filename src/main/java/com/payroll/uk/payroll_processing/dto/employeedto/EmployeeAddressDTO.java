package com.payroll.uk.payroll_processing.dto.employeedto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeAddressDTO {



    private String addressLine1; // building + flat/apartment
    private String addressLine2; // street info (required if foreignCountry present)
    private String addressLine3; // locality / city
    private String addressLine4; // county / region / province
    private String city;         // optional but recommended
    private String postcode;     // required if UK, empty if non-UK
    private String foreignCountry;

}
