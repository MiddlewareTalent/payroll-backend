package com.payroll.uk.payroll_processing.service.employee;

import com.payroll.uk.payroll_processing.dto.employeedto.EmployeeDetailsDTO;
import com.payroll.uk.payroll_processing.dto.mapper.employeedtomapper.EmployeeDetailsDTOMapper;
import com.payroll.uk.payroll_processing.entity.employee.EmployeeDetails;
import com.payroll.uk.payroll_processing.repository.BankDetailsRepository;
import com.payroll.uk.payroll_processing.repository.EmployeeDetailsRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class EmployeeDetailsService {
    private final EmployeeDetailsRepository employeeDetailsRepository;
    private  final EmployeeDetailsDTOMapper employeeDetailsDTOMapper;
    private  final BankDetailsRepository bankDetailsRepository;

    public EmployeeDetailsService(EmployeeDetailsRepository employeeDetailsRepository,
                                  EmployeeDetailsDTOMapper employeeDetailsDTOMapper,BankDetailsRepository bankDetailsRepository)  {
        this.employeeDetailsRepository = employeeDetailsRepository;
        this.employeeDetailsDTOMapper = employeeDetailsDTOMapper;
        this.bankDetailsRepository = bankDetailsRepository;
    }
    // Save employee details
    public EmployeeDetailsDTO saveEmployeeDetails(EmployeeDetailsDTO employeeDetailsDTO){
        if (employeeDetailsDTO.getEmployeeId()==null || employeeDetailsDTO.getEmployeeId().isEmpty()) {
            throw new IllegalArgumentException("Employee ID cannot be null or empty");
        }
        if(employeeDetailsRepository.existsByEmail(employeeDetailsDTO.getEmail())){
            throw new IllegalArgumentException("Employee with this email already exists");
        }
        if(employeeDetailsRepository.findByEmployeeId(employeeDetailsDTO.getEmployeeId()).isPresent()){
            throw new IllegalArgumentException("Employee with this ID already exists");
        }
        validateEmployeeDetails(employeeDetailsDTO);
        EmployeeDetails employeeData = employeeDetailsDTOMapper.mapToEmployeeDetails(employeeDetailsDTO);
//        bankDetailsRepository.save(employeeData.getBankDetails());
        EmployeeDetails savedEmployeeDetails = employeeDetailsRepository.save(employeeData);

        return employeeDetailsDTOMapper.mapToEmployeeDetailsDTO(savedEmployeeDetails);
    }

    // Get employee details by employee ID
    public EmployeeDetailsDTO getEmployeeDetailsByEmployeeId(String employeeId){
        if (employeeId == null || employeeId.isEmpty()) {
            throw new IllegalArgumentException("Employee ID cannot be null or empty");
        }
        EmployeeDetails employeeDetails = employeeDetailsRepository.findByEmployeeId(employeeId)
                .orElseThrow(() -> new IllegalArgumentException("Employee not found with ID: " + employeeId));
        return employeeDetailsDTOMapper.mapToEmployeeDetailsDTO(employeeDetails);
    }
    public List<EmployeeDetailsDTO> getAllEmployeeDetails() {
        List<EmployeeDetails> employeeDetailsList = employeeDetailsRepository.findAll();
        List<EmployeeDetailsDTO> employeeDetailsListedData = employeeDetailsList.stream().map(employeeDetailsDTOMapper::mapToEmployeeDetailsDTO).toList();
        if(employeeDetailsListedData.isEmpty()) {
            throw new IllegalArgumentException("No employee details found");
        }
        return employeeDetailsListedData;
    }
// Get employee details by email
    public EmployeeDetailsDTO getEmployeeDetailsByEmail(String email){
        if (email == null || email.isEmpty()) {
            throw new IllegalArgumentException("Email cannot be null or empty");
        }
        EmployeeDetails employeeDetails = employeeDetailsRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Employee not found with email: " + email));
        return employeeDetailsDTOMapper.mapToEmployeeDetailsDTO(employeeDetails);
    }
    // Update employee details by ID
    public EmployeeDetailsDTO updateEmployeeDetailsById(Long id,EmployeeDetailsDTO employeeDetailsDTO){
        if (employeeDetailsDTO.getEmployeeId() == null || employeeDetailsDTO.getEmployeeId().isEmpty()) {
            throw new IllegalArgumentException("Employee ID or Id cannot be null or empty");
        }

        EmployeeDetails existingEmployeeDetails = employeeDetailsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Employee not found with ID: " + employeeDetailsDTO.getEmployeeId()));

        EmployeeDetails updatedEmployeeDetails = employeeDetailsDTOMapper.mapToEmployeeDetails(employeeDetailsDTO);
        updatedEmployeeDetails.getBankDetails().setId(existingEmployeeDetails.getBankDetails().getId());
        updatedEmployeeDetails.setId(existingEmployeeDetails.getId()); // Preserve the existing ID
        EmployeeDetails savedEmployeeDetails = employeeDetailsRepository.save(updatedEmployeeDetails);
        return employeeDetailsDTOMapper.mapToEmployeeDetailsDTO(savedEmployeeDetails);
    }

    //Update employee details by employee ID
    public EmployeeDetailsDTO updateEmployeeDetailsByEmployeeId(EmployeeDetailsDTO employeeDetailsDTO) {
        if (employeeDetailsDTO.getEmployeeId() == null || employeeDetailsDTO.getEmployeeId().isEmpty()) {
            throw new IllegalArgumentException("Employee ID cannot be null or empty");
        }
        EmployeeDetails existingEmployeeDetails = employeeDetailsRepository.findByEmployeeId(employeeDetailsDTO.getEmployeeId())
                .orElseThrow(() -> new IllegalArgumentException("Employee not found with ID: " + employeeDetailsDTO.getEmployeeId()));

        EmployeeDetails updatedEmployeeDetails = employeeDetailsDTOMapper.mapToEmployeeDetails(employeeDetailsDTO);
//        updatedEmployeeDetails.setId(existingEmployeeDetails.getId()); // Preserve the existing ID
        EmployeeDetails savedEmployeeDetails = employeeDetailsRepository.save(updatedEmployeeDetails);
        return employeeDetailsDTOMapper.mapToEmployeeDetailsDTO(savedEmployeeDetails);
    }

    // Delete employee details by ID
    public Boolean deleteEmployeeDetailsById(Long id){
        if (id == null) {
            throw new IllegalArgumentException("Employee ID cannot be null");
        }

        EmployeeDetails employeeDetails = employeeDetailsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Employee not found with ID: " + id));
        employeeDetailsRepository.delete(employeeDetails);
        return true;
    }
    // Delete employee details by employee ID
    public Boolean deleteEmployeeDetailsByEmployeeId(String employeeId) {
        if (employeeId == null || employeeId.isEmpty()) {
            throw new IllegalArgumentException("Employee ID cannot be null or empty");
        }
        EmployeeDetails employeeDetails = employeeDetailsRepository.findByEmployeeId(employeeId)
                .orElseThrow(() -> new IllegalArgumentException("Employee not found with ID: " + employeeId));
        employeeDetailsRepository.delete(employeeDetails);
        return true;
    }
    private void validateEmployeeDetails(EmployeeDetailsDTO employeeDetailsDTO) {
        // Name validations
        if (employeeDetailsDTO.getFirstName() == null || employeeDetailsDTO.getFirstName().trim().isEmpty()) {
            throw new IllegalArgumentException("First name is required");
        }
        if (employeeDetailsDTO.getLastName() == null || employeeDetailsDTO.getLastName().trim().isEmpty()) {
            throw new IllegalArgumentException("Last name is required");
        }

        // Email validation
        if (employeeDetailsDTO.getEmail() == null || employeeDetailsDTO.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Email is required");
        }
        if (!employeeDetailsDTO.getEmail().matches("^[\\w.-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {
            throw new IllegalArgumentException("Email must be valid");
        }

        // Date validations
        if (employeeDetailsDTO.getDateOfBirth() == null) {
            throw new IllegalArgumentException("Date of birth is required");
        }
        if (employeeDetailsDTO.getDateOfBirth().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Date of birth cannot be in the future");
        }

        // Employment dates validation
        if (employeeDetailsDTO.getEmploymentStartedDate() == null) {
            throw new IllegalArgumentException("Employment start date is required");
        }
        if (employeeDetailsDTO.getEmploymentEndDate() != null &&
                employeeDetailsDTO.getEmploymentEndDate().isBefore(employeeDetailsDTO.getEmploymentStartedDate())) {
            throw new IllegalArgumentException("Employment end date cannot be before start date");
        }

        // Employee ID validation
        if (employeeDetailsDTO.getEmployeeId() == null || employeeDetailsDTO.getEmployeeId().trim().isEmpty()) {
            throw new IllegalArgumentException("Employee ID is required");
        }

        // National Insurance validation
        if (employeeDetailsDTO.getNationalInsuranceNumber() != null &&
                !employeeDetailsDTO.getNationalInsuranceNumber().matches("^[A-Z]{2}[0-9]{6}[A-Z]$")) {
            throw new IllegalArgumentException("National Insurance number must be in format AB123456C");
        }

        // Financial validations
        if (employeeDetailsDTO.getGrossIncome() == null) {
            throw new IllegalArgumentException("Gross income is required");
        }
        if (employeeDetailsDTO.getGrossIncome().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Gross income cannot be negative");
        }

        // Address validations
        if (employeeDetailsDTO.getAddress() == null || employeeDetailsDTO.getAddress().trim().isEmpty()) {
            throw new IllegalArgumentException("Address is required");
        }
        if (employeeDetailsDTO.getPostCode() == null || employeeDetailsDTO.getPostCode().trim().isEmpty()) {
            throw new IllegalArgumentException("Post code is required");
        }

        // Enum validations
        if (employeeDetailsDTO.getEmploymentType() == null) {
            throw new IllegalArgumentException("Employment type is required");
        }
        if (employeeDetailsDTO.getGender() == null) {
            throw new IllegalArgumentException("Gender is required");
        }
        if (employeeDetailsDTO.getEmployeeDepartment() == null) {
            throw new IllegalArgumentException("Department is required");
        }
        if (employeeDetailsDTO.getPayPeriod() == null) {
            throw new IllegalArgumentException("Pay period is required");
        }

        // Bank details validation
        if (employeeDetailsDTO.getBankDetailsDTO().getAccountNumber() == null || employeeDetailsDTO.getBankDetailsDTO().getAccountNumber().trim().isEmpty()) {
            throw new IllegalArgumentException("Account Number are required");
        }
        if (employeeDetailsDTO.getBankDetailsDTO().getAccountName()==null || employeeDetailsDTO.getBankDetailsDTO().getAccountName().trim().isEmpty()) {
            throw new IllegalArgumentException("Account Name is required");
        }
        if (employeeDetailsDTO.getBankDetailsDTO().getSortCode() == null || employeeDetailsDTO.getBankDetailsDTO().getSortCode().trim().isEmpty()) {
            throw new IllegalArgumentException("Sort Code is required");
        }
        if (employeeDetailsDTO.getBankDetailsDTO().getBankName() == null || employeeDetailsDTO.getBankDetailsDTO().getBankName().trim().isEmpty()) {
            throw new IllegalArgumentException("Bank Name is required");
        }

    }




}


