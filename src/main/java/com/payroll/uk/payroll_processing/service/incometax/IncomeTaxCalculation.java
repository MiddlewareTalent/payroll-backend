package com.payroll.uk.payroll_processing.service.incometax;

import com.payroll.uk.payroll_processing.service.PersonalAllowanceCalculation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class IncomeTaxCalculation {

    private static final Logger logger = LoggerFactory.getLogger(IncomeTaxCalculation.class);

    @Autowired
    private ScotlandTaxCalculation scotlandTaxCalculation;

    @Autowired
    private PersonalAllowanceCalculation personalAllowanceCalculation;


  //Method for the England, Wales and Northern Ireland Income Tax Calculation
    BigDecimal calculateIncomeTax(BigDecimal grossIncome, BigDecimal personalAllowance, BigDecimal taxableIncome,BigDecimal[][] taxSlabs, BigDecimal[] taxRates, String payPeriod) {
        // Validate inputs
        if (grossIncome == null || personalAllowance == null || taxSlabs == null || taxRates == null || payPeriod == null) {
            throw new IllegalArgumentException("Parameters cannot be null");
        }
        if (grossIncome.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Income cannot be negative");
        }

        //England, Wales and Northern Ireland Tax Slabs and Rates
        BigDecimal personalRate = taxSlabs[0][1];
        BigDecimal basicRate = (taxSlabs[1][1].subtract(taxSlabs[0][1]));//37700
        BigDecimal higherRate = taxSlabs[2][1].subtract(taxSlabs[1][1]).add(basicRate).add(personalRate);//125140
        BigDecimal additionalRate = taxSlabs[3][0]; //125140
        BigDecimal taxPersonalRate=taxRates[0];//0
        BigDecimal taxBasicRate=taxRates[1];  //0.20
        BigDecimal taxHigherRate=taxRates[2];  //0.40
        BigDecimal taxAdditionalRate=taxRates[3];    // 0.45

//        System.out.println("tax personal rate: " + taxPersonalRate);
//        System.out.println("tax basic rate: " + taxBasicRate);
//        System.out.println("tax higher rate: " + taxHigherRate);
//        System.out.println("tax additional rate: " + taxAdditionalRate);
//        System.out.println("personal Rate: " + personalRate);
//        System.out.println("basic Rate: " + basicRate);
//        System.out.println("higher Rate: " + higherRate);
//        System.out.println("additional Rate: " + additionalRate);

        logger.info("Income tax calculation in calculateIncomeTax - Wales,England,Northern island");
        grossIncome= calculateGrossSalary(grossIncome,payPeriod);
        logger.info("Gross Salary : {}" ,grossIncome);

        // Calculate taxable income
        BigDecimal totalIncomeTax = BigDecimal.ZERO;


        logger.info("Taxable Income: {} " , taxableIncome);
        logger.info("grossIncome: {}, personalAllowance: {}, taxableIncome: {}, payPeriod: {}", grossIncome, personalAllowance, taxableIncome, payPeriod);

        if(taxableIncome.compareTo(BigDecimal.ZERO) <= 0) {
            return totalIncomeTax;
        }
        if (taxableIncome.compareTo(additionalRate) > 0) {
            // Additional rate (45%)
            BigDecimal additionalTaxAmount = taxableIncome.subtract(additionalRate);
            totalIncomeTax = totalIncomeTax.add(additionalTaxAmount.multiply(taxAdditionalRate));
//            logger.info("Annual Additional rate 45% : {}" , additionalTaxAmount.multiply(taxAdditionalRate).setScale(2, RoundingMode.HALF_UP));
            logger.info("Annual Additional rate 45% : {}" , additionalTaxAmount.multiply(taxAdditionalRate));

            // Higher rate (40%)
            BigDecimal highTax = higherRate.subtract(basicRate);
            totalIncomeTax = totalIncomeTax.add(highTax.multiply(taxHigherRate));
//            logger.info("Annual High rate 40% : {}"  ,highTax.multiply(taxHigherRate).setScale(2, RoundingMode.HALF_UP));
            logger.info("Annual High rate 40% : {}"  ,highTax.multiply(taxHigherRate));

            // Basic rate (20%)
//            BigDecimal basicTax = basicRate.subtract(personalRate);
            totalIncomeTax = totalIncomeTax.add(basicRate.multiply(taxBasicRate));
//            logger.info("Annual Basic rate 20% : {}" , basicRate.multiply(taxBasicRate).setScale(2, RoundingMode.HALF_UP));
            logger.info("Annual Basic rate 20% : {}" , basicRate.multiply(taxBasicRate));

        }

        else if (taxableIncome.compareTo(basicRate) > 0
                && taxableIncome.compareTo(higherRate) <= 0)
        {
            // Higher rate (40%)
            BigDecimal highTax = taxableIncome.subtract(basicRate);
            totalIncomeTax = totalIncomeTax.add(highTax.multiply(taxHigherRate));
//            logger.info("Annual High rate 40% : {}" , highTax.multiply(taxHigherRate).setScale(2, RoundingMode.HALF_UP));
            logger.info("Annual High rate 40% : {}" , highTax.multiply(taxHigherRate));

            // Basic rate (20%)
//            BigDecimal basicTax = basicRate.subtract(personalRate);
            totalIncomeTax = totalIncomeTax.add(basicRate.multiply(taxBasicRate));
//            logger.info("Annual Basic rate of 20% : {}" , basicRate.multiply(taxBasicRate).setScale(2, RoundingMode.HALF_UP));
            logger.info("Annual Basic rate of 20% : {}" , basicRate.multiply(taxBasicRate));
        }
        else if (taxableIncome.compareTo(basicRate) <= 0) {
            // Basic rate only (20%)
            totalIncomeTax = totalIncomeTax.add(taxableIncome.multiply(taxBasicRate));
//            logger.info("Annual Basic rate 20% : {}" , taxableIncome.multiply(taxBasicRate).setScale(2, RoundingMode.HALF_UP));
            logger.info("Annual Basic rate 20% : {}" , taxableIncome.multiply(taxBasicRate));
        }
        logger.info("Annual Total Income Tax: {}" ,totalIncomeTax);


        return totalIncomeTax;
    }

    public BigDecimal calculateTaxWithKCode(BigDecimal grossIncome, String normalizedTaxCode,BigDecimal taxableIncomeAnnual, BigDecimal[][] taxSlabs, BigDecimal[] taxRates,String payPeriod) {
        // Validate inputs
        if (grossIncome == null || normalizedTaxCode == null || taxSlabs == null || taxRates == null) {
            throw new IllegalArgumentException("Parameters cannot be null");
        }
        if (grossIncome.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Income cannot be negative");
        }
        BigDecimal personalAllowance = personalAllowanceCalculation.calculatePersonalAllowance(grossIncome, normalizedTaxCode,payPeriod);

        if (normalizedTaxCode.startsWith("SK") || normalizedTaxCode.startsWith("CK")) {
            // Remove the first character ('S' or 'C') from the code
            String processedCode = normalizedTaxCode.substring(2);
            // Convert the processed code to a numeric value
            String numericPart = processedCode.replaceAll("[^0-9]", "");
            if (numericPart.isEmpty()) {
                return BigDecimal.ZERO; // No numeric allowance
            }
            // Calculate base allowance (numeric part × 10)
            BigDecimal baseAllowance = new BigDecimal(numericPart)
                    .multiply(new BigDecimal("10"))
                    .setScale(0, RoundingMode.HALF_UP);
            grossIncome=grossIncome.add(baseAllowance);
            if(normalizedTaxCode.startsWith("SK")){
               return scotlandTaxCalculation.calculateScotlandIncomeTax(grossIncome,personalAllowance,taxableIncomeAnnual,taxSlabs,taxRates,payPeriod);
            }
            else if(normalizedTaxCode.startsWith("CK")){
               return calculateIncomeTax(grossIncome,personalAllowance,taxableIncomeAnnual,taxSlabs,taxRates,payPeriod);
            }

        }
        else if (normalizedTaxCode.startsWith("K")) {
            String processedCode = normalizedTaxCode.substring(1);
            // Convert the processed code to a numeric value
            String numericPart = processedCode.replaceAll("[^0-9]", "");
            if (numericPart.isEmpty()) {
                return BigDecimal.ZERO; // No numeric allowance
            }
            // Calculate base allowance (numeric part × 10)
            BigDecimal baseAllowance = new BigDecimal(numericPart)
                    .multiply(new BigDecimal("10"))
                    .setScale(0, RoundingMode.HALF_UP);
            logger.info("Base Allowance for K code: {}", baseAllowance);
            // Add the base allowance to the gross income
            grossIncome = grossIncome.add(baseAllowance);
            // Calculate the income tax based on the adjusted gross income
            return calculateIncomeTax(grossIncome, personalAllowance, taxableIncomeAnnual,taxSlabs, taxRates,payPeriod);
        } else {
            throw new IllegalArgumentException("Invalid K code: " + normalizedTaxCode);

        }

        return new BigDecimal(0);
    }

    public BigDecimal calculateGrossSalary(BigDecimal grossIncome,String payPeriod){
        BigDecimal annualGross;
         switch (payPeriod.toUpperCase()) {
            case "WEEKLY" -> annualGross=grossIncome.multiply(BigDecimal.valueOf(52));
            case "MONTHLY" -> annualGross=grossIncome.multiply(BigDecimal.valueOf(12));
            case "QUARTERLY" -> annualGross=grossIncome.multiply(BigDecimal.valueOf(4));
            case "YEARLY" -> annualGross=grossIncome;
            default -> throw new IllegalArgumentException("Invalid pay period. Must be WEEKLY, MONTHLY or YEARLY");
        }
         return  annualGross;
//        return annualGross.setScale(2, RoundingMode.HALF_UP);


    }

}


