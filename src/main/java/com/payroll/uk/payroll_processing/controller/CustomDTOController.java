package com.payroll.uk.payroll_processing.controller;

import com.payroll.uk.payroll_processing.dto.customdto.EmployeesSummaryInEmployerDTO;
import com.payroll.uk.payroll_processing.dto.customdto.EmployerDashBoardDetailsDTO;
import com.payroll.uk.payroll_processing.dto.customdto.P45DTO;
import com.payroll.uk.payroll_processing.dto.customdto.P60DTO;
import com.payroll.uk.payroll_processing.service.CustomDTOService;
import com.payroll.uk.payroll_processing.service.P45Service;
import com.payroll.uk.payroll_processing.service.P60Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/custom-dto")
public class CustomDTOController {

    @Autowired
    private CustomDTOService customDTOService;
    @Autowired
    private P45Service p45Service;
    @Autowired
    private P60Service p60Service;
    @GetMapping("/employer-dashboard-details")
    public EmployerDashBoardDetailsDTO getEmployerDashboardDetailsData() {
        return customDTOService.createDataForDashboard();
    }

    @GetMapping("all/employees-summary")
    public ResponseEntity<List<EmployeesSummaryInEmployerDTO>> getAllDataFromPaySlip(){

            List<EmployeesSummaryInEmployerDTO> data = customDTOService.getAllData();
            return ResponseEntity.ok(data);

    }
    @GetMapping("/p45/employee-data/{employeeId}")
    public ResponseEntity<P45DTO> getP45DataForEmployee(@PathVariable String employeeId) {

            P45DTO p45Data = p45Service.generateP45File(employeeId);
            return ResponseEntity.ok(p45Data);

    }
    @GetMapping("/generate/p60/{employeeId}")
    public ResponseEntity<P60DTO> generateP60(@PathVariable String employeeId) {

        P60DTO p60DTO = p60Service.generateP60File(employeeId);
        return ResponseEntity.ok(p60DTO);
    }
}
