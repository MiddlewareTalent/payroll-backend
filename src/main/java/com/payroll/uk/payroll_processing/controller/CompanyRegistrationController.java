package com.payroll.uk.payroll_processing.controller;

import com.payroll.uk.payroll_processing.dto.companydto.CompanyRegistrationDto;
import com.payroll.uk.payroll_processing.entity.PayPeriod;
import com.payroll.uk.payroll_processing.service.CompanyRegistrationService;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/company") // Base URL for the API
public class CompanyRegistrationController {

    private final CompanyRegistrationService companyRegistrationService;

    public CompanyRegistrationController(CompanyRegistrationService companyRegistrationService) {
        this.companyRegistrationService = companyRegistrationService;
    }

    @PostMapping("/register")
    public ResponseEntity<CompanyRegistrationDto> registerCompany(@Valid @RequestBody CompanyRegistrationDto companyRegistrationDto) {
        System.out.println("Received company registration request: " + companyRegistrationDto);
        try {

            CompanyRegistrationDto data = companyRegistrationService.registerCompany(companyRegistrationDto);
            return ResponseEntity.ok(data);
        }
//        catch (RegistrationNotFoundException t){
//            return ResponseEntity.status(404).body(null);
//        }
        catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }
    @GetMapping("/getCompany/{companyName}")
    public ResponseEntity<CompanyRegistrationDto> getCompanyByName(@RequestParam("companyName") String companyName) {
        try {
            CompanyRegistrationDto companyRegistrationDto = companyRegistrationService.getCompanyByName(companyName);
            return ResponseEntity.ok(companyRegistrationDto);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/allyear")
    public ResponseEntity<List<String>> getAllTaxYears() {
        try {
            List<String> taxYears = companyRegistrationService.getAllTaxYears();
            return ResponseEntity.ok(taxYears);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

}
