package com.payroll.uk.payroll_processing.entity.employer;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class TaxOffice {

    // ============== HMRC REFERENCES ==============
//    @NotBlank
//    @Pattern(regexp = "^\\d{3}/\\d{3,5}$",
//            message = "PAYE reference must be in format 123/456 or 123/45678")
    @Schema(description = "HMRC PAYE reference", example = "123/45678", defaultValue = "null")
    private String payeReference;

//    @NotBlank
//    @Pattern(regexp = "^\\d{3}[A-Za-z]\\d{3}$",
//            message = "Accounts office reference must be in format 123A456")
    @Schema(description = "Accounts office reference", example = "123A456", defaultValue = "null")
    private String accountsOfficeReference;

    // ============== PAYMENT & TAX REFERENCES ==============
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod = PaymentMethod.CHEQUE;

//    @Pattern(regexp = "^[0-9]{10}$",
//            message = "Unique Taxpayer Reference must be 10 digits")
    @Schema(description = "Unique Taxpayer Reference", example = "1234567890", defaultValue = "null")
    private String uniqueTaxRef;

//    @Pattern(regexp = "^[A-Za-z0-9]{8,12}$",
//            message = "Corporation Tax reference must be 8-12 alphanumeric characters")
    @Schema(description = "Corporation Tax reference", example = "12345678", defaultValue = "null")
    private String corporationTaxRef;

//    @Pattern(regexp = "^[A-Za-z0-9]{8}$",
//            message = "Payroll Giving reference must be 8 alphanumeric characters")
    @Schema(description = "Payroll Giving reference", example = "12345678", defaultValue = "null")
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

    // ============== CHILD SUPPORT ==============
//    @Pattern(regexp = "^[A-Za-z0-9]{7}$",
//            message = "Child Support reference must be 7 alphanumeric characters")
    @Schema(description = "Child Support reference", example = "1234567", defaultValue = "null")
    private String childSupportRef;




    // ============== PAYMENT METHODS ==============
    public Boolean hasCompleteHmrcReferences() {
        return payeReference != null && !payeReference.isEmpty()
                && accountsOfficeReference != null && !accountsOfficeReference.isEmpty();
    }

    public Boolean isElectronicPaymentMethod() {
        return paymentMethod == PaymentMethod.BACS
                || paymentMethod == PaymentMethod.DIRECT_DEBIT
                || paymentMethod == PaymentMethod.ONLINE;
    }
    @Getter
    public enum PaymentMethod {
        BACS("BACS Transfer"),
        CHEQUE("Cheque"),
        DIRECT_DEBIT("Direct Debit"),
        ONLINE("Online Payment");

        private final String description;

        PaymentMethod(String description) {
            this.description = description;
        }

    }


}
