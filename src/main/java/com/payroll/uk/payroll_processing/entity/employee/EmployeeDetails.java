package com.payroll.uk.payroll_processing.entity.employee;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.payroll.uk.payroll_processing.entity.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "employee_details")
public class EmployeeDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;
    @Column(name = "firstname")
    private String firstName;
    @Column(name = "lastname")
    private String lastName;
    @Email(message = "Email should be valid")
    @NotBlank(message = "Email is required")
    @Column(unique = true,name = "email")
    private String email;

//    @NotNull(message = "Date of birth is required")
//    @Past(message = "Date of birth must be in the past")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;
    @Column(unique = true,name = "employee_id")
    private String employeeId;

    @Column(name = "address")
    private String address;
    @Column(name = "postCode")
    private String postCode;

    @Column(name = "working_company_name")
    private String workingCompanyName;
    @Enumerated(EnumType.STRING)
    @Column(name = "employment_type")
    private EmploymentType employmentType;
    @Column(name = "tax_year", nullable = false, length = 9)
    private String taxYear;

//    @Column(name = "is_director",nullable = false)
//    private boolean isDirector=false;
    @Enumerated(EnumType.STRING)
    @Column(name ="gender" , columnDefinition = "ENUM('MALE','FEMALE','OTHER')")
    private Gender gender;
    @Enumerated(EnumType.STRING)
    @Column(name = "employee_department")
    private Department employeeDepartment;
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(name = "employment_started_date")
    private LocalDate employmentStartedDate;
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(name = "employment_end_date")
    private LocalDate employmentEndDate;
    @PositiveOrZero(message = "previously used personal allowance must be zero or positive")
    @Column(name = "previously_used_personal_allowance")
    private BigDecimal previouslyUsedPersonalAllowance= BigDecimal.ZERO;
    @PositiveOrZero(message = "total personal allowance must be zero or positive")
    @Column(name ="total_personal_allowance" )
    private BigDecimal totalPersonalAllowance=new BigDecimal("12570.00"); // Default value for 2025/26 tax year

//    @Column(name = "employer_id")
//    private String employerId;

    @Column(name = "annual_income_of_employee")
    @PositiveOrZero(message = "Annual income must be zero or positive")
    private BigDecimal annualIncomeOfEmployee;
    @Column(name="pay_period_of_income_of_employee")
    @PositiveOrZero(message = "Pay period of income must be zero or positive")
    private BigDecimal payPeriodOfIncomeOfEmployee;
    @Column(name = "tax_code",nullable = false)
    private String taxCode;
    @Column(name = "is_emergency_code",nullable = false)
    private boolean hasEmergencyCode=false;
//    @Column(name = "totalAllowanceUsedDuringEmergency")
//    private BigDecimal totalAllowanceUsedDuringEmergency=BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @Column(name = "pay_period",nullable = false)
    private PayPeriod payPeriod;
    @Enumerated(EnumType.STRING)
    @Column(name = "region",nullable = false)
    private TaxThreshold.TaxRegion region;

//    @OneToOne
//    @JoinColumn(name = "bank_details_id")
//    private BankDetails bankDetails;
        @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
        @JoinColumn(name = "bank_details_id", referencedColumnName = "id")
        @ToString.Exclude
        private BankDetails bankDetails;


    @Pattern(regexp = "^[A-Z]{2}[0-9]{6}[A-Z]$",
            message = "National Insurance number must be in the format AB123456C")
    @Column(name = "national_insurance_number", unique = true,nullable = false)
    private String nationalInsuranceNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "ni_letter",nullable = false)
    private NICategoryLetters niLetter;

    @Column(name = "payroll_id", unique = true, nullable = false)
    private String payrollId;


    @Embedded
    @Column(name = "otherEmployeeDetails")
    private OtherEmployeeDetails otherEmployeeDetails;

    @Embedded
    @Column(name = "studentLoan")
    private StudentLoan studentLoan;

    @Embedded
    @Column(name = "previousEmploymentData")
    private PreviousEmploymentData previousEmploymentData;

    @Embedded
    @Column(name = "postGraduateLoan")
    private PostGraduateLoan postGraduateLoan;
    @Column(name = "p45_document")
    private String p45Document;
    @Column(name = "has_p45document_submitted",nullable = false)
    private boolean hasP45DocumentSubmitted=false;
    @Column(name = "starter_checklist_document")
    private String starterChecklistDocument;
    @Column(name = "has_starter_checklist_document_submitted",nullable = false)
    private boolean hasStarterChecklistDocumentSubmitted=false;

    @Column(name = "has_pension_eligible", nullable = false)
    private boolean hasPensionEligible = false;

    @Column(name = "k_code_taxable_adjustment_annual")
    @PositiveOrZero(message = "K code taxable adjustment must be zero or positive")
    private BigDecimal kCodeTaxableAdjustmentAnnual;

    @Column(name = "has_married_employee", nullable = false)
    private boolean hasMarriedEmployee;






    public enum Gender{
        MALE,
        FEMALE
    }

    @AssertTrue(message = "You must provide either a tax code OR select W1/M1, but not both")
    public Boolean isValidTaxSelection() {
        boolean hasTaxCode = taxCode != null && !taxCode.trim().isEmpty();
        return hasTaxCode != isHasEmergencyCode(); // XOR: exactly one must be true
    }
    public void setBankDetails(BankDetails bankDetails) {
        this.bankDetails = bankDetails;
        if (bankDetails != null) {
            bankDetails.setEmployeeDetails(this);
        }
    }

    public boolean isNonCumulativeTaxCode(String taxCode) {
        if (taxCode == null) return false;
        String upper = taxCode.trim().toUpperCase();
        return upper.endsWith("W1") || upper.endsWith("M1") || upper.endsWith("X");
    }


    private boolean checkIfEmergencyTaxCode(String code) {
        if (code == null) return false;

        // Remove spaces and convert to uppercase
        String cleaned = code.replaceAll("\\s+", "").toUpperCase();

        // Match: e.g. 1257LM1, 1257LW1, 1257LX
        return cleaned.matches("1257L M1")||cleaned.matches("1257L W1")||cleaned.matches("1257L X");
    }

//    public boolean isHasPensionEligible() {
//        return hasPensionEligible;
//    }
//
//    public void setHasPensionEligible(boolean hasPensionEligible) {
//        this.hasPensionEligible = hasPensionEligible;
//    }
}


