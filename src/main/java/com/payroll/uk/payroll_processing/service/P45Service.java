package com.payroll.uk.payroll_processing.service;

import com.payroll.uk.payroll_processing.dto.customdto.P45DTO;
import com.payroll.uk.payroll_processing.entity.PayPeriod;
import com.payroll.uk.payroll_processing.entity.employee.EmployeeDetails;
import com.payroll.uk.payroll_processing.entity.employer.EmployerDetails;
import com.payroll.uk.payroll_processing.exception.DataValidationException;
import com.payroll.uk.payroll_processing.exception.ResourceNotFoundException;
import com.payroll.uk.payroll_processing.repository.EmployeeDetailsRepository;
import com.payroll.uk.payroll_processing.repository.EmployerDetailsRepository;
import com.payroll.uk.payroll_processing.utils.TaxPeriodUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
public class P45Service {
    @Autowired
    private EmployerDetailsRepository employerDetailsRepository;
    @Autowired
    private EmployeeDetailsRepository employeeDetailsRepository;
    public P45DTO generateP45File(String employeeId){
        if (employeeId==null || employeeId.isEmpty()) {
            throw new DataValidationException("Employee ID cannot be null or empty");
        }
        EmployeeDetails employeeData = employeeDetailsRepository.findByEmployeeId(employeeId)
                .orElseThrow(()-> new ResourceNotFoundException("Employee not found with ID: " + employeeId));
        EmployerDetails employerDetails = employerDetailsRepository.findAll().getFirst();
        if (employerDetails == null) {
            throw new DataValidationException("Employer details not found");
        }
        if (employeeData.getEmploymentEndDate() == null) {
            throw new DataValidationException("Employee has not left the employment yet:");
        }
        P45DTO dtoData= new P45DTO();
        //set 1
        dtoData.setEmployerPAYEReference(employerDetails.getTaxOffice().getPayeReference());
        //set 2
        dtoData.setEmployeeNationalInsuranceNumber(employeeData.getNationalInsuranceNumber());
        //set 3
        dtoData.setEmployeeTitle(getTitleForEmployee(employeeData.isHasMarriedEmployee(), String.valueOf(employeeData.getGender())));
        dtoData.setFirstName(employeeData.getFirstName());
        dtoData.setLastName(employeeData.getLastName());
        //set 4
        dtoData.setEmployeeLeavingDate(employeeData.getEmploymentEndDate());
        //set 5
        dtoData.setStudentLoanToContinue(employeeData.getStudentLoan() != null && employeeData.getStudentLoan().getTotalDeductionAmountInStudentLoan().compareTo(BigDecimal.ZERO) > 0);
        //set 6
        if (!isNonCumulativeTaxCode(employeeData.getTaxCode())){
            dtoData.setTaxCodeAtLeaving(employeeData.getTaxCode());
        }
        else if (isNonCumulativeTaxCode(employeeData.getTaxCode())){
             dtoData.setWeek1Month1Box(true);

        }
        //set 7
        if (!isNonCumulativeTaxCode(employeeData.getTaxCode())){
            if (employerDetails.getCompanyDetails().getCurrentPayPeriod()== PayPeriod.MONTHLY){
                dtoData.setCurrentPayPeriodNumber(TaxPeriodUtils.getUkTaxMonth(LocalDate.now()));
            } else if (employerDetails.getCompanyDetails().getCurrentPayPeriod()==PayPeriod.WEEKLY) {
                dtoData.setCurrentPayPeriodNumber(TaxPeriodUtils.getUkTaxWeek(LocalDate.now()));
            }
            else {
                dtoData.setCurrentPayPeriodNumber("0");
            }
            dtoData.setTotalPayToDate(employeeData.getOtherEmployeeDetails().getTotalEarningsAmountYTD());
            dtoData.setTotalTaxToDate(employeeData.getOtherEmployeeDetails().getTotalTaxPayToDate());
        }
        //set 8
        if (isNonCumulativeTaxCode(employeeData.getTaxCode())){
            dtoData.setTotalPayInThisEmployment(employeeData.getOtherEmployeeDetails().getTotalEarningsAmountInThisEmployment());
            dtoData.setTotalTaxInThisEmployment(employeeData.getOtherEmployeeDetails().getTotalIncomeTaxPaidInThisEmployment());
        }
        //set 9
        dtoData.setPayrollId(employeeData.getPayrollId());
        //set 10
        dtoData.setEmployeeGender(isMale(String.valueOf(employeeData.getGender())));
        //set 11
        dtoData.setEmployeeDateOfBirth(employeeData.getDateOfBirth());
        //set 12
        dtoData.setEmployeeAddress(employeeData.getAddress());
        dtoData.setEmployeePostCode(employeeData.getPostCode());
        //set 13
        dtoData.setCompanyName(employerDetails.getCompanyDetails().getCompanyName());
        dtoData.setCompanyAddress(employerDetails.getCompanyDetails().getCompanyAddress());
        dtoData.setCompanyPostCode(employerDetails.getCompanyDetails().getCompanyPostCode());
        dtoData.setP45Date(LocalDate.now());

        //no use
        dtoData.setCurrentPayPeriod(String.valueOf(employerDetails.getCompanyDetails().getCurrentPayPeriod()));
        return dtoData;
    }

    public String getTitleForEmployee(boolean marriedStatus, String gender) {
        if (gender == null) {
            return "OTHER";
        }

        String normalizedGender = gender.trim().toUpperCase();

        return switch (normalizedGender) {
            case "MALE" -> "MR";
            case "FEMALE" -> marriedStatus ? "MRS" : "MS";
            default -> "OTHER";
        };
    }

    public boolean isNonCumulativeTaxCode(String fullCode) {
        return fullCode != null && (fullCode.toUpperCase().endsWith(" W1") || fullCode.toUpperCase().endsWith(" M1"));
    }
    public boolean isMale(String gender) {
        if (gender == null) return false;

        String cleanedGender = gender.trim().toLowerCase();
        return cleanedGender.equals("male") || cleanedGender.equals("m");
    }



}
