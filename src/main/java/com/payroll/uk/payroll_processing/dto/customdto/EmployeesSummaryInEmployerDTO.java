package com.payroll.uk.payroll_processing.dto.customdto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeesSummaryInEmployerDTO {


    //    private String firstName;
    private String fullName;
    private String employeeId;
    private long countOfPaySlips;
    private BigDecimal totalGrossPay;
    private BigDecimal totalIncomeTax;
    private BigDecimal totalEmployeeNIC;
    private  String taxYear;

//    public CustomEmployerDTO(String fullName, String employeeId,
//                             long countOfPaySlips, BigDecimal totalGrossPay,
//                             BigDecimal totalIncomeTax, BigDecimal totalEmployeeNIC) {
//        this.fullName = fullName;
//        this.employeeId = employeeId;
//        this.countOfPaySlips = countOfPaySlips;
//        this.totalGrossPay = totalGrossPay;
//        this.totalIncomeTax = totalIncomeTax;
//        this.totalEmployeeNIC = totalEmployeeNIC;
//    }

}
