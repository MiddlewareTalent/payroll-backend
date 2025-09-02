package com.payroll.uk.payroll_processing.fps.dto.ir;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NameDTO {
    private String title;                     // Ttl (optional)
    private List<String> forenames;           // Fore (1..2)
    private String surname;                   // Sur (required if Name present)
}
