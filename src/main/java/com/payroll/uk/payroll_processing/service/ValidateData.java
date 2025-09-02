package com.payroll.uk.payroll_processing.service;

import com.payroll.uk.payroll_processing.dto.employeedto.EmployeeDetailsDTO;
import com.payroll.uk.payroll_processing.dto.employerdto.EmployerDetailsDTO;
import com.payroll.uk.payroll_processing.entity.employee.EmployeeDetails;
import com.payroll.uk.payroll_processing.entity.employer.EmployerDetails;
import com.payroll.uk.payroll_processing.exception.DataValidationException;
import com.payroll.uk.payroll_processing.exception.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;

@Component
public class ValidateData {
    private static final Logger logging= LoggerFactory.getLogger(ValidateData.class);
    public void validateCompanyDetails(EmployerDetailsDTO employerDetailsDto) {
        if (employerDetailsDto == null) {
            throw new DataValidationException("Employer details cannot be null");
        }
        if (employerDetailsDto.getCompanyDetailsDTO().getCompanyName() == null|| employerDetailsDto.getCompanyDetailsDTO().getCompanyName().isEmpty()) {
            throw new DataValidationException("Company name is required");
        }
        if (employerDetailsDto.getCompanyDetailsDTO().getCompanyAddress() == null || employerDetailsDto.getCompanyDetailsDTO().getCompanyAddress().isEmpty()) {
            throw new DataValidationException("company address is required");
        }
        if (employerDetailsDto.getCompanyDetailsDTO().getCompanyPostCode()==null || employerDetailsDto.getCompanyDetailsDTO().getCompanyPostCode().isEmpty()) {
            throw new DataValidationException("Company post code is required");
        }
        if (employerDetailsDto.getCompanyDetailsDTO().getCurrentTaxYear() == null || employerDetailsDto.getCompanyDetailsDTO().getCurrentTaxYear().isEmpty()) {
            throw new DataValidationException("current tax year is required");
        }
        if (!employerDetailsDto.getCompanyDetailsDTO().getCurrentTaxYear().matches("^\\d{4}-\\d{4}$")) {
            throw new DataValidationException("Current tax year must be in the format YYYY-YYYY, e.g., 2025-2026");
        }
        if (employerDetailsDto.getCompanyDetailsDTO().getCurrentPayPeriod()==null){
            throw new DataValidationException("Pay period is required");
        }
        if (employerDetailsDto.getCompanyDetailsDTO().getRegion()==null ) {
            throw new DataValidationException("Region is required");
        }
        if (employerDetailsDto.getCompanyDetailsDTO().getPayDate()==null) {
            throw new DataValidationException("Pay date is required");
        }
    }

    public void validateTaxOfficeDetails(EmployerDetailsDTO employerDetailsDto) {


        if (employerDetailsDto.getTaxOfficeDTO().getDatePAYESchemeStarted() == null) {
            throw new DataValidationException("PAYE scheme start is required");
        }
        if (employerDetailsDto.getTaxOfficeDTO().getPayeReference()==null|| employerDetailsDto.getTaxOfficeDTO().getPayeReference().isEmpty()) {
            throw new DataValidationException("PAYE reference is required");
        }
        if (!employerDetailsDto.getTaxOfficeDTO().getPayeReference().matches("^\\d{3}/[A-Za-z0-9]{1,10}$")){
            throw new DataValidationException("Enter the employer PAYE reference is not correct format");
        }
        if (employerDetailsDto.getTaxOfficeDTO().getPayrollGivingRef()==null || employerDetailsDto.getTaxOfficeDTO().getPayrollGivingRef().isEmpty()){
            throw new DataValidationException("Payroll Giving reference is required");
        }

        if (employerDetailsDto.getTaxOfficeDTO().getAccountsOfficeReference()==null|| employerDetailsDto.getTaxOfficeDTO().getAccountsOfficeReference().isEmpty()) {
            throw new DataValidationException("Account Office reference is required");
        }
        if (!employerDetailsDto.getTaxOfficeDTO().getAccountsOfficeReference().matches("^\\d{3}P[A-Z](\\d{8}|\\d{7}X)$")){
            throw new DataValidationException("Enter the employer account office reference is not correct format");
        }
    }

    public void validateTermsDetails(EmployerDetailsDTO employerDetailsDto) {
        if (employerDetailsDto.getTermsDTO().getFemaleRetirementAge()<=0){
            throw new DataValidationException("female retirement age is required ");
        }
        if (employerDetailsDto.getTermsDTO().getMaleRetirementAge()<=0) {
            throw new DataValidationException("Male retirement age is required ");
        }
    }

