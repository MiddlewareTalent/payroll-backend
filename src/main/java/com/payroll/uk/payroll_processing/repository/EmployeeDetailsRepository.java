package com.payroll.uk.payroll_processing.repository;

import com.payroll.uk.payroll_processing.entity.employee.EmployeeDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.Optional;

@EnableJpaRepositories
public interface EmployeeDetailsRepository extends JpaRepository<EmployeeDetails, Long> {

    Optional<EmployeeDetails> findByEmployeeId(String employeeId);

    Optional<EmployeeDetails> findByEmail(String email);

    boolean existsByEmail(String email);

    @Query("SELECT e.otherEmployeeDetails.totalUsedPersonalAllowance FROM EmployeeDetails e WHERE e.employeeId = :employeeId")
    BigDecimal findByTotalUsedPersonalAllowance(@Param("employeeId") String employeeId);

    @Query("SELECT e.otherEmployeeDetails.remainingPersonalAllowance FROM EmployeeDetails e WHERE e.employeeId = :employeeId")
    BigDecimal findByRemainingPersonalAllowance(@Param("employeeId") String employeeId);

    @Query("SELECT e.otherEmployeeDetails.totalIncomeTaxPaidInCompany FROM EmployeeDetails e WHERE e.employeeId = :employeeId")
    BigDecimal findByTotalIncomeTaxPaidInCompany(@Param("employeeId") String employeeId);

    @Query("SELECT e.otherEmployeeDetails.numberOfYearsOfIncomeTaxPaid FROM EmployeeDetails e WHERE e.employeeId = :employeeId")
    BigDecimal findByNumberOfYearsOfIncomeTaxPaid(@Param("employeeId") String employeeId);

    @Query("SELECT e.otherEmployeeDetails.numberOfMonthsOfIncomeTaxPaid FROM EmployeeDetails e WHERE e.employeeId = :employeeId")
    BigDecimal findByNumberOfMonthsOfIncomeTaxPaid(@Param("employeeId") String employeeId);

    @Query("SELECT e.otherEmployeeDetails.numberOfWeeksOfIncomeTaxPaid FROM EmployeeDetails e WHERE e.employeeId = :employeeId")
    BigDecimal findByNumberOfWeeksOfIncomeTaxPaid(@Param("employeeId") String employeeId);

    @Query("SELECT e.otherEmployeeDetails.totalEmployeeNIContributionInCompany FROM EmployeeDetails e WHERE e.employeeId = :employeeId")
    BigDecimal findByTotalEmployeeNIContributionInCompany(@Param("employeeId") String employeeId);

//    int findByNumberOfNIPaidYearsInCompany(String employeeId);

    @Query("SELECT e.otherEmployeeDetails.numberOfYearsOfNIContributions FROM EmployeeDetails e WHERE e.employeeId = :employeeId")
    BigDecimal findByNumberOfYearsOfNIContributions(@Param("employeeId") String employeeId);

    @Query("SELECT e.otherEmployeeDetails.numberOfMonthsOfNIContributions FROM EmployeeDetails e WHERE e.employeeId = :employeeId")
    BigDecimal findByNumberOfMonthsOfNIContributions(@Param("employeeId") String employeeId);

    @Query("SELECT e.otherEmployeeDetails.numberOfWeeksOfNIContributions FROM EmployeeDetails e WHERE e.employeeId = :employeeId")
    BigDecimal findByNumberOfWeeksOfNIContributions(@Param("employeeId") String employeeId);
}
