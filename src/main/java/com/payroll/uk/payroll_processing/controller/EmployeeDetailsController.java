package com.payroll.uk.payroll_processing.controller;

import com.payroll.uk.payroll_processing.dto.BankDetailsDTO;
import com.payroll.uk.payroll_processing.dto.employeedto.EmployeeDetailsDTO;
import com.payroll.uk.payroll_processing.entity.ChangeField;
import com.payroll.uk.payroll_processing.entity.employee.EmployeeDetails;
import com.payroll.uk.payroll_processing.entity.employmentHistory.EmploymentHistory;
import com.payroll.uk.payroll_processing.repository.EmployeeDetailsRepository;
import com.payroll.uk.payroll_processing.service.FileStorageService;
import com.payroll.uk.payroll_processing.service.employee.EmployeeDetailsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/employee-details")
public class EmployeeDetailsController {
    private static final Logger logging= LoggerFactory.getLogger("EmployeeDetailsController.class");
    @Autowired
    private EmployeeDetailsRepository employeeDetailsRepository;

    @Autowired
    private FileStorageService fileStorageService;

    private final EmployeeDetailsService employeeDetailsService;
    public EmployeeDetailsController(EmployeeDetailsService employeeDetailsService) {
        this.employeeDetailsService = employeeDetailsService;
    }
    @PostMapping("/create")
    public ResponseEntity<?> createEmployeeDetails(@RequestBody EmployeeDetailsDTO employeeDetailsDTO) {
        logging.info("Creating employee Data: {}", employeeDetailsDTO);
        EmployeeDetailsDTO createdEmployee = employeeDetailsService.saveEmployeeDetails(employeeDetailsDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdEmployee);
    }


    @PostMapping("/upload-documents")
    public Map<String,String> uploadDocuments(
                                             @RequestParam(value = "p45Document", required = false) MultipartFile p45File,
                                             @RequestParam(value = "starterChecklist", required = false) MultipartFile starterChecklistFile) throws IOException {
        logging.info("Uploading documents: P45 - {}, Starter Checklist - {}", p45File != null ? p45File.getOriginalFilename() : "No File", starterChecklistFile != null ? starterChecklistFile.getOriginalFilename() : "No File");
            return fileStorageService.storeEmployeeDocuments(p45File, starterChecklistFile);
//            return ResponseEntity.ok("Documents uploaded successfully");

    }
    @GetMapping("/allEmployees")
    public ResponseEntity<List<EmployeeDetailsDTO>> getAllEmployeeDetails() {
        return ResponseEntity.ok(employeeDetailsService.getAllEmployeeDetails());

    }

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<?> getEmployeeDataByEmployeeId(@PathVariable String employeeId) {
            EmployeeDetailsDTO employeeDetails = employeeDetailsService.getEmployeeDetailsByEmployeeId(employeeId);
            return ResponseEntity.ok(employeeDetails); // 200 OK

    }

    @GetMapping("/employee/email/{email}")
    public ResponseEntity<EmployeeDetailsDTO> getEmployeeDataByEmail(@PathVariable String email) {
        EmployeeDetailsDTO employeeDetails = employeeDetailsService.getEmployeeDetailsByEmail(email);
            return ResponseEntity.ok(employeeDetails);
    }


    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateEmployeeDetails(
            @PathVariable Long id,
             @RequestBody EmployeeDetailsDTO employeeDetailsDTO) {

            EmployeeDetailsDTO updatedEmployeeDetails = employeeDetailsService.updateEmployeeDetailsById(id, employeeDetailsDTO);
            return ResponseEntity.ok(updatedEmployeeDetails); // 200
    }

    @PutMapping("/update/bank-details/{id}")
    public ResponseEntity<?> updateBankDetails(
            @PathVariable Long id,
            @RequestBody BankDetailsDTO bankDetailsDTO) {
            BankDetailsDTO updatedBankDetails = employeeDetailsService.updateBankDetailsById(id, bankDetailsDTO);
            return ResponseEntity.ok(updatedBankDetails); // 200 OK

    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteEmployeeDetails(@PathVariable Long id) {

            employeeDetailsService.deleteEmployeeDetailsById(id);
            return ResponseEntity.noContent().build(); // 204 No Content

    }

    @GetMapping("/fetch/active-employees")
    public List<EmployeeDetailsDTO> getActiveEmployees(){

            return employeeDetailsService.getAllActiveEmployees();
    }
    @GetMapping("/fetch/inactive-employees")
    public List<EmployeeDetailsDTO> getInactiveEmployees(){

            return employeeDetailsService.getAllInActiveEmployees();
    }
    @GetMapping("/fetch/Ready-for-leave-employees")
    public List<EmployeeDetailsDTO> getReadyForLeaveEmployees(){
            return employeeDetailsService.getAllReadyForLeavingEmployees();

    }

    @GetMapping("/auto-enrollment/status/{annualIncome}/{dateOfBirth}/{taxYear}/{gender}")
    public boolean getAutoEnrollmentStatus(
            @PathVariable("annualIncome") BigDecimal annualIncome,
            @PathVariable("dateOfBirth") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateOfBirth,
            @PathVariable("taxYear") String taxYear,
            @PathVariable("gender") EmployeeDetails.Gender gender) {

        return employeeDetailsService.isEligibleForAutoEnrollment(annualIncome, dateOfBirth, taxYear, gender);
    }

    @GetMapping("/employee-history/{employeeId}")
    public ResponseEntity<List<EmploymentHistory>> getEmployeeChangeHistory(@PathVariable String employeeId) {
        List<EmploymentHistory> changeHistory = employeeDetailsService.getEmployeeChangeHistory(employeeId);
        return ResponseEntity.ok(changeHistory);
    }
    @GetMapping("/all/employment-history")
    public ResponseEntity<List<EmploymentHistory>> getAllEmploymentHistories() {
        List<EmploymentHistory> allHistories = employeeDetailsService.getAllEmployeeChangeHistory();
        return ResponseEntity.ok(allHistories);
    }


    @GetMapping("/employment-history/{employeeId}/field")
    public ResponseEntity<List<EmploymentHistory>> getEmployeeChangedData(
            @PathVariable String employeeId,
            @RequestParam ChangeField changeField) {

        List<EmploymentHistory> changes =
                employeeDetailsService.getEmployeeChangedData(employeeId, changeField);

        if (changes.isEmpty()) {
            return ResponseEntity.noContent().build(); // 204 if no changes
        }

        return ResponseEntity.ok(changes);
    }


    @GetMapping("/test/email/{email}")
    public Boolean checkEmailExist(@PathVariable("email") String email){
        Optional<EmployeeDetails> email_Id = employeeDetailsRepository.findByEmail(email);
        return email_Id.isPresent();
    }



}
