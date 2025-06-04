package com.payroll.uk.payroll_processing.repository;

import com.payroll.uk.payroll_processing.entity.employee.EmployeeDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmployeeDetailsRepository extends JpaRepository<EmployeeDetails, Long> {

    Optional<EmployeeDetails> findByEmployeeId(String employeeId);

    Optional<EmployeeDetails> findByEmail(String email);

    boolean existsByEmail(String email);
}
