package com.payroll.uk.payroll_processing.repository;

import com.payroll.uk.payroll_processing.entity.TaxThreshold;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface TaxThresholdRepository extends JpaRepository<TaxThreshold, Long> {
    List<TaxThreshold> findByTaxYearAndRegionAndBandNameType(String taxYear, TaxThreshold.TaxRegion region, TaxThreshold.TaxBandType bandNameType);


    @Query("SELECT DISTINCT t.taxYear FROM TaxThreshold t")
    List<String> findDistinctTaxYears();

    @Query("SELECT DISTINCT t.region FROM TaxThreshold t WHERE t.taxYear = :taxYear")
    List<TaxThreshold.TaxRegion> findDistinctRegionsByTaxYear(@Param("taxYear") String taxYear);

    boolean existsByTaxYear(String taxYear);

    void deleteByTaxYear(String taxYear);

    @Query("SELECT DISTINCT t.taxYear FROM TaxThreshold t ORDER BY t.taxYear ASC")
    List<String> findAllTaxYearsOrdered();


}