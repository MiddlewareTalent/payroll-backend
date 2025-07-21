package com.payroll.uk.payroll_processing.entity.employer;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.payroll.uk.payroll_processing.entity.PayPeriod;
import com.payroll.uk.payroll_processing.entity.TaxThreshold;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.swing.plaf.synth.Region;
import java.time.LocalDate;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompanyDetails {

    @Column(name = "company_name",nullable = false)
    private String companyName;

    @Column(name = "company_address")
    private String companyAddress;
    @Column(name = "company_post_code")
    private String companyPostCode;
    @Column(name = "company_logo")
    private String companyLogo;

    @Schema(description = "Tax year in format yyyy-yyyy, e.g., 2025-2026", example = "2025-2026")
    @Pattern(regexp = "^\\d{4}-\\d{4}$", message = "Tax year must be in the format YYYY-YYYY, e.g., 2025-2026")
    @Column(name = "tax_year", nullable = false)
    private String currentTaxYear;

    @Enumerated(EnumType.STRING)
    @Column(name ="pay_period",nullable = false )
    private PayPeriod currentPayPeriod;

    @Enumerated(EnumType.STRING)
//    @Column(columnDefinition = "ENUM('ENGLAND','NORTHERN_IRELAND','SCOTLAND','WALES')", nullable = false)
    @Column(name = "region",nullable = false)
    private TaxThreshold.TaxRegion region;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(name = "pay_date", nullable = false)
    private LocalDate payDate;

    //    private String companyRegistrationNumber;
//    private String companyPhoneNumber;
//    private String companyEmail;
//    private String companyWebsite;
//    private String companyVATNumber;


}
