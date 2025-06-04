package com.payroll.uk.payroll_processing.repository;

import com.payroll.uk.payroll_processing.entity.CompanyRegistration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CompanyRegistrationRepository extends JpaRepository<CompanyRegistration,Long> {
    Optional<CompanyRegistration> findByCompanyName(String companyName);
}
