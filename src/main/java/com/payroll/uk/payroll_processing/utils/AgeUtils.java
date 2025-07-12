package com.payroll.uk.payroll_processing.utils;

import java.time.LocalDate;
import java.time.Period;

public class AgeUtils {

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

