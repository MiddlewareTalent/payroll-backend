package com.payroll.uk.payroll_processing.controller;

import com.payroll.uk.payroll_processing.utils.AgeUtils;
import com.payroll.uk.payroll_processing.utils.TaxPeriodUtils;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;

@RestController
@RequestMapping("/tax")
public class TaxMonthController {

    @GetMapping("/month/{date}")
    public int getTaxMonth(
            @Parameter(description = "Date in yyyy-MM-dd format", example = "2025-06-28")
                               @PathVariable("date") String dateStr) {
        LocalDate date = LocalDate.parse(dateStr);
        return TaxPeriodUtils.getUkTaxMonth(date);
    }
    @GetMapping("/week/{date}")
    public int getTaxWeek(
            @Parameter(description = "Date in yyyy-MM-dd format", example = "2025-06-28")
            @PathVariable("date") String dateStr) {
        LocalDate date = LocalDate.parse(dateStr);
        return TaxPeriodUtils.getUkTaxWeek(date);
    }

    /**
     * Get number of days completed in current UK tax year for the given date.
     * Example: GET /api/tax/days-completed?date=2025-06-28
     */
    @GetMapping("/days-completed/{date}")
    public long getDaysCompleted(
            @Parameter(description = "Date in yyyy-MM-dd format", example = "2025-06-28")
            @PathVariable("date") String dateStr) {
        LocalDate date = LocalDate.parse(dateStr);
        return TaxPeriodUtils.getDaysCompletedInTaxYear(date);
    }

    @GetMapping("/employee/age/{dob}")
    public int getEmployeeAge(
            @Parameter(description = "Date in yyyy-MM-dd format", example = "2001-10-10")
            @PathVariable("dob") String dobStr) {
        LocalDate dob = LocalDate.parse(dobStr); // format: YYYY-MM-DD
        return AgeUtils.calculateAge(dob);
    }


}
