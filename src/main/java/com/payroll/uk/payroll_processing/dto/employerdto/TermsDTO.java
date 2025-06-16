package com.payroll.uk.payroll_processing.dto.employerdto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor

public class TermsDTO {
    @Schema(description = "Hours worked per week", example = "40",defaultValue = "40")
    private int hoursWorkedPerWeek;
    @Schema(description = "Is paid overtime", example = "false",defaultValue = "false")
    private Boolean isPaidOvertime;
    @Schema(description = "Weeks notice required", example = "4",defaultValue = "4")
    private int weeksNoticeRequired;
    @Schema(description = "Days sickness on full pay", example = "30",defaultValue = "30")
    private int daysSicknessOnFullPay;
    @Schema(description = "male retirement age", example = "65",defaultValue = "65")
    private int maleRetirementAge;
    @Schema(description = "female retirement age",example = "65",   defaultValue = "65")
    private int femaleRetirementAge;
    @Schema(description = "May join pension scheme", example = "false",defaultValue = "false")
    private Boolean mayJoinPensionScheme;
    @Schema(description = "Days holiday per year", example = "28",defaultValue = "28")
    private int daysHolidayPerYear;
    @Schema(description = "Max days to carry over", example = "28",defaultValue = "28")
    private int maxDaysToCarryOver;
}
