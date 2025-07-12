package com.payroll.uk.payroll_processing.controller;

import com.payroll.uk.payroll_processing.dto.BankDetailsDTO;
import com.payroll.uk.payroll_processing.dto.employeedto.EmployeeDetailsDTO;
import com.payroll.uk.payroll_processing.entity.employee.EmployeeDetails;
import com.payroll.uk.payroll_processing.repository.EmployeeDetailsRepository;
import com.payroll.uk.payroll_processing.service.FileStorageService;
import com.payroll.uk.payroll_processing.service.employee.EmployeeDetailsService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/employee-details")
public class EmployeeDetailsController {
    @Autowired
    private EmployeeDetailsRepository employeeDetailsRepository;

    @Autowired
    private FileStorageService fileStorageService;

    private final EmployeeDetailsService employeeDetailsService;
    public EmployeeDetailsController(EmployeeDetailsService employeeDetailsService) {
        this.employeeDetailsService = employeeDetailsService;
    }
    @PostMapping("/create")
    public ResponseEntity<?> createEmployeeDetails(
            @Valid @RequestBody EmployeeDetailsDTO employeeDetailsDTO,
            BindingResult bindingResult) {
        System.out.println("employeeDetailsDTO : "+employeeDetailsDTO);
        System.out.println("getOtherEmployeeDetailsDTO : "+employeeDetailsDTO.getOtherEmployeeDetailsDTO());

        // Handle validation errors (400 Bad Request)
        if (bindingResult.hasErrors()) {
            StringBuilder errors = new StringBuilder();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errors.append(error.getField())
                        .append(": ").append(error.getDefaultMessage()).append("; ");
            }
            return ResponseEntity.badRequest().body(errors.toString());
        }

        try {
            EmployeeDetailsDTO createdEmployeeDetails = employeeDetailsService.saveEmployeeDetails(employeeDetailsDTO);
            return new ResponseEntity<>(createdEmployeeDetails, HttpStatus.CREATED); // 201
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Related entity not found: " + ex.getMessage()); // 404
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Internal server error: " + e.getMessage()); // 500
        }
    }

    @PostMapping("/upload-documents")
    public Map<String,String> uploadDocuments(
                                             @RequestParam(value = "p45Document", required = false) MultipartFile p45File,
                                             @RequestParam(value = "starterChecklist", required = false) MultipartFile starterChecklistFile) {
        try {
            return fileStorageService.storeEmployeeDocuments(p45File, starterChecklistFile);
//            return ResponseEntity.ok("Documents uploaded successfully");
        } catch (Exception e) {
            throw  new RuntimeException("File is not exist");
        }
    }
    @GetMapping("/allEmployees")
    public ResponseEntity<?> getAllEmployeeDetails() {
        try {
            List<EmployeeDetailsDTO> employees = employeeDetailsService.getAllEmployeeDetails();
            return ResponseEntity.ok(employees); // 200 OK
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to fetch employee details: " + e.getMessage()); // 500
        }
    }

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<?> getEmployeeDataByEmployeeId(@PathVariable String employeeId) {
        try {
            EmployeeDetailsDTO employeeDetails = employeeDetailsService.getEmployeeDetailsByEmployeeId(employeeId);

            if (employeeDetails != null) {
                return ResponseEntity.ok(employeeDetails); // 200 OK
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Employee not found with ID: " + employeeId); // 404 Not Found
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Internal server error: " + e.getMessage()); // 500
        }
    }

    @GetMapping("/employee/email/{email}")
    public ResponseEntity<EmployeeDetailsDTO> getEmployeeDataByEmail(@PathVariable String email) {
        EmployeeDetailsDTO employeeDetails = employeeDetailsService.getEmployeeDetailsByEmail(email);
        if (employeeDetails != null) {
            return ResponseEntity.ok(employeeDetails);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }


    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateEmployeeDetails(
            @PathVariable Long id,
            @Valid @RequestBody EmployeeDetailsDTO employeeDetailsDTO,
            BindingResult bindingResult) {

        // Handle validation errors (400)
        if (bindingResult.hasErrors()) {
            StringBuilder errors = new StringBuilder();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errors.append(error.getField()).append(": ").append(error.getDefaultMessage()).append("; ");
            }
            return ResponseEntity.badRequest().body(errors.toString()); // 400
        }

        try {
            EmployeeDetailsDTO updatedEmployeeDetails = employeeDetailsService.updateEmployeeDetailsById(id, employeeDetailsDTO);
            return ResponseEntity.ok(updatedEmployeeDetails); // 200
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Employee not found with ID: " + id); // 404
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Internal Server Error: " + e.getMessage()); // 500
        }
    }

    @PutMapping("/update/bank-details/{id}")
    public ResponseEntity<?> updateBankDetails(
            @PathVariable Long id,
            @Valid @RequestBody BankDetailsDTO bankDetailsDTO,
            BindingResult bindingResult) {

        // Handle validation errors (400 Bad Request)
        if (bindingResult.hasErrors()) {
            StringBuilder errors = new StringBuilder();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errors.append(error.getField())
                        .append(": ").append(error.getDefaultMessage()).append("; ");
            }
            return ResponseEntity.badRequest().body(errors.toString());
        }

        try {
            BankDetailsDTO updatedBankDetails = employeeDetailsService.updateBankDetailsById(id, bankDetailsDTO);
            return ResponseEntity.ok(updatedBankDetails); // 200 OK
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Employee not found with ID: " + id); // 404 Not Found
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Internal server error: " + e.getMessage()); // 500 Internal Server Error
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteEmployeeDetails(@PathVariable Long id) {
        try {
            employeeDetailsService.deleteEmployeeDetailsById(id);
            return ResponseEntity.noContent().build(); // 204 No Content
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Employee not found with ID: " + id); // 404 Not Found
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Internal server error: " + e.getMessage()); // 500 Internal Server Error
        }
    }
    @GetMapping("/test/email/{email}")
    public Boolean checkEmailExist(@PathVariable("email") String email){
        Optional<EmployeeDetails> email_Id = employeeDetailsRepository.findByEmail(email);
        return email_Id.isPresent();
    }



}
