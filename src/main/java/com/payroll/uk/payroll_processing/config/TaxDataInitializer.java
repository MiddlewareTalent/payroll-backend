package com.payroll.uk.payroll_processing.config;

import com.payroll.uk.payroll_processing.entity.NICBand;
import com.payroll.uk.payroll_processing.entity.TaxThreshold;
import com.payroll.uk.payroll_processing.repository.TaxThresholdRepository;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class TaxDataInitializer {
    private final TaxThresholdRepository taxThresholdRepository;

    public TaxDataInitializer(TaxThresholdRepository taxThresholdRepository) {
        this.taxThresholdRepository = taxThresholdRepository;
    }

    @PostConstruct
    @Transactional
    public void initializeTaxData() {
        try{
            if (!taxThresholdRepository.existsByTaxYear("2025-2026")) {
                initialize2025_2026TaxYear();
                System.out.println("successfully initialized tax data for 2025-2026");

            }
        }
        catch (Exception e) {
            System.err.println("Error initializing tax data for 2025-2026: " + e.getMessage());
        }
        try{
            if (!taxThresholdRepository.existsByTaxYear("2024-2025")) {
                initialize2024_2025TaxYear();
                System.out.println("successfully initialized tax data for 2024-2025");

            }
        }
        catch (Exception e) {
            System.err.println("Error initializing tax data for 2024-2025: " + e.getMessage());
        }


    }
    private TaxThreshold createThreshold(String taxYear,
                                         TaxThreshold.TaxRegion region,
                                         TaxThreshold.BandName bandName,
                                         TaxThreshold.BandNameType bandNameType,
                                         NICBand nicBand,
                                         String lowerBound,
                                         String upperBound,
                                         String ratePercent) {

        TaxThreshold threshold = new TaxThreshold();
        threshold.setTaxYear(taxYear);
        threshold.setRegion(region);
        threshold.setBandName(bandName);
        
        threshold.setBandNameType(bandNameType);
        threshold.setNicBand(nicBand);
        threshold.setLowerBound(new BigDecimal(lowerBound));
        threshold.setUpperBound(upperBound != null ? new BigDecimal(upperBound) : null);
        threshold.setRate(new BigDecimal(ratePercent).divide(new BigDecimal(100),5, RoundingMode.HALF_UP));

        return threshold;
    }
    
    


    private void initialize2025_2026TaxYear() {
        // Implementation for 2025-2026 tax year
        List<TaxThreshold> thresholds = new ArrayList<>();
                // England
                thresholds.add(createThreshold("2025-2026",TaxThreshold.TaxRegion.ENGLAND, TaxThreshold.BandName.PERSONAL_ALLOWANCE, TaxThreshold.BandNameType.INCOME_TAX,null,"0",     "12570", "0"));
                thresholds.add(createThreshold("2025-2026", TaxThreshold.TaxRegion.ENGLAND,TaxThreshold.BandName.BASIC_RATE,TaxThreshold.BandNameType.INCOME_TAX,   null,      "12571", "50270", "20"));
                thresholds.add(createThreshold("2025-2026", TaxThreshold.TaxRegion.ENGLAND,TaxThreshold.BandName.HIGHER_RATE,TaxThreshold.BandNameType.INCOME_TAX,  null,      "50271", "125140","40"));
                thresholds.add(createThreshold("2025-2026", TaxThreshold.TaxRegion.ENGLAND,TaxThreshold.BandName.ADDITIONAL_RATE,TaxThreshold.BandNameType.INCOME_TAX,  null,  "125140",null,    "45"));
                // Scotland
                thresholds.add(createThreshold("2025-2026", TaxThreshold.TaxRegion.SCOTLAND, TaxThreshold.BandName.PERSONAL_ALLOWANCE,TaxThreshold.BandNameType.INCOME_TAX,null,"0", "12570", "0"));
                thresholds.add(createThreshold("2025-2026", TaxThreshold.TaxRegion.SCOTLAND, TaxThreshold.BandName.STARTER_RATE,TaxThreshold.BandNameType.INCOME_TAX,null,"12571", "15397", "19"));
                thresholds.add(createThreshold("2025-2026", TaxThreshold.TaxRegion.SCOTLAND, TaxThreshold.BandName.BASIC_RATE,TaxThreshold.BandNameType.INCOME_TAX,null,"15398", "27491", "20"));
                thresholds.add(createThreshold("2025-2026", TaxThreshold.TaxRegion.SCOTLAND, TaxThreshold.BandName.INTERMEDIATE_RATE,TaxThreshold.BandNameType.INCOME_TAX,null,"27492", "43662", "21"));
                thresholds.add(createThreshold("2025-2026", TaxThreshold.TaxRegion.SCOTLAND, TaxThreshold.BandName.HIGHER_RATE,TaxThreshold.BandNameType.INCOME_TAX,null,"43663", "75000", "42"));
                thresholds.add(createThreshold("2025-2026", TaxThreshold.TaxRegion.SCOTLAND, TaxThreshold.BandName.ADVANCED_RATE,TaxThreshold.BandNameType.INCOME_TAX,null,"75001", "125140", "45"));
                thresholds.add(createThreshold("2025-2026", TaxThreshold.TaxRegion.SCOTLAND, TaxThreshold.BandName.TOP_RATE,TaxThreshold.BandNameType.INCOME_TAX,null,"125140", null, "48"));

                // Wales
                thresholds.add(createThreshold("2025-2026", TaxThreshold.TaxRegion.WALES, TaxThreshold.BandName.PERSONAL_ALLOWANCE,TaxThreshold.BandNameType.INCOME_TAX, null,"0",     "12570", "0"));
                thresholds.add(createThreshold("2025-2026", TaxThreshold.TaxRegion.WALES, TaxThreshold.BandName.BASIC_RATE,TaxThreshold.BandNameType.INCOME_TAX,    null,     "12571", "50270", "20"));
                thresholds.add(createThreshold("2025-2026", TaxThreshold.TaxRegion.WALES, TaxThreshold.BandName.HIGHER_RATE,TaxThreshold.BandNameType.INCOME_TAX, null,       "50271", "125140","40"));
                thresholds.add(createThreshold("2025-2026", TaxThreshold.TaxRegion.WALES, TaxThreshold.BandName.ADDITIONAL_RATE,TaxThreshold.BandNameType.INCOME_TAX,null,    "125140",null,    "45"));
                // Northern Ireland
                thresholds.add(createThreshold("2025-2026", TaxThreshold.TaxRegion.NORTHERN_IRELAND, TaxThreshold.BandName.PERSONAL_ALLOWANCE,TaxThreshold.BandNameType.INCOME_TAX, null,"0",     "12570", "0"));
                thresholds.add(createThreshold("2025-2026", TaxThreshold.TaxRegion.NORTHERN_IRELAND, TaxThreshold.BandName.BASIC_RATE,TaxThreshold.BandNameType.INCOME_TAX,  null,       "12571", "50270", "20"));
                thresholds.add(createThreshold("2025-2026", TaxThreshold.TaxRegion.NORTHERN_IRELAND, TaxThreshold.BandName.HIGHER_RATE,TaxThreshold.BandNameType.INCOME_TAX,    null,    "50271", "125140","40"));
                thresholds.add(createThreshold("2025-2026", TaxThreshold.TaxRegion.NORTHERN_IRELAND, TaxThreshold.BandName.ADDITIONAL_RATE,TaxThreshold.BandNameType.INCOME_TAX, null,   "125140",null,    "45"));

                thresholds.addAll(initializeNIData2025_2026TaxYear());

        taxThresholdRepository.saveAll(Collections.unmodifiableList(thresholds));

    }
    private List<TaxThreshold> initializeNIData2025_2026TaxYear(){
        List<TaxThreshold> niThresholds=new ArrayList<>();
        //Personal Allowance
        niThresholds.add(createThreshold("2025-2026", TaxThreshold.TaxRegion.ALL_REGIONS, TaxThreshold.BandName.PERSONAL_ALLOWANCE, TaxThreshold.BandNameType.PERSONAL_ALLOWANCE, null,"12570", "0", "0"));
        niThresholds.add(createThreshold("2025-2026", TaxThreshold.TaxRegion.ALL_REGIONS, TaxThreshold.BandName.EMPLOYMENT_ALLOWANCE, TaxThreshold.BandNameType.EMPLOYMENT_ALLOWANCE, null,"10500", "0", "0"));
        niThresholds.add(createThreshold("2025-2026", TaxThreshold.TaxRegion.ALL_REGIONS, TaxThreshold.BandName.EMPLOYEE_PENSION_CONTRIBUTION, TaxThreshold.BandNameType.PENSION_CONTRIBUTION, null,"6240", "50270", "5"));
        niThresholds.add(createThreshold("2025-2026", TaxThreshold.TaxRegion.ALL_REGIONS, TaxThreshold.BandName.EMPLOYER_PENSION_CONTRIBUTION, TaxThreshold.BandNameType.PENSION_CONTRIBUTION, null,"6240", "50270", "3"));
        niThresholds.add(createThreshold("2025-2026", TaxThreshold.TaxRegion.ALL_REGIONS, TaxThreshold.BandName.AUTO_ENROLMENT_PENSION_CONTRIBUTION, TaxThreshold.BandNameType.PENSION_CONTRIBUTION, null,"10000", "0", "0"));

        //Student Loans and Postgraduate Loans
        niThresholds.add(createThreshold("2025-2026", TaxThreshold.TaxRegion.ALL_REGIONS, TaxThreshold.BandName.STUDENT_LOAN_PLAN_1, TaxThreshold.BandNameType.STUDENT_LOAN, null,"26065", "28470", "9"));
        niThresholds.add(createThreshold("2025-2026", TaxThreshold.TaxRegion.ALL_REGIONS, TaxThreshold.BandName.STUDENT_LOAN_PLAN_2, TaxThreshold.BandNameType.STUDENT_LOAN, null,"28470", "32745", "9"));
        niThresholds.add(createThreshold("2025-2026", TaxThreshold.TaxRegion.ALL_REGIONS, TaxThreshold.BandName.STUDENT_LOAN_PLAN_4, TaxThreshold.BandNameType.STUDENT_LOAN, null,"32745", "0", "9"));
        niThresholds.add(createThreshold("2025-2026", TaxThreshold.TaxRegion.ALL_REGIONS, TaxThreshold.BandName.POSTGRADUATE_LOAN_PLAN_3, TaxThreshold.BandNameType.POSTGRADUATE_LOAN, null,"21000", "0", "6"));
        //Employee - A,F,H,M,N,V - Type A
        niThresholds.add(createThreshold("2025-2026", TaxThreshold.TaxRegion.ALL_REGIONS, TaxThreshold.BandName.EMPLOYEE_NI_TYPE_A, TaxThreshold.BandNameType.EMPLOYEE_NI, NICBand.EMPLOYEE_BELOW_LEL,"0", "6500", "0"));
        niThresholds.add(createThreshold("2025-2026", TaxThreshold.TaxRegion.ALL_REGIONS, TaxThreshold.BandName.EMPLOYEE_NI_TYPE_A, TaxThreshold.BandNameType.EMPLOYEE_NI, NICBand.EMPLOYEE_LEL_TO_PT,"6500", "12570", "0"));
        niThresholds.add(createThreshold("2025-2026", TaxThreshold.TaxRegion.ALL_REGIONS, TaxThreshold.BandName.EMPLOYEE_NI_TYPE_A, TaxThreshold.BandNameType.EMPLOYEE_NI, NICBand.EMPLOYEE_PT_TO_UEL,"12570", "50270", "8"));
        niThresholds.add(createThreshold("2025-2026", TaxThreshold.TaxRegion.ALL_REGIONS, TaxThreshold.BandName.EMPLOYEE_NI_TYPE_A, TaxThreshold.BandNameType.EMPLOYEE_NI, NICBand.EMPLOYEE_ABOVE_UEL,"50270", null, "2"));

        //Employee - D,J,L,Z - Type B
        niThresholds.add(createThreshold("2025-2026", TaxThreshold.TaxRegion.ALL_REGIONS, TaxThreshold.BandName.EMPLOYEE_NI_TYPE_B, TaxThreshold.BandNameType.EMPLOYEE_NI, NICBand.EMPLOYEE_BELOW_LEL,"0", "6500", "0"));
        niThresholds.add(createThreshold("2025-2026", TaxThreshold.TaxRegion.ALL_REGIONS, TaxThreshold.BandName.EMPLOYEE_NI_TYPE_B, TaxThreshold.BandNameType.EMPLOYEE_NI, NICBand.EMPLOYEE_LEL_TO_PT,"6500", "12570", "0"));
        niThresholds.add(createThreshold("2025-2026", TaxThreshold.TaxRegion.ALL_REGIONS, TaxThreshold.BandName.EMPLOYEE_NI_TYPE_B, TaxThreshold.BandNameType.EMPLOYEE_NI, NICBand.EMPLOYEE_PT_TO_UEL,"12570", "50270", "2"));
        niThresholds.add(createThreshold("2025-2026", TaxThreshold.TaxRegion.ALL_REGIONS, TaxThreshold.BandName.EMPLOYEE_NI_TYPE_B, TaxThreshold.BandNameType.EMPLOYEE_NI, NICBand.EMPLOYEE_ABOVE_UEL,"50270", null, "2"));
        //Employee - B,E,I - Type C
        niThresholds.add(createThreshold("2025-2026", TaxThreshold.TaxRegion.ALL_REGIONS, TaxThreshold.BandName.EMPLOYEE_NI_TYPE_C, TaxThreshold.BandNameType.EMPLOYEE_NI,NICBand.EMPLOYEE_BELOW_LEL, "0", "6500", "0"));
        niThresholds.add(createThreshold("2025-2026", TaxThreshold.TaxRegion.ALL_REGIONS, TaxThreshold.BandName.EMPLOYEE_NI_TYPE_C, TaxThreshold.BandNameType.EMPLOYEE_NI,NICBand.EMPLOYEE_LEL_TO_PT, "6500", "12570", "0"));
        niThresholds.add(createThreshold("2025-2026", TaxThreshold.TaxRegion.ALL_REGIONS, TaxThreshold.BandName.EMPLOYEE_NI_TYPE_C, TaxThreshold.BandNameType.EMPLOYEE_NI, NICBand.EMPLOYEE_PT_TO_UEL,"12570", "50270", "1.85"));
        niThresholds.add(createThreshold("2025-2026", TaxThreshold.TaxRegion.ALL_REGIONS, TaxThreshold.BandName.EMPLOYEE_NI_TYPE_C, TaxThreshold.BandNameType.EMPLOYEE_NI,NICBand.EMPLOYEE_ABOVE_UEL, "50270", null, "2"));
        //Employee - C,S - Type D
        niThresholds.add(createThreshold("2025-2026", TaxThreshold.TaxRegion.ALL_REGIONS, TaxThreshold.BandName.EMPLOYEE_NI_TYPE_D, TaxThreshold.BandNameType.EMPLOYEE_NI, NICBand.EMPLOYEE_BELOW_LEL,"0", "6500", "0"));
        niThresholds.add(createThreshold("2025-2026", TaxThreshold.TaxRegion.ALL_REGIONS, TaxThreshold.BandName.EMPLOYEE_NI_TYPE_D, TaxThreshold.BandNameType.EMPLOYEE_NI, NICBand.EMPLOYEE_LEL_TO_PT,"6500", "12570", "0"));
        niThresholds.add(createThreshold("2025-2026", TaxThreshold.TaxRegion.ALL_REGIONS, TaxThreshold.BandName.EMPLOYEE_NI_TYPE_D, TaxThreshold.BandNameType.EMPLOYEE_NI, NICBand.EMPLOYEE_PT_TO_UEL,"12570", "50270", "0"));
        niThresholds.add(createThreshold("2025-2026", TaxThreshold.TaxRegion.ALL_REGIONS, TaxThreshold.BandName.EMPLOYEE_NI_TYPE_D, TaxThreshold.BandNameType.EMPLOYEE_NI, NICBand.EMPLOYEE_ABOVE_UEL,"50270", null, "0"));
        //Employer - A,B,C,J - Type A
        niThresholds.add(createThreshold("2025-2026", TaxThreshold.TaxRegion.ALL_REGIONS, TaxThreshold.BandName.EMPLOYER_NI_TYPE_A, TaxThreshold.BandNameType.EMPLOYER_NI, NICBand.EMPLOYER_BELOW_ST,"0", "5000", "0"));
        niThresholds.add(createThreshold("2025-2026", TaxThreshold.TaxRegion.ALL_REGIONS, TaxThreshold.BandName.EMPLOYER_NI_TYPE_A, TaxThreshold.BandNameType.EMPLOYER_NI, NICBand.EMPLOYER_ST_TO_LEL,"5000", "6500", "15"));
        niThresholds.add(createThreshold("2025-2026", TaxThreshold.TaxRegion.ALL_REGIONS, TaxThreshold.BandName.EMPLOYER_NI_TYPE_A, TaxThreshold.BandNameType.EMPLOYER_NI,NICBand.EMPLOYER_LEL_TO_UST ,"6500", "25000", "15"));
        niThresholds.add(createThreshold("2025-2026", TaxThreshold.TaxRegion.ALL_REGIONS, TaxThreshold.BandName.EMPLOYER_NI_TYPE_A, TaxThreshold.BandNameType.EMPLOYER_NI,NICBand.EMPLOYER_UST_TO_UEL, "25000", "50270", "15"));
        niThresholds.add(createThreshold("2025-2026", TaxThreshold.TaxRegion.ALL_REGIONS, TaxThreshold.BandName.EMPLOYER_NI_TYPE_A, TaxThreshold.BandNameType.EMPLOYER_NI, NICBand.EMPLOYER_ABOVE_UEL,"50270", null, "15"));

        //Employer - D,E,F,I,K,L,N,S - Type B
        niThresholds.add(createThreshold("2025-2026", TaxThreshold.TaxRegion.ALL_REGIONS, TaxThreshold.BandName.EMPLOYER_NI_TYPE_B, TaxThreshold.BandNameType.EMPLOYER_NI,NICBand.EMPLOYER_BELOW_ST, "0", "5000", "0"));
        niThresholds.add(createThreshold("2025-2026", TaxThreshold.TaxRegion.ALL_REGIONS, TaxThreshold.BandName.EMPLOYER_NI_TYPE_B, TaxThreshold.BandNameType.EMPLOYER_NI, NICBand.EMPLOYER_ST_TO_LEL,"5000", "6500", "0"));
        niThresholds.add(createThreshold("2025-2026", TaxThreshold.TaxRegion.ALL_REGIONS, TaxThreshold.BandName.EMPLOYER_NI_TYPE_B, TaxThreshold.BandNameType.EMPLOYER_NI,NICBand.EMPLOYER_LEL_TO_UST , "6500", "25000", "0"));
        niThresholds.add(createThreshold("2025-2026", TaxThreshold.TaxRegion.ALL_REGIONS, TaxThreshold.BandName.EMPLOYER_NI_TYPE_B, TaxThreshold.BandNameType.EMPLOYER_NI, NICBand.EMPLOYER_UST_TO_UEL,"25000", "50270", "15"));
        niThresholds.add(createThreshold("2025-2026", TaxThreshold.TaxRegion.ALL_REGIONS, TaxThreshold.BandName.EMPLOYER_NI_TYPE_B, TaxThreshold.BandNameType.EMPLOYER_NI, NICBand.EMPLOYER_ABOVE_UEL,"50270", null, "15"));

        //Employer - H,M,V,Z - Type C
        niThresholds.add(createThreshold("2025-2026", TaxThreshold.TaxRegion.ALL_REGIONS, TaxThreshold.BandName.EMPLOYER_NI_TYPE_C, TaxThreshold.BandNameType.EMPLOYER_NI, NICBand.EMPLOYER_BELOW_ST,"0", "5000", "0"));
        niThresholds.add(createThreshold("2025-2026", TaxThreshold.TaxRegion.ALL_REGIONS, TaxThreshold.BandName.EMPLOYER_NI_TYPE_C, TaxThreshold.BandNameType.EMPLOYER_NI, NICBand.EMPLOYER_ST_TO_LEL,"5000", "6500", "0"));
        niThresholds.add(createThreshold("2025-2026", TaxThreshold.TaxRegion.ALL_REGIONS, TaxThreshold.BandName.EMPLOYER_NI_TYPE_C, TaxThreshold.BandNameType.EMPLOYER_NI,NICBand.EMPLOYER_LEL_TO_UST , "6500", "25000", "0"));
        niThresholds.add(createThreshold("2025-2026", TaxThreshold.TaxRegion.ALL_REGIONS, TaxThreshold.BandName.EMPLOYER_NI_TYPE_C, TaxThreshold.BandNameType.EMPLOYER_NI, NICBand.EMPLOYER_UST_TO_UEL,"25000", "50270", "0"));
        niThresholds.add(createThreshold("2025-2026", TaxThreshold.TaxRegion.ALL_REGIONS, TaxThreshold.BandName.EMPLOYER_NI_TYPE_C, TaxThreshold.BandNameType.EMPLOYER_NI,NICBand.EMPLOYER_ABOVE_UEL, "50270", null, "15"));

//        taxThresholdRepository.saveAll(Collections.unmodifiableList(niThresholds));
        return Collections.unmodifiableList(niThresholds);

    }
    private void initialize2024_2025TaxYear() {
        // Implementation for 2024-2025 tax year can be added here
        List<TaxThreshold> thresholds = new ArrayList<>();
        // England
        thresholds.add(createThreshold("2024-2025",TaxThreshold.TaxRegion.ENGLAND, TaxThreshold.BandName.PERSONAL_ALLOWANCE, TaxThreshold.BandNameType.INCOME_TAX,null,"0",     "12570", "0"));
        thresholds.add(createThreshold("2024-2025", TaxThreshold.TaxRegion.ENGLAND,TaxThreshold.BandName.BASIC_RATE,TaxThreshold.BandNameType.INCOME_TAX,   null,      "12571", "50270", "20"));
        thresholds.add(createThreshold("2024-2025", TaxThreshold.TaxRegion.ENGLAND,TaxThreshold.BandName.HIGHER_RATE,TaxThreshold.BandNameType.INCOME_TAX,  null,      "50271", "125140","40"));
        thresholds.add(createThreshold("2024-2025", TaxThreshold.TaxRegion.ENGLAND,TaxThreshold.BandName.ADDITIONAL_RATE,TaxThreshold.BandNameType.INCOME_TAX,  null,  "125140",null,    "45"));

        // Scotland
        thresholds.add(createThreshold("2024-2025", TaxThreshold.TaxRegion.SCOTLAND, TaxThreshold.BandName.PERSONAL_ALLOWANCE,TaxThreshold.BandNameType.INCOME_TAX,null,"0", "12570", "0"));
        thresholds.add(createThreshold("2024-2025", TaxThreshold.TaxRegion.SCOTLAND, TaxThreshold.BandName.STARTER_RATE,TaxThreshold.BandNameType.INCOME_TAX,null,"12571", "14876", "19"));
        thresholds.add(createThreshold("2024-2025", TaxThreshold.TaxRegion.SCOTLAND, TaxThreshold.BandName.BASIC_RATE,TaxThreshold.BandNameType.INCOME_TAX,null,"14877", "26561", "20"));
        thresholds.add(createThreshold("2024-2025", TaxThreshold.TaxRegion.SCOTLAND, TaxThreshold.BandName.INTERMEDIATE_RATE,TaxThreshold.BandNameType.INCOME_TAX,null,"26562", "43662", "21"));
        thresholds.add(createThreshold("2024-2025", TaxThreshold.TaxRegion.SCOTLAND, TaxThreshold.BandName.HIGHER_RATE,TaxThreshold.BandNameType.INCOME_TAX,null,"43663", "75000", "42"));
        thresholds.add(createThreshold("2024-2025", TaxThreshold.TaxRegion.SCOTLAND, TaxThreshold.BandName.ADVANCED_RATE,TaxThreshold.BandNameType.INCOME_TAX,null,"75001", "125140", "45"));
        thresholds.add(createThreshold("2024-2025", TaxThreshold.TaxRegion.SCOTLAND, TaxThreshold.BandName.TOP_RATE,TaxThreshold.BandNameType.INCOME_TAX,null,"125140", null, "48"));

        // Wales
        thresholds.add(createThreshold("2024-2025", TaxThreshold.TaxRegion.WALES, TaxThreshold.BandName.PERSONAL_ALLOWANCE,TaxThreshold.BandNameType.INCOME_TAX, null,"0",     "12570", "0"));
        thresholds.add(createThreshold("2024-2025", TaxThreshold.TaxRegion.WALES, TaxThreshold.BandName.BASIC_RATE,TaxThreshold.BandNameType.INCOME_TAX,    null,     "12571", "50270", "20"));
        thresholds.add(createThreshold("2024-2025", TaxThreshold.TaxRegion.WALES, TaxThreshold.BandName.HIGHER_RATE,TaxThreshold.BandNameType.INCOME_TAX, null,       "50271", "125140","40"));
        thresholds.add(createThreshold("2024-2025", TaxThreshold.TaxRegion.WALES, TaxThreshold.BandName.ADDITIONAL_RATE,TaxThreshold.BandNameType.INCOME_TAX,null,    "125140",null,    "45"));
        // Northern Ireland
        thresholds.add(createThreshold("2024-2025", TaxThreshold.TaxRegion.NORTHERN_IRELAND, TaxThreshold.BandName.PERSONAL_ALLOWANCE,TaxThreshold.BandNameType.INCOME_TAX, null,"0",     "12570", "0"));
        thresholds.add(createThreshold("2024-2025", TaxThreshold.TaxRegion.NORTHERN_IRELAND, TaxThreshold.BandName.BASIC_RATE,TaxThreshold.BandNameType.INCOME_TAX,  null,       "12571", "50270", "20"));
        thresholds.add(createThreshold("2024-2025", TaxThreshold.TaxRegion.NORTHERN_IRELAND, TaxThreshold.BandName.HIGHER_RATE,TaxThreshold.BandNameType.INCOME_TAX,    null,    "50271", "125140","40"));
        thresholds.add(createThreshold("2024-2025", TaxThreshold.TaxRegion.NORTHERN_IRELAND, TaxThreshold.BandName.ADDITIONAL_RATE,TaxThreshold.BandNameType.INCOME_TAX, null,   "125140",null,    "45"));


        thresholds.addAll(initializeNIData2024_2025TaxYear());
        taxThresholdRepository.saveAll(Collections.unmodifiableList(thresholds));
    }

    private List<TaxThreshold> initializeNIData2024_2025TaxYear(){
        List<TaxThreshold> niThresholds=new ArrayList<>();
        //Personal Allowance
        niThresholds.add(createThreshold("2024-2025", TaxThreshold.TaxRegion.ALL_REGIONS, TaxThreshold.BandName.PERSONAL_ALLOWANCE, TaxThreshold.BandNameType.PERSONAL_ALLOWANCE, null,"12570", "0", "0"));
        niThresholds.add(createThreshold("2024-2025", TaxThreshold.TaxRegion.ALL_REGIONS, TaxThreshold.BandName.EMPLOYMENT_ALLOWANCE, TaxThreshold.BandNameType.EMPLOYMENT_ALLOWANCE, null,"5000", "0", "0"));
        niThresholds.add(createThreshold("2024-2025", TaxThreshold.TaxRegion.ALL_REGIONS, TaxThreshold.BandName.EMPLOYEE_PENSION_CONTRIBUTION, TaxThreshold.BandNameType.PENSION_CONTRIBUTION, null,"6240", "50270", "5"));
        niThresholds.add(createThreshold("2024-2025", TaxThreshold.TaxRegion.ALL_REGIONS, TaxThreshold.BandName.EMPLOYER_PENSION_CONTRIBUTION, TaxThreshold.BandNameType.PENSION_CONTRIBUTION, null,"6240", "50270", "3"));
        niThresholds.add(createThreshold("2024-2025", TaxThreshold.TaxRegion.ALL_REGIONS, TaxThreshold.BandName.AUTO_ENROLMENT_PENSION_CONTRIBUTION, TaxThreshold.BandNameType.PENSION_CONTRIBUTION, null,"10000", "0", "0"));

        //Student Loans and Postgraduate Loans
        niThresholds.add(createThreshold("2024-2025", TaxThreshold.TaxRegion.ALL_REGIONS, TaxThreshold.BandName.STUDENT_LOAN_PLAN_1, TaxThreshold.BandNameType.STUDENT_LOAN, null,"24990", "27295", "9"));
        niThresholds.add(createThreshold("2024-2025", TaxThreshold.TaxRegion.ALL_REGIONS, TaxThreshold.BandName.STUDENT_LOAN_PLAN_2, TaxThreshold.BandNameType.STUDENT_LOAN, null,"27295", "31395", "9"));
        niThresholds.add(createThreshold("2024-2025", TaxThreshold.TaxRegion.ALL_REGIONS, TaxThreshold.BandName.STUDENT_LOAN_PLAN_4, TaxThreshold.BandNameType.STUDENT_LOAN, null,"31395", "0", "9"));
        niThresholds.add(createThreshold("2024-2025", TaxThreshold.TaxRegion.ALL_REGIONS, TaxThreshold.BandName.POSTGRADUATE_LOAN_PLAN_3, TaxThreshold.BandNameType.POSTGRADUATE_LOAN, null,"21000", "0", "6"));
        //Employee - A,F,H,M,N,V - Type A
        niThresholds.add(createThreshold("2024-2025", TaxThreshold.TaxRegion.ALL_REGIONS, TaxThreshold.BandName.EMPLOYEE_NI_TYPE_A, TaxThreshold.BandNameType.EMPLOYEE_NI, NICBand.EMPLOYEE_BELOW_LEL,"0", "6396", "0"));
        niThresholds.add(createThreshold("2024-2025", TaxThreshold.TaxRegion.ALL_REGIONS, TaxThreshold.BandName.EMPLOYEE_NI_TYPE_A, TaxThreshold.BandNameType.EMPLOYEE_NI, NICBand.EMPLOYEE_LEL_TO_PT,"6396", "12570", "0"));
        niThresholds.add(createThreshold("2024-2025", TaxThreshold.TaxRegion.ALL_REGIONS, TaxThreshold.BandName.EMPLOYEE_NI_TYPE_A, TaxThreshold.BandNameType.EMPLOYEE_NI, NICBand.EMPLOYEE_PT_TO_UEL,"12570", "50270", "8"));
        niThresholds.add(createThreshold("2024-2025", TaxThreshold.TaxRegion.ALL_REGIONS, TaxThreshold.BandName.EMPLOYEE_NI_TYPE_A, TaxThreshold.BandNameType.EMPLOYEE_NI, NICBand.EMPLOYEE_ABOVE_UEL,"50270", null, "2"));

        //Employee - D,J,L,Z - Type B
        niThresholds.add(createThreshold("2024-2025", TaxThreshold.TaxRegion.ALL_REGIONS, TaxThreshold.BandName.EMPLOYEE_NI_TYPE_B, TaxThreshold.BandNameType.EMPLOYEE_NI, NICBand.EMPLOYEE_BELOW_LEL,"0", "6396", "0"));
        niThresholds.add(createThreshold("2024-2025", TaxThreshold.TaxRegion.ALL_REGIONS, TaxThreshold.BandName.EMPLOYEE_NI_TYPE_B, TaxThreshold.BandNameType.EMPLOYEE_NI, NICBand.EMPLOYEE_LEL_TO_PT,"6396", "12570", "0"));
        niThresholds.add(createThreshold("2024-2025", TaxThreshold.TaxRegion.ALL_REGIONS, TaxThreshold.BandName.EMPLOYEE_NI_TYPE_B, TaxThreshold.BandNameType.EMPLOYEE_NI, NICBand.EMPLOYEE_PT_TO_UEL,"12570", "50270", "2"));
        niThresholds.add(createThreshold("2024-2025", TaxThreshold.TaxRegion.ALL_REGIONS, TaxThreshold.BandName.EMPLOYEE_NI_TYPE_B, TaxThreshold.BandNameType.EMPLOYEE_NI, NICBand.EMPLOYEE_ABOVE_UEL,"50270", null, "2"));
        //Employee - B,E,I - Type C
        niThresholds.add(createThreshold("2024-2025", TaxThreshold.TaxRegion.ALL_REGIONS, TaxThreshold.BandName.EMPLOYEE_NI_TYPE_C, TaxThreshold.BandNameType.EMPLOYEE_NI,NICBand.EMPLOYEE_BELOW_LEL, "0", "6396", "0"));
        niThresholds.add(createThreshold("2024-2025", TaxThreshold.TaxRegion.ALL_REGIONS, TaxThreshold.BandName.EMPLOYEE_NI_TYPE_C, TaxThreshold.BandNameType.EMPLOYEE_NI,NICBand.EMPLOYEE_LEL_TO_PT, "6396", "12570", "0"));
        niThresholds.add(createThreshold("2024-2025", TaxThreshold.TaxRegion.ALL_REGIONS, TaxThreshold.BandName.EMPLOYEE_NI_TYPE_C, TaxThreshold.BandNameType.EMPLOYEE_NI, NICBand.EMPLOYEE_PT_TO_UEL,"12570", "50270", "1.85"));
        niThresholds.add(createThreshold("2024-2025", TaxThreshold.TaxRegion.ALL_REGIONS, TaxThreshold.BandName.EMPLOYEE_NI_TYPE_C, TaxThreshold.BandNameType.EMPLOYEE_NI,NICBand.EMPLOYEE_ABOVE_UEL, "50270", null, "2"));
        //Employee - C,S - Type D
        niThresholds.add(createThreshold("2024-2025", TaxThreshold.TaxRegion.ALL_REGIONS, TaxThreshold.BandName.EMPLOYEE_NI_TYPE_D, TaxThreshold.BandNameType.EMPLOYEE_NI, NICBand.EMPLOYEE_BELOW_LEL,"0", "6396", "0"));
        niThresholds.add(createThreshold("2024-2025", TaxThreshold.TaxRegion.ALL_REGIONS, TaxThreshold.BandName.EMPLOYEE_NI_TYPE_D, TaxThreshold.BandNameType.EMPLOYEE_NI, NICBand.EMPLOYEE_LEL_TO_PT,"6396", "12570", "0"));
        niThresholds.add(createThreshold("2024-2025", TaxThreshold.TaxRegion.ALL_REGIONS, TaxThreshold.BandName.EMPLOYEE_NI_TYPE_D, TaxThreshold.BandNameType.EMPLOYEE_NI, NICBand.EMPLOYEE_PT_TO_UEL,"12570", "50270", "0"));
        niThresholds.add(createThreshold("2024-2025", TaxThreshold.TaxRegion.ALL_REGIONS, TaxThreshold.BandName.EMPLOYEE_NI_TYPE_D, TaxThreshold.BandNameType.EMPLOYEE_NI, NICBand.EMPLOYEE_ABOVE_UEL,"50270", null, "0"));

        // Employer - A,B,C,J - Type A
        niThresholds.add(createThreshold("2024-2025", TaxThreshold.TaxRegion.ALL_REGIONS, TaxThreshold.BandName.EMPLOYER_NI_TYPE_A, TaxThreshold.BandNameType.EMPLOYER_NI, NICBand.EMPLOYER_BELOW_ST, "0", "9100", "0"));
        niThresholds.add(createThreshold("2024-2025", TaxThreshold.TaxRegion.ALL_REGIONS, TaxThreshold.BandName.EMPLOYER_NI_TYPE_A, TaxThreshold.BandNameType.EMPLOYER_NI, NICBand.EMPLOYER_LEL_TO_UST, "9100", "25000", "13.8"));
        niThresholds.add(createThreshold("2024-2025", TaxThreshold.TaxRegion.ALL_REGIONS, TaxThreshold.BandName.EMPLOYER_NI_TYPE_A, TaxThreshold.BandNameType.EMPLOYER_NI, NICBand.EMPLOYER_UST_TO_UEL, "25000", "50270", "13.8"));
        niThresholds.add(createThreshold("2024-2025", TaxThreshold.TaxRegion.ALL_REGIONS, TaxThreshold.BandName.EMPLOYER_NI_TYPE_A, TaxThreshold.BandNameType.EMPLOYER_NI, NICBand.EMPLOYER_ABOVE_UEL, "50270", null, "13.8"));


        // Employer - D,E,F,I,K,L,N,S - Type B
        niThresholds.add(createThreshold("2024-2025", TaxThreshold.TaxRegion.ALL_REGIONS, TaxThreshold.BandName.EMPLOYER_NI_TYPE_B, TaxThreshold.BandNameType.EMPLOYER_NI, NICBand.EMPLOYER_BELOW_ST, "0", "9100", "0"));
        niThresholds.add(createThreshold("2024-2025", TaxThreshold.TaxRegion.ALL_REGIONS, TaxThreshold.BandName.EMPLOYER_NI_TYPE_B, TaxThreshold.BandNameType.EMPLOYER_NI, NICBand.EMPLOYER_LEL_TO_UST, "9100", "25000", "0")); // exempt
        niThresholds.add(createThreshold("2024-2025", TaxThreshold.TaxRegion.ALL_REGIONS, TaxThreshold.BandName.EMPLOYER_NI_TYPE_B, TaxThreshold.BandNameType.EMPLOYER_NI, NICBand.EMPLOYER_UST_TO_UEL, "25000", "50270", "13.8"));
        niThresholds.add(createThreshold("2024-2025", TaxThreshold.TaxRegion.ALL_REGIONS, TaxThreshold.BandName.EMPLOYER_NI_TYPE_B, TaxThreshold.BandNameType.EMPLOYER_NI, NICBand.EMPLOYER_ABOVE_UEL, "50270", null, "13.8"));

        // Employer - H,M,V,Z - Type C
        niThresholds.add(createThreshold("2024-2025", TaxThreshold.TaxRegion.ALL_REGIONS, TaxThreshold.BandName.EMPLOYER_NI_TYPE_C, TaxThreshold.BandNameType.EMPLOYER_NI, NICBand.EMPLOYER_BELOW_ST, "0", "9100", "0"));
        niThresholds.add(createThreshold("2024-2025", TaxThreshold.TaxRegion.ALL_REGIONS, TaxThreshold.BandName.EMPLOYER_NI_TYPE_C, TaxThreshold.BandNameType.EMPLOYER_NI, NICBand.EMPLOYER_LEL_TO_UST, "9100", "25000", "0")); // Full relief up to UEL
        niThresholds.add(createThreshold("2024-2025", TaxThreshold.TaxRegion.ALL_REGIONS, TaxThreshold.BandName.EMPLOYER_NI_TYPE_C, TaxThreshold.BandNameType.EMPLOYER_NI, NICBand.EMPLOYER_UST_TO_UEL, "25000", "50270", "0"));
        niThresholds.add(createThreshold("2024-2025", TaxThreshold.TaxRegion.ALL_REGIONS, TaxThreshold.BandName.EMPLOYER_NI_TYPE_C, TaxThreshold.BandNameType.EMPLOYER_NI, NICBand.EMPLOYER_ABOVE_UEL, "50270", null, "13.8"));



        return Collections.unmodifiableList(niThresholds);

    }
}

