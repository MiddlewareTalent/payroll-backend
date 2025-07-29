package com.payroll.uk.payroll_processing.entity.employer;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class TaxOffice {

    @NotNull(message = "PAYE scheme start date cannot be null")
//    @PastOrPresent(message = "PAYE scheme start date must be in the past or present")
    @Column(name = "date_paye_scheme_started")
    private LocalDate datePAYESchemeStarted;

    //    @FutureOrPresent(message = "PAYE scheme ceased date must be in the future or present")
    @Column(name = "date_paye_scheme_ceased")
    private LocalDate datePAYESchemeCeased;

    // ============== HMRC REFERENCES ==============
//    @NotBlank
//    @Pattern(regexp = "^\\d{3}/\\d{3,5}$",
//            message = "PAYE reference must be in format 123/456 or 123/45678")
    @Schema(description = "HMRC PAYE reference", example = "123/45678", defaultValue = "null")
    @Column(name = "payereference")
    private String payeReference;

//    @NotBlank
//    @Pattern(regexp = "^\\d{3}[A-Za-z]\\d{3}$",
//            message = "Accounts office reference must be in format 123A456")
    @Schema(description = "Accounts office reference", example = "123A456", defaultValue = "null")
    @Column(name = "accounts_office_reference")
    private String accountsOfficeReference;

    // ============== PAYMENT & TAX REFERENCES ==============
    @Enumerated(EnumType.STRING)
    @Column(name = "paymentMethod")
    private PaymentMethod paymentMethod = PaymentMethod.CHEQUE;

//    @Pattern(regexp = "^[0-9]{10}$",
//            message = "Unique Taxpayer Reference must be 10 digits")
    @Schema(description = "Unique Taxpayer Reference", example = "1234567890", defaultValue = "null")
    @Column(name = "uniqueTaxRef")
    private String uniqueTaxRef;

//    @Pattern(regexp = "^[A-Za-z0-9]{8,12}$",
//            message = "Corporation Tax reference must be 8-12 alphanumeric characters")
    @Schema(description = "Corporation Tax reference", example = "12345678", defaultValue = "null")
    @Column(name = "corporationTaxRef")
    private String corporationTaxRef;

//    @Pattern(regexp = "^[A-Za-z0-9]{8}$",
//            message = "Payroll Giving reference must be 8 alphanumeric characters")
    @Schema(description = "Payroll Giving reference", example = "12345678", defaultValue = "null")
    @Column(name = "payrollGivingRef")
    private String payrollGivingRef;

    // ============== STATUS FLAGS ==============

    @Schema(defaultValue = "false")
    @Column(name = "claimEmploymentAllowance")
    private boolean claimEmploymentAllowance=false;





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
