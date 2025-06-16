package com.payroll.uk.payroll_processing.dto.employerdto;

import com.payroll.uk.payroll_processing.entity.employer.TaxOffice;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaxOfficeDTO {
    @Schema(description = "HMRC PAYE reference", example = "123/45678")
    private String payeReference;
    @Schema(description = "Accounts office reference", example = "123A456")
    private String accountsOfficeReference;

    // ============== PAYMENT & TAX REFERENCES ==============
    @Enumerated(EnumType.STRING)
    private TaxOffice.PaymentMethod paymentMethod= TaxOffice.PaymentMethod.ONLINE;
    @Schema(description = "Unique Taxpayer Reference", example = "1234567890",defaultValue = "null")
    private String uniqueTaxRef;
    @Schema(description = "Corporation Tax reference", example = "12345678",defaultValue = "null")
    private String corporationTaxRef;
    @Schema(description = "Payroll Giving reference", example = "12345678",defaultValue = "null")
    private String payrollGivingRef;

    // ============== STATUS FLAGS ==============
    @Schema(defaultValue = "false")
    private Boolean serQualifiedThisYear=false ;
    @Schema(defaultValue = "false")
    private Boolean serQualifiedLastYear=false ;
    @Schema(defaultValue = "false")
    private Boolean noRtiDueWarnings = false;
    @Schema(defaultValue = "false")
    private Boolean claimNICAllowance=false; ;
    @Schema(defaultValue = "false")
    private Boolean claimEmploymentAllowance=false;

    @Schema(description = "Child Support reference", example = "1234567",defaultValue = "null")
    private String childSupportRef;







}
