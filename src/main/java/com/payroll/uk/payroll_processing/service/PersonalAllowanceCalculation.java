package com.payroll.uk.payroll_processing.service;

import com.payroll.uk.payroll_processing.exception.DataValidationException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.LinkedHashMap;

@Service
public class PersonalAllowanceCalculation {
//    public BigDecimal usedAllowance= BigDecimal.valueOf(0.0);
//    public BigDecimal remainingAllowance= BigDecimal.valueOf(0.0);
//    public BigDecimal totalAllowance= BigDecimal.valueOf(12570);
//    public BigDecimal deductedAllowance= BigDecimal.valueOf(0.0);

    private static final BigDecimal STANDARD_ALLOWANCE = new BigDecimal("12570");
    private static final BigDecimal MARRIAGE_ALLOWANCE_TRANSFER = new BigDecimal("1257"); // 10% of £12,570
    private static final RoundingMode ROUNDING_MODE = RoundingMode.HALF_UP;


    public BigDecimal calculatePersonalAllowance(String taxCode,String payPeriod) {
        if (taxCode == null || taxCode.isEmpty()) {
            throw new DataValidationException("Tax code cannot be null or empty");
        }


        taxCode = taxCode.toUpperCase();

        // Zero-allowance cases (BR, D0, D1, NT, K-codes, 0T, etc.)
        if (isZeroAllowanceCode(taxCode)) {
            return BigDecimal.ZERO;
        }



        // Check for Scottish Marriage Allowance (invalid)
        if (taxCode.startsWith("SM") || taxCode.startsWith("SN")) {
            throw new DataValidationException("Marriage Allowance not available in Scotland");
        }

        // Handle pure Marriage Allowance codes (M/N without numbers)
        if (taxCode.equals("M") || taxCode.equals("CM") ) {
            return MARRIAGE_ALLOWANCE_TRANSFER;
        }
        if (taxCode.equals("N") || taxCode.equals("CN")) {
            return MARRIAGE_ALLOWANCE_TRANSFER.negate();
        }



        // Strip regional prefixes (S, C, NIL)
        String processedCode = stripRegionalPrefix(taxCode);

        // Extract numeric part (e.g., "1257" from "1257L")
        String numericPart = processedCode.replaceAll("[^0-9]", "");
        if (numericPart.isEmpty()) {
            return BigDecimal.ZERO; // No numeric allowance
        }

        // Calculate base allowance (numeric part × 10)
        BigDecimal baseAllowance = new BigDecimal(numericPart)
                .multiply(new BigDecimal("10"))
                .setScale(0, ROUNDING_MODE);

        // Apply Marriage Allowance adjustment (if applicable)
        if (processedCode.startsWith("M")) {
//            return baseAllowance.add(MARRIAGE_ALLOWANCE_TRANSFER);
            return baseAllowance.add(baseAllowance.multiply(new BigDecimal("0.10")));
        } else if (processedCode.startsWith("N")) {
//            return baseAllowance.subtract(MARRIAGE_ALLOWANCE_TRANSFER);
            return baseAllowance.subtract(baseAllowance.multiply(new BigDecimal("0.10")));
        }
        System.out.println("Base Allowance: " + baseAllowance);

        return baseAllowance;
    }
    private boolean isZeroAllowanceCode(String taxCode) {
        return taxCode.equals("BR") || taxCode.equals("D0") || taxCode.equals("D1") ||
                taxCode.equals("NT") || taxCode.startsWith("K") || taxCode.equals("0T") ||taxCode.equals("S0T") ||
                taxCode.equals("CBR") || taxCode.equals("SD0") || taxCode.equals("SD1") ||taxCode.equals("SD2") ||taxCode.equals("SBR") ||
                taxCode.equals("CD0") ||taxCode.equals("CD1") ||taxCode.startsWith("CK") ||taxCode.startsWith("SK")|| taxCode.equals("C0T");
    }

