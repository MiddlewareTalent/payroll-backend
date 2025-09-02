package com.payroll.uk.payroll_processing.fps.dto.ir;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddressDTO {
    private List<String> lines;  // 1..4
    private String postCode;     // optional
    private String country;      // optional
}
