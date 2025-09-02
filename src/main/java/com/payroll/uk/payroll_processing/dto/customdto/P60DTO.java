package com.payroll.uk.payroll_processing.dto.customdto;

import com.payroll.uk.payroll_processing.dto.employeedto.EmployeeAddressDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class P60DTO {

    private String employeeId;
    private String employeeNationalInsuranceNumber;
    private String firstName;
    private String lastName;
    private String taxYear;
    private String payrollId;
    private BigDecimal previousEmploymentPay;
    private BigDecimal previousEmploymentTaxDeducted;
    private BigDecimal currentEmploymentPay;
    private BigDecimal currentEmploymentTaxDeducted;
    private BigDecimal totalForYearPay;
    private BigDecimal totalForYearTaxDeducted;
    private String finalTaxCode;

    private List<NIContributionDTO> niContributions;
    private StatutoryPaymentDTO statutoryPayments;

    private BigDecimal studentLoanDeducted;
    private BigDecimal postgraduateLoanDeducted;
//    private String employeeAddress;
//    private String employeePostCode;
    private EmployeeAddressDTO employeeAddressDTO;
    private String employerPAYEReference;
    private String companyName;
    private String companyAddress;
    private String companyPostCode;


}
