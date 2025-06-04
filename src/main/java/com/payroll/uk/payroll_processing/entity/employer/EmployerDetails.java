package com.payroll.uk.payroll_processing.entity.employer;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.payroll.uk.payroll_processing.entity.BankDetails;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    private String employerReference;

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

    private BigDecimal employmentAllowanceThreshold= new BigDecimal("10500");
    private BigDecimal usedEmploymentAllowance = BigDecimal.ZERO;
    private BigDecimal remainingEmploymentAllowance = BigDecimal.ZERO;


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

//    @OneToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name = "bank_details_id",referencedColumnName = "id")
//    private BankDetails bankDetails;

//    @Embedded
//    private BankDetails bankDetails;




}



//@DecimalMin(value = "0.00", message = "Apprenticeship levy must be positive")
//@Digits(integer = 10, fraction = 2, message = "Apprenticeship levy must have max 10 digits and 2 decimals")
//private BigDecimal apprenticeshipLevyAllowance = new BigDecimal("15000.00");