    private String stripRegionalPrefix(String taxCode) {
        if (taxCode.startsWith("S") || taxCode.startsWith("C")) {
            return taxCode.substring(1);
        }
        return taxCode;
    }
    public boolean isEmergencyCode(String code) {
        return code.matches("^\\d+L\\s?(M1|W1|X)$") || code.matches("^C\\d+L\\s?(M1|W1|X)$")||code.matches("^S\\d+L\\s?(M1|W1|X)$");
    }


    public BigDecimal calculateMarriageAllowance(String taxCode) {
        if (taxCode == null || taxCode.isEmpty()) {
            throw new DataValidationException("Tax code cannot be null or empty");
        }

        taxCode = taxCode.toUpperCase();

        if (taxCode.startsWith("SM") || taxCode.startsWith("SN")) {
            throw new DataValidationException("Marriage Allowance not available in Scotland");
        }

        if (taxCode.equals("M") || taxCode.equals("CM") ) {
            return MARRIAGE_ALLOWANCE_TRANSFER;
        }
        if (taxCode.equals("N") || taxCode.equals("CN")) {
            return MARRIAGE_ALLOWANCE_TRANSFER.negate();
        }

        return BigDecimal.ZERO;
    }

    public BigDecimal getPersonalAllowanceFromEmergencyTaxCode(String taxCode, String payFrequency) {
        String normalizedTaxCode = taxCode.toUpperCase();
        BigDecimal annualAllowance = new BigDecimal("12570");
        BigDecimal allowance=BigDecimal.ZERO;

        if (normalizedTaxCode.matches("1257L M1")) {
            allowance = annualAllowance.divide(new BigDecimal("12"), 2, RoundingMode.HALF_UP); // Monthly
        } else if (normalizedTaxCode.matches("1257L W1")) {
            allowance = annualAllowance.divide(new BigDecimal("52"), 2, RoundingMode.HALF_UP); // Weekly
        } else if (normalizedTaxCode.matches("1257L X")) {
            // 'X' is usually non-cumulative, treat as per pay frequency
            if (payFrequency.equalsIgnoreCase("monthly")) {
                allowance = annualAllowance.divide(new BigDecimal("12"), 2, RoundingMode.HALF_UP);
            } else if (payFrequency.equalsIgnoreCase("weekly")) {
                allowance = annualAllowance.divide(new BigDecimal("52"), 2, RoundingMode.HALF_UP);
            } else {
                throw new DataValidationException("Unsupported pay frequency for X suffix: " + payFrequency);
            }
        }


        /*else {
            // Regular cumulative tax code
            allowance = annualAllowance;
        }*/

        return allowance;
    }


    public BigDecimal calculatePersonalAllowanceByPayPeriod(String taxCode,String payPeriod){
        BigDecimal baseAllowance = calculatePersonalAllowance( taxCode,payPeriod);
        return switch (payPeriod.toUpperCase()) {
            case "WEEKLY" -> baseAllowance.divide(BigDecimal.valueOf(52), 2, ROUNDING_MODE);
            case "MONTHLY" -> baseAllowance.divide(BigDecimal.valueOf(12), 2, ROUNDING_MODE);
            case "QUARTERLY" -> baseAllowance.divide(BigDecimal.valueOf(4),2, ROUNDING_MODE);
            case "YEARLY" -> baseAllowance;
            default -> throw new DataValidationException("Invalid pay period. Must be WEEKLY, MONTHLY or YEARLY");
        };

    }

    public  BigDecimal getRemainingAllowance(BigDecimal remainingPersonalAllowance, BigDecimal getPersonalAllowance) {
        BigDecimal personalAllowance;

        if (remainingPersonalAllowance.compareTo(BigDecimal.ZERO) <= 0) {
            // If no personal allowance left
            personalAllowance = BigDecimal.ZERO;
        } else if (getPersonalAllowance.compareTo(remainingPersonalAllowance) >= 0) {
            // If this month's allowance is more than what's remaining
            personalAllowance = remainingPersonalAllowance;
        } else {
            // Apply the full amount scheduled for this pay period
            personalAllowance = getPersonalAllowance;
        }
        return personalAllowance;
    }




}

