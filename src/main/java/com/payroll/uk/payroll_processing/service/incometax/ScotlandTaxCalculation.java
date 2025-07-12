package com.payroll.uk.payroll_processing.service.incometax;


import com.payroll.uk.payroll_processing.service.TaxThresholdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class ScotlandTaxCalculation {
    @Autowired

    private TaxThresholdService taxThresholdService;

    public BigDecimal calculateScotlandIncomeTax(BigDecimal income, BigDecimal personalAllowance,BigDecimal taxableIncomeAnnual, BigDecimal[][] taxSlabs, BigDecimal[] taxRates, String payPeriod) {

//        BigDecimal[][] taxSlabs = taxThresholdService.getTaxBounds("2025-2026", TaxThreshold.TaxRegion.SCOTLAND);
//        BigDecimal[] taxRates = taxThresholdService.getTaxRates("2025-2026", TaxThreshold.TaxRegion.SCOTLAND);

//        Arrays.stream(taxSlabs)
//                .forEach(taxSlab -> System.out.println(Arrays.toString(taxSlab)));

        BigDecimal personalRate = BigDecimal.valueOf(12570); //12570
        BigDecimal starterRate = taxSlabs[1][1].subtract(taxSlabs[0][1]); //2827
        BigDecimal basicRate = (taxSlabs[2][1].subtract(taxSlabs[1][1])).add(starterRate); //14921
        BigDecimal intermediateRate = (taxSlabs[3][1].subtract(taxSlabs[2][1])).add(basicRate);//31092
        BigDecimal higherRate = taxSlabs[4][1].subtract(taxSlabs[3][1]).add(intermediateRate);//62430
        BigDecimal additionalRate = taxSlabs[5][1].subtract(taxSlabs[4][1]).add(higherRate).add(personalRate); //125140
        BigDecimal topRate = taxSlabs[6][0];  //>125140

        BigDecimal taxPersonalRate=taxRates[0];//0
        BigDecimal taxStarterRate=taxRates[1];  //0.19
        BigDecimal taxBasicRate=taxRates[2];  //0.20
        BigDecimal taxIntermediateRate=taxRates[3];  //0.21
        BigDecimal taxHigherRate=taxRates[4];  //0.42
        BigDecimal taxAdditionalRate=taxRates[5];    // 0.45
        BigDecimal taxTopTaxRate=taxRates[6];  //0.48

        payPeriod=payPeriod.toUpperCase();
        BigDecimal grossIncome= calculateGrossSalary(income,payPeriod);
        System.out.println("Annual Gross Income: " + grossIncome);
        BigDecimal totalIncomeTax = BigDecimal.ZERO;

        BigDecimal taxableIncome=taxableIncomeAnnual;
//        BigDecimal taxableIncome = grossIncome.subtract(personalAllowance);
//        taxableIncome = taxableIncome.compareTo(BigDecimal.ZERO) > 0 ? taxableIncome : BigDecimal.ZERO;
        System.out.println("Taxable Income: " + taxableIncome);
        if (taxableIncome.compareTo(BigDecimal.ZERO) <= 0) {
            return totalIncomeTax;
        }
        //
        if(taxableIncome.compareTo(topRate)>0) {
            //Top rate (48%)
            BigDecimal topRateTax = taxableIncome.subtract(additionalRate);
            totalIncomeTax = totalIncomeTax.add(topRateTax.multiply(taxTopTaxRate));
            System.out.println("Top rate tax: " + topRateTax.multiply(taxTopTaxRate));

            //Additional rate (45%)
            BigDecimal additionalRateTax = additionalRate.subtract(higherRate);
            totalIncomeTax = totalIncomeTax.add(additionalRateTax.multiply(taxAdditionalRate));
            System.out.println("Additional rate tax: " + additionalRateTax.multiply(taxAdditionalRate));

            //Higher rate (42%)
            BigDecimal higherRateTax = higherRate.subtract(intermediateRate);
            totalIncomeTax = totalIncomeTax.add(higherRateTax.multiply(taxHigherRate));
            System.out.println("Higher rate tax: " + higherRateTax.multiply(taxHigherRate));

            //Intermediate rate (21%)
            BigDecimal intermediateRateTax = intermediateRate.subtract(basicRate);
            totalIncomeTax = totalIncomeTax.add(intermediateRateTax.multiply(taxIntermediateRate));
            System.out.println("Intermediate rate tax: " + intermediateRateTax.multiply(taxIntermediateRate));

            //Basic rate (20%)
            BigDecimal basicRateTax = basicRate.subtract(starterRate);
            totalIncomeTax = totalIncomeTax.add(basicRateTax.multiply(taxBasicRate));
            System.out.println("Basic rate tax: " + basicRateTax.multiply(taxBasicRate));

            //Starter rate (19%)
//            BigDecimal starterRateTax = starterRate.subtract(taxSlabs[0][1]);
            totalIncomeTax = totalIncomeTax.add(starterRate.multiply(taxStarterRate));
            System.out.println("Starter rate tax: " + starterRate.multiply(taxStarterRate));
        }
        else if (taxableIncome.compareTo(higherRate)>0 && taxableIncome.compareTo(additionalRate)<=0){
            //Additional rate (45%)
            BigDecimal additionalTaxRate=additionalRate.subtract(taxableIncome);
            totalIncomeTax = totalIncomeTax.add(additionalTaxRate.multiply(taxAdditionalRate));
            System.out.println("Additional rate tax: " + additionalTaxRate.multiply(taxAdditionalRate));

            //Higher rate (42%)
            BigDecimal higherTaxRate = higherRate.subtract(intermediateRate);
            totalIncomeTax = totalIncomeTax.add(higherTaxRate.multiply(taxHigherRate));
            System.out.println("Higher rate tax: " + higherTaxRate.multiply(taxHigherRate));

            //Intermediate rate (21%)
            BigDecimal intermediateTaxRate = intermediateRate.subtract(basicRate);
            totalIncomeTax = totalIncomeTax.add(intermediateTaxRate.multiply(taxIntermediateRate));
            System.out.println("Intermediate rate tax: " + intermediateTaxRate.multiply(taxIntermediateRate));

            //Basic rate (20%)
            BigDecimal basicTaxRate = basicRate.subtract(starterRate);
            totalIncomeTax = totalIncomeTax.add(basicTaxRate.multiply(taxBasicRate));
            System.out.println("Basic rate tax: " + basicTaxRate.multiply(taxBasicRate));
            //Starter rate (19%)
            BigDecimal starterTaxRate = starterRate.multiply(taxStarterRate);
            totalIncomeTax = totalIncomeTax.add(starterTaxRate);
            System.out.println("Starter rate tax: " + starterTaxRate);
        } else if (taxableIncome.compareTo(intermediateRate)>0 && taxableIncome.compareTo(higherRate)<=0) {
            //Higher rate (42%)
             BigDecimal higherTaxRate=taxableIncome.subtract(intermediateRate);
            totalIncomeTax = totalIncomeTax.add(higherTaxRate.multiply(taxHigherRate));
            System.out.println("Higher rate tax: " + higherTaxRate.multiply(taxHigherRate));

            //Intermediate rate (21%)
            BigDecimal intermediateTaxRate = intermediateRate.subtract(basicRate);
            totalIncomeTax = totalIncomeTax.add(intermediateTaxRate.multiply(taxIntermediateRate));
            System.out.println("Intermediate rate tax: " + intermediateTaxRate.multiply(taxIntermediateRate));

            //Basic rate (20%)
            BigDecimal basicTaxRate = basicRate.subtract(starterRate);
            totalIncomeTax = totalIncomeTax.add(basicTaxRate.multiply(taxBasicRate));
            System.out.println("Basic rate tax: " + basicTaxRate.multiply(taxBasicRate));
            //Starter rate (19%)
            BigDecimal starterTaxRate = starterRate.multiply(taxStarterRate);
            totalIncomeTax = totalIncomeTax.add(starterTaxRate);
            System.out.println("Starter rate tax: " + starterTaxRate);
        } else if (taxableIncome.compareTo(basicRate)>0 && taxableIncome.compareTo(intermediateRate)<=0) {
            //Intermediate rate (21%)
            BigDecimal intermediateTaxRate = taxableIncome.subtract(basicRate);
            totalIncomeTax = totalIncomeTax.add(intermediateTaxRate.multiply(taxIntermediateRate));
            System.out.println("Intermediate rate tax: " + intermediateTaxRate.multiply(taxIntermediateRate));
            //Basic rate (20%)
            BigDecimal basicTaxRate = basicRate.subtract(starterRate);
            totalIncomeTax = totalIncomeTax.add(basicTaxRate.multiply(taxBasicRate));
            System.out.println("Basic rate tax: " + basicTaxRate.multiply(taxBasicRate));
            //Starter rate (19%)
            BigDecimal starterTaxRate = starterRate.multiply(taxStarterRate);
            totalIncomeTax = totalIncomeTax.add(starterTaxRate);
            System.out.println("Starter rate tax: " + starterTaxRate);

        }
        else if (taxableIncome.compareTo(starterRate)>0 && taxableIncome.compareTo(basicRate)<=0) {
            //Basic rate (20%)
            BigDecimal basicTaxRate = taxableIncome.subtract(starterRate);
            totalIncomeTax = totalIncomeTax.add(basicTaxRate.multiply(taxBasicRate));
            System.out.println("Basic rate tax: " + basicTaxRate.multiply(taxBasicRate));
            //Starter rate (19%)
            BigDecimal starterTaxRate = starterRate.multiply(taxStarterRate);
            totalIncomeTax = totalIncomeTax.add(starterTaxRate);
            System.out.println("Starter rate tax: " + starterTaxRate);
        } else if (taxableIncome.compareTo(starterRate)<=0) {
            //Starter rate (19%)
            BigDecimal starterTaxRate = taxableIncome.multiply(taxStarterRate);
            totalIncomeTax = totalIncomeTax.add(starterTaxRate);
            System.out.println("Starter rate tax: " + starterTaxRate);
        }
         return calculateIncomeTaxBasedOnPayPeriod(totalIncomeTax, payPeriod);

//        return totalIncomeTax.setScale(2, RoundingMode.HALF_UP);
    }
    public BigDecimal calculatePersonalAllowance(BigDecimal income) {


        BigDecimal allowance = new BigDecimal("12570");
        BigDecimal threshold = new BigDecimal("100000");
        BigDecimal reductionRate = new BigDecimal("2");

        if (income.compareTo(threshold) > 0) {
            BigDecimal reduction = income.subtract(threshold)
                    .divide(reductionRate, 2, RoundingMode.HALF_UP);
            allowance = allowance.subtract(reduction);
            if (allowance.compareTo(BigDecimal.ZERO) < 0) {
                allowance = BigDecimal.ZERO;
            }
        }

        return allowance;
    }

    public BigDecimal calculateGrossSalary(BigDecimal grossIncome,String payPeriod){
        return switch (payPeriod.toUpperCase()) {
            case "WEEKLY" -> grossIncome.multiply(BigDecimal.valueOf(52));
            case "MONTHLY" -> grossIncome.multiply(BigDecimal.valueOf(12));
            case "QUARTERLY" -> grossIncome.multiply(BigDecimal.valueOf(4));
            case "YEARLY" -> grossIncome;
            default -> throw new IllegalArgumentException("Invalid pay period. Must be WEEKLY, MONTHLY or YEARLY");
        };


    }

    public BigDecimal  calculateIncomeTaxBasedOnPayPeriod(BigDecimal incomeTax,String payPeriod){
        return switch (payPeriod.toUpperCase()) {
            case "WEEKLY" -> incomeTax.divide(BigDecimal.valueOf(52), 2, RoundingMode.HALF_UP);
            case "MONTHLY" -> incomeTax.divide(BigDecimal.valueOf(12), 2, RoundingMode.HALF_UP);
            case "QUARTERLY" -> incomeTax.divide(BigDecimal.valueOf(4), 2, RoundingMode.HALF_UP);
            case "YEARLY" -> incomeTax;
            default -> throw new IllegalArgumentException("Invalid pay period. Must be WEEKLY, MONTHLY or YEARLY");
        };
    }

}
