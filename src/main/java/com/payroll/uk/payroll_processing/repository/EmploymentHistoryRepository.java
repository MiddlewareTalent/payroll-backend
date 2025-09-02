package com.payroll.uk.payroll_processing.repository;

import com.payroll.uk.payroll_processing.entity.employmentHistory.EmploymentHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmploymentHistoryRepository extends JpaRepository<EmploymentHistory,Long> {
    List<EmploymentHistory> findByEmployeeId(String employeeId);

    List<EmploymentHistory> findByEmployeeIdAndNiLetterChangedTrue(String employeeId);

    List<EmploymentHistory> findByEmployeeIdAndAddressChangedTrue(String employeeId);

    List<EmploymentHistory> findByEmployeeIdAndTaxCodeChangedTrue(String employeeId);

    List<EmploymentHistory> findByEmployeeIdAndPostCodeChangedTrue(String employeeId);

    List<EmploymentHistory> findByEmployeeIdAndAnnualIncomeChangedTrue(String employeeId);

    List<EmploymentHistory> findByEmployeeIdAndDateOfBirthChangedTrue(String employeeId);

    List<EmploymentHistory> findByEmployeeIdAndNationalInsuranceNumberChangedTrue(String employeeId);

    List<EmploymentHistory> findByEmployeeIdAndPayrollIdChangedTrue(String employeeId);

    List<EmploymentHistory> findByEmployeeIdAndStudentLoanPlanTypeChangedTrue(String employeeId);

    List<EmploymentHistory> findByEmployeeIdAndPostgraduateLoanPlanTypeChangedTrue(String employeeId);
}
