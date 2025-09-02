package com.payroll.uk.payroll_processing.fps.dto.employment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PayrollIdChangedDTO {
    private String payrollIdChangedIndicator;
    private String oldPayrollId;
}
