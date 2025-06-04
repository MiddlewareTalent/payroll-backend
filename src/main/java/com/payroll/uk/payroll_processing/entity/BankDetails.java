package com.payroll.uk.payroll_processing.entity;


import com.payroll.uk.payroll_processing.entity.employee.EmployeeDetails;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class BankDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
//    @NotBlank(message = "Account name is required")
    @Size(max = 100, message = "Account name must not exceed 100 characters")
    private String  accountName;

//    @NotBlank(message = "Account number is required")
//    @Pattern(regexp = "^\\d{8}$", message = "Account number must be exactly 8 digits")
    private String accountNumber;

    @Size(max = 50, message = "Payment reference must not exceed 50 characters")
    private String paymentReference;

//    @NotBlank(message = "Bank name is required")
    @Size(max = 100, message = "Bank name must not exceed 100 characters")
    private String bankName;

//    @NotBlank(message = "Sort code is required")
//    @Pattern(regexp = "^\\d{2}-\\d{2}-\\d{2}$", message = "Sort code must be in format 12-34-56")
    private String sortCode;

    @Size(max = 200, message = "Address must not exceed 200 characters")
    private String bankAddress;

//    @Pattern(
//            regexp = "^([A-Z]{1,2}[0-9][A-Z0-9]? ?[0-9][A-Z]{2}|GIR ?0A{2})$",
//            message = "Invalid UK postcode format"
//    )
    private String bankPostCode;
//    @Pattern(
//            regexp = "^(\\+44|0)[1-9]\\d{1,4}\\d{6,7}$",
//            message = "Invalid UK telephone number format")
    private String telephone;
    private int paymentLeadDays=0;
    private Boolean isRTIReturnsIncluded=false;
    @OneToOne(mappedBy = "bankDetails")
    private EmployeeDetails employeeDetails;

}
