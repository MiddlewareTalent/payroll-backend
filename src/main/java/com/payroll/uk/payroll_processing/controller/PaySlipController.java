package com.payroll.uk.payroll_processing.controller;


import com.payroll.uk.payroll_processing.dto.PaySlipCreateDto;
import com.payroll.uk.payroll_processing.service.PaySlipCreationService;
import com.payroll.uk.payroll_processing.service.payslip.AutoPaySlip;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<PaySlipCreateDto> autoPaySlip(@RequestParam ("employeeId") String employeeId){
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
}
