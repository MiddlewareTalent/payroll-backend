package com.payroll.uk.payroll_processing.dto.employerdto;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor

public class TermsDTO {

    private int hoursWorkedPerWeek;
    private int weeksNoticeRequired;

    private int daysSicknessOnFullPay;

    private int maleRetirementAge;

    private int femaleRetirementAge;

    private int daysHolidayPerYear;
    private int maxDaysToCarryOver;
}
