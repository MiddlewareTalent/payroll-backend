package com.payroll.uk.payroll_processing.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "tax_thresholds")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Access(AccessType.FIELD)
public class TaxThreshold {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @Column(name = "year_range", nullable = false, length = 9)
    private String taxYear;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "ENUM('ENGLAND','NORTHERN_IRELAND','SCOTLAND','WALES')",nullable = false)
    private TaxRegion region;

    @Column(precision = 12, scale = 2, nullable = false)
    private BigDecimal lowerBound;

    @Column(precision = 12, scale = 2)
    private BigDecimal upperBound;

    @Column(precision = 5, scale = 2, nullable = false)
    private BigDecimal rate;

    @Column(name = "band_name", length = 50)
    private String bandName;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "ENUM('INCOME_TAX','NATIONAL_INSURANCE','STUDENT_LOAN','POSTGRADUATE_LOAN','PENSION','EMPLOYER_NATIONAL_INSURANCE','EMPLOYMENT_ALLOWANCE')", nullable = false)
    private TaxBandType bandNameType;


    public enum TaxBandType{
        INCOME_TAX,
        NATIONAL_INSURANCE,
        STUDENT_LOAN,
        POSTGRADUATE_LOAN,
        PENSION,
        EMPLOYER_NATIONAL_INSURANCE,
        EMPLOYMENT_ALLOWANCE
    }



    public enum TaxRegion {
        ENGLAND,
        SCOTLAND,
        WALES,
        NORTHERN_IRELAND
    }

}
