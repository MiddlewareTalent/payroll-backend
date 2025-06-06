package com.payroll.uk.payroll_processing.repository;

import com.payroll.uk.payroll_processing.entity.employer.EmployerDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployerDetailsRepository extends JpaRepository<EmployerDetails,Long> {
    boolean existsByEmployerId(String employerId);

    Optional<EmployerDetails> findByEmployerId(String employerId);
}
