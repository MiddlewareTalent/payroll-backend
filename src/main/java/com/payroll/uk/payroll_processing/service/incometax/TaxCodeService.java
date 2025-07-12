package com.payroll.uk.payroll_processing.service.incometax;

import com.payroll.uk.payroll_processing.entity.TaxThreshold;
import com.payroll.uk.payroll_processing.exception.InvalidPayPeriodException;
import com.payroll.uk.payroll_processing.exception.InvalidTaxCodeException;
import com.payroll.uk.payroll_processing.repository.EmployeeDetailsRepository;
import com.payroll.uk.payroll_processing.service.PersonalAllowanceCalculation;
import com.payroll.uk.payroll_processing.service.TaxThresholdService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

//@Slf4j
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
            throw new IllegalArgumentException("Parameters cannot be null");
        }




        if (grossIncome.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Income cannot be negative");
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
            if (normalizedTaxCode.matches("^K\\d+$") || normalizedTaxCode.matches("^SK\\d+$") || normalizedTaxCode.matches("^CK\\d+$")) {
                BigDecimal AnnualKCodeIncomeTax = incomeTaxCalculation.calculateTaxWithKCode(grossIncome, normalizedTaxCode, taxableIncomeAnnual, taxSlabs, taxRates, payPeriod);
                return calculateIncomeTaxBasedOnPayPeriod(AnnualKCodeIncomeTax, payPeriod);
            }

            // Handle Scottish codes (starting with S followed by numbers and ending with L)
            if (normalizedTaxCode.matches("^S\\d+L$")) {
                return scotlandTaxCalculation.calculateScotlandIncomeTax(grossIncome, personalAllowance, taxableIncomeAnnual, taxSlabs, taxRates, payPeriod);
//            return calculateIncomeTaxBasedOnPayPeriod(AnnualIncomeTax,payPeriod);
            }

            // Handle special tax codes
            if (TaxThreshold.TaxRegion.ENGLAND == region || TaxThreshold.TaxRegion.NORTHERN_IRELAND == region) {
                switch (normalizedTaxCode) {
                    case "NT":
                        return BigDecimal.ZERO;
                    case "BR":
                        return grossIncome.multiply(taxRates[1]); // Basic Rate (20%)

                    case "D0":
                        return grossIncome.multiply(taxRates[2]); // Higher Rate (40%)

                    case "D1":
                        return grossIncome.multiply(taxRates[3]); // Additional Rate (45%)

                    case "0T":
                        BigDecimal Annual0TTax = incomeTaxCalculation.calculateIncomeTax(grossIncome, personalAllowance, taxableIncomeAnnual, taxSlabs, taxRates, payPeriod);
                        return calculateIncomeTaxBasedOnPayPeriod(Annual0TTax, payPeriod);
                }
                throw new InvalidTaxCodeException("Unrecognized tax code: " + taxCode);

            } else if (TaxThreshold.TaxRegion.WALES == region) {
                switch (normalizedTaxCode) {
                    case "CNT":
                        return BigDecimal.ZERO;
                    case "CBR":
                        return grossIncome.multiply(taxRates[1]); // Basic Rate (20%)
                    case "CD0":
                        return grossIncome.multiply(taxRates[2]); // Higher Rate (40%)
                    case "CD1":
                        return grossIncome.multiply(taxRates[3]); // Additional Rate (45%)
                    case "C0T":
                        BigDecimal AnnualC0TTax = incomeTaxCalculation.calculateIncomeTax(grossIncome, personalAllowance, taxableIncomeAnnual, taxSlabs, taxRates, payPeriod);
                        return calculateIncomeTaxBasedOnPayPeriod(AnnualC0TTax, payPeriod);
                }
                throw new InvalidTaxCodeException("Unrecognized tax code: " + taxCode);
            } else if (TaxThreshold.TaxRegion.SCOTLAND == region) {
                switch (normalizedTaxCode) {
                    case "SNT":
                        return BigDecimal.ZERO;
                    case "SBR":
                        return grossIncome.multiply(taxRates[1]); // Basic Rate (20%)

                    case "SD0":
                        return grossIncome.multiply(taxRates[2]); // Higher Rate (40%)

                    case "SD1":
                        return grossIncome.multiply(taxRates[3]); // Additional Rate (45%)

                    case "S0T":
                        return scotlandTaxCalculation.calculateScotlandIncomeTax(grossIncome, personalAllowance, taxableIncomeAnnual, taxSlabs, taxRates, payPeriod);
//                    return calculateIncomeTaxBasedOnPayPeriod(AnnualIncomeTax,payPeriod);
                }
                throw new InvalidTaxCodeException("Unrecognized tax code: " + taxCode);

            } else {
                throw new IllegalArgumentException("Invalid region: " + region);
            }
        }
        catch (IllegalArgumentException e) {
            logger.error("Error in calculateIncomeBasedOnTaxCode: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error in calculateIncomeBasedOnTaxCode: {}", e.getMessage(), e);
            throw new RuntimeException("An unexpected error occurred while calculating income tax", e);
        }

    }


    public BigDecimal  calculateIncomeTaxBasedOnPayPeriod(BigDecimal incomeTax,String payPeriod){
        return switch (payPeriod.toUpperCase()) {
            case "WEEKLY" -> incomeTax.divide(BigDecimal.valueOf(52), 4, RoundingMode.HALF_UP);
            case "MONTHLY" -> incomeTax.divide(BigDecimal.valueOf(12), 4, RoundingMode.HALF_UP);
            case "QUARTERLY" -> incomeTax.divide(BigDecimal.valueOf(4), 4, RoundingMode.HALF_UP);
            case "YEARLY" -> incomeTax;
            default -> throw new InvalidPayPeriodException("Invalid pay period. Must be "+payPeriod);
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
            default -> throw new IllegalArgumentException("Invalid pay period. Must be WEEKLY, MONTHLY or YEARLY");
        };
        return annualGross.setScale(2, RoundingMode.HALF_UP);


    }


}
