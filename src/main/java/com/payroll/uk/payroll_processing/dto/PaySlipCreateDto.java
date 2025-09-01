package com.payroll.uk.payroll_processing.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.payroll.uk.payroll_processing.entity.NICategoryLetters;
import com.payroll.uk.payroll_processing.entity.TaxThreshold;
import com.payroll.uk.payroll_processing.entity.employee.PostGraduateLoan;
import com.payroll.uk.payroll_processing.entity.employee.StudentLoan;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaySlipCreateDto {
//    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
//    private Long id;
    private String firstName;
    private String lastName;
//    private String address;
//    private String postCode;
    private String employeeId;
    private String workingCompanyName;
    @Enumerated(EnumType.STRING)
    private NICategoryLetters niCategoryLetter;
    @Enumerated(EnumType.STRING)
    private TaxThreshold.TaxRegion region;
    private String taxYear;
    private String taxCode;
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
    private String paySlipReference;

    private boolean hasPensionEligible= false;
    private BigDecimal employeePensionContribution;
    private  BigDecimal employerPensionContribution;
    private BigDecimal earningsAtLEL;
    private BigDecimal earningsLelToPt;
    private BigDecimal earningsPtToUel;

    /*public LocalDate getPreviousMonthEndDate() {
        // Get current system date as Pay Date
        LocalDate payDate = LocalDate.now();

        // Get end of previous month
        return payDate.withDayOfMonth(1).minusDays(1);
    }*/
}


