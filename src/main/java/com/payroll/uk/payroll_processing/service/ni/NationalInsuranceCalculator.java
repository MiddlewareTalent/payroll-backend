package com.payroll.uk.payroll_processing.service.ni;

import com.payroll.uk.payroll_processing.entity.NICategoryLetters;
import com.payroll.uk.payroll_processing.entity.TaxThreshold;
import com.payroll.uk.payroll_processing.exception.DataValidationException;
import com.payroll.uk.payroll_processing.service.TaxThresholdService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

@Service
//@Slf4j
public class NationalInsuranceCalculator {
    private static final Logger logger = LoggerFactory.getLogger(NationalInsuranceCalculator.class);
    private final TaxThresholdService taxThresholdService;

    public NationalInsuranceCalculator(TaxThresholdService taxThresholdService) {
        this.taxThresholdService = taxThresholdService;
    }


    public HashMap<String,BigDecimal> calculateEmployeeNIContribution(BigDecimal income, String taxYear,  String payPeriod, NICategoryLetters niCategoryLetter){
//        System.out.println("Calculating Employee NI Contribution for income: " + income + ", taxYear: " + taxYear + ", payPeriod: " + payPeriod + ", NI Category Letter: " + niCategoryLetter);
        if (income == null || payPeriod == null || niCategoryLetter == null) {
            throw new DataValidationException("Income, pay period, and NI category letter cannot be null");
        }
        HashMap<String,BigDecimal> niContributionMap = new LinkedHashMap<>();
         BigDecimal earningsAtLEL= BigDecimal.ZERO; // Earnings at LEL YTD
         BigDecimal earningsLelToPt= BigDecimal.ZERO; // Earnings at LEL to PT YTD
         BigDecimal earningsPtToUel= BigDecimal.ZERO; // Earnings at PT to UEL YTD

        BigDecimal niContribution = BigDecimal.ZERO;
        // Category X = no NI due
        if(NICategoryLetters.X ==niCategoryLetter){
            niContributionMap.put("earningsAtLEL", BigDecimal.ZERO);
            niContributionMap.put("earningsLelToPt", BigDecimal.ZERO);
            niContributionMap.put("earningsPtToUel", BigDecimal.ZERO);
            niContributionMap.put("niContribution", BigDecimal.ZERO);
            return niContributionMap;
        }
        // Get thresholds & rates
       BigDecimal[][] niSlabs=taxThresholdService.getEmployeeNIThreshold(taxYear, TaxThreshold.TaxRegion.ALL_REGIONS, TaxThreshold.BandNameType.EMPLOYEE_NI,niCategoryLetter);
       BigDecimal[] niRates=taxThresholdService.getEmployeeNIRates(taxYear,TaxThreshold.TaxRegion.ALL_REGIONS, TaxThreshold.BandNameType.EMPLOYEE_NI,niCategoryLetter);

        BigDecimal lowerEarningsLimit =niSlabs[0][1];//6500
        BigDecimal primaryThreshold=niSlabs[1][1];//12570
        BigDecimal upperEarningsLimit=niSlabs[2][1];  //50270
        BigDecimal aboveUpperEarningsLimit=niSlabs[3][0];  //above 50270
//        System.out.println("Threshold 0: " + belowLowerEarningsLimit);
//        System.out.println("Threshold 1: " + primaryThreshold);
//        System.out.println("Threshold 2: " + upperEarningsLimit);
//        System.out.println("Threshold 3: " + aboveUpperEarningsLimit);

        BigDecimal BLEL_Rate=niRates[0];  //0
        BigDecimal PT_Rate=niRates[1];  //0
        BigDecimal UEL_Rate=niRates[2];  //8
        BigDecimal ABOVE_UEL_Rate=niRates[3];  //2

//        System.out.println("Threshold Rate 0: " + BLEL_Rate);
//        System.out.println("Threshold Rate 1: " + PT_Rate);
//        System.out.println("Threshold Rate 2: " + UEL_Rate);
//        System.out.println("Threshold Rate 3: " + ABOVE_UEL_Rate);

        BigDecimal grossIncome = calculateGrossSalary(income, payPeriod);
        // Above UEL band
        if (grossIncome.compareTo(upperEarningsLimit) > 0) {
            BigDecimal bandAboveUel  = grossIncome.subtract(upperEarningsLimit);
            niContribution = niContribution.add(bandAboveUel .multiply(ABOVE_UEL_Rate));

            logger.info("NI Contribution Above UEL band: {}",  niContribution);
            grossIncome = upperEarningsLimit;
        }
        // PT to UEL band
        if (grossIncome.compareTo(primaryThreshold) > 0) {
            BigDecimal bandPtToUel  = grossIncome.subtract(primaryThreshold);
            earningsPtToUel = bandPtToUel; // store for YTD output
            niContribution = niContribution.add(bandPtToUel .multiply(UEL_Rate));

            logger.info("NI Contribution PT to UEL band: {}" , niContribution);
            grossIncome= primaryThreshold;
        }
        // LEL to PT band
        if (grossIncome.compareTo(lowerEarningsLimit) > 0) {
            BigDecimal bandLelToPt  = grossIncome.subtract(lowerEarningsLimit);
            earningsLelToPt = bandLelToPt;
            niContribution = niContribution.add(bandLelToPt .multiply(PT_Rate));

            logger.info("NI Contribution LEL to PT band: {}",  niContribution);
            grossIncome = lowerEarningsLimit;
//           niContribution =niContribution.add(grossIncome.multiply(thresholdRate1));
        }
        // Below LEL band
        if (grossIncome.compareTo(lowerEarningsLimit) <= 0) {
            earningsAtLEL = grossIncome; // store for YTD output
            niContribution = niContribution.add(grossIncome.multiply(BLEL_Rate));
        } else {
            earningsAtLEL= lowerEarningsLimit; // reached LEL fully
        }
        logger.info("NI Contribution: {} " , niContribution);
        niContributionMap.put("earningsAtLEL", earningsAtLEL);
        niContributionMap.put("earningsLelToPt", earningsLelToPt);
        niContributionMap.put("earningsPtToUel", earningsPtToUel);
        niContributionMap.put("niContribution", niContribution);

        // Adjust all amounts in the map based on pay period
        niContributionMap.replaceAll((key, value) -> calculateIncomeTaxBasedOnPayPeriod(value, payPeriod));
        logger.info("NI Contribution Map: {}", niContributionMap);

        return niContributionMap;

    }

