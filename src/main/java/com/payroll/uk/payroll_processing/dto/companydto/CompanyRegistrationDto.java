package com.payroll.uk.payroll_processing.dto.companydto;

import com.payroll.uk.payroll_processing.entity.PayPeriod;
import com.payroll.uk.payroll_processing.entity.TaxThreshold;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompanyRegistrationDto {
    @Schema(defaultValue = "0")
    private Long id;
    @NotBlank
    private String companyName;

    @NotBlank
    private String taxYear;

    @Enumerated(EnumType.STRING)
    private PayPeriod payPeriod;

    @Enumerated(EnumType.STRING)
    private TaxThreshold.TaxRegion region;


}
