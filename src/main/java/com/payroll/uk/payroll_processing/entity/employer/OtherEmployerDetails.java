package com.payroll.uk.payroll_processing.entity.employer;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.PrePersist;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class OtherEmployerDetails {

    @Column(name = "total_paid_gross_amount_ytd",nullable = false)
    @PositiveOrZero(message = "Total paid amount YTD must be positive or zero")
    private BigDecimal totalPaidGrossAmountYTD= BigDecimal.ZERO;
    @Column(name = "current_pay_period_paid_gross_pay", nullable = false)
    @PositiveOrZero(message = "Current pay period paid gross pay must be positive or zero")
    private BigDecimal currentPayPeriodPaidGrossPay = BigDecimal.ZERO;


    @Column(name = "current_pay_period_paye", nullable = false)
    @PositiveOrZero(message = "Current pay period PAYE must be positive or zero")
    private BigDecimal currentPayPeriodPAYE = BigDecimal.ZERO;

    @Column(name = "totalpayeytd",nullable = false)
    @PositiveOrZero(message = "Total PAYE YTD must be positive or zero")
    private BigDecimal totalPAYEYTD;

    @Column(name = "current_pay_period_employeesni", nullable = false)
    @PositiveOrZero(message = "Current pay period Employees NI must be positive or zero")
    private BigDecimal currentPayPeriodEmployeesNI = BigDecimal.ZERO;

    @Column(name = "total_employeesniytd",nullable = false)
    @PositiveOrZero(message = "Total Employees NI YTD must be positive or zero")
    private BigDecimal totalEmployeesNIYTD;

    @Column(name = "current_pay_period_employersni", nullable = false)
    @PositiveOrZero(message = "Current pay period Employers NI must be positive or zero")
    private BigDecimal currentPayPeriodEmployersNI = BigDecimal.ZERO;

    @Column(name = "total_employersniytd",nullable = false)
    @PositiveOrZero(message = "Total Employers NI YTD must be positive or zero")
    private BigDecimal totalEmployersNIYTD;

    //Pension related fields
    @Column(name = "total_employee_pension_contribution")
    @PositiveOrZero(message = "Total employee pension contribution must be positive or zero")
    private BigDecimal totalEmployeePensionContribution = BigDecimal.ZERO;
    @Column(name = "current_pay_period_employee_pension_contribution")
    @PositiveOrZero(message = "Current pay period employee pension contribution must be positive or zero")
    private BigDecimal currentPayPeriodEmployeePensionContribution = BigDecimal.ZERO;

    @Column(name = "total_employer_pension_contribution")
    @PositiveOrZero(message = "Total employer pension contribution must be positive or zero")
    private BigDecimal totalEmployerPensionContribution = BigDecimal.ZERO;
    @Column(name = "current_pay_period_employer_pension_contribution")
    @PositiveOrZero(message = "Current pay period employer pension contribution must be positive or zero")
    private BigDecimal currentPayPeriodEmployerPensionContribution = BigDecimal.ZERO;



    @PrePersist
    public void setDefaults() {
        if (totalPaidGrossAmountYTD == null) totalPaidGrossAmountYTD = BigDecimal.ZERO;
        if (currentPayPeriodPaidGrossPay == null) currentPayPeriodPaidGrossPay = BigDecimal.ZERO;

        if (currentPayPeriodPAYE == null) currentPayPeriodPAYE = BigDecimal.ZERO;
        if (totalPAYEYTD == null) totalPAYEYTD = BigDecimal.ZERO;

        if (currentPayPeriodEmployeesNI == null) currentPayPeriodEmployeesNI = BigDecimal.ZERO;
        if (totalEmployeesNIYTD == null) totalEmployeesNIYTD = BigDecimal.ZERO;

       /* if (monthlyEmployersNI == null) monthlyEmployersNI = BigDecimal.ZERO;
        if (weeklyEmployersNI == null) weeklyEmployersNI = BigDecimal.ZERO;
        if (yearlyEmployersNI == null) yearlyEmployersNI = BigDecimal.ZERO;*/

        if (currentPayPeriodEmployersNI == null) currentPayPeriodEmployersNI = BigDecimal.ZERO;
        if (totalEmployersNIYTD == null) totalEmployersNIYTD = BigDecimal.ZERO;

        if( totalEmployerPensionContribution == null) totalEmployerPensionContribution = BigDecimal.ZERO;
        if( currentPayPeriodEmployerPensionContribution == null) currentPayPeriodEmployerPensionContribution = BigDecimal.ZERO;

        if( totalEmployeePensionContribution == null) totalEmployeePensionContribution = BigDecimal.ZERO;
        if( currentPayPeriodEmployeePensionContribution == null) currentPayPeriodEmployeePensionContribution = BigDecimal.ZERO;

    }

     /* @Column(name = "monthlypaye",nullable = false)
    @PositiveOrZero(message = "Monthly PAYE must be positive or zero")
    private BigDecimal monthlyPAYE;

    @Column(name = "weeklypaye",nullable = false)
    @PositiveOrZero(message = "Weekly PAYE must be positive or zero")
    private BigDecimal weeklyPAYE;

    @Column(name = "yearlypaye",nullable = false)
    @PositiveOrZero(message = "Yearly PAYE must be positive or zero")
    private BigDecimal yearlyPAYE;*/

}
