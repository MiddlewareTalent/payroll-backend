package com.payroll.uk.payroll_processing.controller;

import com.payroll.uk.payroll_processing.entity.TaxThreshold;
import com.payroll.uk.payroll_processing.service.TaxThresholdService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/tax-thresholds")
public class TaxThresholdController {

    private final TaxThresholdService taxThresholdService;

    public TaxThresholdController(TaxThresholdService taxThresholdService) {
        this.taxThresholdService = taxThresholdService;
    }

    @GetMapping
    public ResponseEntity<List<TaxThreshold>> getThresholdsByYearAndRegion(
            @RequestParam("taxYear") String taxYear,
            @RequestParam("region") TaxThreshold.TaxRegion region) {

        List<TaxThreshold> thresholds = taxThresholdService.getCombinedThresholds(taxYear, region);
        return ResponseEntity.ok(thresholds);
    }
}
