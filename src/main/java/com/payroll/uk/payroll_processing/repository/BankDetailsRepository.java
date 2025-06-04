package com.payroll.uk.payroll_processing.repository;

import com.payroll.uk.payroll_processing.entity.BankDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankDetailsRepository extends JpaRepository<BankDetails,Long> {
}
