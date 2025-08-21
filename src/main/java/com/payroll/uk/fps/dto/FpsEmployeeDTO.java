package com.payroll.uk.fps.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FpsEmployeeDTO {

    // Personal Details
    private String employeeId;
    private String title;
    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private String gender; // "M" or "F"
    private String address;
    private String postCode;
    private String nino; // National Insurance Number

    //Employment & Tax
    private String taxCode;           // e.g., "1257L"
    private String payId;            // Unique Pay ID if applicable
    private String payFrequency;     // e.g., "M1", "W1"
    private LocalDate paymentDate;   // Actual payment date
    private String niCategory;       // e.g., "A", "B"
//    private boolean isIrregularPaymentPattern;
//    private boolean isOnHoldOrLeave;
    private boolean isNewStarter;
    private boolean isLeaver;
    private LocalDate leavingDate;

    // Starter declaration
    private boolean onStarterDeclaration;

    // Emergency Tax Code flag (for M1/W1 codes)
    private boolean hasEmergencyCode;

    // Current Period Financials
    private BigDecimal grossPay;
    private BigDecimal taxDeducted;
    private BigDecimal employeeNI;
    private BigDecimal employerNI;

    // Year-To-Date Totals
    private BigDecimal ytdGrossPay;
    private BigDecimal ytdTax;
    private BigDecimal ytdEmployeeNI;
    private BigDecimal ytdEmployerNI;

    // Student Loan Info
    private boolean hasStudentLoan;
    private String studentLoanPlanType; // e.g. "Plan1", "Plan2", "Plan4"
    private BigDecimal studentLoanDeductionAmount;

    // Postgraduate Loan Info
    private boolean hasPostgraduateLoan;
    private String postgraduateLoanPlanType; // e.g. "PGL1"
    private BigDecimal postgraduateDeductionAmount;

    // Pension
    private boolean hasPensionEligible;
//    private boolean isReceivingOccupationalPension;
    private BigDecimal employeePensionContribution;
    private BigDecimal employerPensionContribution;
}
