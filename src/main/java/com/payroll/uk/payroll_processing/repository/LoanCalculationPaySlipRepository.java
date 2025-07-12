package com.payroll.uk.payroll_processing.repository;

import com.payroll.uk.payroll_processing.entity.LoanCalculationPaySlip;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LoanCalculationPaySlipRepository extends JpaRepository<LoanCalculationPaySlip, Long> {
    List<LoanCalculationPaySlip> findAllByEmployeeId(String employeeId);

    List<LoanCalculationPaySlip> findAllByNationalInsuranceNumber(String nationalInsuranceNumber);
}
