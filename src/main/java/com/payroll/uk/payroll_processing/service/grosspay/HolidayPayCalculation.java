package com.payroll.uk.payroll_processing.service.grosspay;

import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
public class HolidayPayCalculation {

    private static final int WEEKS_PER_YEAR = 52;
    private static final double STATUTORY_HOLIDAY_WEEKS = 5.6; // UK minimum

    public BigDecimal calculateHolidayPay(BigDecimal income,
                                          int holidayDays,
                                          String payPeriod,
                                          int workingDaysPerWeek) {

        validateInput(income, holidayDays, payPeriod, workingDaysPerWeek);

        BigDecimal annualSalary = convertToAnnualSalary(income, payPeriod);
        BigDecimal weeklySalary = annualSalary.divide(
                BigDecimal.valueOf(WEEKS_PER_YEAR), 2, RoundingMode.HALF_UP);

        BigDecimal dailyPay = weeklySalary.divide(
                BigDecimal.valueOf(workingDaysPerWeek), 2, RoundingMode.HALF_UP);

        return dailyPay.multiply(BigDecimal.valueOf(holidayDays));
    }

    public BigDecimal calculateStatutoryHolidayEntitlement(BigDecimal income,
                                                           String payPeriod,
                                                           int workingDaysPerWeek) {
        BigDecimal annualSalary = convertToAnnualSalary(income, payPeriod);
        BigDecimal weeklySalary = annualSalary.divide(
                BigDecimal.valueOf(WEEKS_PER_YEAR), 2, RoundingMode.HALF_UP);

        return weeklySalary.multiply(BigDecimal.valueOf(STATUTORY_HOLIDAY_WEEKS));
    }

    private BigDecimal convertToAnnualSalary(BigDecimal income, String payPeriod) {
        return switch (payPeriod.toUpperCase()) {
            case "WEEKLY" -> income.multiply(BigDecimal.valueOf(52));
            case "MONTHLY" -> income.multiply(BigDecimal.valueOf(12));
            case "YEARLY" -> income;
            default -> throw new IllegalArgumentException("Invalid pay period");
        };
    }

    public BigDecimal deductHolidayFromEntitlement(
            BigDecimal annualEntitlement,
            BigDecimal holidayPayToDeduct) {

        // Validate inputs
        if (annualEntitlement == null || holidayPayToDeduct == null) {
            throw new IllegalArgumentException("Arguments cannot be null");
        }
        if (holidayPayToDeduct.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Deduction amount must be positive");
        }
        if (holidayPayToDeduct.compareTo(annualEntitlement) > 0) {
            throw new IllegalArgumentException("Cannot deduct more than remaining entitlement");
        }

        return annualEntitlement.subtract(holidayPayToDeduct);
    }

    private void validateInput(BigDecimal income,
                               int holidayDays,
                               String payPeriod,
                               int workingDaysPerWeek) {

        if (income == null || income.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Income must be positive");
        }
        if (holidayDays <= 0 || holidayDays > 28) {
            throw new IllegalArgumentException("Holiday days must be between 1-28");
        }
        if (payPeriod == null || !List.of("WEEKLY","MONTHLY","YEARLY").contains(payPeriod.toUpperCase())) {
            throw new IllegalArgumentException("Invalid pay period");
        }
        if (workingDaysPerWeek < 1 || workingDaysPerWeek > 7) {
            throw new IllegalArgumentException("Invalid working days per week");
        }
    }
}