package com.payroll.uk.payroll_processing.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.payroll.uk.payroll_processing.entity.PayPeriod;
import com.payroll.uk.payroll_processing.entity.TaxThreshold;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaySlipCreateDto {
//    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
//    private Long id;
    private String firstName;
    private String lastName;
    private String address;
    private String postCode;
    private String employeeId;
    @Enumerated(EnumType.STRING)
    private TaxThreshold.TaxRegion region;
    private String taxYear;
    private String taxCode;
    private String NI_Number;
    private String payPeriod;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate payDate;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate periodEnd;
    private BigDecimal grossPayTotal;
    private BigDecimal taxableIncome;
    private BigDecimal personalAllowance;
    private BigDecimal incomeTaxTotal;
    private BigDecimal nationalInsurance;
    private BigDecimal employersNationalInsurance;
    private BigDecimal deductionsTotal;
    private BigDecimal takeHomePayTotal;
    private String paySlipReference;

    /*public LocalDate getPreviousMonthEndDate() {
        // Get current system date as Pay Date
        LocalDate payDate = LocalDate.now();

        // Get end of previous month
        return payDate.withDayOfMonth(1).minusDays(1);
    }*/
}


