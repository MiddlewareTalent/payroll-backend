package com.payroll.uk.payroll_processing.dto.employerdto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OtherEmployerDetailsDTO {
    private BigDecimal monthlyPAYE=BigDecimal.ZERO;
    private BigDecimal weeklyPAYE=BigDecimal.ZERO;
    private BigDecimal yearlyPAYE=BigDecimal.ZERO;

    private BigDecimal totalPAYEYTD=BigDecimal.ZERO;

    private BigDecimal monthlyEmployeesNI=BigDecimal.ZERO;
    private BigDecimal weeklyEmployeesNI=BigDecimal.ZERO;
    private BigDecimal yearlyEmployeesNI=BigDecimal.ZERO;

    private BigDecimal totalEmployeesNIYTD=BigDecimal.ZERO;

    private BigDecimal monthlyEmployersNI=BigDecimal.ZERO;
    private BigDecimal weeklyEmployersNI=BigDecimal.ZERO;
    private BigDecimal yearlyEmployersNI=BigDecimal.ZERO;

    private BigDecimal totalEmployersNIYTD=BigDecimal.ZERO;
}
