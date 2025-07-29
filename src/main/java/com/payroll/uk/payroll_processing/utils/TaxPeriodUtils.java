package com.payroll.uk.payroll_processing.utils;

import com.payroll.uk.payroll_processing.exception.DataValidationException;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.time.Month;
import java.time.Period;
import java.time.temporal.ChronoUnit;

/**
 * TaxMonthUtils

 * Utility class to calculate the UK tax month number (1–12) based on a given date.

 * The UK tax year starts on **6th April** and ends on **5th April** the following year.
 * Each tax month runs from the 6th of one month to the 5th of the next:

 * Tax Month Ranges:
 *  - Month 1: 6 April – 5 May
 *  - Month 2: 6 May – 5 June
 *  - Month 3: 6 June – 5 July
 *  - Month 4: 6 July – 5 August
 *  - Month 5: 6 August – 5 September
 *  - Month 6: 6 September – 5 October
 *  - Month 7: 6 October – 5 November
 *  - Month 8: 6 November – 5 December
 *  - Month 9: 6 December – 5 January
 *  - Month 10: 6 January – 5 February
 *  - Month 11: 6 February – 5 March
 *  - Month 12: 6 March – 5 April

 * This class estimates the tax month by counting days since the start of the tax year.
 */


@Configuration
public class TaxPeriodUtils {

    /**
     * Returns the UK tax month (1–12) for the given date.
     * UK tax year starts on 6th April.
     */
    /*public static int getUkTaxMonth(LocalDate date) {

        // Get the year of the given date
        int year = date.getYear();

        // Start of tax year is 6th April of that year
        LocalDate taxYearStart = LocalDate.of(year, Month.APRIL, 6);

        // If the date is before 6th April, it's part of the previous tax year
        if (date.isBefore(taxYearStart)) {
            taxYearStart = LocalDate.of(year - 1, Month.APRIL, 6);
        }

        // Calculate how many days have passed since the start of the tax year
        long daysPassed = ChronoUnit.DAYS.between(taxYearStart, date);

        // Convert days into a month number (approximate)
        int taxMonth = (int) (daysPassed / 30.4375) + 1;

        // Return tax month (max 12)
        return Math.min(taxMonth, 12);
    }*/

    public static String getUkTaxMonth(LocalDate date) {
        int year = date.getYear();
        LocalDate taxYearStart = LocalDate.of(year, Month.APRIL, 6);

        if (date.isBefore(taxYearStart)) {
            taxYearStart = taxYearStart.minusYears(1);
        }

        for (int month = 1; month <= 12; month++) {
            LocalDate monthStart = taxYearStart.plusMonths(month - 1);
            LocalDate monthEnd = monthStart.plusMonths(1).minusDays(1);

            if (!date.isBefore(monthStart) && !date.isAfter(monthEnd)) {
                return String.format("%02d", month);
            }

        }

        throw new DataValidationException("Invalid date for tax month calculation "+ date);
    }

    public static String getUkTaxWeek(LocalDate date) {
        if (date == null) {
            throw new DataValidationException("Date must not be null");
        }

        // Determine tax year start (6 April of current or previous year)
        int year = date.getYear();
        LocalDate taxYearStart = LocalDate.of(year, Month.APRIL, 6);

        if (date.isBefore(taxYearStart)) {
            taxYearStart = taxYearStart.minusYears(1);
        }

        // Calculate days elapsed since tax year start
        long daysBetween = ChronoUnit.DAYS.between(taxYearStart, date);

        // Validate date is within tax year
        if (daysBetween < 0 || daysBetween > 366) {
            throw new DataValidationException("Date is not within valid tax year range");
        }

        // Calculate week (1-53) with safety cap
        int week= Math.min((int) (daysBetween / 7) + 1, 53);
        return String.format("%02d", week);
    }


    /**
     * Calculates how many days have passed in the current UK tax year
     * from 6 April up to the given date.
     */
    public static long getDaysCompletedInTaxYear(LocalDate date) {
        int year = date.getYear();
        LocalDate taxYearStart = LocalDate.of(year, Month.APRIL, 6);

        // If date is before 6 April, it's part of the previous tax year
        if (date.isBefore(taxYearStart)) {
            taxYearStart = LocalDate.of(year - 1, Month.APRIL, 6);
        }

        return ChronoUnit.DAYS.between(taxYearStart, date);
    }

    /**
     * Returns the age in years, based on date of birth.
     * @param dateOfBirth the employee's date of birth (YYYY-MM-DD)
     * @return age in years
     */
    public static int calculateAge(LocalDate dateOfBirth) {
        LocalDate today = LocalDate.now();
        return Period.between(dateOfBirth, today).getYears();
    }
}
