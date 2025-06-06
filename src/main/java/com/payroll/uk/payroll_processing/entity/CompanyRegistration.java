package com.payroll.uk.payroll_processing.entity;

import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Table(name="company_registration")
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class CompanyRegistration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "company_name", nullable = false)
    private String companyName;

    @Schema(description = "Tax year in format yyyy-yyyy, e.g., 2025-2026", example = "2025-2026")
    @Pattern(regexp = "^\\d{4}-\\d{4}$", message = "Tax year must be in the format YYYY-YYYY, e.g., 2025-2026")
    private String taxYear;

    @Enumerated(EnumType.STRING)
    private PayPeriod payPeriod=PayPeriod.MONTHLY;

    @Enumerated(EnumType.STRING)
//    @Column(columnDefinition = "ENUM('ENGLAND','NORTHERN_IRELAND','SCOTLAND','WALES')", nullable = false)
    private TaxThreshold.TaxRegion region;


}