    private BigDecimal calculateGrossSalary(BigDecimal grossIncome,String payPeriod){
        return switch (payPeriod.toUpperCase()) {
            case "WEEKLY" -> grossIncome.multiply(BigDecimal.valueOf(52));
            case "MONTHLY" -> grossIncome.multiply(BigDecimal.valueOf(12));
            case "YEARLY" -> grossIncome;
            default -> throw new DataValidationException("Invalid pay period. Must be WEEKLY, MONTHLY or YEARLY");
        };


    }

    private BigDecimal  calculateIncomeTaxBasedOnPayPeriod(BigDecimal incomeTax,String payPeriod){
        return switch (payPeriod.toUpperCase()) {
            case "WEEKLY" -> incomeTax.divide(BigDecimal.valueOf(52), 4, RoundingMode.HALF_UP);
            case "MONTHLY" -> incomeTax.divide(BigDecimal.valueOf(12), 4, RoundingMode.HALF_UP);
            case "YEARLY" -> incomeTax;
            default -> throw new DataValidationException("Invalid pay period. Must be WEEKLY, MONTHLY or YEARLY");
        };
    }

    // Updated Employer NI Contribution Logic for All Categories
    public BigDecimal calculatingEmployerNIContribution(BigDecimal income, String taxYear,  String payPeriod, NICategoryLetters niCategoryLetter) {
        if (income == null || payPeriod == null || niCategoryLetter == null) {
            throw new DataValidationException("Income, pay period, and NI category letter cannot be null");
        }

        BigDecimal employerNIContributions = BigDecimal.ZERO;
        BigDecimal[][] niSlabs = taxThresholdService.getEmployerThreshold(
                taxYear,
                TaxThreshold.TaxRegion.ALL_REGIONS,
                TaxThreshold.BandNameType.EMPLOYER_NI,
                niCategoryLetter);

        BigDecimal[] niRates = taxThresholdService.getEmployerRates(
                taxYear,
                TaxThreshold.TaxRegion.ALL_REGIONS,
                TaxThreshold.BandNameType.EMPLOYER_NI,
                niCategoryLetter);

        BigDecimal grossIncome = calculateGrossSalary(income, payPeriod);
        logger.info("Gross Income in Employer: {}" ,grossIncome);

        // Handle categories with flat 15% over secondary threshold (A, B, C, J, etc.)
        if (List.of(NICategoryLetters.A, NICategoryLetters.B, NICategoryLetters.C, NICategoryLetters.J).contains(niCategoryLetter)) {
            BigDecimal threshold = niSlabs[0][1]; // e.g., £5000 annually
            if (grossIncome.compareTo(threshold) > 0) {
                BigDecimal niableAmount = grossIncome.subtract(threshold);
                employerNIContributions = niableAmount.multiply(niRates[1]); // Assuming niRates[1] = 15%
            }
            return calculateIncomeTaxBasedOnPayPeriod(employerNIContributions, payPeriod);
        }

        // Handle categories with full exemption up to upper secondary threshold (H, M, V, Z)
        if (List.of(NICategoryLetters.H, NICategoryLetters.M, NICategoryLetters.V, NICategoryLetters.Z).contains(niCategoryLetter)) {
//            BigDecimal upperSecondaryThreshold = niSlabs[3][1]; // e.g., £50,270 annually
//            upperSecondaryThreshold=upperSecondaryThreshold!=null?upperSecondaryThreshold:niSlabs[3][0];
            logger.info("M Category Block");
            BigDecimal upperSecondaryThreshold;
            if (taxYear.equalsIgnoreCase("2025-2026")) {
                upperSecondaryThreshold = niSlabs[3][1]; // £50270

            }
            else if (taxYear.equalsIgnoreCase("2024-2025")){
                upperSecondaryThreshold=niSlabs[3][0];

            }
            else {
                upperSecondaryThreshold=BigDecimal.ZERO;

            }

            if (grossIncome.compareTo(upperSecondaryThreshold) > 0) {
                BigDecimal niableAmount = grossIncome.subtract(upperSecondaryThreshold);
                employerNIContributions = niableAmount.multiply(niRates[niRates.length-1]); // Assuming niRates[4] = 15%
            }
            return calculateIncomeTaxBasedOnPayPeriod(employerNIContributions, payPeriod);
        }

        // Handle all remaining categories (D, E, F, I, K, L, N, S)
        if (List.of(NICategoryLetters.D, NICategoryLetters.E, NICategoryLetters.F, NICategoryLetters.I,
                NICategoryLetters.K, NICategoryLetters.L, NICategoryLetters.N, NICategoryLetters.S).contains(niCategoryLetter)) {
            BigDecimal threshold;
            if (taxYear.equalsIgnoreCase("2025-2026")) {
                threshold = niSlabs[2][1]; // £25000
            }
            else if (taxYear.equalsIgnoreCase("2024-2025")){
                threshold=niSlabs[2][0];
            }
            else {
                threshold=BigDecimal.ZERO;
            }
            if (grossIncome.compareTo(threshold) > 0) {
                BigDecimal niableAmount = grossIncome.subtract(threshold);
                employerNIContributions = niableAmount.multiply(niRates[3]); // Assuming 15%
            }
            return calculateIncomeTaxBasedOnPayPeriod(employerNIContributions, payPeriod);
        }

        return BigDecimal.ZERO;
    }

