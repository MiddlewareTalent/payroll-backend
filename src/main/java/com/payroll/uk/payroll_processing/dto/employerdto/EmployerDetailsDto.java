package com.payroll.uk.payroll_processing.dto.employerdto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.payroll.uk.payroll_processing.entity.employer.TaxOffice;
import com.payroll.uk.payroll_processing.entity.employer.Terms;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployerDetailsDto {

//    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
//    private Long id;
    @NotBlank(message = "Employer name cannot be blank")
    @Schema(description = "Employer name", example = "ABC Ltd")
    private String employerName;
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



    private TaxOffice taxOffice;

    @Schema(nullable = true)
    private Terms terms;
}
