package com.payroll.uk.payroll_processing.fps.dto.ir;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContactDetailsDTO {
    private NameDTO name;                     // optional
    private List<EmailDTO> emails;            // 0..n
    private List<TelephoneDTO> telephones;    // 0..n
    private List<TelephoneDTO> faxes;         // 0..n
}
