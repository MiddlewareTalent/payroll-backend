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

    @Query("SELECT e.remainingPersonalAllowanceInYear FROM EmployeeDetails e WHERE e.employeeId = :employeeId")
    BigDecimal findByRemainingPersonalAllowanceInYear(@Param("employeeId") String employeeId);

    //StudentLoan
    @Query("SELECT e.studentLoan.yearlyDeductionAmountInStudentLoan FROM EmployeeDetails e WHERE e.employeeId = :employeeId")
    BigDecimal findByYearlyDeductionAmountInStudentLoan(@Param("employeeId") String employeeId);

    @Query("SELECT e.studentLoan.monthlyDeductionAmountInStudentLoan FROM EmployeeDetails e WHERE e.employeeId = :employeeId")
    BigDecimal findByMonthlyDeductionAmountInStudentLoan(@Param("employeeId") String employeeId);

    @Query("SELECT e.studentLoan.weeklyDeductionAmountInStudentLoan FROM EmployeeDetails e WHERE e.employeeId = :employeeId")
    BigDecimal findByWeeklyDeductionAmountInStudentLoan(@Param("employeeId") String employeeId);

    @Query("SELECT e.studentLoan.totalDeductionAmountInStudentLoan FROM EmployeeDetails e WHERE e.employeeId = :employeeId")
    BigDecimal findByTotalDeductionAmountInStudentLoan(@Param("employeeId") String employeeId);

    //PostgraduateLoan
    @Query("SELECT e.postGraduateLoan.yearlyDeductionAmountInPostgraduateLoan FROM EmployeeDetails e WHERE e.employeeId = :employeeId")
    BigDecimal findByYearlyDeductionAmountInPostgraduateLoan(@Param("employeeId") String employeeId);

    @Query("SELECT e.postGraduateLoan.monthlyDeductionAmountInPostgraduateLoan FROM EmployeeDetails e WHERE e.employeeId = :employeeId")
    BigDecimal findByMonthlyDeductionAmountInPostgraduateLoan(@Param("employeeId") String employeeId);

    @Query("SELECT e.postGraduateLoan.weeklyDeductionAmountInPostgraduateLoan FROM EmployeeDetails e WHERE e.employeeId = :employeeId")
    BigDecimal findByWeeklyDeductionAmountInPostgraduateLoan(@Param("employeeId") String employeeId);

    @Query("SELECT e.postGraduateLoan.totalDeductionAmountInPostgraduateLoan FROM EmployeeDetails e WHERE e.employeeId = :employeeId")
    BigDecimal findByTotalDeductionAmountInPostgraduateLoan(@Param("employeeId") String employeeId);
}
