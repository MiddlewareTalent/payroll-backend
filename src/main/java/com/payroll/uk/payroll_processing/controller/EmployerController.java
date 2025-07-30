package com.payroll.uk.payroll_processing.controller;

import com.payroll.uk.payroll_processing.dto.employerdto.EmployerDetailsDTO;
import com.payroll.uk.payroll_processing.entity.employer.EmployerDetails;
import com.payroll.uk.payroll_processing.repository.EmployerDetailsRepository;
import com.payroll.uk.payroll_processing.service.EmployerService;
import com.payroll.uk.payroll_processing.service.FileStorageService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/employer") // Base URL for the API
public class EmployerController {

    @Autowired
    private EmployerDetailsRepository employerDetailsRepository;  // DTO for employer details
    private  final EmployerService employerService;

    @Autowired
    private  FileStorageService fileStorageService;

    public EmployerController(EmployerService employerService){

        this.employerService=employerService;
    }
    // Add methods to handle HTTP requests here
    // For example, to register an employer:
    @PostMapping("/register/employers")
    public ResponseEntity<String> registerEmployer(@RequestBody EmployerDetailsDTO employerDetailsDto) {

            String result = employerService.registerEmployer(employerDetailsDto);
            return ResponseEntity.ok(result);

    }

    @PostMapping("/company-logo/upload-documents")
    public Map<String,String> uploadDocuments(
            @RequestParam(value = "companyLogo", required = false) MultipartFile companyLogoFile) throws IOException {

            return fileStorageService.storeLogoDocument(companyLogoFile);
//            return ResponseEntity.ok("Documents uploaded successfully");

    }

    @GetMapping("/employers/{id}")
    public ResponseEntity<?> getEmployerDetails(@PathVariable Long id) {

            EmployerDetailsDTO employerDetails = employerService.getEmployerDetails(id);
            return ResponseEntity.ok(employerDetails);

    }
    @GetMapping("/allEmployers")
    public ResponseEntity<?> getAllEmployeeDetails() {

            List<EmployerDetailsDTO> employees = employerService.getAllEmployerDetails();
            return ResponseEntity.ok(employees); // 200 OK

    }
    @PutMapping("/update/employers/{id}")
    public ResponseEntity<EmployerDetailsDTO> updateEmployer(@PathVariable Long id, @Valid @RequestBody EmployerDetailsDTO employerDetailsDto) {

            EmployerDetailsDTO result = employerService.updateEmployerDetailsById(id,employerDetailsDto);
            return ResponseEntity.ok(result);
    }
    @DeleteMapping("/delete/employers/{id}")
    public ResponseEntity<String> deleteEmployer(@PathVariable Long id) {
            String result = employerService.deleteEmployer(id);
            return ResponseEntity.ok(result);

    }

    @GetMapping("/test/email/{employerEmail}")
    public Boolean checkEmailExist(@PathVariable String employerEmail) {
        System.out.println("Employer : " + employerEmail);
        Optional<EmployerDetails> email_Id = employerDetailsRepository.findByEmployerEmail(employerEmail);
        if (email_Id.isPresent()) {
            return true;
        }
        return false;
    }







}
