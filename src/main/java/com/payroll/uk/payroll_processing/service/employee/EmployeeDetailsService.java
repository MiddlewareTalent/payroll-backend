package com.payroll.uk.payroll_processing.service.employee;

import com.payroll.uk.payroll_processing.dto.BankDetailsDTO;
import com.payroll.uk.payroll_processing.dto.employeedto.EmployeeDetailsDTO;
import com.payroll.uk.payroll_processing.dto.mapper.EmployeeDetailsDTOMapper;
import com.payroll.uk.payroll_processing.entity.BankDetails;
import com.payroll.uk.payroll_processing.entity.TaxThreshold;
import com.payroll.uk.payroll_processing.entity.employee.EmployeeDetails;
import com.payroll.uk.payroll_processing.exception.*;
import com.payroll.uk.payroll_processing.repository.BankDetailsRepository;
import com.payroll.uk.payroll_processing.repository.EmployeeDetailsRepository;
import com.payroll.uk.payroll_processing.repository.EmployerDetailsRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class EmployeeDetailsService {
    private final EmployeeDetailsRepository employeeDetailsRepository;
    private  final EmployeeDetailsDTOMapper employeeDetailsDTOMapper;
    private  final BankDetailsRepository bankDetailsRepository;
    private final EmployerDetailsRepository employerDetailsRepository;

    public EmployeeDetailsService(EmployeeDetailsRepository employeeDetailsRepository,
                                  EmployeeDetailsDTOMapper employeeDetailsDTOMapper,BankDetailsRepository bankDetailsRepository, EmployerDetailsRepository employerDetailsRepository) {
        this.employeeDetailsRepository = employeeDetailsRepository;
        this.employeeDetailsDTOMapper = employeeDetailsDTOMapper;
        this.bankDetailsRepository = bankDetailsRepository;
        this.employerDetailsRepository = employerDetailsRepository;
    }
    // Save employee details
    public EmployeeDetailsDTO saveEmployeeDetails(EmployeeDetailsDTO employeeDetailsDTO){
        if (employeeDetailsDTO.getEmployeeId()==null || employeeDetailsDTO.getEmployeeId().isEmpty()) {
            throw new EmployeeNotFoundException("Employee ID cannot be null or empty");
        }
        if(employeeDetailsRepository.existsByEmail(employeeDetailsDTO.getEmail())){
            throw new EmployeeNotFoundException("Employee with this email already exists");
        }
        if(employerDetailsRepository.existsByEmployerEmail(employeeDetailsDTO.getEmail())){
            throw new EmployerRegistrationException("Employee email already exists as Employer email");
        }
        if(employeeDetailsRepository.findByEmployeeId(employeeDetailsDTO.getEmployeeId()).isPresent()){
            throw new EmployeeNotFoundException("Employee with this ID already exists");
        }
        if (employerDetailsRepository.findByEmployerId(employeeDetailsDTO.getEmployeeId()).isPresent()){
            throw new EmployerRegistrationException("Employee Id already exists as Employer ID");
        }
        if (!employerDetailsRepository.existsByEmployerId( employeeDetailsDTO.getEmployerId())) {
            throw new EmployerRegistrationException("Employer with this ID does not exist");
        }
        if (employeeDetailsRepository.existsByNationalInsuranceNumber(employeeDetailsDTO.getNationalInsuranceNumber())){
            throw new DuplicateNationalInsuranceException("Employee with this National Insurance Number already exists");
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
                .orElseThrow(() -> new EmployeeNotFoundException("Employee with ID " + employeeId + " not found"));
        return employeeDetailsDTOMapper.mapToEmployeeDetailsDTO(employeeDetails);
    }
    public List<EmployeeDetailsDTO> getAllEmployeeDetails() {
        List<EmployeeDetails> employeeDetailsList = employeeDetailsRepository.findAll();
        List<EmployeeDetailsDTO> employeeDetailsListedData = employeeDetailsList.stream().map(employeeDetailsDTOMapper::mapToEmployeeDetailsDTO).toList();
        if(employeeDetailsListedData.isEmpty()) {
            throw new  NoEmployeeDataFoundException("No employee details found for the given criteria.");
        }
        return employeeDetailsListedData;
    }
// Get employee details by email
    public EmployeeDetailsDTO getEmployeeDetailsByEmail(String email){
        if (email == null || email.isEmpty()) {
            throw new IllegalArgumentException("Email cannot be null or empty");
        }
        EmployeeDetails employeeDetails = employeeDetailsRepository.findByEmail(email)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee not found with email: " + email));
        return employeeDetailsDTOMapper.mapToEmployeeDetailsDTO(employeeDetails);
    }
    // Update employee details by ID
    public EmployeeDetailsDTO updateEmployeeDetailsById(Long id,EmployeeDetailsDTO employeeDetailsDTO){
        if (employeeDetailsDTO.getEmployeeId() == null || employeeDetailsDTO.getEmployeeId().isEmpty()) {
            throw new EmployeeNotFoundException("Employee ID or Id cannot be null or empty");
        }
        if (!employerDetailsRepository.existsByEmployerId(employeeDetailsDTO.getEmployerId())) {
            throw new EmployeeNotFoundException("Employer with this ID does not exist");
        }

        EmployeeDetails existingEmployeeDetails = employeeDetailsRepository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee not found with ID: " + employeeDetailsDTO.getEmployeeId()));

        EmployeeDetails updatedEmployeeDetails = employeeDetailsDTOMapper.mapToEmployeeDetails(employeeDetailsDTO);
        updatedEmployeeDetails.getBankDetails().setId(existingEmployeeDetails.getBankDetails().getId());
        updatedEmployeeDetails.setId(existingEmployeeDetails.getId()); // Preserve the existing ID
        EmployeeDetails savedEmployeeDetails = employeeDetailsRepository.save(updatedEmployeeDetails);
        return employeeDetailsDTOMapper.mapToEmployeeDetailsDTO(savedEmployeeDetails);
    }

    //Update employee details by employee ID
    public EmployeeDetailsDTO updateEmployeeDetailsByEmployeeId(EmployeeDetailsDTO employeeDetailsDTO) {
        if (employeeDetailsDTO.getEmployeeId() == null || employeeDetailsDTO.getEmployeeId().isEmpty()) {
            throw new EmployeeNotFoundException("Employee ID cannot be null or empty");
        }
        if (!employerDetailsRepository.existsByEmployerId(employeeDetailsDTO.getEmployerId())) {
            throw new IllegalArgumentException("Employer with this ID does not exist");
        }
        EmployeeDetails existingEmployeeDetails = employeeDetailsRepository.findByEmployeeId(employeeDetailsDTO.getEmployeeId())
                .orElseThrow(() -> new EmployeeNotFoundException("Employee not found with ID: " + employeeDetailsDTO.getEmployeeId()));

        EmployeeDetails updatedEmployeeDetails = employeeDetailsDTOMapper.mapToEmployeeDetails(employeeDetailsDTO);
//        updatedEmployeeDetails.setId(existingEmployeeDetails.getId()); // Preserve the existing ID
        EmployeeDetails savedEmployeeDetails = employeeDetailsRepository.save(updatedEmployeeDetails);
        return employeeDetailsDTOMapper.mapToEmployeeDetailsDTO(savedEmployeeDetails);
    }

    //Update employee Bank details by id
    public BankDetailsDTO updateBankDetailsById(Long id,BankDetailsDTO  bankDetailsDTO){
        if(id==null){
            throw new IllegalArgumentException("Bank details ID cannot be null");
        }
        if (bankDetailsDTO.getAccountNumber() == null || bankDetailsDTO.getAccountNumber().isEmpty()) {
            throw new IllegalArgumentException("Account Number cannot be null or empty");
        }
        if (bankDetailsDTO.getAccountName() == null || bankDetailsDTO.getAccountName().isEmpty()) {
            throw new IllegalArgumentException("Account Name cannot be null or empty");
        }
//        if (bankDetailsDTO.getSortCode() == null || bankDetailsDTO.getSortCode().isEmpty()) {
//            throw new IllegalArgumentException("Sort Code cannot be null or empty");
//        }
        BankDetails bankDetails = bankDetailsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Bank details not found with ID: " + id));
        bankDetails.setAccountNumber(bankDetailsDTO.getAccountNumber());
        bankDetails.setAccountName(bankDetailsDTO.getAccountName());
        bankDetails.setSortCode(bankDetailsDTO.getSortCode());
        bankDetails.setBankName(bankDetailsDTO.getBankName());
        bankDetails.setBankAddress(bankDetailsDTO.getBankAddress());
        bankDetails.setBankPostCode(bankDetailsDTO.getBankPostCode());
        bankDetails.setTelephone(bankDetailsDTO.getTelephone());
        bankDetails.setPaymentReference(bankDetailsDTO.getPaymentReference());
        bankDetails.setIsRTIReturnsIncluded(bankDetailsDTO.getIsRTIReturnsIncluded());
        bankDetails.setPaymentLeadDays(bankDetailsDTO.getPaymentLeadDays());
        bankDetails.setId(bankDetails.getId());
        return employeeDetailsDTOMapper.mapToBanKDetailsDTO(bankDetailsRepository.save(bankDetails));

    }

    public EmployeeDetailsDTO updateEmployeeOtherDetailsById(String employeeId){
       if(employeeId.isEmpty()|| employeeId == null) {
            throw new IllegalArgumentException("Employee ID cannot be null or empty");
       }
        EmployeeDetails existingEmployeeDetails = employeeDetailsRepository.findByEmployeeId(employeeId)
                .orElseThrow(() -> new IllegalArgumentException("Employee not found with ID: " + employeeId));
        return null;


    }

    // Delete employee details by ID
    public Boolean deleteEmployeeDetailsById(Long id){
        if (id == null) {
            throw new EmployeeNotFoundException("Id of Employee cannot be null");
        }

        EmployeeDetails employeeDetails = employeeDetailsRepository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee not found with ID: " + id));
        employeeDetailsRepository.delete(employeeDetails);
        return true;
    }
    // Delete employee details by employee ID
    public Boolean deleteEmployeeDetailsByEmployeeId(String employeeId) {
        if (employeeId == null || employeeId.isEmpty()) {
            throw new EmployeeNotFoundException("Employee ID cannot be null or empty");
        }
        EmployeeDetails employeeDetails = employeeDetailsRepository.findByEmployeeId(employeeId)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee Id not found with ID: " + employeeId));
        employeeDetailsRepository.delete(employeeDetails);
        return true;
    }


    private void validateEmployeeDetails(EmployeeDetailsDTO employeeDetailsDTO) {

        // Name validations
        if (employeeDetailsDTO.getFirstName() == null || employeeDetailsDTO.getFirstName().trim().isEmpty()) {
            throw new EmployeeValidationException("First name is required");
        }
        if (employeeDetailsDTO.getLastName() == null || employeeDetailsDTO.getLastName().trim().isEmpty()) {
            throw new EmployeeValidationException("Last name is required");
        }

        // Email validation
        if (employeeDetailsDTO.getEmail() == null || employeeDetailsDTO.getEmail().trim().isEmpty()) {
            throw new EmployeeValidationException("Email is required");
        }
        if (!employeeDetailsDTO.getEmail().matches("^[\\w.-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {
            throw new EmployeeValidationException("Email must be valid");
        }

        // Date validations
        if (employeeDetailsDTO.getDateOfBirth() == null) {
            throw new EmployeeValidationException("Date of birth is required");
        }
        if (employeeDetailsDTO.getDateOfBirth().isAfter(LocalDate.now())) {
            throw new EmployeeValidationException("Date of birth cannot be in the future");
        }

        // Employment dates validation
        if (employeeDetailsDTO.getEmploymentStartedDate() == null) {
            throw new EmployeeValidationException("Employment start date is required");
        }
        if (employeeDetailsDTO.getEmploymentEndDate() != null &&
                employeeDetailsDTO.getEmploymentEndDate().isBefore(employeeDetailsDTO.getEmploymentStartedDate())) {
            throw new EmployeeValidationException("Employment end date cannot be before start date");
        }

        // Employee ID validation
        if (employeeDetailsDTO.getEmployeeId() == null || employeeDetailsDTO.getEmployeeId().trim().isEmpty()) {
            throw new EmployeeValidationException("Employee ID is required");
        }

        // National Insurance validation
        if (employeeDetailsDTO.getNationalInsuranceNumber() != null &&
                !employeeDetailsDTO.getNationalInsuranceNumber().matches("^[A-Z]{2}[0-9]{6}[A-Z]$")) {
            throw new EmployeeValidationException("National Insurance number must be in format AB123456C");
        }

        if (employeeDetailsDTO.getNiLetter() == null) {
            throw new EmployeeValidationException("NI Category Letter is required");
        }

        // Financial validations
        if (employeeDetailsDTO.getAnnualIncomeOfEmployee() == null) {
            throw new EmployeeValidationException("Gross income is required");
        }
        if (employeeDetailsDTO.getAnnualIncomeOfEmployee().compareTo(BigDecimal.ZERO) < 0) {
            throw new EmployeeValidationException("Gross income cannot be negative");
        }

        // Address validations
        if (employeeDetailsDTO.getAddress() == null || employeeDetailsDTO.getAddress().trim().isEmpty()) {
            throw new EmployeeValidationException("Address is required");
        }
        if (employeeDetailsDTO.getPostCode() == null || employeeDetailsDTO.getPostCode().trim().isEmpty()) {
            throw new EmployeeValidationException("Post code is required");
        }

        // Enum validations
        if (employeeDetailsDTO.getEmploymentType() == null) {
            throw new EmployeeValidationException("Employment type is required");
        }
        if (employeeDetailsDTO.getGender() == null) {
            throw new EmployeeValidationException("Gender is required");
        }
        if (employeeDetailsDTO.getEmployeeDepartment() == null) {
            throw new EmployeeValidationException("Department is required");
        }
        if (employeeDetailsDTO.getPayPeriod() == null) {
            throw new EmployeeValidationException("Pay period is required");
        }

        // Bank details validation
        if (employeeDetailsDTO.getBankDetailsDTO() == null) {
            throw new EmployeeValidationException("Bank details are required");
        }

        if (employeeDetailsDTO.getBankDetailsDTO().getAccountNumber() == null || employeeDetailsDTO.getBankDetailsDTO().getAccountNumber().trim().isEmpty()) {
            throw new EmployeeValidationException("Account number is required");
        }
        if (employeeDetailsDTO.getBankDetailsDTO().getAccountName() == null || employeeDetailsDTO.getBankDetailsDTO().getAccountName().trim().isEmpty()) {
            throw new EmployeeValidationException("Account name is required");
        }
        if (employeeDetailsDTO.getBankDetailsDTO().getSortCode() == null || employeeDetailsDTO.getBankDetailsDTO().getSortCode().trim().isEmpty()) {
            throw new EmployeeValidationException("Sort code is required");
        }
        if (employeeDetailsDTO.getBankDetailsDTO().getBankName() == null || employeeDetailsDTO.getBankDetailsDTO().getBankName().trim().isEmpty()) {
            throw new EmployeeValidationException("Bank name is required");
        }
    }





}


