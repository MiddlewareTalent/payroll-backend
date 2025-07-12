package com.payroll.uk.payroll_processing.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class PensionCalculationPaySlip {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "employee_id", nullable = false)
    private String employeeId;
    @Column(name = "has_pension_eligible", nullable = false)
    private boolean hasPensionEligible= false;
    @Column(name = "national_insurance_number", nullable = false)
    private String nationalInsuranceNumber;
    @Column(name = "pay_slip_reference", nullable = false)
    private String paySlipReference;
    @Column(name = "income",nullable = false)
    @PositiveOrZero
    private BigDecimal income;
    @Column(name = "deduction_amount", nullable = false)
    @PositiveOrZero
    private BigDecimal pensionContributionDeductionAmount;

    @Column(name = "employer_pension_contribution_amount", nullable = false)
    @PositiveOrZero
    private BigDecimal employerPensionContributionAmount;
}