    public BigDecimal calculateEmployerNIContribution(BigDecimal income, String taxYear,  String payPeriod, NICategoryLetters niCategoryLetter) {
        if (income == null || payPeriod == null || niCategoryLetter == null) {
            throw new IllegalArgumentException("Income, pay period, and NI category letter cannot be null");
        }

        BigDecimal employerNIContributions ;
        BigDecimal[][] niSlabs=taxThresholdService.getEmployerThreshold(taxYear, TaxThreshold.TaxRegion.ALL_REGIONS, TaxThreshold.BandNameType.EMPLOYER_NI,niCategoryLetter);
        BigDecimal[] niRates=taxThresholdService.getEmployerRates(taxYear, TaxThreshold.TaxRegion.ALL_REGIONS, TaxThreshold.BandNameType.EMPLOYER_NI,niCategoryLetter);
        BigDecimal secondaryThreshold1 = niSlabs[0][1]; //5000
        BigDecimal secondaryThreshold2 = niSlabs[1][1]; //6500
        BigDecimal secondaryThreshold3 = niSlabs[2][1]; //25000
        BigDecimal secondaryThreshold4 = niSlabs[3][1]; //50270
        BigDecimal secondaryThreshold5 = niSlabs[4][1]; //above 50270

//        System.out.println("secondaryThreshold1 "+secondaryThreshold1);
//        System.out.println("secondaryThreshold2 "+secondaryThreshold2);
//        System.out.println("secondaryThreshold3 "+secondaryThreshold3);
//        System.out.println("secondaryThreshold4 "+secondaryThreshold4);
//        System.out.println("secondaryThreshold5 "+secondaryThreshold5);

//        BigDecimal secondaryThreshold1 = BigDecimal.valueOf(5000);
//        BigDecimal secondaryThreshold2 = BigDecimal.valueOf(6500);
//        BigDecimal secondaryThreshold3 = BigDecimal.valueOf(25000);
//        BigDecimal secondaryThreshold4 = BigDecimal.valueOf(50270);
//        BigDecimal secondaryThreshold5 = BigDecimal.valueOf(50270);

        // Rates for each threshold
        BigDecimal secondaryRate1 = niRates[0]; //0
        BigDecimal secondaryRate2 = niRates[1]; 
        BigDecimal secondaryRate3 = niRates[2];
        BigDecimal secondaryRate4 = niRates[3];
        BigDecimal secondaryRate5 = niRates[4];

//        System.out.println("secondaryRate1: "+secondaryRate1);
//        System.out.println("secondaryRate2: "+secondaryRate2);
//        System.out.println("secondaryRate3: "+secondaryRate3);
//        System.out.println("secondaryRate4: "+secondaryRate4);
//        System.out.println("secondaryRate5: "+secondaryRate5);

        BigDecimal grossIncome = calculateGrossSalary(income, payPeriod);
        logger.info("Gross Income in Employer: {}",grossIncome);
        if(grossIncome.compareTo(secondaryThreshold1)<=0){
            employerNIContributions= grossIncome.multiply(secondaryRate1);
            return calculateIncomeTaxBasedOnPayPeriod(employerNIContributions,payPeriod);
        }
        else if (grossIncome.compareTo(secondaryThreshold1)>0 && grossIncome.compareTo(secondaryThreshold2)<=0) {
            BigDecimal aboveSecondaryThreshold1 = grossIncome.subtract(secondaryThreshold1);
            employerNIContributions= aboveSecondaryThreshold1.multiply(secondaryRate2).setScale(4, RoundingMode.HALF_UP);
            return calculateIncomeTaxBasedOnPayPeriod(employerNIContributions,payPeriod);

        } else if (grossIncome.compareTo(secondaryThreshold2)>0 && grossIncome.compareTo(secondaryThreshold3)<=0){
            BigDecimal aboveSecondaryThreshold2 = grossIncome.subtract(secondaryThreshold2);
            employerNIContributions= aboveSecondaryThreshold2.multiply(secondaryRate3).setScale(4, RoundingMode.HALF_UP);
            return calculateIncomeTaxBasedOnPayPeriod(employerNIContributions,payPeriod);
        }
        else if (grossIncome.compareTo(secondaryThreshold3)>0 && grossIncome.compareTo(secondaryThreshold4)<=0) {
            BigDecimal aboveSecondaryThreshold3 = grossIncome.subtract(secondaryThreshold3);
            employerNIContributions= aboveSecondaryThreshold3.multiply(secondaryRate4).setScale(4, RoundingMode.HALF_UP);
            return calculateIncomeTaxBasedOnPayPeriod(employerNIContributions,payPeriod);
        }
        else if (grossIncome.compareTo(secondaryThreshold4)>0) {
            BigDecimal aboveSecondaryThreshold4 = grossIncome.subtract(secondaryThreshold4);
            employerNIContributions= aboveSecondaryThreshold4.multiply(secondaryRate5).setScale(4, RoundingMode.HALF_UP);
            return calculateIncomeTaxBasedOnPayPeriod(employerNIContributions,payPeriod);

        }

      return BigDecimal.ZERO; // If no conditions met, return zero
    }

}