    public void validateEmployerDetails(EmployerDetailsDTO employerDetailsDto) {
        if (employerDetailsDto.getCompanyDetailsDTO()==null){
            throw new ResourceNotFoundException("Company Details not found");
        }
        else{
            validateCompanyDetails(employerDetailsDto);
        }
        if (employerDetailsDto.getTaxOfficeDTO()==null){
            throw new ResourceNotFoundException("Tax office details not found");
        }
        else{
            validateTaxOfficeDetails(employerDetailsDto);
        }
        if (employerDetailsDto.getTermsDTO()==null){
            throw new ResourceNotFoundException("Terms details not found");
        }
        else{
            validateTermsDetails(employerDetailsDto);
        }
        if (employerDetailsDto.getBankDetailsDTO()==null){
            throw new ResourceNotFoundException("Bank details not found");
        }
        else{
            validateBankDetails(employerDetailsDto);
        }

        if (employerDetailsDto.getEmployerId()==null || employerDetailsDto.getEmployerId().isEmpty()) {
            throw new DataValidationException("Employer ID is required");
        }
        if (employerDetailsDto.getEmployerName()==null|| employerDetailsDto.getEmployerName().isEmpty()) {
            throw new DataValidationException("Employer name is required");
        }
    }

    public void validateBankDetails(EmployerDetailsDTO employerDetailsDto) {
        if (employerDetailsDto.getBankDetailsDTO().getAccountName() == null || employerDetailsDto.getBankDetailsDTO().getAccountName().isEmpty()) {
            throw new DataValidationException("Account name is required");
        }
        if (employerDetailsDto.getBankDetailsDTO().getSortCode() == null || employerDetailsDto.getBankDetailsDTO().getSortCode().isEmpty()) {
            throw new DataValidationException("Sort code is required");
        }
        if (!employerDetailsDto.getBankDetailsDTO().getSortCode().matches("^\\d{2}-\\d{2}-\\d{2}$")) {
            throw new DataValidationException("Sort code must be in the format XX-XX-XX");
        }
        if (employerDetailsDto.getBankDetailsDTO().getAccountNumber() == null || employerDetailsDto.getBankDetailsDTO().getAccountNumber().isEmpty()) {
            throw new DataValidationException("Account number is required");
        }
        if (!employerDetailsDto.getBankDetailsDTO().getAccountNumber().matches("^\\d{8}$")) {
            throw new DataValidationException("Account number must be 8 digits");
        }
    }

