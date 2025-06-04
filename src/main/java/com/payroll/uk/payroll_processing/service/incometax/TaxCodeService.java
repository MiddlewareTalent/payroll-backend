package com.payroll.uk.payroll_processing.service.incometax;

import com.payroll.uk.payroll_processing.entity.TaxThreshold;
import com.payroll.uk.payroll_processing.service.PersonalAllowanceCalculation;
import com.payroll.uk.payroll_processing.service.TaxThresholdService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class TaxCodeService {
    private final TaxThresholdService taxThresholdService;
    private final IncomeTaxCalculation incomeTaxCalculation;
    private final PersonalAllowanceCalculation personalAllowanceCalculation;
    private  final ScotlandTaxCalculation scotlandTaxCalculation;
    public TaxCodeService(TaxThresholdService taxThresholdService, IncomeTaxCalculation incomeTaxCalculation, PersonalAllowanceCalculation personalAllowanceCalculation, ScotlandTaxCalculation scotlandTaxCalculation) {
        this.taxThresholdService = taxThresholdService;
        this.incomeTaxCalculation = incomeTaxCalculation;
        this.personalAllowanceCalculation = personalAllowanceCalculation;
        this.scotlandTaxCalculation = scotlandTaxCalculation;
    }

    public BigDecimal calculateIncomeBasedOnTaxCode(BigDecimal grossIncome, String taxYear,
                                                    TaxThreshold.TaxRegion region, String taxCode,String payPeriod) {
        // Validate inputs
        if (grossIncome == null || taxCode == null || taxYear == null || region == null|| payPeriod == null) {
            throw new IllegalArgumentException("Parameters cannot be null");
        }
        if ((region == TaxThreshold.TaxRegion.SCOTLAND && !taxCode.startsWith("S")) ||
                (region == TaxThreshold.TaxRegion.WALES && !taxCode.startsWith("C")) ||
                (region == TaxThreshold.TaxRegion.NORTHERN_IRELAND && (taxCode.startsWith("S") || taxCode.startsWith("C"))) ||
                (region == TaxThreshold.TaxRegion.ENGLAND && (taxCode.startsWith("S") || taxCode.startsWith("C")))) {
            throw new IllegalArgumentException("Invalid tax code '" + taxCode + "' for the specified region: " + region);
        }



        if (grossIncome.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Income cannot be negative");
        }

        // Get tax thresholds and rates
        BigDecimal[][] taxSlabs = taxThresholdService.getTaxBounds(taxYear, region, TaxThreshold.TaxBandType.INCOME_TAX);
        BigDecimal[] taxRates = taxThresholdService.getTaxRates(taxYear, region, TaxThreshold.TaxBandType.INCOME_TAX);

        System.out.println(payPeriod+" Gross Income: " + grossIncome);

        // Calculate personal allowance
        BigDecimal personalAllowance = personalAllowanceCalculation.calculatePersonalAllowance(grossIncome,taxCode);
        System.out.println("Personal Allowance: " + personalAllowance);
        // Convert to uppercase for case-insensitive comparison
        String normalizedTaxCode = taxCode.toUpperCase();

        // Handle numeric tax codes ending with L (like 1257L)
        if (normalizedTaxCode.matches("^\\d+L$")||normalizedTaxCode.matches("^C\\d+L$") ) {
            BigDecimal AnnualIncomeTax = incomeTaxCalculation.calculateIncomeTax(grossIncome, personalAllowance, taxSlabs, taxRates,payPeriod);
            return calculateIncomeTaxBasedOnPayPeriod(AnnualIncomeTax,payPeriod);
        }

        // Handle K codes (negative allowances)
        if (normalizedTaxCode.matches("^K\\d+$")|| normalizedTaxCode.matches("^SK\\d+$")||normalizedTaxCode.matches("^CK\\d+$")) {
            BigDecimal AnnualKCodeIncomeTax = incomeTaxCalculation.calculateTaxWithKCode(grossIncome, normalizedTaxCode, taxSlabs, taxRates,payPeriod);
            return calculateIncomeTaxBasedOnPayPeriod(AnnualKCodeIncomeTax,payPeriod);
        }

        // Handle Scottish codes (starting with S followed by numbers and ending with L)
        if (normalizedTaxCode.matches("^S\\d+L$")) {
            BigDecimal AnnualIncomeTax = scotlandTaxCalculation.calculateScotlandIncomeTax(grossIncome, personalAllowance, taxSlabs, taxRates,payPeriod);
            return calculateIncomeTaxBasedOnPayPeriod(AnnualIncomeTax,payPeriod);
        }

        // Handle special tax codes
        if(TaxThreshold.TaxRegion.ENGLAND==region || TaxThreshold.TaxRegion.NORTHERN_IRELAND==region) {
            switch(normalizedTaxCode) {
                case "NT":
                    return BigDecimal.ZERO;
                case "BR":
                    BigDecimal AnnualBRTax= grossIncome.multiply(taxRates[1]); // Basic Rate (20%)
                    return calculateIncomeTaxBasedOnPayPeriod(AnnualBRTax,payPeriod);
                case "D0":
                    BigDecimal AnnualD0Tax=grossIncome.multiply(taxRates[2]); // Higher Rate (40%)
                    return calculateIncomeTaxBasedOnPayPeriod(AnnualD0Tax,payPeriod);
                case "D1":
                    BigDecimal AnnualD1Tax = grossIncome.multiply(taxRates[3]); // Additional Rate (45%)
                    return calculateIncomeTaxBasedOnPayPeriod(AnnualD1Tax,payPeriod);
                case "0T":
                    BigDecimal Annual0TTax = incomeTaxCalculation.calculateIncomeTax(grossIncome,personalAllowance, taxSlabs, taxRates,payPeriod);
                    return calculateIncomeTaxBasedOnPayPeriod(Annual0TTax,payPeriod);
            }
            throw new IllegalArgumentException("Unrecognized tax code: " + taxCode);

        }

        else if (TaxThreshold.TaxRegion.WALES==region){
            switch(normalizedTaxCode) {
                case "CNT":
                    return BigDecimal.ZERO;
                case "CBR":
                    BigDecimal AnnualCBRTax=grossIncome.multiply(taxRates[1]); // Basic Rate (20%)
                    return calculateIncomeTaxBasedOnPayPeriod(AnnualCBRTax,payPeriod);
                case "CD0":
                    BigDecimal AnnualCD0Tax= grossIncome.multiply(taxRates[2]); // Higher Rate (40%)
                    return calculateIncomeTaxBasedOnPayPeriod(AnnualCD0Tax,payPeriod);
                case "CD1":
                    BigDecimal AnnualCD1Tax= grossIncome.multiply(taxRates[3]); // Additional Rate (45%)
                    return calculateIncomeTaxBasedOnPayPeriod(AnnualCD1Tax,payPeriod);
                case "C0T":
                    BigDecimal AnnualC0TTax = incomeTaxCalculation.calculateIncomeTax(grossIncome,personalAllowance, taxSlabs, taxRates,payPeriod);
                    return calculateIncomeTaxBasedOnPayPeriod(AnnualC0TTax,payPeriod);
            }
            throw new IllegalArgumentException("Unrecognized tax code: " + taxCode);
        }
        else if (TaxThreshold.TaxRegion.SCOTLAND==region){
            switch(normalizedTaxCode) {
                case "SNT":
                    return BigDecimal.ZERO;
                case "SBR":
                    BigDecimal AnnualSBRTax = grossIncome.multiply(taxRates[1]); // Basic Rate (20%)
                    return calculateIncomeTaxBasedOnPayPeriod(AnnualSBRTax,payPeriod);
                case "SD0":
                    BigDecimal AnnualSD0Tax = grossIncome.multiply(taxRates[2]); // Higher Rate (40%)
                    return calculateIncomeTaxBasedOnPayPeriod(AnnualSD0Tax,payPeriod);
                case "SD1":
                    BigDecimal AnnualSD1Tax = grossIncome.multiply(taxRates[3]); // Additional Rate (45%)
                    return calculateIncomeTaxBasedOnPayPeriod(AnnualSD1Tax,payPeriod);
                case "S0T":
                    BigDecimal AnnualIncomeTax = scotlandTaxCalculation.calculateScotlandIncomeTax(grossIncome,personalAllowance, taxSlabs, taxRates,payPeriod);
                    return calculateIncomeTaxBasedOnPayPeriod(AnnualIncomeTax,payPeriod);
            }
            throw new IllegalArgumentException("Unrecognized tax code: " + taxCode);

        }
        else{
            throw new IllegalArgumentException("Invalid region: " + region);
        }



//        throw new IllegalArgumentException("Unrecognized tax code: " + taxCode);
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
    public BigDecimal calculateTaxableIncome(BigDecimal grossIncome,BigDecimal personalAllowance){
        BigDecimal taxableIncome = grossIncome.subtract(personalAllowance);
        taxableIncome = taxableIncome.compareTo(BigDecimal.ZERO) > 0 ? taxableIncome : BigDecimal.ZERO;
        return taxableIncome;

    }


}

 /*else if (TaxThreshold.TaxRegion.NORTHERN_IRELAND==region) {
        switch(normalizedTaxCode) {
        case "NILNT":
        return BigDecimal.ZERO;
                case "NILBR":
                        return grossIncome.multiply(taxRates[1]); // Basic Rate (20%)
                case "NILD0":
                        return grossIncome.multiply(taxRates[2]); // Higher Rate (40%)
                case "NILD1":
                        return grossIncome.multiply(taxRates[3]); // Additional Rate (45%)
                case "NIL0T":
BigDecimal AnnualIncomeTax = incomeTaxCalculation.calculateIncomeTax(grossIncome,normalizedTaxCode, taxSlabs, taxRates,region);
                    return calculateIncomeTaxBasedOnPayPeriod(AnnualIncomeTax,payPeriod);
            }


                    }*/