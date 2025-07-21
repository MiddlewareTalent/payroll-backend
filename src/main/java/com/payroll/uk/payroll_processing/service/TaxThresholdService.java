package com.payroll.uk.payroll_processing.service;

import com.payroll.uk.payroll_processing.entity.NICategoryLetters;
import com.payroll.uk.payroll_processing.entity.TaxThreshold;
import com.payroll.uk.payroll_processing.repository.TaxThresholdRepository;
import jdk.jfr.Threshold;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class TaxThresholdService {
    private static  final Logger logging= LoggerFactory.getLogger(TaxThresholdService.class);
    private final TaxThresholdRepository taxThresholdRepository;

    public TaxThresholdService(TaxThresholdRepository taxThresholdRepository) {
        this.taxThresholdRepository = taxThresholdRepository;
    }

    public List<TaxThreshold> getTaxThresholds(String taxYear, TaxThreshold.TaxRegion region, TaxThreshold.BandNameType bandNameType) {
//        System.out.println("Fetching tax thresholds for year: " + taxYear + ", region: " + region + ", tax band type: " + bandNameType);
        return taxThresholdRepository.findByTaxYearAndRegionAndBandNameType(taxYear, region, bandNameType);
    }

    // Get NI thresholds by tax year, region, band name type, and band name
    public List<TaxThreshold> getNIThresholds(String taxYear, TaxThreshold.TaxRegion region, TaxThreshold.BandNameType bandNameType, TaxThreshold.BandName bandName){
//        System.out.println("Fetching NI thresholds for year: " + taxYear + ", region: " + region + ", tax band type: " + bandNameType+ ", band name: " + bandName);
        return taxThresholdRepository.findByTaxYearAndRegionAndBandNameTypeAndBandName(taxYear, region, bandNameType, bandName);
    }


    //Get Pension thresholds by tax year, region, band name type, and band name
    public BigDecimal[][] getPensionThresholds(String taxYear, TaxThreshold.TaxRegion region, TaxThreshold.BandNameType bandNameType, TaxThreshold.BandName bandName){
        return  getThresholdsBands( taxThresholdRepository.findByTaxYearAndRegionAndBandNameTypeAndBandName(taxYear, region, bandNameType, bandName));
    }

    // Get pension contribution rates by tax year, region, band name type, and band name
    public BigDecimal[] getPensionContributionRate(String taxYear, TaxThreshold.TaxRegion region, TaxThreshold.BandNameType bandNameType, TaxThreshold.BandName bandName){

        return getThresholdRates(taxThresholdRepository.findByTaxYearAndRegionAndBandNameTypeAndBandName(taxYear, region, bandNameType, bandName));
    }
    // Get student loan thresholds by tax year, region, band name type, and band name
    public BigDecimal[][] getStudentLoanThresholds(String taxYear, TaxThreshold.TaxRegion region, TaxThreshold.BandNameType bandNameType) {
      return  getThresholdsBands( taxThresholdRepository.findByTaxYearAndRegionAndBandNameType(taxYear, region, bandNameType));

    }
    //
    public BigDecimal[][] getStudentLoanThresholdsByPlanType(String taxYear, TaxThreshold.TaxRegion region, TaxThreshold.BandNameType bandNameType, TaxThreshold.BandName bandName){

        return  getThresholdsBands( taxThresholdRepository.findByTaxYearAndRegionAndBandNameTypeAndBandName(taxYear, region, bandNameType, bandName));
    }
    public BigDecimal[] getStudentLoanBandByPlanTypeRate(String taxYear, TaxThreshold.TaxRegion region, TaxThreshold.BandNameType bandNameType, TaxThreshold.BandName bandName){

        return getThresholdRates(taxThresholdRepository.findByTaxYearAndRegionAndBandNameTypeAndBandName(taxYear, region, bandNameType, bandName));
    }

    public List<TaxThreshold> getPostGraduateLoan(String taxYear, TaxThreshold.TaxRegion region, TaxThreshold.BandNameType bandNameType,TaxThreshold.BandName bandName){
        return taxThresholdRepository.findByTaxYearAndRegionAndBandNameTypeAndBandName(taxYear, region, bandNameType,bandName);
    }
  // Get all available tax years
    public List<String> getAvailableYears() {

        return taxThresholdRepository.findDistinctTaxYears();
    }

    // Get personal allowance By tax year
    public BigDecimal getPersonalAllowance(String taxYear) {
        List<TaxThreshold> personalAllowance = taxThresholdRepository.findByTaxYearAndRegionAndBandNameType(
                taxYear, TaxThreshold.TaxRegion.ALL_REGIONS, TaxThreshold.BandNameType.PERSONAL_ALLOWANCE);
        if (personalAllowance.isEmpty()) {
            throw new IllegalStateException("Personal allowance not found for year: " + taxYear );
        }

        return personalAllowance.getFirst().getLowerBound();
    }
    // Get employment allowance By tax year
    public BigDecimal getEmploymentAllowance(String taxYear) {
        List<TaxThreshold> employmentAllowance = taxThresholdRepository.findByTaxYearAndRegionAndBandNameType(
                taxYear, TaxThreshold.TaxRegion.ALL_REGIONS, TaxThreshold.BandNameType.EMPLOYMENT_ALLOWANCE);
        if (employmentAllowance.isEmpty()) {
            throw new IllegalStateException("Personal allowance not found for year: " + taxYear );
        }

        return employmentAllowance.getFirst().getLowerBound();
    }
  // Get all available regions for a given tax year
    public List<TaxThreshold.TaxRegion> getAvailableRegions(String taxYear) {
        return taxThresholdRepository.findDistinctRegionsByTaxYear(taxYear);
    }
    public BigDecimal[][] getTaxBounds(String taxYear, TaxThreshold.TaxRegion region,
                                       TaxThreshold.BandNameType BandNameType) {
        logging.info("Tax Thresholds Service in Fetching tax bounds for year: {}, region: {}, band name type: {}", taxYear, region, BandNameType);
        boolean isExist = taxThresholdRepository.existsByTaxYear(taxYear);
        if (!isExist) {
            throw new IllegalStateException("Year range not found: " + taxYear);
        }
        List<TaxThreshold> taxBands = getTaxThresholds(taxYear, region,BandNameType);

        // Different validation for Scotland
        if(BandNameType== TaxThreshold.BandNameType.INCOME_TAX) {
            int minBands = (region == TaxThreshold.TaxRegion.SCOTLAND) ? 5 : 3;
            if (taxBands.size() < minBands) {
                throw new IllegalStateException(String.format(
                        "Need at least %d tax bands for %s, but found %d",
                        minBands, region, taxBands.size()));
            }
        }
        else{
            if (taxBands.isEmpty()){
                throw new IllegalStateException(String.format(
                        "Need at least 1 tax band for %s, but found %d",
                        region, taxBands.size()));
            }
        }

        // Convert to bounds array
        BigDecimal[][] bounds = new BigDecimal[taxBands.size()][2];
        for (int i = 0; i < taxBands.size(); i++) {
            bounds[i][0] = taxBands.get(i).getLowerBound();
            bounds[i][1] = taxBands.get(i).getUpperBound();
        }

        return bounds;
    }
    public BigDecimal[] getTaxRates(String taxYear, TaxThreshold.TaxRegion region,
                                    TaxThreshold.BandNameType BandNameType) {
        boolean isExist = taxThresholdRepository.existsByTaxYear(taxYear);
        if (!isExist) {
            throw new IllegalStateException("Year range not found: " + taxYear);
        }
        List<TaxThreshold> taxBands = getTaxThresholds(taxYear, region, BandNameType);


        if (BandNameType == TaxThreshold.BandNameType.INCOME_TAX) {
            // Different validation for Scotland
            int minBands = (region == TaxThreshold.TaxRegion.SCOTLAND) ? 5 : 3;
            if (taxBands.size() < minBands) {
                throw new IllegalStateException(String.format(
                        "Need at least %d tax bands for %s, but found %d",
                        minBands, region, taxBands.size()));
            }
        }
        else{
            if (taxBands.isEmpty()){
                throw new IllegalStateException(String.format(
                        "Need at least 1 tax band for %s, but found %d",
                        region, taxBands.size()));
            }
        }

        // Convert to rates array
        BigDecimal[] rates = new BigDecimal[taxBands.size()];
        for (int i = 0; i < taxBands.size(); i++) {
            rates[i] = taxBands.get(i).getRate();
        }

        return rates;
    }
    public BigDecimal[][] getEmployeeNIThreshold(String taxYear,TaxThreshold.TaxRegion region,TaxThreshold.BandNameType BandNameType,NICategoryLetters NILetter){
        boolean isExist = taxThresholdRepository.existsByTaxYear(taxYear);
        if (!isExist) {
            throw new IllegalStateException("Year range not found: " + taxYear);
        }
        if(List.of(NICategoryLetters.A,NICategoryLetters.F,NICategoryLetters.H,NICategoryLetters.M,NICategoryLetters.N,
                NICategoryLetters.V).contains(NILetter)){
            List<TaxThreshold>Employee_NI_A=getNIThresholds(taxYear,region,BandNameType, TaxThreshold.BandName.EMPLOYEE_NI_TYPE_A);
            return getThresholdsBands(Employee_NI_A);

        }
        if(List.of(NICategoryLetters.D,NICategoryLetters.J,NICategoryLetters.L,NICategoryLetters.Z).contains(NILetter)){
            List<TaxThreshold>Employee_NI_B=getNIThresholds(taxYear,region,BandNameType, TaxThreshold.BandName.EMPLOYEE_NI_TYPE_B);
            return getThresholdsBands(Employee_NI_B);
        }
        if(List.of(NICategoryLetters.B,NICategoryLetters.E,NICategoryLetters.I).contains(NILetter)){
            List<TaxThreshold>Employee_NI_C=getNIThresholds(taxYear,region,BandNameType, TaxThreshold.BandName.EMPLOYEE_NI_TYPE_C);
            return getThresholdsBands(Employee_NI_C);
        }
        if(List.of(NICategoryLetters.C,NICategoryLetters.S).contains(NILetter)){
            List<TaxThreshold>Employee_NI_D=getNIThresholds(taxYear,region,BandNameType, TaxThreshold.BandName.EMPLOYEE_NI_TYPE_D);
            return getThresholdsBands(Employee_NI_D);
        }
        if(List.of(NICategoryLetters.X).contains(NILetter)){
            List<TaxThreshold>Employee_NI_D=getNIThresholds(taxYear,region,BandNameType, TaxThreshold.BandName.EMPLOYEE_NI_TYPE_D);
            return getThresholdsBands(Employee_NI_D);
        }

        return getThresholdsBands(getNIThresholds(taxYear,region,BandNameType, TaxThreshold.BandName.EMPLOYEE_NI_TYPE_D));

    }

    public BigDecimal[][] getThresholdsBands(List<TaxThreshold> niThresholds) {
        if (niThresholds.isEmpty()) {
            throw new IllegalStateException("No NI thresholds found");
        }
        BigDecimal[][] bounds = new BigDecimal[niThresholds.size()][2];
        for (int i = 0; i < niThresholds.size(); i++) {
            bounds[i][0] = niThresholds.get(i).getLowerBound();
            bounds[i][1] = niThresholds.get(i).getUpperBound();
        }

        return bounds;
    }


    public BigDecimal[] getEmployeeNIRates(String taxYear,TaxThreshold.TaxRegion region,TaxThreshold.BandNameType BandNameType,NICategoryLetters NILetter){
        boolean isExist = taxThresholdRepository.existsByTaxYear(taxYear);
        if (!isExist) {
            throw new IllegalStateException("Year range not found: " + taxYear);
        }
        if(List.of(NICategoryLetters.A,NICategoryLetters.F,NICategoryLetters.H,NICategoryLetters.M,NICategoryLetters.N,
                NICategoryLetters.V).contains(NILetter)){
            List<TaxThreshold>Employee_NI_A=getNIThresholds(taxYear,region,BandNameType, TaxThreshold.BandName.EMPLOYEE_NI_TYPE_A);
            return getThresholdRates(Employee_NI_A);

        }
        if(List.of(NICategoryLetters.D,NICategoryLetters.J,NICategoryLetters.L,NICategoryLetters.Z).contains(NILetter)){
            List<TaxThreshold>Employee_NI_B=getNIThresholds(taxYear,region,BandNameType, TaxThreshold.BandName.EMPLOYEE_NI_TYPE_B);
            return getThresholdRates(Employee_NI_B);
        }
        if(List.of(NICategoryLetters.B,NICategoryLetters.E,NICategoryLetters.I).contains(NILetter)){
            List<TaxThreshold>Employee_NI_C=getNIThresholds(taxYear,region,BandNameType, TaxThreshold.BandName.EMPLOYEE_NI_TYPE_C);
            return getThresholdRates(Employee_NI_C);
        }
        if(List.of(NICategoryLetters.C,NICategoryLetters.S).contains(NILetter)){
            List<TaxThreshold>Employee_NI_D=getNIThresholds(taxYear,region,BandNameType, TaxThreshold.BandName.EMPLOYEE_NI_TYPE_D);
            return getThresholdRates(Employee_NI_D);
        }
        if(List.of(NICategoryLetters.X).contains(NILetter)){
            List<TaxThreshold>Employee_NI_D=getNIThresholds(taxYear,region,BandNameType, TaxThreshold.BandName.EMPLOYEE_NI_TYPE_D);
            return getThresholdRates(Employee_NI_D);
        }

        return getThresholdRates(getNIThresholds(taxYear,region,BandNameType, TaxThreshold.BandName.EMPLOYEE_NI_TYPE_D));

    }
    public BigDecimal[] getThresholdRates(List<TaxThreshold> niThresholdsRate) {
        if (niThresholdsRate.isEmpty()) {
            throw new IllegalStateException("No NI thresholds found");
        }
        BigDecimal[] rates = new BigDecimal[niThresholdsRate.size()];
        for (int i = 0; i < niThresholdsRate.size(); i++) {
            rates[i] = niThresholdsRate.get(i).getRate();
        }

        return rates;
    }

    public BigDecimal[][] getEmployerThreshold(String taxYear,TaxThreshold.TaxRegion region,TaxThreshold.BandNameType BandNameType,NICategoryLetters NILetter){
        boolean isExist = taxThresholdRepository.existsByTaxYear(taxYear);
        if (!isExist) {
            throw new IllegalStateException("Year range not found: " + taxYear);
        }
        if(List.of(NICategoryLetters.A,NICategoryLetters.B,NICategoryLetters.C,NICategoryLetters.J).contains(NILetter)){
            List<TaxThreshold>Employee_NI_A=getNIThresholds(taxYear,region,BandNameType, TaxThreshold.BandName.EMPLOYER_NI_TYPE_A);
            return getThresholdsBands(Employee_NI_A);

        }
        if(List.of(NICategoryLetters.D,NICategoryLetters.E,NICategoryLetters.F,NICategoryLetters.I,
                NICategoryLetters.K,NICategoryLetters.L,NICategoryLetters.N,NICategoryLetters.S).contains(NILetter)){
            List<TaxThreshold>Employee_NI_B=getNIThresholds(taxYear,region,BandNameType, TaxThreshold.BandName.EMPLOYER_NI_TYPE_B);
            return getThresholdsBands(Employee_NI_B);
        }
        if(List.of(NICategoryLetters.H,NICategoryLetters.M,NICategoryLetters.V,NICategoryLetters.Z).contains(NILetter)){
            List<TaxThreshold>Employee_NI_C=getNIThresholds(taxYear,region,BandNameType, TaxThreshold.BandName.EMPLOYER_NI_TYPE_C);
            return getThresholdsBands(Employee_NI_C);
        }

        return new BigDecimal[0][0];

    }
    public BigDecimal[] getEmployerRates(String taxYear,TaxThreshold.TaxRegion region,TaxThreshold.BandNameType BandNameType,NICategoryLetters NILetter){
        boolean isExist = taxThresholdRepository.existsByTaxYear(taxYear);
        if (!isExist) {
            throw new IllegalStateException("Year range not found: " + taxYear);
        }
        if(List.of(NICategoryLetters.A,NICategoryLetters.B,NICategoryLetters.C,NICategoryLetters.J).contains(NILetter)){
            List<TaxThreshold>Employee_NI_A=getNIThresholds(taxYear,region,BandNameType, TaxThreshold.BandName.EMPLOYER_NI_TYPE_A);
            return getThresholdRates(Employee_NI_A);

        }
        if(List.of(NICategoryLetters.D,NICategoryLetters.E,NICategoryLetters.F,NICategoryLetters.I,
                NICategoryLetters.K,NICategoryLetters.L,NICategoryLetters.N,NICategoryLetters.S).contains(NILetter)){
            List<TaxThreshold>Employee_NI_B=getNIThresholds(taxYear,region,BandNameType, TaxThreshold.BandName.EMPLOYER_NI_TYPE_B);
            return getThresholdRates(Employee_NI_B);
        }
        if(List.of(NICategoryLetters.H,NICategoryLetters.M,NICategoryLetters.V,NICategoryLetters.Z).contains(NILetter)){
            List<TaxThreshold>Employee_NI_C=getNIThresholds(taxYear,region,BandNameType, TaxThreshold.BandName.EMPLOYER_NI_TYPE_C);
            return getThresholdRates(Employee_NI_C);
        }

        return new BigDecimal[0];

    }

   /* public Map<String,BigDecimal> AllowanceData(String taxYear){
        if( taxYear == null || taxYear.isEmpty()) {
            throw new IllegalArgumentException("Tax year and band name cannot be null or empty");
        }
        boolean isExist = taxThresholdRepository.existsByTaxYear(taxYear);
        if (!isExist) {
            throw new IllegalStateException("Year range not found: " + taxYear);
        }
        TaxThreshold personalAllowanceData = taxThresholdRepository.findByTaxYearAndRegionAndBandName(taxYear, TaxThreshold.TaxRegion.ALL_REGIONS, TaxThreshold.BandName.PERSONAL_ALLOWANCE);
        TaxThreshold employmentAllowanceData = taxThresholdRepository.findByTaxYearAndRegionAndBandName(taxYear, TaxThreshold.TaxRegion.ALL_REGIONS, TaxThreshold.BandName.EMPLOYMENT_ALLOWANCE);
        TaxThreshold autoEnrollmentTriggerData = taxThresholdRepository.findByTaxYearAndRegionAndBandName(taxYear, TaxThreshold.TaxRegion.ALL_REGIONS, TaxThreshold.BandName.AUTO_ENROLMENT_PENSION_CONTRIBUTION);

     taxThresholdRepository.findFixedBandsByTaxYear(taxYear);
        Map<String,BigDecimal> allowanceData= new HashMap<>();
        allowanceData.put("personalAllowance",personalAllowanceData.getLowerBound());
        allowanceData.put("employmentAllowance",employmentAllowanceData.getLowerBound());
        allowanceData.put("AutoEnrollForPension",autoEnrollmentTriggerData.getLowerBound());

        return allowanceData;
    }*/

    public Map<String, BigDecimal> getAllowanceData(String taxYear) {
        List<TaxThreshold> thresholds = taxThresholdRepository.findFixedBandsByTaxYear(taxYear);

        Map<String, BigDecimal> allowanceData = new HashMap<>();

        for (TaxThreshold threshold : thresholds) {
            switch (threshold.getBandName()) {
                case TaxThreshold.BandName.PERSONAL_ALLOWANCE:
                    allowanceData.put("personalAllowance", threshold.getLowerBound());
                    break;
                case TaxThreshold.BandName.EMPLOYMENT_ALLOWANCE:
                    allowanceData.put("employmentAllowance", threshold.getLowerBound());
                    break;
                case TaxThreshold.BandName.AUTO_ENROLMENT_PENSION_CONTRIBUTION:
                    allowanceData.put("autoEnrollForPension", threshold.getLowerBound());
                    break;
            }
        }

        return allowanceData;
    }


    public List<TaxThreshold> getCombinedThresholds(String taxYear, TaxThreshold.TaxRegion region) {
        List<TaxThreshold> incomeTaxThresholds = taxThresholdRepository
                .findByTaxYearAndRegionAndBandNameType(taxYear, region, TaxThreshold.BandNameType.INCOME_TAX);

        List<TaxThreshold> otherThresholds = taxThresholdRepository
                .findByTaxYearAndRegion(taxYear, TaxThreshold.TaxRegion.ALL_REGIONS);

        List<TaxThreshold> all = new ArrayList<>();
        all.addAll(incomeTaxThresholds);
        all.addAll(otherThresholds);

        return all;
    }
}