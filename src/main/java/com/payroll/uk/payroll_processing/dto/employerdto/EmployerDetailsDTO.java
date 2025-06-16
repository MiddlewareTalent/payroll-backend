package com.payroll.uk.payroll_processing.dto.employerdto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.payroll.uk.payroll_processing.dto.BankDetailsDTO;
import com.payroll.uk.payroll_processing.entity.PayPeriod;
import com.payroll.uk.payroll_processing.entity.TaxThreshold;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployerDetailsDTO {

//    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;
    @NotBlank(message = "Employer name cannot be blank")
    @Schema(description = "Employer name", example = "ABC Ltd")
    private String employerName;
    @Schema(description = "Employer ID", example = "EMP12345")
    @Column(unique = true)
    private String employerId;
    @Schema(nullable = true)
    private String employerAddress;
    @Schema(nullable = true)
    private String employerPostCode;
    @NotBlank(message = "Telephone number cannot be blank")
    @Schema(description = "Telephone number", example = "+441234567890")
    private String employerTelephone;

    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Email must be valid")
    @Schema(description = "Employer email", example = "string@gmail.com")
//    @Column(unique = true)
    private String employerEmail;
    @Schema(nullable = true)
    private String contactForename;
    @Schema(nullable = true)
    private String contactSurname;

    // PAYE Scheme Information
    private String pdfPassword;

    @Schema(nullable = true)
    private String userReference;
    @Schema(nullable = true)
    private LocalDate datePAYESchemeStarted;
    @Schema(nullable = true)
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
    // Company Logo
    @Lob
    private String companyLogo;

    @Schema(description = "Company name", example = "ABC Ltd")
    private String companyName;

    @Schema(description = "Tax year in format yyyy-yyyy, e.g., 2025-2026", example = "2025-2026")
    @Pattern(regexp = "^\\d{4}-\\d{4}$", message = "Tax year must be in the format YYYY-YYYY, e.g., 2025-2026")
    private String taxYear;

    @Enumerated(EnumType.STRING)
    private PayPeriod payPeriod=PayPeriod.MONTHLY;

    @Enumerated(EnumType.STRING)
//    @Column(columnDefinition = "ENUM('ENGLAND','NORTHERN_IRELAND','SCOTLAND','WALES')", nullable = false)
    private TaxThreshold.TaxRegion region;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate payDate;

//    private String companyRegistrationNumber;



    private TaxOfficeDTO taxOfficeDto;
    private BankDetailsDTO bankDetailsDTO;

    @Schema(nullable = true)
    private TermsDTO termsDto;

    private OtherEmployerDetailsDTO otherEmployerDetailsDto;
}
