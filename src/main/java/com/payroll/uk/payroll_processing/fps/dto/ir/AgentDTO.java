package com.payroll.uk.payroll_processing.fps.dto.ir;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AgentDTO {
    private String agentId;              // optional
    private String company;              // optional
    private AddressDTO address;          // optional
    private ContactDetailsDTO contact;   // optional (same as Principal)
    private String defaultCurrency;      // optional (ISO code)
}

