package com.payroll.uk.payroll_processing.controller;


import com.payroll.uk.payroll_processing.dto.PaySlipCreateDto;
import com.payroll.uk.payroll_processing.dto.employeedto.EmployeeDetailsDTO;
import com.payroll.uk.payroll_processing.service.payslip.PaySlipCreationService;
import com.payroll.uk.payroll_processing.service.payslip.AutoPaySlip;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/payslip")
public class PaySlipController {
    @Autowired
    private PaySlipCreationService paySlipCreationService;
    @Autowired
    private AutoPaySlip autoPaySlip;

    @PostMapping("/create")
    public ResponseEntity<PaySlipCreateDto> createPaySlip(@RequestBody PaySlipCreateDto paySlipCreateDto){
        System.out.println("paySlipCreateDto: "+paySlipCreateDto);
        try{
            PaySlipCreateDto data = paySlipCreationService.createPaySlip(paySlipCreateDto);
            System.out.println("Data:"+data);
            return  ResponseEntity.ok(data);
        }
        catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }

    }

    @PostMapping("/create/auto/{employeeId}")
    public ResponseEntity<PaySlipCreateDto> autoPaySlip(@PathVariable ("employeeId") String employeeId){
        System.out.println("paySlipCreateDto: "+employeeId);
        try{
            PaySlipCreateDto data = autoPaySlip.fillPaySlip(employeeId);
            System.out.println("Data:"+data);
            return  ResponseEntity.ok(data);
        }
        catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }

    }

    @GetMapping("/all/payslips/{employeeId}")
    public ResponseEntity<List<PaySlipCreateDto>> getAllPayslipsByEmployeeId(@PathVariable String employeeId) {
        try {
            List<PaySlipCreateDto> data = autoPaySlip.getAllEmployeeId(employeeId);
            return ResponseEntity.ok(data);
        } catch (Exception e) {
            // Optionally log the error
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/reference/number/{paySlipReference}")
    public ResponseEntity<PaySlipCreateDto> getByReferenceNumber(@PathVariable ("paySlipReference") String paySlipReference){
        System.out.println("paySlipCreateDto: "+paySlipReference);
        try{
            PaySlipCreateDto data = autoPaySlip.getPaySlipByReferences(paySlipReference);
            System.out.println("Data:"+data);
            return  ResponseEntity.ok(data);
        }
        catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }

    }


}
