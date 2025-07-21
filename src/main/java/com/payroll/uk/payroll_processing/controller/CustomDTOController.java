package com.payroll.uk.payroll_processing.controller;

import com.payroll.uk.payroll_processing.dto.customdto.EmployeesSummaryInEmployerDTO;
import com.payroll.uk.payroll_processing.dto.customdto.EmployerDashBoardDetailsDTO;
import com.payroll.uk.payroll_processing.dto.customdto.P45DTO;
import com.payroll.uk.payroll_processing.service.CustomDTOService;
import com.payroll.uk.payroll_processing.service.P45Service;
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
    @GetMapping("/employer-dashboard-details")
    public EmployerDashBoardDetailsDTO getEmployerDashboardDetailsData() {
        return customDTOService.createDataForDashboard();
    }

    @GetMapping("all/employees-summary")
    public ResponseEntity<List<EmployeesSummaryInEmployerDTO>> getAllDataFromPaySlip(){
        try{
            List<EmployeesSummaryInEmployerDTO> data = customDTOService.getAllData();
            return ResponseEntity.ok(data);
        }
        catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    @GetMapping("/p45/employee-data/{employeeId}")
    public ResponseEntity<P45DTO> getP45DataForEmployee(@PathVariable String employeeId) {
        try {
            P45DTO p45Data = p45Service.generateP45File(employeeId);
            return ResponseEntity.ok(p45Data);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
