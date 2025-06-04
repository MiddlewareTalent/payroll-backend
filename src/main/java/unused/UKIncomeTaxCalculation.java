//package unused;
//
//import com.payroll.uk.payroll_processing.entity.TaxThreshold;
//
//import java.math.BigDecimal;
//import java.math.RoundingMode;
//
//public class UKIncomeTaxCalculation {
//
//    BigDecimal calculateIncomeTaxWithPersonalAllowance(BigDecimal grossIncome, String normalizedTaxCode, BigDecimal[][] taxSlabs, BigDecimal[] taxRates, TaxThreshold.TaxRegion region) {
//
//        BigDecimal personalAllowance = personalAllowanceCalculation.calculatePersonalAllowance(grossIncome,normalizedTaxCode);
//        System.out.println("Personal Allowance: " + personalAllowance);
////        BigDecimal taxableIncome=income.subtract(calculatePersonalAllowance(income));
//        BigDecimal totalIncomeTax = BigDecimal.ZERO;
//        BigDecimal taxableIncome = grossIncome.subtract(personalAllowanceCalculation.calculatePersonalAllowance(grossIncome,normalizedTaxCode));
//        System.out.println("Taxable Income: " + taxableIncome);
//        if(taxableIncome.compareTo(BigDecimal.ZERO) <= 0) {
////            throw new RuntimeException("Taxable income is zero And No tax is applicable:"+income);
//            return totalIncomeTax;
//        }
//        if (taxableIncome.compareTo(taxSlabs[3][0]) > 0) {
//            // Additional rate (45%)
//            BigDecimal additionalTax = taxableIncome.subtract(taxSlabs[3][0]);
//            totalIncomeTax = totalIncomeTax.add(additionalTax.multiply(new BigDecimal("0.45")));
//            System.out.println(region+" Annual Additional rate 45% : " + additionalTax.multiply(new BigDecimal("0.45").setScale(2, RoundingMode.HALF_UP)));
//
//            // Higher rate (40%)
//            BigDecimal highTax = taxSlabs[2][1].subtract(taxSlabs[2][0].subtract(taxSlabs[1][0]));
//            totalIncomeTax = totalIncomeTax.add(highTax.multiply(new BigDecimal("0.40")));
//            System.out.println(region+" Annual High rate 40% : " + highTax.multiply(new BigDecimal("0.40").setScale(2, RoundingMode.HALF_UP)));
//
//            // Basic rate (20%)
//            BigDecimal basicTax = taxSlabs[2][0].subtract(taxSlabs[1][0].subtract(taxSlabs[0][0]));
//            totalIncomeTax = totalIncomeTax.add(basicTax.multiply(new BigDecimal("0.20")));
//            System.out.println(region+" Annual Basic rate 20% : " + basicTax.multiply(new BigDecimal("0.20").setScale(2, RoundingMode.HALF_UP)));
//
//        }
//
//        else if (taxableIncome.compareTo(taxSlabs[2][0].subtract(taxSlabs[1][0])) >= 0
//                && taxableIncome.compareTo(taxSlabs[2][1]) <= 0)
//        {
//            // Higher rate (40%)
//            BigDecimal highTax = taxableIncome.subtract(taxSlabs[2][0].subtract(taxSlabs[1][0]));
//            totalIncomeTax = totalIncomeTax.add(highTax.multiply(new BigDecimal("0.40")));
//            System.out.println(region+" Annual High rate 40% : " + highTax.multiply(new BigDecimal("0.40").setScale(2, RoundingMode.HALF_UP)));
//
//            // Basic rate (20%)
//            BigDecimal basicTax = taxSlabs[2][0].subtract(taxSlabs[1][0].subtract(taxSlabs[0][0]));
//            totalIncomeTax = totalIncomeTax.add(basicTax.multiply(new BigDecimal("0.20")));
//            System.out.println(region+" Annual Basic rate 20% : " + basicTax.multiply(new BigDecimal("0.20").setScale(2, RoundingMode.HALF_UP)));
//        }
//        else if (taxableIncome.compareTo(taxSlabs[2][0].subtract(taxSlabs[1][0])) <= 0) {
//            // Basic rate only (20%)
//            totalIncomeTax = totalIncomeTax.add(taxableIncome.multiply(new BigDecimal("0.20")));
//            System.out.println(region+" Annual Basic rate 20% : " + taxableIncome.multiply(new BigDecimal("0.20").setScale(2, RoundingMode.HALF_UP)));
//        }
//        System.out.println(region+" Annual Total Income Tax: " + totalIncomeTax);
//
//
//        return totalIncomeTax.setScale(2, RoundingMode.HALF_UP);
//    }
//
//    BigDecimal calculateIncomeTaxWithOutAllowance(BigDecimal grossIncome, String normalizedTaxCode,BigDecimal[][] taxSlabs, BigDecimal[] taxRates, TaxThreshold.TaxRegion region) {
//        BigDecimal totalIncomeTax = BigDecimal.ZERO;
//        BigDecimal taxableIncome=grossIncome.subtract(personalAllowanceCalculation.calculatePersonalAllowance(grossIncome,normalizedTaxCode));
//
//        if (taxableIncome.compareTo(taxSlabs[3][0]) > 0) {
//            // Additional rate (45%)
//            BigDecimal additionalTax = taxableIncome.subtract(taxSlabs[3][0]);
//            totalIncomeTax = totalIncomeTax.add(additionalTax.multiply(new BigDecimal("0.45")));
//            System.out.println(region+" Annual Additional rate 45% : " + additionalTax.multiply(new BigDecimal("0.45").setScale(2, RoundingMode.HALF_UP)));
//
//            // Higher rate (40%)
//            BigDecimal highTax = taxSlabs[2][1].subtract(taxSlabs[2][0].subtract(taxSlabs[1][0]));
//            totalIncomeTax = totalIncomeTax.add(highTax.multiply(new BigDecimal("0.40")));
//            System.out.println(region+" Annual High rate 40% : " + highTax.multiply(new BigDecimal("0.40").setScale(2, RoundingMode.HALF_UP)));
//
//            // Basic rate (20%)
//            BigDecimal basicTax = taxSlabs[2][0].subtract(taxSlabs[1][0].subtract(taxSlabs[0][0]));
//            totalIncomeTax = totalIncomeTax.add(basicTax.multiply(new BigDecimal("0.20")));
//            System.out.println(region+" Annual Basic rate 20% : " + basicTax.multiply(new BigDecimal("0.20").setScale(2, RoundingMode.HALF_UP)));
//
//        }
//        else if (taxableIncome.compareTo(taxSlabs[2][0].subtract(taxSlabs[1][0])) >= 0
//                && taxableIncome.compareTo(taxSlabs[2][1]) <= 0)
//        {
//            // Higher rate (40%)
//            BigDecimal highTax = taxableIncome.subtract(taxSlabs[2][0].subtract(taxSlabs[1][0]));
//            totalIncomeTax = totalIncomeTax.add(highTax.multiply(new BigDecimal("0.40")));
//            System.out.println(region+" Annual High rate 40% : " + highTax.multiply(new BigDecimal("0.40").setScale(2, RoundingMode.HALF_UP)));
//
//            // Basic rate (20%)
//            BigDecimal basicTax = taxSlabs[2][0].subtract(taxSlabs[1][0].subtract(taxSlabs[0][0]));
//            totalIncomeTax = totalIncomeTax.add(basicTax.multiply(new BigDecimal("0.20")));
//            System.out.println(region+" Annual Basic rate 20% : " + basicTax.multiply(new BigDecimal("0.20").setScale(2, RoundingMode.HALF_UP)));
//        }
//        else if (taxableIncome.compareTo(taxSlabs[2][0].subtract(taxSlabs[1][0])) <= 0) {
//            // Basic rate only (20%)
//            totalIncomeTax = totalIncomeTax.add(taxableIncome.multiply(new BigDecimal("0.20")));
//            System.out.println(region+" Annual Basic rate 20% : " + taxableIncome.multiply(new BigDecimal("0.20").setScale(2, RoundingMode.HALF_UP)));
//        }
//        return totalIncomeTax.setScale(2, RoundingMode.HALF_UP);
//    }
//
//
//}


