package com.payroll.uk.payroll_processing.repository;

import com.payroll.uk.payroll_processing.entity.employer.EmployerDetails;
import jakarta.annotation.Nullable;
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

    @Query("SELECT e.otherEmployerDetails.totalPAYEYTD FROM EmployerDetails e WHERE e.employerId = :employerId ")
    BigDecimal findByTotalPAYEYTD(@Param("employerId") String employerId);


    @Query("SELECT e.otherEmployerDetails.totalEmployeesNIYTD FROM EmployerDetails e WHERE e.employerId = :employerId ")
    BigDecimal findByTotalEmployeesNIYTD(@Param("employerId") String employerId);

    @Query("SELECT e.otherEmployerDetails.totalEmployersNIYTD FROM EmployerDetails e WHERE e.employerId = :employerId ")
    BigDecimal findByTotalEmployersNIYTD(@Param("employerId") String employerId);

    @Modifying
    @Transactional
    @Query("UPDATE EmployerDetails e SET e.payDate = :payDate")
    void updatePayDateForAll(@Param("payDate") LocalDate payDate);
}

