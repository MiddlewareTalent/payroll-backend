package com.payroll.uk.payroll_processing.entity.employee;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostGraduateLoan {

    private Boolean hasPostgraduateLoan = false;
    private BigDecimal monthlyDeductionAmountInPostgraduateLoan = BigDecimal.ZERO;
    private BigDecimal weeklyDeductionAmountInPostgraduateLoan = BigDecimal.ZERO;
    private BigDecimal yearlyDeductionAmountInPostgraduateLoan = BigDecimal.ZERO;
    private BigDecimal totalDeductionAmountInPostgraduateLoan = BigDecimal.ZERO;
    @Enumerated(EnumType.STRING)
    private PostgraduateLoanPlanType postgraduateLoanPlanType= PostgraduateLoanPlanType.NONE;


    public enum PostgraduateLoanPlanType {
        POSTGRADUATE_LOAN_PLAN_3,
        NONE
    }
}
