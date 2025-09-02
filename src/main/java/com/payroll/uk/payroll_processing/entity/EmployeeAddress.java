package com.payroll.uk.payroll_processing.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class EmployeeAddress {

    @Column(name = "address_line_1", nullable = false)
    private String addressLine1; // building + flat/apartment

    @Column(name = "address_line_2")
    private String addressLine2; // street info (required if foreignCountry present)

    @Column(name = "address_line_3")
    private String addressLine3; // locality / city

    @Column(name = "address_line_4")
    private String addressLine4; // county / region / province

    @Column(name = "city")
    private String city;         // optional but recommended

    @Column(name = "postcode")
    private String postcode;     // required if UK, empty if non-UK

    @Column(name = "foreign_country")
    private String foreignCountry; // required if non-UK, maps to XML <ForeignCountry>
}
