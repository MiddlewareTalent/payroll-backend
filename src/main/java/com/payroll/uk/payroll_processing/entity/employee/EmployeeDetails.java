package com.payroll.uk.payroll_processing.entity.employee;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.payroll.uk.payroll_processing.entity.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private String firstName;
    private String lastName;
    @Email(message = "Email should be valid")
    @NotBlank(message = "Email is required")
    @Column(unique = true)
    private String email;
    @NotNull(message = "Date of birth is required")
    @Past(message = "Date of birth must be in the past")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;
    @Column(unique = true)
    private String employeeId;

    private String address;
    private String postCode;


    @Enumerated(EnumType.STRING)
    private EmploymentType employmentType;

    private Boolean isDirector=false;
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "ENUM('MALE','FEMALE')")
    private Gender gender;
    @Enumerated(EnumType.STRING)
    private Department employeeDepartment;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate employmentStartedDate;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate employmentEndDate;


    private BigDecimal grossIncome;
    private String taxCode;
    private Boolean isEmergencyCode=false;
    private Boolean isPostgraduateLoan=false;
    @Enumerated(EnumType.STRING)
    private StudentLoan studentLoan=StudentLoan.NONE;
    @Enumerated(EnumType.STRING)
    private PayPeriod payPeriod=PayPeriod.MONTHLY;

//    @OneToOne
//    @JoinColumn(name = "bank_details_id")
//    private BankDetails bankDetails;
        @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
        @JoinColumn(name = "bank_details_id", referencedColumnName = "id")
        private BankDetails bankDetails;


    @Pattern(regexp = "^[A-Z]{2}[0-9]{6}[A-Z]$",
            message = "National Insurance number must be in the format AB123456C")
    @Column(unique = true)
    private String nationalInsuranceNumber;
    private String NICategoryLetter;

    @Embedded
    private OtherEmployeeDetails otherEmployeeDetails;


//    @OneToOne
//    @JoinColumn(name = "other_employee_details_id")
//    private OtherEmployeeDetails otherEmployeeDetails;




    public enum Gender{
        MALE,
        FEMALE
    }

    @AssertTrue(message = "You must provide either a tax code OR select W1/M1, but not both")
    public Boolean isValidTaxSelection() {
        boolean hasTaxCode = taxCode != null && !taxCode.trim().isEmpty();
        return hasTaxCode != isEmergencyCode; // XOR: exactly one must be true
    }
    public void setBankDetails(BankDetails bankDetails) {
        this.bankDetails = bankDetails;
        if (bankDetails != null) {
            bankDetails.setEmployeeDetails(this);
        }
    }






}

//        private BigDecimal TotalPersonalAllowance=new BigDecimal("12570");
//        private BigDecimal usedPersonalAllowance=BigDecimal.ZERO;
//        private BigDecimal remainingPersonalAllowance=BigDecimal.ZERO;
//private BigDecimal totalTaxDeducted;
