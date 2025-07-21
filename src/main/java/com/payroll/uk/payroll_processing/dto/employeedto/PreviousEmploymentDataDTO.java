package com.payroll.uk.payroll_processing.dto.employeedto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;


@Data
@AllArgsConstructor
@NoArgsConstructor

public class PreviousEmploymentDataDTO {
    private String previousTaxCode;
    private BigDecimal previousTotalPayToDate;
    private BigDecimal previousTotalTaxToDate;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate previousEmploymentEndDate;
}
