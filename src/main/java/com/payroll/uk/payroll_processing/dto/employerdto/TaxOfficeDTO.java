package com.payroll.uk.payroll_processing.dto.employerdto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.payroll.uk.payroll_processing.entity.employer.TaxOffice;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaxOfficeDTO {

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate datePAYESchemeStarted;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate datePAYESchemeCeased;
    private String payeReference;
    private String accountsOfficeReference;
    private TaxOffice.PaymentMethod paymentMethod = TaxOffice.PaymentMethod.CHEQUE;
    private String uniqueTaxRef;
    private String corporationTaxRef;
    private String payrollGivingRef;
    private boolean claimEmploymentAllowance=false;







}
