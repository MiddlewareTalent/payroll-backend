//package com.payroll.uk.payroll_processing.repository;
//
//import com.payroll.uk.payroll_processing.entity.employee.OtherEmployeeDetails;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//
//import java.math.BigDecimal;
//
//public interface OtherEmployeeDetailsRepository extends JpaRepository<OtherEmployeeDetails, Long> {
//
//    @Query("SELECT e.numberOfMonthsOfIncomeTaxPaid FROM OtherEmployeeDetails e WHERE e.employeeId = :employeeId")
//    BigDecimal findNumberOfMonthsOfIncomeTaxPaidByEmployeeId(@Param("employeeId") String employeeId);
//
//    @Query("SELECT e.totalIncomeTaxPaidInCompany FROM OtherEmployeeDetails e WHERE e.totalIncomeTaxPaidInCompany = :totalIncomeTaxPaidInCompany")
//    public abstract BigDecimal findByTotalIncomeTaxPaidInCompany(@Param("totalIncomeTaxPaidInCompany") String totalIncomeTaxPaidInCompany);
//
//    @Query("SELECT e.totalUsedPersonalAllowance FROM OtherEmployeeDetails e WHERE e.totalUsedPersonalAllowance = :totalUsedPersonalAllowance")
//    public abstract BigDecimal findByTotalUsedPersonalAllowance(@Param("totalUsedPersonalAllowance") String totalUsedPersonalAllowance);
//
//    @Query("SELECT e.remainingPersonalAllowance FROM OtherEmployeeDetails e WHERE e.remainingPersonalAllowance = :remainingPersonalAllowance")
//    public abstract BigDecimal findByRemainingPersonalAllowance(@Param("remainingPersonalAllowance") String remainingPersonalAllowance);
//
//    @Query("SELECT e.numberOfYearsOfIncomeTaxPaid FROM OtherEmployeeDetails e WHERE e.numberOfYearsOfIncomeTaxPaid = :numberOfYearsOfIncomeTaxPaid")
//    public abstract BigDecimal findByNumberOfYearsOfIncomeTaxPaid(@Param("numberOfYearsOfIncomeTaxPaid") String numberOfYearsOfIncomeTaxPaid);
//
//    @Query("SELECT e.numberOfMonthsOfIncomeTaxPaid FROM OtherEmployeeDetails e WHERE e.numberOfMonthsOfIncomeTaxPaid = :numberOfMonthsOfIncomeTaxPaid")
//    public abstract BigDecimal findByNumberOfMonthsOfIncomeTaxPaid(@Param("numberOfMonthsOfIncomeTaxPaid") String numberOfMonthsOfIncomeTaxPaid);
//
//    @Query("SELECT e.numberOfWeeksOfIncomeTaxPaid FROM OtherEmployeeDetails e WHERE e.numberOfWeeksOfIncomeTaxPaid = :numberOfWeeksOfIncomeTaxPaid")
//    public abstract BigDecimal findByNumberOfWeeksOfIncomeTaxPaid(@Param("numberOfWeeksOfIncomeTaxPaid") String numberOfWeeksOfIncomeTaxPaid);
//
//    @Query("SELECT e.totalEmployeeNIContributionInCompany FROM OtherEmployeeDetails e WHERE e.totalEmployeeNIContributionInCompany = :totalEmployeeNIContributionInCompany")
//    public abstract BigDecimal findByTotalEmployeeNIContributionInCompany(@Param("totalEmployeeNIContributionInCompany") String totalEmployeeNIContributionInCompany);
//
////    int findByNumberOfNIPaidYearsInCompany();
//
//    @Query("SELECT e.numberOfYearsOfNIContributions FROM OtherEmployeeDetails e WHERE e.employeeId = :employeeId")
//    public abstract BigDecimal findByNumberOfYearsOfNIContributions(@Param("employeeId") String employeeId);
//
//    @Query("SELECT e.numberOfMonthsOfNIContributions FROM OtherEmployeeDetails e WHERE e.employeeId = :employeeId")
//    public abstract BigDecimal findByNumberOfMonthsOfNIContributions(@Param("employeeId") String employeeId);
//
//    @Query("SELECT e.numberOfWeeksOfNIContributions FROM OtherEmployeeDetails e WHERE e.employeeId = :employeeId")
//    public abstract BigDecimal findByNumberOfWeeksOfNIContributions(@Param("employeeId") String employeeId);
//    // Define any custom query methods if needed
//}
