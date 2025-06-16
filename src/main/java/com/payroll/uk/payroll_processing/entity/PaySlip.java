package com.payroll.uk.payroll_processing.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.payroll.uk.payroll_processing.entity.employee.PostGraduateLoan;
import com.payroll.uk.payroll_processing.entity.employee.StudentLoan;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "pay_slip")
public class PaySlip {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    private String firstName;
    private String lastName;
    private String address;
    private String postCode;
    private String employeeId;
    private String workingCompanyName;
    @Enumerated(EnumType.STRING)
    private NICategoryLetters niLetter;
    private String taxCode;
    private String taxYear;
    @Enumerated(EnumType.STRING)
    private TaxThreshold.TaxRegion region;
    private String NI_Number;
    private String payPeriod;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate payDate;

    private String periodEnd;
    private BigDecimal grossPayTotal;
    private BigDecimal taxableIncome;
    private BigDecimal personalAllowance;
    private BigDecimal incomeTaxTotal;
    private BigDecimal employeeNationalInsurance;
    private BigDecimal employersNationalInsurance;
    private Boolean hasStudentLoanStart;
    private  Boolean hasPostGraduateLoanStart;
    @Enumerated(EnumType.STRING)
    private StudentLoan.StudentLoanPlan studentLoanPlanType;
    @Enumerated(EnumType.STRING)
    private PostGraduateLoan.PostgraduateLoanPlanType postgraduateLoanPlanType;
    private BigDecimal studentLoanDeductionAmount;
    private BigDecimal postgraduateDeductionAmount;
    private BigDecimal deductionsTotal;
    private BigDecimal takeHomePayTotal;
    @Column(name = "pay_slip_reference", unique = true, nullable = false)
    private String paySlipReference;

    public String getPreviousMonthEndDate() {
        // Get current system date as Pay Date
        LocalDate payDate = LocalDate.now();

        // Go to the first day of this month, then subtract one day to get end of previous month
        LocalDate previousMonthEnd = payDate.withDayOfMonth(1).minusDays(1);

        // Format as dd/MM/yyyy
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return previousMonthEnd.format(formatter);
    }


   /* public LocalDate getPreviousMonthEndDate() {
        // Get current system date as Pay Date
        LocalDate payDate = LocalDate.now();

        // Get end of previous month
        return payDate.withDayOfMonth(1).minusDays(1);
    }*/


    /*public LocalDate getPeriodEndFromPayDate(LocalDate payDate) {
        if (payDate == null) {
            throw new IllegalArgumentException("Pay date cannot be null");
        }
        YearMonth yearMonth = YearMonth.from(payDate);
        return yearMonth.atEndOfMonth(); // returns 30 June 2025 if payDate is 15 June 2025
    }*/


}
