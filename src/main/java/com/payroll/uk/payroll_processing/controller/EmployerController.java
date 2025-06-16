package com.payroll.uk.payroll_processing.controller;

import com.payroll.uk.payroll_processing.dto.employerdto.EmployerDetailsDTO;
import com.payroll.uk.payroll_processing.exception.EmployerRegistrationException;
import com.payroll.uk.payroll_processing.service.EmployerService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/employer") // Base URL for the API
public class EmployerController {

    private  final EmployerService employerService;

    public EmployerController(EmployerService employerService){
        this.employerService=employerService;
    }
    // Add methods to handle HTTP requests here
    // For example, to register an employer:
    @PostMapping("/register/employers")
    public ResponseEntity<String> registerEmployer(@RequestBody EmployerDetailsDTO employerDetailsDto) {

        try {
            String result = employerService.registerEmployer(employerDetailsDto);
            return ResponseEntity.ok(result);
        } catch (EmployerRegistrationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Registration failed. Please try again.");
        }
    }

    @GetMapping("/employers/{id}")
    public ResponseEntity<?> getEmployerDetails(@RequestParam Long id) {
        try {
            EmployerDetailsDTO employerDetails = employerService.getEmployerDetails(id);
            return ResponseEntity.ok(employerDetails);
        } catch (EmployerRegistrationException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Failed to fetch employer details");
        }
    }
    @GetMapping("/allEmployers")
    public ResponseEntity<?> getAllEmployeeDetails() {
        try {
            List<EmployerDetailsDTO> employees = employerService.getAllEmployeeDetails();
            return ResponseEntity.ok(employees); // 200 OK
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to fetch employee details: " + e.getMessage()); // 500
        }
    }
    @PutMapping("/update/employers/{id}")
    public ResponseEntity<EmployerDetailsDTO> updateEmployer(@PathVariable Long id, @Valid @RequestBody EmployerDetailsDTO employerDetailsDto) {
        try {
            EmployerDetailsDTO result = employerService.updateEmployerDetailsById(id,employerDetailsDto);
            return ResponseEntity.ok(result);
        }
        catch (EmployerRegistrationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
//        catch (EmployerRegistrationException e) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body("Update failed. Please try again.");
//        }
    }
    @DeleteMapping("/delete/employers")
    public ResponseEntity<String> deleteEmployer(@RequestParam Long id) {
        try {
            String result = employerService.deleteEmployer(id);
            return ResponseEntity.ok(result);
        } catch (EmployerRegistrationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Deletion failed. Please try again.");
        }
    }






}
