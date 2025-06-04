package com.payroll.uk.payroll_processing.service;

import com.payroll.uk.payroll_processing.entity.TaxThreshold;
import com.payroll.uk.payroll_processing.repository.TaxThresholdRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class TaxThresholdService {
    private final TaxThresholdRepository taxThresholdRepository;

    public TaxThresholdService(TaxThresholdRepository taxThresholdRepository) {
        this.taxThresholdRepository = taxThresholdRepository;
    }

    public List<TaxThreshold> getTaxThresholds(String taxYear, TaxThreshold.TaxRegion region, TaxThreshold.TaxBandType bandType) {
        System.out.println("Fetching tax thresholds for year: " + taxYear + ", region: " + region + ", tax band type: " + bandType);
        return taxThresholdRepository.findByTaxYearAndRegionAndBandNameType(taxYear, region, bandType);
    }

    public List<String> getAvailableYears() {

        return taxThresholdRepository.findDistinctTaxYears();
    }

    public List<TaxThreshold.TaxRegion> getAvailableRegions(String taxYear) {
        return taxThresholdRepository.findDistinctRegionsByTaxYear(taxYear);
    }
    public BigDecimal[][] getTaxBounds(String taxYear, TaxThreshold.TaxRegion region,
                                       TaxThreshold.TaxBandType taxBandType) {
        boolean isExist = taxThresholdRepository.existsByTaxYear(taxYear);
        if (!isExist) {
            throw new IllegalStateException("Year range not found: " + taxYear);
        }
        List<TaxThreshold> taxBands = getTaxThresholds(taxYear, region,taxBandType);

        // Different validation for Scotland
        if(taxBandType== TaxThreshold.TaxBandType.INCOME_TAX) {
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
                                    TaxThreshold.TaxBandType taxBandType) {
        boolean isExist = taxThresholdRepository.existsByTaxYear(taxYear);
        if (!isExist) {
            throw new IllegalStateException("Year range not found: " + taxYear);
        }
        List<TaxThreshold> taxBands = getTaxThresholds(taxYear, region, taxBandType);


        if (taxBandType == TaxThreshold.TaxBandType.INCOME_TAX) {
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

}