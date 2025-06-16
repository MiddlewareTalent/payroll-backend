package com.payroll.uk.payroll_processing.service.ni;

import com.payroll.uk.payroll_processing.entity.NICategoryLetters;
import com.payroll.uk.payroll_processing.entity.TaxThreshold;
import com.payroll.uk.payroll_processing.service.TaxThresholdService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class NationalInsuranceCalculator {
    private final TaxThresholdService taxThresholdService;

    public NationalInsuranceCalculator(TaxThresholdService taxThresholdService) {
        this.taxThresholdService = taxThresholdService;
    }
    public BigDecimal calculateEmployeeNIContribution(BigDecimal income, String taxYear,  String payPeriod, NICategoryLetters niCategoryLetter){
//        System.out.println("Calculating Employee NI Contribution for income: " + income + ", taxYear: " + taxYear + ", payPeriod: " + payPeriod + ", NI Category Letter: " + niCategoryLetter);
        if (income == null || payPeriod == null || niCategoryLetter == null) {
            throw new IllegalArgumentException("Income, pay period, and NI category letter cannot be null");
        }

        BigDecimal niContribution = BigDecimal.ZERO;
        if(NICategoryLetters.X ==niCategoryLetter){
            return niContribution;
        }
       BigDecimal[][] niSlabs=taxThresholdService.getEmployeeNIThreshold(taxYear, TaxThreshold.TaxRegion.ALL_REGIONS, TaxThreshold.BandNameType.EMPLOYEE_NI,niCategoryLetter);
       BigDecimal[] niRates=taxThresholdService.getEmployeeNIRates(taxYear,TaxThreshold.TaxRegion.ALL_REGIONS, TaxThreshold.BandNameType.EMPLOYEE_NI,niCategoryLetter);

        BigDecimal belowLowerEarningsLimit=niSlabs[0][1];//6500
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
        if (grossIncome.compareTo(upperEarningsLimit) > 0) {
            BigDecimal aboveThreshold2 = grossIncome.subtract(upperEarningsLimit);
            niContribution = niContribution.add(aboveThreshold2.multiply(ABOVE_UEL_Rate));
            System.out.println("Above Threshold 2: " + aboveThreshold2);
            System.out.println("NI Contribution after Threshold 2: " + niContribution);
            grossIncome = upperEarningsLimit;
        }
        if (grossIncome.compareTo(primaryThreshold) > 0) {
            BigDecimal aboveThreshold1 = grossIncome.subtract(primaryThreshold);
            niContribution = niContribution.add(aboveThreshold1.multiply(UEL_Rate));
            System.out.println("Above Threshold 1: " + aboveThreshold1);
            System.out.println("NI Contribution after Threshold 1: " + niContribution);
            grossIncome= primaryThreshold;
        }
        if (grossIncome.compareTo(belowLowerEarningsLimit) > 0) {
            BigDecimal aboveThreshold0 = grossIncome.subtract(belowLowerEarningsLimit);
            niContribution = niContribution.add(aboveThreshold0.multiply(PT_Rate));
            System.out.println("Above Threshold 0: " + aboveThreshold0);
            System.out.println("NI Contribution after Threshold 0: " + niContribution);
            grossIncome = belowLowerEarningsLimit;
//           niContribution =niContribution.add(grossIncome.multiply(thresholdRate1));
        }
        if (grossIncome.compareTo(belowLowerEarningsLimit) <0) {
            niContribution =niContribution.add(grossIncome.multiply(BLEL_Rate));
            System.out.println("NI Contribution for income below Threshold 0: " + niContribution);
        }
        System.out.println("NI Contribution: " + niContribution);
        return calculateIncomeTaxBasedOnPayPeriod(niContribution,payPeriod);
    }

    private BigDecimal calculateGrossSalary(BigDecimal grossIncome,String payPeriod){
        return switch (payPeriod.toUpperCase()) {
            case "WEEKLY" -> grossIncome.multiply(BigDecimal.valueOf(52));
            case "MONTHLY" -> grossIncome.multiply(BigDecimal.valueOf(12));
            case "YEARLY" -> grossIncome;
            default -> throw new IllegalArgumentException("Invalid pay period. Must be WEEKLY, MONTHLY or YEARLY");
        };


    }

    private BigDecimal  calculateIncomeTaxBasedOnPayPeriod(BigDecimal incomeTax,String payPeriod){
        return switch (payPeriod.toUpperCase()) {
            case "WEEKLY" -> incomeTax.divide(BigDecimal.valueOf(52), 4, RoundingMode.HALF_UP);
            case "MONTHLY" -> incomeTax.divide(BigDecimal.valueOf(12), 4, RoundingMode.HALF_UP);
            case "YEARLY" -> incomeTax;
            default -> throw new IllegalArgumentException("Invalid pay period. Must be WEEKLY, MONTHLY or YEARLY");
        };
    }
    public BigDecimal calculateEmployerNIContribution(BigDecimal income, String taxYear,  String payPeriod, NICategoryLetters niCategoryLetter) {
        if (income == null || payPeriod == null || niCategoryLetter == null) {
            throw new IllegalArgumentException("Income, pay period, and NI category letter cannot be null");
        }

        BigDecimal employerNIContributions = BigDecimal.ZERO;
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
        System.out.println("Gross Income in Employer: "+grossIncome);
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
