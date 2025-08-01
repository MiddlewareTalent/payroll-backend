package com.payroll.uk.payroll_processing.service.incometax;

import com.payroll.uk.payroll_processing.entity.TaxThreshold;
import com.payroll.uk.payroll_processing.exception.DataValidationException;
import com.payroll.uk.payroll_processing.exception.InvalidComputationException;
import com.payroll.uk.payroll_processing.service.TaxThresholdService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;


@Service
public class TaxCodeService {

    private static final Logger logger = LoggerFactory.getLogger(TaxCodeService.class);
    private final TaxThresholdService taxThresholdService;
    private final IncomeTaxCalculation incomeTaxCalculation;
    private  final ScotlandTaxCalculation scotlandTaxCalculation;
    public TaxCodeService(TaxThresholdService taxThresholdService, IncomeTaxCalculation incomeTaxCalculation,  ScotlandTaxCalculation scotlandTaxCalculation)  {
        this.taxThresholdService = taxThresholdService;
        this.incomeTaxCalculation = incomeTaxCalculation;
        this.scotlandTaxCalculation = scotlandTaxCalculation;

    }

    public BigDecimal calculateIncomeBasedOnTaxCode(BigDecimal grossIncome,BigDecimal personalAllowanceGet,BigDecimal taxableIncomeGet, String taxYear,
                                                    TaxThreshold.TaxRegion region, String taxCode,String payPeriod) {
        logger.info("Income tax calculation");
        // Validate inputs
        if (grossIncome == null || taxCode == null || taxYear == null || region == null || payPeriod == null) {
            throw new DataValidationException("Parameters cannot be null");
        }


       logger.info("Gross Income: {} , Personal Allowance: {}, Taxable Income: {}, Tax Year: {}, Region: {}, Tax Code: {}, Pay Period: {}",
                grossIncome, personalAllowanceGet, taxableIncomeGet, taxYear, region, taxCode, payPeriod);

        if (grossIncome.compareTo(BigDecimal.ZERO) < 0) {
            throw new DataValidationException("Income cannot be negative");
        }

        try {
            // Get tax thresholds and rates
            BigDecimal[][] taxSlabs = taxThresholdService.getTaxBounds(taxYear, region, TaxThreshold.BandNameType.INCOME_TAX);
            BigDecimal[] taxRates = taxThresholdService.getTaxRates(taxYear, region, TaxThreshold.BandNameType.INCOME_TAX);

            logger.info(payPeriod, " {} Gross Income:{} ", grossIncome);
            BigDecimal personalAllowance = calculateToAnnual(personalAllowanceGet, payPeriod);
            logger.info("Personal Allowance Annual: {} ", personalAllowance);
            //Calculate taxable income
            BigDecimal taxableIncomeAnnual = calculateToAnnual(taxableIncomeGet, payPeriod);
            logger.info("Taxable Income Annual: {} ", taxableIncomeAnnual);


            // Convert to uppercase for case-insensitive comparison
            String normalizedTaxCode = taxCode.toUpperCase();
            logger.info("normalizedTaxCode: {}", normalizedTaxCode);

            // Handle numeric tax codes ending with L (like 1257L)
            if (normalizedTaxCode.matches("^\\d+L$") || normalizedTaxCode.matches("^C\\d+L$")) {
                logger.info("Inside standard tax code: {} ", normalizedTaxCode);
                BigDecimal AnnualIncomeTax = incomeTaxCalculation.calculateIncomeTax(grossIncome, personalAllowance, taxableIncomeAnnual, taxSlabs, taxRates, payPeriod);
                return calculateIncomeTaxBasedOnPayPeriod(AnnualIncomeTax, payPeriod);
            }
            // Handle Scottish codes (starting with S followed by numbers and ending with L)
            else if (normalizedTaxCode.matches("^S\\d+L$")) {
                return scotlandTaxCalculation.calculateScotlandIncomeTax(grossIncome, personalAllowance, taxableIncomeAnnual, taxSlabs, taxRates, payPeriod);
//            return calculateIncomeTaxBasedOnPayPeriod(AnnualIncomeTax,payPeriod);
            }

            if (normalizedTaxCode.matches("1257L M1") && (region == TaxThreshold.TaxRegion.ENGLAND || region == TaxThreshold.TaxRegion.WALES || region == TaxThreshold.TaxRegion.NORTHERN_IRELAND) ||
                    normalizedTaxCode.matches("1257L W1") && (region == TaxThreshold.TaxRegion.ENGLAND || region == TaxThreshold.TaxRegion.WALES || region == TaxThreshold.TaxRegion.NORTHERN_IRELAND)
                    || normalizedTaxCode.matches("1257L X") && (region == TaxThreshold.TaxRegion.ENGLAND || region == TaxThreshold.TaxRegion.WALES || region == TaxThreshold.TaxRegion.NORTHERN_IRELAND)) {
                logger.info("Inside the emergency tax code: {} ", normalizedTaxCode);
                BigDecimal AnnualIncomeTax = incomeTaxCalculation.calculateIncomeTax(grossIncome, personalAllowance, taxableIncomeAnnual, taxSlabs, taxRates, payPeriod);
                return calculateIncomeTaxBasedOnPayPeriod(AnnualIncomeTax, payPeriod);
            } else if (normalizedTaxCode.matches("1257L M1") && (region == TaxThreshold.TaxRegion.SCOTLAND) ||
                    normalizedTaxCode.matches("1257L W1") && (region == TaxThreshold.TaxRegion.SCOTLAND) ||
                    normalizedTaxCode.matches("1257L X") && (region == TaxThreshold.TaxRegion.SCOTLAND)) {
                return scotlandTaxCalculation.calculateScotlandIncomeTax(grossIncome, personalAllowance, taxableIncomeAnnual, taxSlabs, taxRates, payPeriod);
//            return calculateIncomeTaxBasedOnPayPeriod(AnnualIncomeTax,payPeriod);
            }


            // Handle K codes (negative allowances)
           /* if (normalizedTaxCode.matches("^K\\d+$") || normalizedTaxCode.matches("^SK\\d+$") || normalizedTaxCode.matches("^CK\\d+$")) {
                BigDecimal AnnualKCodeIncomeTax = incomeTaxCalculation.calculateTaxWithKCode(grossIncome, normalizedTaxCode, taxableIncomeAnnual, taxSlabs, taxRates, payPeriod);
                return calculateIncomeTaxBasedOnPayPeriod(AnnualKCodeIncomeTax, payPeriod);
            }*/
            if (normalizedTaxCode.matches("^K\\d+$")|| normalizedTaxCode.matches("^CK\\d+$")){
                logger.info("Inside standard tax code: {} ", normalizedTaxCode);
                BigDecimal AnnualIncomeTax = incomeTaxCalculation.calculateIncomeTax(grossIncome, personalAllowance, taxableIncomeAnnual, taxSlabs, taxRates, payPeriod);
                return calculateIncomeTaxBasedOnPayPeriod(AnnualIncomeTax, payPeriod);

            }
            else if(normalizedTaxCode.matches("^SK\\d+$")){
                return scotlandTaxCalculation.calculateScotlandIncomeTax(grossIncome, personalAllowance, taxableIncomeAnnual, taxSlabs, taxRates, payPeriod);
            }


            // Handle special tax codes
            if (TaxThreshold.TaxRegion.ENGLAND == region || TaxThreshold.TaxRegion.NORTHERN_IRELAND == region) {
                return switch (normalizedTaxCode) {
                    case "NT" -> BigDecimal.ZERO;
                    case "BR" -> grossIncome.multiply(taxRates[1]); // Basic Rate (20%)

                    case "D0" -> grossIncome.multiply(taxRates[2]); // Higher Rate (40%)

                    case "D1" -> grossIncome.multiply(taxRates[3]); // Additional Rate (45%)

                    case "0T" -> {
                        BigDecimal Annual0TTax = incomeTaxCalculation.calculateIncomeTax(grossIncome, personalAllowance, taxableIncomeAnnual, taxSlabs, taxRates, payPeriod);
                        yield calculateIncomeTaxBasedOnPayPeriod(Annual0TTax, payPeriod);
                    }
                    default -> throw new DataValidationException("Unrecognized tax code: " + taxCode);
                };

            } else if (TaxThreshold.TaxRegion.WALES == region) {
                return switch (normalizedTaxCode) {
                    case "NT" -> BigDecimal.ZERO;
                    case "CBR" -> grossIncome.multiply(taxRates[1]); // Basic Rate (20%)
                    case "CD0" -> grossIncome.multiply(taxRates[2]); // Higher Rate (40%)
                    case "CD1" -> grossIncome.multiply(taxRates[3]); // Additional Rate (45%)
                    case "C0T" -> {
                        BigDecimal AnnualC0TTax = incomeTaxCalculation.calculateIncomeTax(grossIncome, personalAllowance, taxableIncomeAnnual, taxSlabs, taxRates, payPeriod);
                        yield calculateIncomeTaxBasedOnPayPeriod(AnnualC0TTax, payPeriod);
                    }
                    default -> throw new DataValidationException("Unrecognized tax code: " + taxCode);
                };
            } else if (TaxThreshold.TaxRegion.SCOTLAND == region) {
                return switch (normalizedTaxCode) {
                    case "NT" -> BigDecimal.ZERO;
                    case "SBR" -> grossIncome.multiply(taxRates[2]); // Basic Rate (20%)


                    case "SD0" -> grossIncome.multiply(taxRates[3]); // Intermediate Rate (21%)

                    case "SD1" -> grossIncome.multiply(taxRates[4]); // higher Rate (42%)
                    case "SD2" -> grossIncome.multiply(taxRates[5]);  // Additional Rate (48%)
                    case "SD3" -> grossIncome.multiply(taxRates[6]);  // Top Rate (48%)

                    case "S0T" ->
                            scotlandTaxCalculation.calculateScotlandIncomeTax(grossIncome, personalAllowance, taxableIncomeAnnual, taxSlabs, taxRates, payPeriod);
//                    return calculateIncomeTaxBasedOnPayPeriod(AnnualIncomeTax,payPeriod);
                    default -> throw new DataValidationException("Unrecognized tax code: " + taxCode);
                };

            } else {
                throw new DataValidationException("Invalid region: " + region);
            }
        }
        catch (InvalidComputationException e) {
            logger.error("Error in calculateIncomeBasedOnTaxCode: {}", e.getMessage());
            throw new InvalidComputationException("Error in income tax calculation: " + e.getMessage(), e);
        } catch (Exception e) {
            logger.error("Unexpected error in calculateIncomeBasedOnTaxCode: {}", e.getMessage(), e);
            throw new RuntimeException("An unexpected error occurred while calculating income tax", e);
        }

    }


