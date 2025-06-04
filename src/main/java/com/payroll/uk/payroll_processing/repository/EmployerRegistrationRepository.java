package com.payroll.uk.payroll_processing.repository;

import com.payroll.uk.payroll_processing.entity.employer.EmployerDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployerRegistrationRepository extends JpaRepository<EmployerDetails,Long> {
}
