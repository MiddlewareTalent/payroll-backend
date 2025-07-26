package com.payroll.uk.payroll_processing.repository;

import com.payroll.uk.payroll_processing.entity.employee.EmployeeDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@EnableJpaRepositories
public interface EmployeeDetailsRepository extends JpaRepository<EmployeeDetails, Long> {

    Optional<EmployeeDetails> findByEmployeeId(String employeeId);

    Optional<EmployeeDetails> findByEmail(String email);

    boolean existsByEmail(String email);

    @Query("SELECT e.otherEmployeeDetails.remainingPersonalAllowance FROM EmployeeDetails e WHERE e.employeeId = :employeeId")
    BigDecimal findByRemainingPersonalAllowance(@Param("employeeId") String employeeId);

    boolean existsByNationalInsuranceNumber(String nationalInsuranceNumber);
    @Modifying
    @Query("UPDATE EmployeeDetails e SET " +
            "e.previousEmploymentData.previousTotalPayToDate = :zero, " +
            "e.previousEmploymentData.previousTotalTaxToDate = :zero, " +
            "e.previousEmploymentData.previousEmploymentEndDate = null, " +
            "e.previousEmploymentData.previousTaxCode = null")
    void resetPreviousEmploymentFieldsForAll(@Param("zero") BigDecimal zero);

    @Query("SELECT e.payrollId FROM EmployeeDetails e ORDER BY e.payrollId DESC")
    List<String> findLastEmployeePayrollId();


    @Query(value = "SELECT * FROM employee_details e " +
            "WHERE e.employee_id = :employeeId " +
            "AND (e.employment_end_date IS NULL " +
            "OR e.employment_end_date >= DATE_FORMAT(CURDATE(), '%Y-%m-01'))",
            nativeQuery = true)
    List<EmployeeDetails> findCurrentPayPeriodEmployeesForPaySlipGeneration(@Param("employeeId") String employeeId);

    /*@Query(value = "SELECT * FROM employee_details e " +
            "WHERE e.employment_end_date IS NULL " +
            "OR e.employment_end_date >= DATE_FORMAT(CURDATE(), '%Y-%m-01')",
            nativeQuery = true)
    List<EmployeeDetails> findCurrentPayPeriodEmployeesForPaySlipGeneration();*/


    @Query(value = "SELECT * FROM employee_details e " +
            "WHERE e.employment_end_date IS NULL " +
            "OR e.employment_end_date >= DATE_FORMAT(CURDATE(), '%Y-%m-01')",
            nativeQuery = true)
    List<EmployeeDetails> findActiveEmployees();

    @Query(value = "SELECT * FROM employee_details e " +
            "WHERE e.employment_end_date IS NOT NULL " +
            "AND e.employment_end_date < DATE_FORMAT(CURDATE(), '%Y-%m-01') "+
            "ORDER BY e.employment_end_date ASC",
            nativeQuery = true)
    List<EmployeeDetails> findInactiveEmployees();

    @Query(value = "SELECT * FROM employee_details e " +
            "WHERE e.employment_end_date IS NOT NULL " +
            "AND e.employment_end_date >= DATE_FORMAT(CURDATE(), '%Y-%m-01') " +
            "ORDER BY e.employment_end_date ASC",
            nativeQuery = true)
    List<EmployeeDetails> findReadyForLeavingEmployees();










}
