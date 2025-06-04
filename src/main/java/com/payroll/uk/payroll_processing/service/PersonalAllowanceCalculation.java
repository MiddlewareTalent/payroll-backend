package com.payroll.uk.payroll_processing.service;

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


    public BigDecimal calculatePersonalAllowance(BigDecimal grossIncome,String taxCode) {
        if (taxCode == null || taxCode.isEmpty()) {
            throw new IllegalArgumentException("Tax code cannot be null or empty");
        }

//        if (grossIncome.compareTo(new BigDecimal("100000")) > 0) {
//            return BigDecimal.ZERO;
//        }


        taxCode = taxCode.toUpperCase();

        // Zero-allowance cases (BR, D0, D1, NT, K-codes, 0T, etc.)
        if (isZeroAllowanceCode(taxCode)) {
            return BigDecimal.ZERO;
        }



        // Check for Scottish Marriage Allowance (invalid)
        if (taxCode.startsWith("SM") || taxCode.startsWith("SN")) {
            throw new IllegalArgumentException("Marriage Allowance not available in Scotland");
        }

        // Handle pure Marriage Allowance codes (M/N without numbers)
        if (taxCode.equals("M") || taxCode.equals("CM") ) {
            return MARRIAGE_ALLOWANCE_TRANSFER;
        }
        if (taxCode.equals("N") || taxCode.equals("CN")) {
            return MARRIAGE_ALLOWANCE_TRANSFER.negate();
        }
        if (grossIncome.compareTo(new BigDecimal("125140")) >= 0) {
            return BigDecimal.ZERO;
        }
        if(taxCode.equals("1257L")|| taxCode.equals("C1257L") ||taxCode.equals("S1257L")) {
            return STANDARD_ALLOWANCE;
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
                taxCode.equals("NT") || taxCode.startsWith("K") || taxCode.equals("0T") ||
                taxCode.equals("CBR") || taxCode.equals("CD0") || taxCode.equals("CD1") ||
                taxCode.equals("CNT") || taxCode.startsWith("CK") ||taxCode.startsWith("SK")|| taxCode.equals("C0T");
    }

    private String stripRegionalPrefix(String taxCode) {
        if (taxCode.startsWith("S") || taxCode.startsWith("C")) {
            return taxCode.substring(1);
        }
        return taxCode;
    }

    public BigDecimal calculateMarriageAllowance(String taxCode) {
        if (taxCode == null || taxCode.isEmpty()) {
            throw new IllegalArgumentException("Tax code cannot be null or empty");
        }

        taxCode = taxCode.toUpperCase();

        if (taxCode.startsWith("SM") || taxCode.startsWith("SN")) {
            throw new IllegalArgumentException("Marriage Allowance not available in Scotland");
        }

        if (taxCode.equals("M") || taxCode.equals("CM") ) {
            return MARRIAGE_ALLOWANCE_TRANSFER;
        }
        if (taxCode.equals("N") || taxCode.equals("CN")) {
            return MARRIAGE_ALLOWANCE_TRANSFER.negate();
        }

        return BigDecimal.ZERO;
    }

    public BigDecimal calculatePersonalAllowanceByPayPeriod(BigDecimal grossIncome,String taxCode,String payPeriod){
        BigDecimal baseAllowance = calculatePersonalAllowance(grossIncome, taxCode);
        return switch (payPeriod.toUpperCase()) {
            case "WEEKLY" -> baseAllowance.divide(BigDecimal.valueOf(52), 2, ROUNDING_MODE);
            case "MONTHLY" -> baseAllowance.divide(BigDecimal.valueOf(12), 2, ROUNDING_MODE);
            case "QUARTERLY" -> baseAllowance.divide(BigDecimal.valueOf(4),2, ROUNDING_MODE);
            case "YEARLY" -> baseAllowance;
            default -> throw new IllegalArgumentException("Invalid pay period. Must be WEEKLY, MONTHLY or YEARLY");
        };

    }

//    public LinkedHashMap<String,BigDecimal> calculateTotalPersonalAllowance(){
//         BigDecimal previouslyUsedPersonalAllowance;
//         BigDecimal totalPersonalAllowanceInCompany;
//         BigDecimal usedPersonalAllowance;
//         BigDecimal remainingPersonalAllowance;
//        LinkedHashMap<String, BigDecimal> personalAllowanceMap = new LinkedHashMap<>();
//        personalAllowanceMap.put(usedPersonalAllowance,)
//        return personalAllowanceMap;
//    }


}

