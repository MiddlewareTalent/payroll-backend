package com.payroll.uk.payroll_processing.controller;

import com.payroll.uk.payroll_processing.dto.customdto.EmployerDashBoardDetailsDTO;
import com.payroll.uk.payroll_processing.service.CustomDTOService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/custom-dto")
public class CustomDTOController {

    @Autowired
    private CustomDTOService customDTOService;
    @GetMapping("/employer-dashboard-details/{employerId}")
    public EmployerDashBoardDetailsDTO getEmployerDashboardDetailsData(String employerId) {
        return customDTOService.createDataForDashboard(employerId);
    }
}
