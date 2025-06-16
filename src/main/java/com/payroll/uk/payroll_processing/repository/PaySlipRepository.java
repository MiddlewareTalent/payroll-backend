package com.payroll.uk.payroll_processing.repository;

import com.payroll.uk.payroll_processing.entity.PaySlip;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaySlipRepository extends JpaRepository<PaySlip,Long> {
    List<PaySlip> findByEmployeeId(String employeeId);
    boolean existsByEmployeeId(String employeeId);


    boolean existsByPaySlipReference(String paySlipReference);

    PaySlip findByPaySlipReference(String paySlipReference);
}
