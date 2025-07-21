package com.payroll.uk.payroll_processing.entity.employer;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class Terms {
    @Schema(description = "Hours worked per week", example = "40", defaultValue = "40")
    @Column(name = "hours_worked_per_week")
    private int hoursWorkedPerWeek;

//    @Schema(description = "Is paid overtime", example = "false", defaultValue = "false")
//    @Column(name = "is_paid_overtime")
//    private Boolean isPaidOvertime;

    @Schema(description = "Weeks notice required", example = "4", defaultValue = "4")
    @Column(name = "weeks_notice_required")
    private int weeksNoticeRequired;

    @Schema(description = "Days sickness on full pay", example = "30", defaultValue = "30")
    @Column(name = "days_sickness_on_full_pay")
    private int daysSicknessOnFullPay;

    @Schema(description = "Male retirement age", example = "65", defaultValue = "65")
    @Column(name = "male_retirement_age")
    private int maleRetirementAge;

    @Schema(description = "Female retirement age", example = "65", defaultValue = "65")
    @Column(name = "female_retirement_age")
    private int femaleRetirementAge;

//    @Schema(description = "May join pension scheme", example = "false", defaultValue = "false")
//    @Column(name = "may_join_pension_scheme")
//    private Boolean mayJoinPensionScheme;

    @Schema(description = "Days holiday per year", example = "28", defaultValue = "28")
    @Column(name = "days_holiday_per_year")
    private int daysHolidayPerYear;

    @Schema(description = "Max days to carry over", example = "28", defaultValue = "28")
    @Column(name = "max_days_to_carry_over")
    private int maxDaysToCarryOver;

}
