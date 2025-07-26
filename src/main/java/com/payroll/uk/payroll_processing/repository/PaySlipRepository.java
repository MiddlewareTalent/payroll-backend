package com.payroll.uk.payroll_processing.repository;

import com.payroll.uk.payroll_processing.dto.customdto.EmployeesSummaryInEmployerDTO;
import com.payroll.uk.payroll_processing.entity.PaySlip;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PaySlipRepository extends JpaRepository<PaySlip,Long> {
    List<PaySlip> findByEmployeeId(String employeeId);
    boolean existsByEmployeeId(String employeeId);


    boolean existsByPaySlipReference(String paySlipReference);

    PaySlip findByPaySlipReference(String paySlipReference);

    @Query("""
    SELECT new com.payroll.uk.payroll_processing.dto.customdto.EmployeesSummaryInEmployerDTO(
        CONCAT(p.firstName, ' ', p.lastName),
        p.employeeId,
        COUNT(p.id),
        SUM(p.grossPayTotal),
        SUM(p.incomeTaxTotal),
        SUM(p.employeeNationalInsurance),
         p.taxYear
    )
    FROM PaySlip p
    GROUP BY p.employeeId, p.firstName, p.lastName,p.taxYear
""")
    List<EmployeesSummaryInEmployerDTO> findByAllData();

    @Query("SELECT p FROM PaySlip p WHERE p.periodEnd = :periodEnd ORDER BY p.employeeId ASC")
    List<PaySlip> findAllPaySlipsByPeriod(@Param("periodEnd") String periodEnd);


    @Query("SELECT p FROM PaySlip p WHERE p.employeeId = :employeeId AND p.periodEnd = :periodEnd")
    List<PaySlip> findPaySlipsByEmployeeIdAndPeriodEnd(@Param("employeeId") String employeeId,@Param("periodEnd") String periodEnd);

}