//import com.payroll.uk.payroll_processing.entity.TaxThreshold;
//
//import java.math.BigDecimal;
//import java.util.Arrays;
//
//BigDecimal calculateScottishTaxWithAllowance(BigDecimal grossIncome, String normalizedTaxCode, BigDecimal[][] taxSlabs, BigDecimal[] taxRates) {
//    Arrays.stream(taxSlabs)
//            .forEach(taxSlab -> System.out.println(Arrays.toString(taxSlab)));
//    BigDecimal personalAllowance = BigDecimal.valueOf(12570);
//    BigDecimal starterRate = taxSlabs[1][1].subtract(taxSlabs[0][1]);
//    BigDecimal basicRate = (taxSlabs[2][1].subtract(taxSlabs[1][1])).add(starterRate);
//    BigDecimal intermediateRate = (taxSlabs[3][1].subtract(taxSlabs[2][1])).add(basicRate);
//    BigDecimal higherRate = taxSlabs[4][1].subtract(taxSlabs[3][1]).add(intermediateRate);
//    BigDecimal additionalRate = taxSlabs[5][1].subtract(taxSlabs[4][1]).add(higherRate).add(personalAllowance);
//    BigDecimal topRate = taxSlabs[6][0];
//
//    BigDecimal totalIncomeTax = BigDecimal.ZERO;
//    BigDecimal taxableIncome = grossIncome.subtract(personalAllowanceCalculation.calculatePersonalAllowance(grossIncome,normalizedTaxCode));
//    System.out.println("Taxable Income: " + taxableIncome);
//    if (taxableIncome.compareTo(BigDecimal.ZERO) <= 0) {
//        return totalIncomeTax;
//    }
//
//    return null;
//}
//
//BigDecimal calculateTaxWithKCode(BigDecimal income, String normalizedTaxCode, BigDecimal[][] taxSlabs, BigDecimal[] taxRates, TaxThreshold.TaxRegion region) {
//    return null;
//}