    public void validateEmployeeDetailsData(EmployeeDetailsDTO employeeDetailsDTO) {


        // Employee ID validation
        if (employeeDetailsDTO.getEmployeeId() == null || employeeDetailsDTO.getEmployeeId().trim().isEmpty()) {
            throw new DataValidationException("Employee ID is required");
        }

        // Name validations
        if (employeeDetailsDTO.getFirstName() == null || employeeDetailsDTO.getFirstName().trim().isEmpty()) {
            throw new DataValidationException("First name is required");
        }
        if (employeeDetailsDTO.getLastName() == null || employeeDetailsDTO.getLastName().trim().isEmpty()) {
            throw new DataValidationException("Last name is required");
        }
        // Address validations
        /*if (employeeDetailsDTO.getAddress() == null || employeeDetailsDTO.getAddress().trim().isEmpty()) {
            throw new DataValidationException("Address is required");
        }
        if (employeeDetailsDTO.getPostCode() == null || employeeDetailsDTO.getPostCode().trim().isEmpty()) {
            throw new DataValidationException("Post code is required");
        }*/
        //Employee Address
        if (employeeDetailsDTO.getEmployeeAddressDTO() == null) {
            throw new DataValidationException("Address is required");
        }

        // Email validation
        if (employeeDetailsDTO.getEmail() == null || employeeDetailsDTO.getEmail().trim().isEmpty()) {
            throw new DataValidationException("Email is required");
        }
        if (!employeeDetailsDTO.getEmail().matches("^[\\w.-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {
            throw new DataValidationException("Email must be valid");
        }

        // Date validations
        if (employeeDetailsDTO.getDateOfBirth() == null) {
            throw new DataValidationException("Date of birth is required");
        }
        if (employeeDetailsDTO.getDateOfBirth().isAfter(LocalDate.now())) {
            throw new DataValidationException("Date of birth cannot be in the future");
        }

        // Employment dates validation
        if (employeeDetailsDTO.getEmploymentStartedDate() == null) {
            throw new DataValidationException("Employment start date is required");
        }
        if (employeeDetailsDTO.getEmploymentEndDate() != null &&
                employeeDetailsDTO.getEmploymentEndDate().isBefore(employeeDetailsDTO.getEmploymentStartedDate())) {
            throw new DataValidationException("Employment end date cannot be before start date");
        }


        // National Insurance validation
        if (employeeDetailsDTO.getNationalInsuranceNumber() != null &&
                !employeeDetailsDTO.getNationalInsuranceNumber().matches("^[A-Z]{2}[0-9]{6}[A-Z]$")) {
            throw new DataValidationException("National Insurance number must be in format AB123456C");
        }

        if (employeeDetailsDTO.getNiLetter() == null) {
            throw new DataValidationException("NI Category Letter is required");
        }

        // Financial validations
        if (employeeDetailsDTO.getAnnualIncomeOfEmployee() == null) {
            throw new DataValidationException("Gross income is required");
        }
        if (employeeDetailsDTO.getAnnualIncomeOfEmployee().compareTo(BigDecimal.ZERO) < 0) {
            throw new DataValidationException("Gross income cannot be negative");
        }
        if (employeeDetailsDTO.getTaxCode()==null || employeeDetailsDTO.getTaxCode().isEmpty()) {
            throw new DataValidationException("Tax code is required");
        }
        if (employeeDetailsDTO.getWorkingCompanyName()==null || employeeDetailsDTO.getWorkingCompanyName().isEmpty()) {
            throw new DataValidationException("Working company name is required");
        }


        // Enum validations
        if (employeeDetailsDTO.getEmploymentType() == null) {
            throw new DataValidationException("Employment type is required");
        }
        if (employeeDetailsDTO.getGender() == null) {
            throw new DataValidationException("Gender is required");
        }
        if (employeeDetailsDTO.getEmployeeDepartment() == null) {
            throw new DataValidationException("Department is required");
        }
        if (employeeDetailsDTO.getPayPeriod() == null) {
            throw new DataValidationException("Pay period is required");
        }
        if (employeeDetailsDTO.getBankDetailsDTO()==null) {
            throw new DataValidationException("Bank details are required");
        } else {
            validateBankDetails(employeeDetailsDTO);
        }

        if (employeeDetailsDTO.isHasP45DocumentSubmitted()){
            validatePreviousEmploymentData(employeeDetailsDTO);
        }
//        else {
//            if (employeeDetailsDTO.getPreviousEmploymentDataDTO() != null) {
//                throw new DataValidationException("Previous employment data should not be provided if P45 document is not submitted");
//            }
//        }


    }

    public void validateBankDetails(EmployeeDetailsDTO employeeDetailsDto) {
        if (employeeDetailsDto.getBankDetailsDTO().getAccountName() == null || employeeDetailsDto.getBankDetailsDTO().getAccountName().isEmpty()) {
            throw new DataValidationException("Account name is required");
        }
        if (employeeDetailsDto.getBankDetailsDTO().getSortCode() == null || employeeDetailsDto.getBankDetailsDTO().getSortCode().isEmpty()) {
            throw new DataValidationException("Sort code is required");
        }
        if (!employeeDetailsDto.getBankDetailsDTO().getSortCode().matches("^\\d{2}-\\d{2}-\\d{2}$")) {
            throw new DataValidationException("Sort code must be in the format XX-XX-XX");
        }
        if (employeeDetailsDto.getBankDetailsDTO().getAccountNumber() == null || employeeDetailsDto.getBankDetailsDTO().getAccountNumber().isEmpty()) {
            throw new DataValidationException("Account number is required");
        }
        if (!employeeDetailsDto.getBankDetailsDTO().getAccountNumber().matches("^\\d{8}$")) {
            throw new DataValidationException("Account number must be 8 digits");
        }
    }

    public void validatePreviousEmploymentData(EmployeeDetailsDTO employeeDetailsDto) {
        if (employeeDetailsDto.getPreviousEmploymentDataDTO() == null) {
            throw new DataValidationException("Previous employment data is required");
        }
        if (employeeDetailsDto.getPreviousEmploymentDataDTO().getPreviousTotalTaxToDate() == null ) {
            throw new DataValidationException("Previous total tax to date  is required");
        }
        if (employeeDetailsDto.getPreviousEmploymentDataDTO().getPreviousEmploymentEndDate() == null) {
            throw new DataValidationException("Previous employment end date is required");
        }
        if (employeeDetailsDto.getPreviousEmploymentDataDTO().getPreviousTaxCode() == null || employeeDetailsDto.getPreviousEmploymentDataDTO().getPreviousTaxCode().isEmpty()) {
            throw new DataValidationException("Previous tax code is required");
        }
        if (employeeDetailsDto.getPreviousEmploymentDataDTO().getPreviousTotalPayToDate() == null) {
            throw new DataValidationException("Previous total pay to date  is required");
        }
    }


    public void validateEmployeeDetails(EmployeeDetails employeeDetails) {

        logging.info("Checking employee details for validation: {}", employeeDetails);
        // Name validations
        if (employeeDetails.getFirstName() == null || employeeDetails.getFirstName().trim().isEmpty()) {
            throw new DataValidationException("First name is not found");
        }
        if (employeeDetails.getLastName() == null || employeeDetails.getLastName().trim().isEmpty()) {
            throw new DataValidationException("Last name is not found");
        }
       /* if (employeeDetails.getAddress() == null || employeeDetails.getAddress().trim().isEmpty()) {
            throw new DataValidationException("Address is not found");
        }
        if (employeeDetails.getPostCode() == null || employeeDetails.getPostCode().trim().isEmpty()) {
            throw new DataValidationException("PostCode is not found");
        }*/
        //Employee Address
        if (employeeDetails.getEmployeeAddress() == null) {
            throw new DataValidationException("Address is not found");
        }
        if (employeeDetails.getRegion() == null ) {
            throw new DataValidationException("Region is not found");
        }
        if (employeeDetails.getTaxCode() == null || employeeDetails.getTaxCode().trim().isEmpty()) {
            throw new DataValidationException("tax code is not found");
        }
        if (employeeDetails.getWorkingCompanyName() == null || employeeDetails.getWorkingCompanyName().trim().isEmpty()) {
            throw new DataValidationException("Working Company Name is not found");
        }

        // Email validation
        if (employeeDetails.getEmail() == null || employeeDetails.getEmail().trim().isEmpty()) {
            throw new DataValidationException("Email is not found");
        }


        // Employee ID validation
        if (employeeDetails.getEmployeeId() == null || employeeDetails.getEmployeeId().trim().isEmpty()) {
            throw new DataValidationException("Employee ID is not found");
        }

        // National Insurance validation
        if (employeeDetails.getNationalInsuranceNumber() != null &&
                !employeeDetails.getNationalInsuranceNumber().matches("^[A-Z]{2}[0-9]{6}[A-Z]$")) {
            throw new DataValidationException("National Insurance number is not found");
        }

        if (employeeDetails.getNiLetter() ==null){
            throw new DataValidationException("NI Category Letter is not found");
        }

        // Financial validations
        if (employeeDetails.getPayPeriodOfIncomeOfEmployee() == null) {
            throw new DataValidationException("Gross income is not found");
        }
        if (employeeDetails.getPayPeriodOfIncomeOfEmployee().compareTo(BigDecimal.ZERO) < 0) {
            throw new DataValidationException("Gross income cannot be negative");
        }

        if (employeeDetails.getPayPeriod() == null) {
            throw new DataValidationException("Pay period is not found");
        }
        if(employeeDetails.getStudentLoan().getHasStudentLoan()==null){
            throw new DataValidationException("student loan cannot be null and it can true or false");

        }
        if(employeeDetails.getStudentLoan().getStudentLoanPlanType()==null){
            throw new DataValidationException("student loan plan Type cannot be null");
        }



    }
    public void validateEmployerDetails(EmployerDetails employerDetails){
        if(employerDetails.getCompanyDetails().getCurrentTaxYear()==null){
            throw  new DataValidationException("tax year is not found");
        }
        if (employerDetails.getCompanyDetails().getPayDate()==null){
            throw new DataValidationException("pay date is not found");
        }
    }

    public void validateP45Data(EmployeeDetails employeeDetails,EmployerDetails employerDetails){
        if (employeeDetails.getNationalInsuranceNumber()==null || employeeDetails.getNationalInsuranceNumber().isEmpty()) {
            throw new DataValidationException("National Insurance number is required for P45 data");
        }
        if(employeeDetails.getDateOfBirth()==null){
            throw new DataValidationException("Date of birth is required for P45 data");
        }
        if (employeeDetails.getFirstName()==null || employeeDetails.getFirstName().isEmpty()) {
            throw new DataValidationException("First name is required for P45 data");
        }
        if (employeeDetails.getLastName()==null || employeeDetails.getLastName().isEmpty()) {
            throw new DataValidationException("Last name is required for P45 data");
        }
        if(employeeDetails.getEmploymentEndDate()==null){
            throw new DataValidationException("Employment end date is required for P45 data");
        }
        if (employeeDetails.getGender() == null) {
            throw new DataValidationException("Employee Gender is required for P45 data");
        }
       /* if (employeeDetails.getAddress() == null || employeeDetails.getAddress().isEmpty()) {
            throw new DataValidationException("Employee address is required for P45 data");
        }
        if (employeeDetails.getPostCode() == null || employeeDetails.getPostCode().isEmpty()) {
            throw new DataValidationException("Employee post code is required for P45 data");
        }*/
        //Employee Address
        if (employeeDetails.getEmployeeAddress()== null) {
            throw new DataValidationException("Employee address is required for P45 data");
        }
        if (employerDetails.getTaxOffice().getPayeReference()==null || employerDetails.getTaxOffice().getPayeReference().isEmpty()) {
            throw new DataValidationException("Employer PAYE reference is required for P45 data");
        }
        if (employerDetails.getCompanyDetails().getCompanyAddress() == null || employerDetails.getCompanyDetails().getCompanyAddress().isEmpty()) {
            throw new DataValidationException("Employer company address is required for P45 data");
        }
        if (employerDetails.getCompanyDetails().getCompanyPostCode() == null || employerDetails.getCompanyDetails().getCompanyPostCode().isEmpty()) {
            throw new DataValidationException("Employer company post code is required for P45 data");
        }
        if (employerDetails.getCompanyDetails().getCompanyName() == null || employerDetails.getCompanyDetails().getCompanyName().isEmpty()) {
            throw new DataValidationException("Employer company name is required for P45 data");
        }
        if (employeeDetails.getPayPeriod() == null ) {
            throw new DataValidationException("Current pay period is required for P45 data");
        }
        if (employeeDetails.getTaxCode()==null || employeeDetails.getTaxCode().isEmpty()) {
            throw new DataValidationException("Tax code at leaving is required for P45 data");
        }
       if (employeeDetails.getPayrollId()==null||employeeDetails.getPayrollId().isEmpty()){
           throw new DataValidationException("Payroll ID is required for P45 data");
       }
    }

    public void validateP60Data(EmployeeDetails employeeDetails,EmployerDetails employerDetails){
        if (employeeDetails.getEmployeeId()==null || employeeDetails.getEmployeeId().isEmpty()) {
            throw new DataValidationException("Employee ID is required for P60 data");
        }

        if (employeeDetails.getFirstName()==null || employeeDetails.getFirstName().isEmpty()) {
            throw new DataValidationException("First name is required for P60 data");
        }
        if (employeeDetails.getLastName()==null || employeeDetails.getLastName().isEmpty()) {
            throw new DataValidationException("Last name is required for P60 data");
        }
        if (employeeDetails.getNationalInsuranceNumber()==null || employeeDetails.getNationalInsuranceNumber().isEmpty()) {
            throw new DataValidationException("National Insurance number is required for P60 data");
        }
        if (employeeDetails.getTaxYear()==null || employeeDetails.getTaxYear().isEmpty()) {
            throw new DataValidationException("Tax year is required for P60 data");
        }

        if (employeeDetails.getTaxCode()==null || employeeDetails.getTaxCode().isEmpty()) {
            throw new DataValidationException("Tax code at leaving is required for P60 data");
        }
        if (employeeDetails.getPayrollId()==null||employeeDetails.getPayrollId().isEmpty()){
            throw new DataValidationException("Payroll ID is required for P60 data");
        }
       /* if (employeeDetails.getAddress() == null || employeeDetails.getAddress().isEmpty()) {
            throw new DataValidationException("Employee address is required for P60 data");
        }
        if (employeeDetails.getPostCode() == null || employeeDetails.getPostCode().isEmpty()) {
            throw new DataValidationException("Employee post code is required for P60 data");
        }*/
        //Employee Address
        if (employeeDetails.getEmployeeAddress() == null) {
            throw new DataValidationException("Employee address is required for P60 data");
        }

        if (employerDetails.getTaxOffice().getPayeReference()==null || employerDetails.getTaxOffice().getPayeReference().isEmpty()) {
            throw new DataValidationException("Employer PAYE reference is required for P60 data");
        }
        if (employerDetails.getCompanyDetails().getCompanyAddress() == null || employerDetails.getCompanyDetails().getCompanyAddress().isEmpty()) {
            throw new DataValidationException("Employer company address is required for P60 data");
        }
        if (employerDetails.getCompanyDetails().getCompanyPostCode() == null || employerDetails.getCompanyDetails().getCompanyPostCode().isEmpty()) {
            throw new DataValidationException("Employer company post code is required for P60 data");
        }
        if (employerDetails.getCompanyDetails().getCompanyName() == null || employerDetails.getCompanyDetails().getCompanyName().isEmpty()) {
            throw new DataValidationException("Employer company name is required for P60 data");
        }
    }
}
