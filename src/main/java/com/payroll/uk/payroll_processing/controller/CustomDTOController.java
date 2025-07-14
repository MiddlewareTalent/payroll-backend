package com.payroll.uk.payroll_processing.controller;

import com.payroll.uk.payroll_processing.dto.customdto.EmployeesSummaryInEmployerDTO;
import com.payroll.uk.payroll_processing.dto.customdto.EmployerDashBoardDetailsDTO;
import com.payroll.uk.payroll_processing.service.CustomDTOService;
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
    @GetMapping("/employer-dashboard-details/{employerId}")
    public EmployerDashBoardDetailsDTO getEmployerDashboardDetailsData(@PathVariable String employerId) {
        return customDTOService.createDataForDashboard(employerId);
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
}
