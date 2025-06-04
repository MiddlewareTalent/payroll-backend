package com.payroll.uk.payroll_processing.config;

import com.payroll.uk.payroll_processing.entity.TaxThreshold;
import com.payroll.uk.payroll_processing.repository.TaxThresholdRepository;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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
//        if (taxThresholdRepository.count() == 0) {
//            initialize2025_2026TaxYear();
//            // initialize2024_2025TaxYear(); // Uncomment when needed
//        }

     /*   if(taxThresholdRepository.existsByTaxYear("2025-2026")){
            taxThresholdRepository.deleteByTaxYear("2025-2026");
            System.out.println("Deleted existing tax data for 2025-2026");
        }*/


        try{
            if (!taxThresholdRepository.existsByTaxYear("2025-2026")) {
                initialize2025_2026TaxYear();
            }
        }
        catch (Exception e) {
            System.err.println("Error initializing tax data for 2025-2026: " + e.getMessage());
        }


    }


   /* @PostConstruct
    @Transactional
    public void initializeTaxData() {
        final String taxYear = "2025-2026";

        // Step 1: Delete if tax year already exists
        if (taxThresholdRepository.existsByTaxYear("2025-2026")) {
            taxThresholdRepository.deleteByTaxYear("2025-2026");
            System.out.println("Deleted existing tax data for " + taxYear);
        }

        // Step 2: Insert new data
//        try {
//            if (!taxThresholdRepository.existsByTaxYear(taxYear)) {
//                initialize2025_2026TaxYear();
//                System.out.println("Initialized tax data for " + taxYear);
//            }
//        } catch (Exception e) {
//            System.err.println("Error initializing tax data for " + taxYear + ": " + e.getMessage());
//        }

        // Step 3: Delete oldest tax years if more than 5
        List<String> allTaxYears = taxThresholdRepository.findAllTaxYearsOrdered();
        if (allTaxYears.size() > 5) {
            List<String> yearsToDelete = allTaxYears.subList(0, allTaxYears.size() - 5);
            for (String year : yearsToDelete) {
                taxThresholdRepository.deleteByTaxYear(year);
                System.out.println("Deleted tax data for old tax year: " + year);
            }
        }
    }*/

    private TaxThreshold createThreshold(String taxYear,
                                         TaxThreshold.TaxRegion region,
                                         String bandName,
                                         TaxThreshold.TaxBandType bandNameType,
                                         String lowerBound,
                                         String upperBound,
                                         String ratePercent) {
        TaxThreshold threshold = new TaxThreshold();
        threshold.setTaxYear(taxYear);
        threshold.setRegion(region);
        threshold.setBandName(bandName);
        threshold.setBandNameType(bandNameType);
        threshold.setLowerBound(new BigDecimal(lowerBound));
        threshold.setUpperBound(upperBound != null ? new BigDecimal(upperBound) : null);
        threshold.setRate(new BigDecimal(ratePercent).divide(new BigDecimal(100)));

        return threshold;
    }


    private void initialize2025_2026TaxYear() {
        // Implementation for 2025-2026 tax year
        List<TaxThreshold> thresholds = new ArrayList<>();
                // England
                thresholds.add(createThreshold("2025-2026",TaxThreshold.TaxRegion.ENGLAND, "Personal Allowance", TaxThreshold.TaxBandType.INCOME_TAX,"0",     "12570", "0"));
                thresholds.add(createThreshold("2025-2026", TaxThreshold.TaxRegion.ENGLAND,"Basic rate",TaxThreshold.TaxBandType.INCOME_TAX,         "12571", "50270", "20"));
                thresholds.add(createThreshold("2025-2026", TaxThreshold.TaxRegion.ENGLAND,"Higher rate",TaxThreshold.TaxBandType.INCOME_TAX,        "50271", "125140","40"));
                thresholds.add(createThreshold("2025-2026", TaxThreshold.TaxRegion.ENGLAND,"Additional rate",TaxThreshold.TaxBandType.INCOME_TAX,    "125140",null,    "45"));
                thresholds.add(createThreshold("2025-2026", TaxThreshold.TaxRegion.ENGLAND,"NI Class 1", TaxThreshold.TaxBandType.NATIONAL_INSURANCE,"12570", "50270", "8"));
                thresholds.add(createThreshold("2025-2026", TaxThreshold.TaxRegion.ENGLAND,"NI Class 1", TaxThreshold.TaxBandType.NATIONAL_INSURANCE,"50270", null,    "2"));
                thresholds.add(createThreshold("2025-2026", TaxThreshold.TaxRegion.ENGLAND,"NI Class 1A", TaxThreshold.TaxBandType.EMPLOYER_NATIONAL_INSURANCE,"5000",     null,    "15"));
                thresholds.add(createThreshold("2025-2026", TaxThreshold.TaxRegion.ENGLAND,"NI Class 1A", TaxThreshold.TaxBandType.EMPLOYMENT_ALLOWANCE,"10500",     null,    "0"));
                // Scotland
                thresholds.add(createThreshold("2025-2026", TaxThreshold.TaxRegion.SCOTLAND, "Personal Allowance",TaxThreshold.TaxBandType.INCOME_TAX,"0", "12570", "0"));
                thresholds.add(createThreshold("2025-2026", TaxThreshold.TaxRegion.SCOTLAND, "Starter rate",TaxThreshold.TaxBandType.INCOME_TAX,"12571", "15397", "19"));
                thresholds.add(createThreshold("2025-2026", TaxThreshold.TaxRegion.SCOTLAND, "Basic rate",TaxThreshold.TaxBandType.INCOME_TAX,"15398", "27491", "20"));
                thresholds.add(createThreshold("2025-2026", TaxThreshold.TaxRegion.SCOTLAND, "Intermediate rate",TaxThreshold.TaxBandType.INCOME_TAX,"27492", "43662", "21"));
                thresholds.add(createThreshold("2025-2026", TaxThreshold.TaxRegion.SCOTLAND, "Higher rate",TaxThreshold.TaxBandType.INCOME_TAX,"43663", "75000", "42"));
                thresholds.add(createThreshold("2025-2026", TaxThreshold.TaxRegion.SCOTLAND, "Advanced rate",TaxThreshold.TaxBandType.INCOME_TAX,"75001", "125140", "45"));
                thresholds.add(createThreshold("2025-2026", TaxThreshold.TaxRegion.SCOTLAND, "Top rate",TaxThreshold.TaxBandType.INCOME_TAX,"125140", null, "48"));
                thresholds.add(createThreshold("2025-2026", TaxThreshold.TaxRegion.SCOTLAND, "NI Class 1", TaxThreshold.TaxBandType.NATIONAL_INSURANCE,"12570","50270","8"));
                thresholds.add(createThreshold("2025-2026", TaxThreshold.TaxRegion.SCOTLAND, "NI Class 1", TaxThreshold.TaxBandType.NATIONAL_INSURANCE,"50270",null,"2"));
                thresholds.add(createThreshold("2025-2026", TaxThreshold.TaxRegion.SCOTLAND,"NI Class 1A", TaxThreshold.TaxBandType.EMPLOYER_NATIONAL_INSURANCE,"5000",     null,    "15"));
                thresholds.add(createThreshold("2025-2026", TaxThreshold.TaxRegion.SCOTLAND,"NI Class 1A", TaxThreshold.TaxBandType.EMPLOYMENT_ALLOWANCE,"10500",     null,    "0"));


        // Wales
                thresholds.add(createThreshold("2025-2026", TaxThreshold.TaxRegion.WALES, "Personal Allowance",TaxThreshold.TaxBandType.INCOME_TAX, "0",     "12570", "0"));
                thresholds.add(createThreshold("2025-2026", TaxThreshold.TaxRegion.WALES, "Basic rate",TaxThreshold.TaxBandType.INCOME_TAX,         "12571", "50270", "20"));
                thresholds.add(createThreshold("2025-2026", TaxThreshold.TaxRegion.WALES, "Higher rate",TaxThreshold.TaxBandType.INCOME_TAX,        "50271", "125140","40"));
                thresholds.add(createThreshold("2025-2026", TaxThreshold.TaxRegion.WALES, "Additional rate",TaxThreshold.TaxBandType.INCOME_TAX,    "125140",null,    "45"));
                thresholds.add(createThreshold("2025-2026", TaxThreshold.TaxRegion.WALES, "NI Class 1", TaxThreshold.TaxBandType.NATIONAL_INSURANCE,"12570", "50270", "8"));
                thresholds.add(createThreshold("2025-2026", TaxThreshold.TaxRegion.WALES, "NI Class 1", TaxThreshold.TaxBandType.NATIONAL_INSURANCE,"50270", null,    "2"));
                thresholds.add(createThreshold("2025-2026", TaxThreshold.TaxRegion.WALES,"NI Class 1A", TaxThreshold.TaxBandType.EMPLOYER_NATIONAL_INSURANCE,"5000",     null,    "15"));
                thresholds.add(createThreshold("2025-2026", TaxThreshold.TaxRegion.WALES,"NI Class 1A", TaxThreshold.TaxBandType.EMPLOYMENT_ALLOWANCE,"10500",     null,    "0"));


        // Northern Ireland
                thresholds.add(createThreshold("2025-2026", TaxThreshold.TaxRegion.NORTHERN_IRELAND, "Personal Allowance",TaxThreshold.TaxBandType.INCOME_TAX, "0",     "12570", "0"));
                thresholds.add(createThreshold("2025-2026", TaxThreshold.TaxRegion.NORTHERN_IRELAND, "Basic rate",TaxThreshold.TaxBandType.INCOME_TAX,         "12571", "50270", "20"));
                thresholds.add(createThreshold("2025-2026", TaxThreshold.TaxRegion.NORTHERN_IRELAND, "Higher rate",TaxThreshold.TaxBandType.INCOME_TAX,        "50271", "125140","40"));
                thresholds.add(createThreshold("2025-2026", TaxThreshold.TaxRegion.NORTHERN_IRELAND, "Additional rate",TaxThreshold.TaxBandType.INCOME_TAX,    "125140",null,    "45"));
                thresholds.add(createThreshold("2025-2026", TaxThreshold.TaxRegion.NORTHERN_IRELAND, "NI Class 1", TaxThreshold.TaxBandType.NATIONAL_INSURANCE,"12570", "50270", "8"));
                thresholds.add(createThreshold("2025-2026", TaxThreshold.TaxRegion.NORTHERN_IRELAND, "NI Class 1", TaxThreshold.TaxBandType.NATIONAL_INSURANCE,"50270", null,    "2"));
                thresholds.add(createThreshold("2025-2026", TaxThreshold.TaxRegion.NORTHERN_IRELAND, "NI Class 1A", TaxThreshold.TaxBandType.EMPLOYER_NATIONAL_INSURANCE,"5000",     null,    "15"));
                thresholds.add(createThreshold("2025-2026", TaxThreshold.TaxRegion.NORTHERN_IRELAND, "NI Class 1A", TaxThreshold.TaxBandType.EMPLOYMENT_ALLOWANCE,"10500",     null,    "0"));


        taxThresholdRepository.saveAll(Collections.unmodifiableList(thresholds));

    }
    private void initialize2024_2025TaxYear() {
        // Implementation for 2024-2025 tax year can be added here
        List<TaxThreshold> thresholds = new ArrayList<>();
        // England
        thresholds.add(createThreshold("2024-2025", TaxThreshold.TaxRegion.ENGLAND, "Personal Allowance", TaxThreshold.TaxBandType.INCOME_TAX, "0",     "12570", "0"));
        thresholds.add(createThreshold("2024-2025", TaxThreshold.TaxRegion.ENGLAND, "Basic rate", TaxThreshold.TaxBandType.INCOME_TAX,         "12571", "50270", "20"));
        thresholds.add(createThreshold("2024-2025", TaxThreshold.TaxRegion.ENGLAND, "Higher rate", TaxThreshold.TaxBandType.INCOME_TAX,        "50271", "125140","40"));
        thresholds.add(createThreshold("2024-2025", TaxThreshold.TaxRegion.ENGLAND, "Additional rate", TaxThreshold.TaxBandType.INCOME_TAX,    "125140",null,    "45"));
        thresholds.add(createThreshold("2024-2025", TaxThreshold.TaxRegion.ENGLAND, "NI Class 1", TaxThreshold.TaxBandType.NATIONAL_INSURANCE, "12570", "50270", "8"));
        thresholds.add(createThreshold("2024-2025", TaxThreshold.TaxRegion.ENGLAND, "NI Class 1", TaxThreshold.TaxBandType.NATIONAL_INSURANCE, "50270", null,    "2"));
        // Scotland
        thresholds.add(createThreshold("2024-2025", TaxThreshold.TaxRegion.SCOTLAND, "Personal Allowance", TaxThreshold.TaxBandType.INCOME_TAX, "0",     "12570", "0"));
        thresholds.add(createThreshold("2024-2025", TaxThreshold.TaxRegion.SCOTLAND, "Starter rate", TaxThreshold.TaxBandType.INCOME_TAX,       "12571", "14876", "19"));
        thresholds.add(createThreshold("2024-2025", TaxThreshold.TaxRegion.SCOTLAND, "Basic rate", TaxThreshold.TaxBandType.INCOME_TAX,         "14877", "26561", "20"));
        thresholds.add(createThreshold("2024-2025", TaxThreshold.TaxRegion.SCOTLAND, "Intermediate rate", TaxThreshold.TaxBandType.INCOME_TAX,  "26562", "43662", "21"));
        thresholds.add(createThreshold("2024-2025", TaxThreshold.TaxRegion.SCOTLAND, "Higher rate", TaxThreshold.TaxBandType.INCOME_TAX,        "43663", "75000", "42"));
        thresholds.add(createThreshold("2024-2025", TaxThreshold.TaxRegion.SCOTLAND, "Advanced rate", TaxThreshold.TaxBandType.INCOME_TAX,      "75001", "125140","45"));
        thresholds.add(createThreshold("2024-2025", TaxThreshold.TaxRegion.SCOTLAND, "Top rate", TaxThreshold.TaxBandType.INCOME_TAX,           "125140",null,    "48"));
        thresholds.add(createThreshold("2024-2025", TaxThreshold.TaxRegion.SCOTLAND, "NI Class 1", TaxThreshold.TaxBandType.NATIONAL_INSURANCE, "12570", "50270", "8"));
        thresholds.add(createThreshold("2024-2025", TaxThreshold.TaxRegion.SCOTLAND, "NI Class 1", TaxThreshold.TaxBandType.NATIONAL_INSURANCE, "50270", null,    "2"));
        // Wales
        thresholds.add(createThreshold("2024-2025", TaxThreshold.TaxRegion.WALES, "Personal Allowance", TaxThreshold.TaxBandType.INCOME_TAX, "0",     "12570", "0"));
        thresholds.add(createThreshold("2024-2025", TaxThreshold.TaxRegion.WALES, "Basic rate", TaxThreshold.TaxBandType.INCOME_TAX,         "12571", "50270", "20"));
        thresholds.add(createThreshold("2024-2025", TaxThreshold.TaxRegion.WALES, "Higher rate", TaxThreshold.TaxBandType.INCOME_TAX,        "50271", "125140","40"));
        thresholds.add(createThreshold("2024-2025", TaxThreshold.TaxRegion.WALES, "Additional rate", TaxThreshold.TaxBandType.INCOME_TAX,    "125140",null,    "45"));
        thresholds.add(createThreshold("2024-2025", TaxThreshold.TaxRegion.WALES, "NI Class 1", TaxThreshold.TaxBandType.NATIONAL_INSURANCE, "12570", "50270", "8"));
        thresholds.add(createThreshold("2024-2025", TaxThreshold.TaxRegion.WALES, "NI Class 1", TaxThreshold.TaxBandType.NATIONAL_INSURANCE, "50270", null,    "2"));
        // Northern Ireland
        thresholds.add(createThreshold("2024-2025", TaxThreshold.TaxRegion.NORTHERN_IRELAND, "Personal Allowance", TaxThreshold.TaxBandType.INCOME_TAX, "0",     "12570", "0"));
        thresholds.add(createThreshold("2024-2025", TaxThreshold.TaxRegion.NORTHERN_IRELAND, "Basic rate", TaxThreshold.TaxBandType.INCOME_TAX,         "12571", "50270", "20"));
        thresholds.add(createThreshold("2024-2025", TaxThreshold.TaxRegion.NORTHERN_IRELAND, "Higher rate", TaxThreshold.TaxBandType.INCOME_TAX,        "50271", "125140","40"));
        thresholds.add(createThreshold("2024-2025", TaxThreshold.TaxRegion.NORTHERN_IRELAND, "Additional rate", TaxThreshold.TaxBandType.INCOME_TAX,    "125140",null,    "45"));
        thresholds.add(createThreshold("2024-2025", TaxThreshold.TaxRegion.NORTHERN_IRELAND, "NI Class 1", TaxThreshold.TaxBandType.NATIONAL_INSURANCE, "12570", "50270", "8"));
        thresholds.add(createThreshold("2024-2025", TaxThreshold.TaxRegion.NORTHERN_IRELAND, "NI Class 1", TaxThreshold.TaxBandType.NATIONAL_INSURANCE, "50270", null,    "2"));
        taxThresholdRepository.saveAll(Collections.unmodifiableList(thresholds));
    }
}

