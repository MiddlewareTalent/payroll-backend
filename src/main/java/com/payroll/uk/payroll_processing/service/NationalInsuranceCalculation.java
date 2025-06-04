package com.payroll.uk.payroll_processing.service;

import com.payroll.uk.payroll_processing.entity.TaxThreshold;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class NationalInsuranceCalculation {
    private final TaxThresholdService taxThresholdService;
    NationalInsuranceCalculation(TaxThresholdService taxThresholdService) {
        this.taxThresholdService = taxThresholdService;
    }


    public  BigDecimal calculateNationalInsurance(BigDecimal income, String taxYear,
                                                  TaxThreshold.TaxRegion region,String payPeriod) {
        // Get tax thresholds and rates
        BigDecimal[][] taxSlabs = taxThresholdService.getTaxBounds(taxYear, region, TaxThreshold.TaxBandType.NATIONAL_INSURANCE);
        BigDecimal[] taxRates = taxThresholdService.getTaxRates(taxYear, region, TaxThreshold.TaxBandType.NATIONAL_INSURANCE);

//        BigDecimal threshold1 = BigDecimal.valueOf(12570);
//        BigDecimal threshold2 = BigDecimal.valueOf(50270);
//        BigDecimal rate1 = BigDecimal.valueOf(0.08);
//        BigDecimal rate2 = BigDecimal.valueOf(0.02);
        BigDecimal NI = BigDecimal.ZERO;
        BigDecimal threshold1 = taxSlabs[0][0];
        BigDecimal threshold2 = taxSlabs[1][0];
        BigDecimal rate1 = taxRates[0];
        BigDecimal rate2 = taxRates[1];
        if (income == null || payPeriod == null) {
            throw new IllegalArgumentException("Income and pay period cannot be null");
        }
        BigDecimal grossIncome = calculateGrossSalary(income, payPeriod);

        if (grossIncome.compareTo(threshold2) > 0) {
            BigDecimal aboveThreshold2 = grossIncome.subtract(threshold2);
            NI = NI.add(aboveThreshold2.multiply(rate2));
            grossIncome = threshold2;
        }
        if (grossIncome.compareTo(threshold1) > 0) {
            BigDecimal aboveThreshold1 = grossIncome.subtract(threshold1);
            NI = NI.add(aboveThreshold1.multiply(rate1));
        }


        return calculateIncomeTaxBasedOnPayPeriod(NI,payPeriod);
    }
    public BigDecimal calculateEmploymentAllowance(BigDecimal income, String taxYear,
                                                   TaxThreshold.TaxRegion region,String payPeriod) {
         final BigDecimal EMPLOYMENT_ALLOWANCE = new BigDecimal("10500");
         final BigDecimal SecondaryThreshold = new BigDecimal("5000"); //9100
         final  BigDecimal EmployerNIRate=new BigDecimal("0.15");  //0.138
//        BigDecimal[][] taxSlabs = taxThresholdService.getTaxBounds(taxYear, region, TaxThreshold.TaxBandType.EMPLOYER_NATIONAL_INSURANCE);
//        BigDecimal[] taxRates = taxThresholdService.getTaxRates(taxYear, region, TaxThreshold.TaxBandType.EMPLOYER_NATIONAL_INSURANCE);
//        BigDecimal[] taxRatesForAllowance= taxThresholdService.getTaxRates(taxYear, region, TaxThreshold.TaxBandType.EMPLOYMENT_ALLOWANCE);

//        final BigDecimal EMPLOYMENT_ALLOWANCE = taxRatesForAllowance[0];
//         final BigDecimal SecondaryThreshold = taxSlabs[0][0];
//         final  BigDecimal EmployerNIRate= taxRates[0];


        BigDecimal employerNI = BigDecimal.ZERO;
        if (income == null || payPeriod == null) {
            throw new IllegalArgumentException("Income and pay period cannot be null");
        }

        BigDecimal grossIncome = calculateGrossSalary(income, payPeriod);
        if(grossIncome.compareTo(BigDecimal.ZERO)<0){
            throw new IllegalArgumentException("Income cannot be negative");
        }
        if(grossIncome.compareTo(SecondaryThreshold)>0){
            BigDecimal TaxableEmployerNI = grossIncome.subtract(SecondaryThreshold);
             employerNI = TaxableEmployerNI.multiply(EmployerNIRate);
        }
//         return employerNI.setScale(2, RoundingMode.HALF_UP);
        return calculateIncomeTaxBasedOnPayPeriod(employerNI,payPeriod);
    }

    public BigDecimal calculateGrossSalary(BigDecimal grossIncome,String payPeriod){
        return switch (payPeriod.toUpperCase()) {
            case "WEEKLY" -> grossIncome.multiply(BigDecimal.valueOf(52));
            case "MONTHLY" -> grossIncome.multiply(BigDecimal.valueOf(12));
            case "YEARLY" -> grossIncome;
            default -> throw new IllegalArgumentException("Invalid pay period. Must be WEEKLY, MONTHLY or YEARLY");
        };


    }

    public BigDecimal  calculateIncomeTaxBasedOnPayPeriod(BigDecimal incomeTax,String payPeriod){
        return switch (payPeriod.toUpperCase()) {
            case "WEEKLY" -> incomeTax.divide(BigDecimal.valueOf(52), 2, RoundingMode.HALF_UP);
            case "MONTHLY" -> incomeTax.divide(BigDecimal.valueOf(12), 2, RoundingMode.HALF_UP);
            case "YEARLY" -> incomeTax;
            default -> throw new IllegalArgumentException("Invalid pay period. Must be WEEKLY, MONTHLY or YEARLY");
        };
    }


}
