package com.payroll.uk.payroll_processing.controller;


import com.payroll.uk.payroll_processing.dto.PaySlipCreateDto;
import com.payroll.uk.payroll_processing.entity.TaxThreshold;
import com.payroll.uk.payroll_processing.exception.ResourceConflictException;
import com.payroll.uk.payroll_processing.service.TaxThresholdService;
import com.payroll.uk.payroll_processing.service.incometax.TaxCodeService;
import com.payroll.uk.payroll_processing.service.payslip.PaySlipGeneration;
import com.payroll.uk.payroll_processing.service.payslip.PaySlipService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/payslip")
public class PaySlipController {
    private static  final Logger logging= LoggerFactory.getLogger(PaySlipController.class);
    @Autowired
    private PaySlipService paySlipService;
    @Autowired
    private PaySlipGeneration paySlipGeneration;
    @Autowired
    private TaxCodeService taxCodeService;

    @Autowired
    private TaxThresholdService taxThresholdService;

    @PostMapping("/auto/{employeeId}")
    public ResponseEntity<PaySlipCreateDto> autoPaySlip(@PathVariable ("employeeId") String employeeId){
        logging.info("paySlipCreateDto: {} ",employeeId);
//        try{
            PaySlipCreateDto data = paySlipService.createPaySlip(employeeId);
            logging.info("Data: {}",data);
            return  ResponseEntity.ok(data);
//        }
//        catch (ResourceConflictException e){
//            return  ResponseEntity.status(HttpStatus.CONFLICT)
//                    .body(null);
//        }
//        catch (Exception e) {
//            return ResponseEntity.status(500).body(null);
//        }

    }

    @GetMapping("/all/payslips/{employeeId}")
    public ResponseEntity<List<PaySlipCreateDto>> getAllPayslipsByEmployeeId(@PathVariable String employeeId) {

            List<PaySlipCreateDto> data = paySlipService.getAllEmployeeId(employeeId);
            return ResponseEntity.ok(data);

    }

    @GetMapping("/reference/number/{paySlipReference}")
    public ResponseEntity<PaySlipCreateDto> getByReferenceNumber(@PathVariable ("paySlipReference") String paySlipReference){

            PaySlipCreateDto data = paySlipService.getPaySlipByReferences(paySlipReference);
           logging.info("Data: {}",data);
            return  ResponseEntity.ok(data);


    }
    @GetMapping("/all/total/payslips")
    public ResponseEntity<List<PaySlipCreateDto>> getAllPaySlips() {

            List<PaySlipCreateDto> data = paySlipService.getAllPaySlips();
            return ResponseEntity.ok(data);

    }


    @PostMapping("/calculate/income/tax")
    public BigDecimal incomeTaxCalculation(@RequestParam BigDecimal grossIncome,@RequestParam BigDecimal personalAllowanceGet,
                                           @RequestParam BigDecimal taxableIncomeGet, @RequestParam String taxYear,
                                           @RequestParam TaxThreshold.TaxRegion region, @RequestParam String taxCode, @RequestParam String payPeriod){
        return taxCodeService.calculateIncomeBasedOnTaxCode( grossIncome, personalAllowanceGet, taxableIncomeGet, taxYear, region, taxCode, payPeriod);

    }

    @GetMapping("/fetch/allowance/data/{taxYear}")
    public ResponseEntity<Map<String,BigDecimal>> fetchAllowanceData(@PathVariable("taxYear") String taxYear) {

            Map<String, BigDecimal> data = taxThresholdService.getAllowanceData(taxYear);
            return ResponseEntity.ok(data);

    }

    @GetMapping("/fetch/all/payslips/{periodEnd}")
    public ResponseEntity<List<PaySlipCreateDto>> getAllPaySlipsByPayPeriod(@PathVariable String periodEnd){
            List<PaySlipCreateDto> data = paySlipService.getAllPaySlipsByPeriod(periodEnd);
            return ResponseEntity.ok(data);

    }

    @GetMapping("/fetch/payslip/{employeeId}/{periodEnd}")
    public ResponseEntity<PaySlipCreateDto> getPaySlipByEmployeeIdAndPeriodEnd(@PathVariable String employeeId, @PathVariable String periodEnd) {

            PaySlipCreateDto data = paySlipService.getPaySlipByEmployeeIdAndPeriodEnd(employeeId, periodEnd);
            return ResponseEntity.ok(data);

    }

    @GetMapping("/payslips")
    public ResponseEntity<Page<PaySlipCreateDto>> getAllPayslips(
            @RequestParam String periodEnd,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "employeeId") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {

        Page<PaySlipCreateDto> result = paySlipService.getAllPaySlipsByPeriodData(periodEnd, page, size, sortBy, sortDir);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/test")
    public void test() {
        throw new ResourceConflictException("Test exception");
    }


}
