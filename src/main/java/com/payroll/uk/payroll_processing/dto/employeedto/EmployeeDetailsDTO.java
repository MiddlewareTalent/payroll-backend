package com.payroll.uk.payroll_processing.dto.employeedto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.payroll.uk.payroll_processing.dto.BankDetailsDTO;
import com.payroll.uk.payroll_processing.entity.*;
import com.payroll.uk.payroll_processing.entity.employee.EmployeeDetails;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeDetailsDTO {

//    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
//    @Schema(defaultValue = "0")
//    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    @Past(message = "Date of birth must be in the past")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "Date of birth in yyyy-MM-dd format", example = "1990-01-01")
    private LocalDate dateOfBirth;
    @Column(unique = true)
    private String employeeId;

    private String address;
    private String postCode;
    @Enumerated(EnumType.STRING)
    @Schema(description = "Employment type of the employee", example = "FULL_TIME")
    private EmploymentType employmentType;

    @Schema(defaultValue = "false")
    private Boolean isDirector=false;



//    @Schema(description = "Gender of the employee", example = "MALE")
    @Enumerated(EnumType.STRING)
    private EmployeeDetails.Gender gender;
    @Enumerated(EnumType.STRING)
    @Schema(description = "Department of the employee", example = "DEVELOPMENT")
    private Department employeeDepartment;

    @Schema(description = "Employment start date in yyyy-MM-dd format", example = "2025-06-30")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate employmentStartedDate;
    @Schema(description = "Employment end date in yyyy-MM-dd format", example = "2025-06-30")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate employmentEndDate;



    private BigDecimal grossIncome;
    @Schema(defaultValue = "1257L")
    private String taxCode;
    @Schema(defaultValue = "false")
    private Boolean isEmergencyCode=false;
    @Schema(defaultValue = "false")
    private Boolean isPostgraduateLoan=false;
    @Enumerated(EnumType.STRING)
    @Schema(description = "Student loan type", example = "NONE")
    private StudentLoan studentLoan=StudentLoan.NONE;
    @Enumerated(EnumType.STRING)
    @Schema(description = "Pay period type", example = "MONTHLY")
    private PayPeriod payPeriod=PayPeriod.MONTHLY;
    private BankDetailsDTO bankDetailsDTO;
//    @Pattern(regexp = "^[A-Z]{2}[0-9]{6}[A-Z]$",
//            message = "National Insurance number must be in the format AB123456C")
    @Column(unique = true)
    @Schema(description = "National Insurance number in the format AB123456C", example = "AB123456C")
    private String nationalInsuranceNumber;
    private String NICategoryLetter;
    private OtherEmployeeDetailsDTO otherEmployeeDetailsDTO;

}
