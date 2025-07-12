package com.payroll.uk.payroll_processing.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.payroll.uk.payroll_processing.entity.employee.EmployeeDetails;
import com.payroll.uk.payroll_processing.entity.employer.EmployerDetails;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
//@ToString(exclude = {"employeeDetails", "employerDetails"})
public class BankDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
//    @NotBlank(message = "Account name is required")
    @Size(max = 100, message = "Account name must not exceed 100 characters")
    @Column(name = "account_name")
    private String  accountName;

//    @NotBlank(message = "Account number is required")
//    @Pattern(regexp = "^\\d{8}$", message = "Account number must be exactly 8 digits")
    @Column(name = "account_number")
    private String accountNumber;

    @Size(max = 50, message = "Payment reference must not exceed 50 characters")
    @Column(name = "payment_reference")
    private String paymentReference;

//    @NotBlank(message = "Bank name is required")
    @Size(max = 100, message = "Bank name must not exceed 100 characters")
    @Column(name = "bank_name")
    private String bankName;

//    @NotBlank(message = "Sort code is required")
//    @Pattern(regexp = "^\\d{2}-\\d{2}-\\d{2}$", message = "Sort code must be in format 12-34-56")
    @Column(name = "sort_code")
    private String sortCode;

    @Size(max = 200, message = "Address must not exceed 200 characters")
    @Column(name = "bank_address")
    private String bankAddress;

//    @Pattern(
//            regexp = "^([A-Z]{1,2}[0-9][A-Z0-9]? ?[0-9][A-Z]{2}|GIR ?0A{2})$",
//            message = "Invalid UK postcode format"
//    )
    @Column(name = "bank_post_code")
    private String bankPostCode;
//    @Pattern(
//            regexp = "^(\\+44|0)[1-9]\\d{1,4}\\d{6,7}$",
//            message = "Invalid UK telephone number format")
    @Column(name = "telephone")
    private String telephone;
    @Column(name = "payment_lead_days")
    private int paymentLeadDays=0;
    @Column(name = "isRTIReturnsIncluded")
    private Boolean isRTIReturnsIncluded=false;
    //EmployeeDetails
    @OneToOne(mappedBy = "bankDetails")
    @ToString.Exclude
    private EmployeeDetails employeeDetails;
    //EmployerDetails
    @OneToOne(mappedBy = "bankDetails")
    @ToString.Exclude
    private EmployerDetails employerDetails;

}
