package com.payroll.uk.payroll_processing.dto.customdto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatutoryPaymentDTO {
    private BigDecimal statutoryMaternityPay;        // SMP
    private BigDecimal statutoryPaternityPay;        // SPP
    private BigDecimal statutorySharedParentalPay;   // ShPP
    private BigDecimal statutoryAdoptionPay;         // SAP
    private BigDecimal statutoryParentalBereavementPay; // SPBP
    private BigDecimal statutoryNeonatalCarePay;     // SNCP
}
