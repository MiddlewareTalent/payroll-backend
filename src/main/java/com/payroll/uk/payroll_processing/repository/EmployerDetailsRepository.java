package com.payroll.uk.payroll_processing.repository;

import com.payroll.uk.payroll_processing.entity.employer.EmployerDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface EmployerDetailsRepository extends JpaRepository<EmployerDetails,Long> {
    boolean existsByEmployerId(String employerId);
    Optional<EmployerDetails> findByEmployerId(String employerId);
    @Modifying
    @Transactional
    @Query("UPDATE EmployerDetails e SET e.payDate = :payDate")
    void updatePayDateForAll(@Param("payDate") LocalDate payDate);

    Optional<EmployerDetails> findByEmployerEmail(String employerEmail);

    boolean existsByEmployerEmail(String employerEmail);


    @Query("SELECT sum(e.otherEmployerDetails.totalPaidGrossAmountYTD) FROM EmployerDetails e WHERE e.employerId = :employerId")
    BigDecimal findByTotalPaidGrossAmountYTD(@Param("employerId") String employerId);

    @Modifying
    @Query("UPDATE EmployerDetails e SET " +
            "e.otherEmployerDetails.currentPayPeriodEmployeePensionContribution = :zero, " +
            "e.otherEmployerDetails.currentPayPeriodEmployerPensionContribution = :zero, " +
            "e.otherEmployerDetails.currentPayPeriodEmployersNI = :zero, " +
            "e.otherEmployerDetails.currentPayPeriodEmployeesNI = :zero, " +
            "e.otherEmployerDetails.currentPayPeriodPAYE = :zero, " +
            "e.otherEmployerDetails.currentPayPeriodPaidGrossPay = :zero")
    void resetCurrentPayPeriodFieldsForAll(@Param("zero") BigDecimal zero);


}


