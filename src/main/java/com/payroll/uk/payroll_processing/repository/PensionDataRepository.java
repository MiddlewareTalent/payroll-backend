package com.payroll.uk.payroll_processing.repository;

import com.payroll.uk.payroll_processing.entity.PensionCalculationPaySlip;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PensionDataRepository extends JpaRepository<PensionCalculationPaySlip,Long> {
}
