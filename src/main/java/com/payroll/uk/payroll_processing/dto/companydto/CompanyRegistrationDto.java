package com.payroll.uk.payroll_processing.dto.companydto;

import com.payroll.uk.payroll_processing.entity.PayPeriod;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompanyRegistrationDto {
    @NotBlank  private String companyName;
    @NotBlank  private String taxYear;
    private PayPeriod payDates;


}
