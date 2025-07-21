package com.payroll.uk.payroll_processing.dto.employerdto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.payroll.uk.payroll_processing.entity.PayPeriod;
import com.payroll.uk.payroll_processing.entity.TaxThreshold;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompanyDetailsDTO {

    private String companyName;
    private String companyAddress;
    private String companyPostCode;
    private String companyLogo;
    private String currentTaxYear;
    private PayPeriod currentPayPeriod;
    private TaxThreshold.TaxRegion region;
    private LocalDate payDate;
}
