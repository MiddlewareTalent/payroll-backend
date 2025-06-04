package com.payroll.uk.payroll_processing.repository;

import com.payroll.uk.payroll_processing.entity.PaySlip;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaySlipRepository extends JpaRepository<PaySlip,Long> {
}
