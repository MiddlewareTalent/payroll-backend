package com.payroll.uk.payroll_processing.entity.employer;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.payroll.uk.payroll_processing.entity.BankDetails;
import com.payroll.uk.payroll_processing.entity.PayPeriod;
import com.payroll.uk.payroll_processing.entity.TaxThreshold;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "employer_details")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployerDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    private String employerId;

    // Company Information
    @NotBlank(message = "Employer name cannot be blank")
//    @Size(min = 2, max = 100, message = "Employer name must be between 2-100 characters")
//    @Pattern(regexp = "^[a-zA-Z0-9 \\-\\'&,.]*$", message = "Employer name contains invalid characters")
    private String employerName;

//    @NotBlank(message = "Address cannot be blank")
//    @Size(max = 200)
    private String employerAddress;

//    @NotBlank(message = "Postcode cannot be blank")
//    @Pattern(regexp = "^([A-Za-z][A-Ha-hJ-Yj-y]?[0-9][A-Za-z0-9]? ?[0-9][A-Za-z]{2}|[Gg][Ii][Rr] ?0[Aa]{2})$",
//            message = "Invalid UK postcode format")
    private String employerPostCode;


    @NotBlank(message = "Telephone number cannot be blank")
//    @Pattern(regexp = "^(\\+44|0)[1-9]\\d{8,9}$",
//            message = "Invalid UK telephone number format")
    private String employerTelephone;

    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Email must be valid")
//    @Column(unique = true)
    private String employerEmail;

    // Contact Person Information
//    @NotBlank(message = "Contact forename cannot be blank")
//    @Pattern(regexp = "^[A-Z][a-zA-Z\\-']{1,29}$",
//            message = "Contact forename must start with a capital letter and contain only letters, hyphen or apostrophe")
//    @Column(nullable = true)
    private String contactForename;

//    @NotBlank(message = "Contact surname cannot be blank")
//    @Pattern(regexp = "^[A-Z][a-zA-Z\\-']{1,29}$",
//            message = "Contact surname must start with a capital letter and contain only letters, hyphen or apostrophe")

    private String contactSurname;

    // PAYE Scheme Information

//    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
//            message = "PDF password must be at least 8 characters with uppercase, lowercase, number and special character")
    private String pdfPassword;


    private String userReference;

    @NotNull(message = "PAYE scheme start date cannot be null")
    @PastOrPresent(message = "PAYE scheme start date must be in the past or present")
    private LocalDate datePAYESchemeStarted;

    @FutureOrPresent(message = "PAYE scheme ceased date must be in the future or present")
    private LocalDate datePAYESchemeCeased;

    // System Preferences
    @Schema(defaultValue = "false")
    private Boolean rtiBatchProcessing = false;
    @Schema(defaultValue = "false")
    private Boolean previousWorksNumberUnknown = false;
    @Schema(defaultValue = "false")
    private Boolean ensureUniqueWorksNumber= false;
    @Schema(defaultValue = "false")
    private Boolean warnBelowNationalMinimumWage= false;
    @Schema(defaultValue = "false")
    private Boolean showAgeOnHourlyTab= false;

//    private BigDecimal employmentAllowanceThreshold= new BigDecimal("10500");
//    private BigDecimal usedEmploymentAllowance = BigDecimal.ZERO;
//    private BigDecimal remainingEmploymentAllowance = BigDecimal.ZERO;

    @Column(name = "company_name", nullable = false)
    private String companyName;

    @Schema(description = "Tax year in format yyyy-yyyy, e.g., 2025-2026", example = "2025-2026")
    @Pattern(regexp = "^\\d{4}-\\d{4}$", message = "Tax year must be in the format YYYY-YYYY, e.g., 2025-2026")
    private String taxYear;

    @Enumerated(EnumType.STRING)
    private PayPeriod payPeriod=PayPeriod.MONTHLY;

    @Enumerated(EnumType.STRING)
//    @Column(columnDefinition = "ENUM('ENGLAND','NORTHERN_IRELAND','SCOTLAND','WALES')", nullable = false)
    private TaxThreshold.TaxRegion region;


    // Company Logo
    @Lob
    private String companyLogo;

    @Embedded
    private TaxOffice taxOffice;

    @Embedded
    private Terms terms;

//    @OneToOne
//    @JoinColumn(name = "bank_details_id")
//    private BankDetails bankDetails;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "bank_details_id", referencedColumnName = "id")
    @ToString.Exclude
    private BankDetails bankDetails;

    public void setBankDetails(BankDetails bankDetails) {
        this.bankDetails = bankDetails;
        if (bankDetails != null) {
            bankDetails.setEmployerDetails(this);
        }
    }

    /*// Helper method for bidirectional relationship
    public void setBankDetails(BankDetails bankDetails) {
        if (bankDetails == null) {
            if (this.bankDetails != null) {
                this.bankDetails.setEmployer(null);
            }
        } else {
            bankDetails.setEmployer(this);
        }
        this.bankDetails = bankDetails;
    }*/

//    @Embedded
//    private BankDetails bankDetails;




}



//@DecimalMin(value = "0.00", message = "Apprenticeship levy must be positive")
//@Digits(integer = 10, fraction = 2, message = "Apprenticeship levy must have max 10 digits and 2 decimals")
//private BigDecimal apprenticeshipLevyAllowance = new BigDecimal("15000.00");


