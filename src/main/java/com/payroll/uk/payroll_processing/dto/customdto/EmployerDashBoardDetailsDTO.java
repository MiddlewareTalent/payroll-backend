package com.payroll.uk.payroll_processing.dto.customdto;

import com.payroll.uk.payroll_processing.entity.PayPeriod;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployerDashBoardDetailsDTO {
    private Long TotalEmployees;
    private Long TotalPaySlips;

    private PayPeriod payPeriod;
    private  String currentPayPeriodNumber;
    private String currentYear;
    private Long currentYearCompletedDays;

    private BigDecimal totalPaidGrossAmountYTD;
    private BigDecimal totalIncomeTax;
    private BigDecimal totalEmployeeNI;
    private BigDecimal totalEmployerNI;
}
