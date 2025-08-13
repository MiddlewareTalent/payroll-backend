package com.payroll.uk.payroll_processing.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.payroll.uk.payroll_processing.entity.employee.PostGraduateLoan;
import com.payroll.uk.payroll_processing.entity.employee.StudentLoan;
import jakarta.persistence.*;
import jakarta.validation.constraints.PositiveOrZero;
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

    @Column(name = "firstName")
    private String firstName;
    @Column(name = "lastName")
    private String lastName;
    @Column(name = "address")
    private String address;
    @Column(name = "postCode")
    private String postCode;
    @Column(name = "employeeId")
    private String employeeId;
    @Column(name = "workingCompanyName")
    private String workingCompanyName;
    @Enumerated(EnumType.STRING)
    @Column(name="niLetter")
    private NICategoryLetters niLetter;
    private String taxCode;
    @Column(name = "taxYear")
    private String taxYear;
    @Enumerated(EnumType.STRING)
    @Column(name = "region")
    private TaxThreshold.TaxRegion region;
    @Column(name = "NI_Number")
    private String NI_Number;
    @Column(name = "payPeriod")
    private String payPeriod;
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(name = "payDate")
    private LocalDate payDate;
    @Column(name = "periodEnd")
    private String periodEnd;
    @Column(name = "grossPayTotal")
    private BigDecimal grossPayTotal;
    @Column(name = "taxableIncome")
    private BigDecimal taxableIncome;
    @Column(name = "personalAllowance")
    private BigDecimal personalAllowance;
    @Column(name = "incomeTaxTotal")
    private BigDecimal incomeTaxTotal;
    @Column(name = "employeeNationalInsurance")
    private BigDecimal employeeNationalInsurance;
    @Column(name = "employersNationalInsurance")
    private BigDecimal employersNationalInsurance;
    @Column(name = "hasStudentLoanStart")
    private Boolean hasStudentLoanStart;
    @Column(name = "hasPostGraduateLoanStart")
    private  Boolean hasPostGraduateLoanStart;
    @Enumerated(EnumType.STRING)
    @Column(name = "studentLoanPlanType")
    private StudentLoan.StudentLoanPlan studentLoanPlanType;
    @Enumerated(EnumType.STRING)
    @Column(name = "postgraduateLoanPlanType")
    private PostGraduateLoan.PostgraduateLoanPlanType postgraduateLoanPlanType;
    @Column(name = "studentLoanDeductionAmount")
    private BigDecimal studentLoanDeductionAmount;
    @Column(name = "postgraduateDeductionAmount")
    private BigDecimal postgraduateDeductionAmount;
    @Column(name = "deductionsTotal")
    private BigDecimal deductionsTotal;
    @Column(name = "takeHomePayTotal")
    private BigDecimal takeHomePayTotal;
    @Column(name = "paySlipReference", unique = true, nullable = false)
    private String paySlipReference;

    @Column(name = "hasPensionEligible", nullable = false)
    private boolean hasPensionEligible= false;
    @Column(name = "employeePensionContribution")
    @PositiveOrZero(message = "Employee pension contribution must be zero or positive")
    private BigDecimal employeePensionContribution;
    @Column(name = "employerPensionContribution")
    @PositiveOrZero(message = "Employer pension contribution must be zero or positive")
    private  BigDecimal employerPensionContribution;

    @Column(name = "earnings_at_lel")
    @PositiveOrZero(message = "Earnings at LEL must be zero or positive")
    private BigDecimal earningsAtLEL;
    @Column(name = "earnings_lel_to_pt")
    @PositiveOrZero(message = "Earnings LEL to PT must be zero or positive")
    private BigDecimal earningsLelToPt;
    @Column(name = "earnings_pt_to_uel")
    @PositiveOrZero(message = "Earnings PT to UEL must be zero or positive")
    private BigDecimal earningsPtToUel;




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
