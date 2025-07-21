package com.payroll.uk.payroll_processing.entity.employee;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.PrePersist;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Embeddable
@AllArgsConstructor
@Data
@NoArgsConstructor
public class PreviousEmploymentData {

//    @Column(name = "previous_total_pay_to_date", nullable = false, columnDefinition = "DECIMAL(10,2) DEFAULT 0.00")

    @Column(name = "previous_tax_code")
    private String previousTaxCode;
    @Column(name = "previous_total_pay_to_date")
    @PositiveOrZero(message = "previous total pay to date must be zero or positive")
    private BigDecimal previousTotalPayToDate;
    @Column(name = "previous_total_tax_to_date")
    @PositiveOrZero(message = "previous total tax to date must be zero or positive")
    private BigDecimal previousTotalTaxToDate;
    @Column(name = "previous_employment_end_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate previousEmploymentEndDate;

    @PrePersist
    public void setDefaultsIfNull() {

        if (previousTotalPayToDate == null) {
            previousTotalPayToDate = BigDecimal.ZERO; // Default total pay to date
        }
        if (previousTotalTaxToDate == null) {
            previousTotalTaxToDate = BigDecimal.ZERO; // Default tax paid
        }

        //aa

    }


}
