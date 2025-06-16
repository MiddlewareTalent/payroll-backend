package com.payroll.uk.payroll_processing.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.payroll.uk.payroll_processing.entity.employee.StudentLoan;
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
    @Column(columnDefinition = "ENUM('ENGLAND','NORTHERN_IRELAND','SCOTLAND','WALES','ALL_REGIONS')",nullable = false)
    private TaxRegion region;

    @Column(precision = 12, scale = 2, nullable = false)
    private BigDecimal lowerBound;

    @Column(precision = 12, scale = 2)
    private BigDecimal upperBound;

    @Column(precision = 5, scale = 5, nullable = false)
    private BigDecimal rate;


    @Column(name = "band_name", length = 50)
    @Enumerated(EnumType.STRING)
    private BandName bandName;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "ENUM('INCOME_TAX','EMPLOYEE_NI','STUDENT_LOAN','POSTGRADUATE_LOAN','PENSION','EMPLOYER_NI','EMPLOYMENT_ALLOWANCE','PERSONAL_ALLOWANCE')", nullable = false)
    private BandNameType bandNameType;

    @Enumerated(EnumType.STRING)
    private NICBand nicBand;


    public enum BandNameType{
        INCOME_TAX,
        EMPLOYEE_NI,
        STUDENT_LOAN,
        POSTGRADUATE_LOAN,
        PENSION,
        EMPLOYER_NI,
        EMPLOYMENT_ALLOWANCE,
        PERSONAL_ALLOWANCE,

    }
    public enum BandName{

        STARTER_RATE,
        BASIC_RATE,
        INTERMEDIATE_RATE,
        HIGHER_RATE,
        ADDITIONAL_RATE,
        ADVANCED_RATE,
        TOP_RATE,
        PERSONAL_ALLOWANCE,
        EMPLOYMENT_ALLOWANCE,
        STUDENT_LOAN_PLAN_1,
        STUDENT_LOAN_PLAN_2,
        STUDENT_LOAN_PLAN_4,
        POSTGRADUATE_LOAN_PLAN_3,



//        EMPLOYEE_NI_PRIMARY,
        EMPLOYEE_NI_TYPE_A,
        EMPLOYEE_NI_TYPE_B,
        EMPLOYEE_NI_TYPE_C,
        EMPLOYEE_NI_TYPE_D,

        EMPLOYER_NI_TYPE_A,
        EMPLOYER_NI_TYPE_B,
        EMPLOYER_NI_TYPE_C,

//        EMPLOYER_NI_SECONDARY

    }




//    public enum NIBandName{
//        EMPLOYEE_NI_TYPE_A,
//        EMPLOYEE_NI_TYPE_B,
//        EMPLOYEE_NI_TYPE_C,
//
//        EMPLOYER_NI_TYPE_A,
//        EMPLOYER_NI_TYPE_B,
//        EMPLOYER_NI_TYPE_C,
//        EMPLOYER_NI_TYPE_D
//    }



    public enum TaxRegion {
        ENGLAND,
        SCOTLAND,
        WALES,
        NORTHERN_IRELAND,
        ALL_REGIONS
    }

}
