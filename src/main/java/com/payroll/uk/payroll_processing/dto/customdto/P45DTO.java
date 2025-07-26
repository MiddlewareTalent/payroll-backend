package com.payroll.uk.payroll_processing.dto.customdto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class P45DTO {

    //Employee Details
    private String employeeNationalInsuranceNumber;
    private LocalDate employeeDateOfBirth;
    private String firstName;
    private String lastName;
    private LocalDate employeeLeavingDate;
    private boolean employeeGender;
    private String employeeAddress;
    private String employeePostCode;

    //Employer Details
    private String employerPAYEReference;
    private String companyAddress;
    private String companyPostCode;
    private String companyName;


    //Payroll & tax information
//   private BigDecimal studentLoanDeductionAmount;
    private String currentPayPeriod;

    private String currentPayPeriodNumber;
    private String taxCodeAtLeaving;
    private BigDecimal totalPayToDate;
    private BigDecimal totalTaxToDate;
    private LocalDate p45Date;
    private boolean studentLoanToContinue;

    private BigDecimal totalPayInThisEmployment;
    private BigDecimal totalTaxInThisEmployment;
    private String employeeTitle;
    private boolean week1Month1Box; // true = tick X

}
