package com.payroll.uk.payroll_processing.repository;

import com.payroll.uk.payroll_processing.entity.TaxThreshold;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface TaxThresholdRepository extends JpaRepository<TaxThreshold, Long> {
    List<TaxThreshold> findByTaxYearAndRegionAndBandNameType(String taxYear, TaxThreshold.TaxRegion region, TaxThreshold.BandNameType bandNameType);




    @Query("SELECT DISTINCT t.taxYear FROM TaxThreshold t")
    List<String> findDistinctTaxYears();

    @Query("SELECT DISTINCT t.region FROM TaxThreshold t WHERE t.taxYear = :taxYear")
    List<TaxThreshold.TaxRegion> findDistinctRegionsByTaxYear(@Param("taxYear") String taxYear);

    boolean existsByTaxYear(String taxYear);

    void deleteByTaxYear(String taxYear);

    @Query("SELECT DISTINCT t.taxYear FROM TaxThreshold t ORDER BY t.taxYear ASC")
    List<String> findAllTaxYearsOrdered();


    List<TaxThreshold> findByTaxYearAndRegionAndBandNameTypeAndBandName(String taxYear, TaxThreshold.TaxRegion region, TaxThreshold.BandNameType bandNameType, TaxThreshold.BandName bandName);

    @Query("SELECT t FROM TaxThreshold t WHERE t.taxYear = :taxYear AND " +
            "(t.region = :region OR t.region = 'ALL_REGIONS')")
    List<TaxThreshold> findByTaxYearAndRegionIncludingAll(@Param("taxYear") String taxYear,
                                                          @Param("region") String region);



//    List<TaxThreshold> findByTaxYearAndRegionAndBandNameType(String taxYear,
//                                                             TaxThreshold.TaxRegion region,
//                                                             TaxThreshold.BandNameType bandNameType);

    List<TaxThreshold> findByTaxYearAndRegion(String taxYear, TaxThreshold.TaxRegion region);


    TaxThreshold findByTaxYearAndRegionAndBandName(String taxYear, TaxThreshold.TaxRegion taxRegion, TaxThreshold.BandName bandName);

    @Query("SELECT t FROM TaxThreshold t " +
            "WHERE t.region = 'ALL_REGIONS' " +
            "AND t.taxYear = :taxYear " +
            "AND t.bandName IN ('PERSONAL_ALLOWANCE', 'EMPLOYMENT_ALLOWANCE', 'AUTO_ENROLMENT_PENSION_CONTRIBUTION')")
    List<TaxThreshold> findFixedBandsByTaxYear(@Param("taxYear") String taxYear);

}