    public BigDecimal  calculateIncomeTaxBasedOnPayPeriod(BigDecimal incomeTax,String payPeriod){
        return switch (payPeriod.toUpperCase()) {
            case "WEEKLY" -> incomeTax.divide(BigDecimal.valueOf(52),4, RoundingMode.HALF_UP);
            case "MONTHLY" -> incomeTax.divide(BigDecimal.valueOf(12), 4, RoundingMode.HALF_UP);
            case "QUARTERLY" -> incomeTax.divide(BigDecimal.valueOf(4), 4, RoundingMode.HALF_UP);
            case "YEARLY" -> incomeTax;
            default -> throw new DataValidationException("Invalid pay period. Must be "+payPeriod);
        };
    }
    public BigDecimal calculateTaxableIncome(BigDecimal grossIncome,BigDecimal personalAllowance){
        BigDecimal taxableIncome = grossIncome.subtract(personalAllowance);
        taxableIncome = taxableIncome.compareTo(BigDecimal.ZERO) > 0 ? taxableIncome : BigDecimal.ZERO;
        return taxableIncome;

    }

    public BigDecimal calculateToAnnual(BigDecimal grossIncome,String payPeriod){
        BigDecimal annualGross;
        switch (payPeriod.toUpperCase()) {
            case "WEEKLY" -> annualGross=grossIncome.multiply(BigDecimal.valueOf(52));
            case "MONTHLY" -> annualGross=grossIncome.multiply(BigDecimal.valueOf(12));
            case "QUARTERLY" -> annualGross=grossIncome.multiply(BigDecimal.valueOf(4));
            case "YEARLY" -> annualGross=grossIncome;
            default -> throw new DataValidationException("Invalid pay period. Must be WEEKLY, MONTHLY or YEARLY");
        }
        return annualGross;
//        return annualGross.setScale(2, RoundingMode.HALF_UP);


    }


}
