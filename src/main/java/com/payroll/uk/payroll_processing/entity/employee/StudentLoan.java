package com.payroll.uk.payroll_processing.entity.employee;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class StudentLoan {
    private Boolean hasStudentLoan = false;

    private BigDecimal monthlyDeductionAmountInStudentLoan = BigDecimal.ZERO;
    private BigDecimal weeklyDeductionAmountInStudentLoan = BigDecimal.ZERO;
    private BigDecimal yearlyDeductionAmountInStudentLoan = BigDecimal.ZERO;
    private BigDecimal totalDeductionAmountInStudentLoan = BigDecimal.ZERO;

     @Enumerated(EnumType.STRING)
    private StudentLoanPlan studentLoanPlanType= StudentLoanPlan.NONE;




    public enum StudentLoanPlan {

        STUDENT_LOAN_PLAN_1,
        STUDENT_LOAN_PLAN_2,
        STUDENT_LOAN_PLAN_3,
        NONE

    }


}
