package com.payroll.uk.payroll_processing.entity.employer;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class OtherEmployerDetails {

    private BigDecimal monthlyPAYE;
    private BigDecimal weeklyPAYE;
    private BigDecimal yearlyPAYE;

    private BigDecimal totalPAYEYTD;

    private BigDecimal monthlyEmployeesNI;
    private BigDecimal weeklyEmployeesNI;
    private BigDecimal yearlyEmployeesNI;

    private BigDecimal totalEmployeesNIYTD;

    private BigDecimal monthlyEmployersNI;
    private BigDecimal weeklyEmployersNI;
    private BigDecimal yearlyEmployersNI;

    private BigDecimal totalEmployersNIYTD;

}
