package com.payroll.uk.payroll_processing.dto.mapper.employeedtomapper;

import com.payroll.uk.payroll_processing.dto.BankDetailsDTO;
import com.payroll.uk.payroll_processing.dto.employeedto.EmployeeDetailsDTO;
import com.payroll.uk.payroll_processing.dto.employeedto.OtherEmployeeDetailsDTO;
import com.payroll.uk.payroll_processing.entity.BankDetails;
import com.payroll.uk.payroll_processing.entity.employee.EmployeeDetails;
import com.payroll.uk.payroll_processing.entity.employee.OtherEmployeeDetails;
import org.springframework.stereotype.Component;

@Component
public class EmployeeDetailsDTOMapper {

    // Method to map EmployeeDetails entity to EmployeeDetailsDTO

    public EmployeeDetailsDTO mapToEmployeeDetailsDTO(EmployeeDetails employeeDetails){
        EmployeeDetailsDTO employeeDetailsDTO = new EmployeeDetailsDTO();
//        employeeDetailsDTO.setId(employeeDetails.getId());
        employeeDetailsDTO.setFirstName(employeeDetails.getFirstName());
        employeeDetailsDTO.setLastName(employeeDetails.getLastName());
        employeeDetailsDTO.setEmail(employeeDetails.getEmail());
        employeeDetailsDTO.setRegion(employeeDetails.getRegion());
        employeeDetailsDTO.setDateOfBirth(employeeDetails.getDateOfBirth());
        employeeDetailsDTO.setEmployeeId(employeeDetails.getEmployeeId());
        employeeDetailsDTO.setAddress(employeeDetails.getAddress());
        employeeDetailsDTO.setPostCode(employeeDetails.getPostCode());
        employeeDetailsDTO.setEmploymentType(employeeDetails.getEmploymentType());
        employeeDetailsDTO.setIsDirector(employeeDetails.getIsDirector());
        employeeDetailsDTO.setGender(employeeDetails.getGender());
        employeeDetailsDTO.setWorkingCompanyName(employeeDetails.getWorkingCompanyName());
        employeeDetailsDTO.setEmployeeDepartment(employeeDetails.getEmployeeDepartment());
        employeeDetailsDTO.setEmploymentStartedDate(employeeDetails.getEmploymentStartedDate());
        employeeDetailsDTO.setEmploymentEndDate(employeeDetails.getEmploymentEndDate());
        employeeDetailsDTO.setGrossIncome(employeeDetails.getGrossIncome());
        employeeDetailsDTO.setTaxCode(employeeDetails.getTaxCode());
        employeeDetailsDTO.setIsEmergencyCode(employeeDetails.getIsEmergencyCode());
        employeeDetailsDTO.setIsPostgraduateLoan(employeeDetails.getIsPostgraduateLoan());
        employeeDetailsDTO.setStudentLoan(employeeDetails.getStudentLoan());
        employeeDetailsDTO.setPayPeriod(employeeDetails.getPayPeriod());
        employeeDetailsDTO.setNationalInsuranceNumber(employeeDetails.getNationalInsuranceNumber());
        employeeDetailsDTO.setNICategoryLetter(employeeDetails.getNICategoryLetter());
        employeeDetailsDTO.setBankDetailsDTO(mapToBanKDetailsDTO(employeeDetails.getBankDetails()));
        employeeDetailsDTO.setOtherEmployeeDetailsDTO(mapToOtherEmployeeDetailsDTO(employeeDetails));
        return employeeDetailsDTO;

    }
    public BankDetailsDTO mapToBanKDetailsDTO(BankDetails bankDetails) {
        if (bankDetails == null) {
            throw new IllegalArgumentException("Bank details cannot be null");
        }
        BankDetailsDTO bankDetailsDTO = new BankDetailsDTO();
//        bankDetailsDTO.setId(employeeDetails.getBankDetails().getId());
        bankDetailsDTO.setAccountName(bankDetails.getAccountName());
        bankDetailsDTO.setAccountNumber(bankDetails.getAccountNumber());
        bankDetailsDTO.setPaymentReference(bankDetails.getPaymentReference());
        bankDetailsDTO.setBankName(bankDetails.getBankName());
        bankDetailsDTO.setSortCode(bankDetails.getSortCode());
        bankDetailsDTO.setBankAddress(bankDetails.getBankAddress());
        bankDetailsDTO.setBankPostCode(bankDetails.getBankPostCode());
        bankDetailsDTO.setTelephone(bankDetails.getTelephone());
        bankDetailsDTO.setPaymentLeadDays(bankDetails.getPaymentLeadDays());
        bankDetailsDTO.setIsRTIReturnsIncluded(bankDetails.getIsRTIReturnsIncluded());
        return bankDetailsDTO;

    }
    public OtherEmployeeDetailsDTO mapToOtherEmployeeDetailsDTO(EmployeeDetails employeeDetails) {
        OtherEmployeeDetailsDTO otherEmployeeDetailsDTO = new OtherEmployeeDetailsDTO();
        otherEmployeeDetailsDTO.setPreviouslyUsedPersonalAllowance(employeeDetails.getOtherEmployeeDetails().getPreviouslyUsedPersonalAllowance());
        otherEmployeeDetailsDTO.setTotalPersonalAllowanceInCompany(employeeDetails.getOtherEmployeeDetails().getTotalPersonalAllowanceInCompany());
        otherEmployeeDetailsDTO.setUsedPersonalAllowance(employeeDetails.getOtherEmployeeDetails().getUsedPersonalAllowance());
        otherEmployeeDetailsDTO.setTotalUsedPersonalAllowance(employeeDetails.getOtherEmployeeDetails().getTotalUsedPersonalAllowance());
        otherEmployeeDetailsDTO.setRemainingPersonalAllowance(employeeDetails.getOtherEmployeeDetails().getRemainingPersonalAllowance());
        otherEmployeeDetailsDTO.setIncomeTaxPaid(employeeDetails.getOtherEmployeeDetails().getIncomeTaxPaid());
        otherEmployeeDetailsDTO.setTotalIncomeTaxPaidInCompany(employeeDetails.getOtherEmployeeDetails().getTotalIncomeTaxPaidInCompany());
        otherEmployeeDetailsDTO.setNumberOfMonthsOfIncomeTaxPaid(employeeDetails.getOtherEmployeeDetails().getNumberOfMonthsOfIncomeTaxPaid());
        otherEmployeeDetailsDTO.setNumberOfYearsOfIncomeTaxPaid(employeeDetails.getOtherEmployeeDetails().getNumberOfYearsOfIncomeTaxPaid());
        otherEmployeeDetailsDTO.setNumberOfWeeksOfIncomeTaxPaid(employeeDetails.getOtherEmployeeDetails().getNumberOfWeeksOfIncomeTaxPaid());
        otherEmployeeDetailsDTO.setEmployeeNIContribution(employeeDetails.getOtherEmployeeDetails().getEmployeeNIContribution());
        otherEmployeeDetailsDTO.setTotalEmployeeNIContributionInCompany(employeeDetails.getOtherEmployeeDetails().getTotalEmployeeNIContributionInCompany());
        otherEmployeeDetailsDTO.setNumberOfMonthsOfNIContributions(employeeDetails.getOtherEmployeeDetails().getNumberOfMonthsOfNIContributions());
        otherEmployeeDetailsDTO.setNumberOfWeeksOfNIContributions(employeeDetails.getOtherEmployeeDetails().getNumberOfWeeksOfNIContributions());
        otherEmployeeDetailsDTO.setNumberOfYearsOfNIContributions(employeeDetails.getOtherEmployeeDetails().getNumberOfYearsOfNIContributions());
        return otherEmployeeDetailsDTO;

    }
    //DTO to Entity mapping methods
    public EmployeeDetails mapToEmployeeDetails(EmployeeDetailsDTO employeeDetailsDTO) {
        EmployeeDetails employeeDetails = new EmployeeDetails();
        // Map DTO to Entity
        employeeDetails.setFirstName(employeeDetailsDTO.getFirstName());
        employeeDetails.setLastName(employeeDetailsDTO.getLastName());
        employeeDetails.setEmail(employeeDetailsDTO.getEmail());
        employeeDetails.setRegion(employeeDetailsDTO.getRegion());
        employeeDetails.setDateOfBirth(employeeDetailsDTO.getDateOfBirth());
        employeeDetails.setEmployeeId(employeeDetailsDTO.getEmployeeId());
        employeeDetails.setAddress(employeeDetailsDTO.getAddress());
        employeeDetails.setPostCode(employeeDetailsDTO.getPostCode());
        employeeDetails.setWorkingCompanyName(employeeDetailsDTO.getWorkingCompanyName());
        employeeDetails.setEmploymentType(employeeDetailsDTO.getEmploymentType());
        employeeDetails.setIsDirector(employeeDetailsDTO.getIsDirector());
        employeeDetails.setGender(employeeDetailsDTO.getGender());
        employeeDetails.setEmployeeDepartment(employeeDetailsDTO.getEmployeeDepartment());
        employeeDetails.setEmploymentStartedDate(employeeDetailsDTO.getEmploymentStartedDate());
        employeeDetails.setEmploymentEndDate(employeeDetailsDTO.getEmploymentEndDate());
        employeeDetails.setGrossIncome(employeeDetailsDTO.getGrossIncome());
        employeeDetails.setTaxCode(employeeDetailsDTO.getTaxCode());
        employeeDetails.setIsEmergencyCode(employeeDetailsDTO.getIsEmergencyCode());
        employeeDetails.setIsPostgraduateLoan(employeeDetailsDTO.getIsPostgraduateLoan());
        employeeDetails.setStudentLoan(employeeDetailsDTO.getStudentLoan());
        employeeDetails.setPayPeriod(employeeDetailsDTO.getPayPeriod());
        employeeDetails.setNationalInsuranceNumber(employeeDetailsDTO.getNationalInsuranceNumber());
        employeeDetails.setNICategoryLetter(employeeDetailsDTO.getNICategoryLetter());


        BankDetails bankDetails = mapToBankDetails(employeeDetailsDTO);
        employeeDetails.setBankDetails(bankDetails);

        OtherEmployeeDetailsDTO otherEmployeeDetailsDTO = employeeDetailsDTO.getOtherEmployeeDetailsDTO();
        OtherEmployeeDetails otherEmployeeDetails = getOtherEmployeeDetails(otherEmployeeDetailsDTO);
        employeeDetails.setOtherEmployeeDetails(otherEmployeeDetails);

        return employeeDetails;
    }
    public BankDetails mapToBankDetails(EmployeeDetailsDTO employeeDetailsDTO) {
        BankDetails bankDetails = new BankDetails();
        bankDetails.setAccountName(employeeDetailsDTO.getBankDetailsDTO().getAccountName());
        bankDetails.setAccountNumber(employeeDetailsDTO.getBankDetailsDTO().getAccountNumber());
        bankDetails.setPaymentReference(employeeDetailsDTO.getBankDetailsDTO().getPaymentReference());
        bankDetails.setBankName(employeeDetailsDTO.getBankDetailsDTO().getBankName());
        bankDetails.setSortCode(employeeDetailsDTO.getBankDetailsDTO().getSortCode());
        bankDetails.setBankAddress(employeeDetailsDTO.getBankDetailsDTO().getBankAddress());
        bankDetails.setBankPostCode(employeeDetailsDTO.getBankDetailsDTO().getBankPostCode());
        bankDetails.setTelephone(employeeDetailsDTO.getBankDetailsDTO().getTelephone());
        bankDetails.setPaymentLeadDays(employeeDetailsDTO.getBankDetailsDTO().getPaymentLeadDays());
        bankDetails.setIsRTIReturnsIncluded(employeeDetailsDTO.getBankDetailsDTO().getIsRTIReturnsIncluded());
        return bankDetails;
    }
    public OtherEmployeeDetails getOtherEmployeeDetails(OtherEmployeeDetailsDTO otherEmployeeDetailsDTO) {
        OtherEmployeeDetails otherEmployeeDetails = new OtherEmployeeDetails();
        otherEmployeeDetails.setPreviouslyUsedPersonalAllowance(otherEmployeeDetailsDTO.getPreviouslyUsedPersonalAllowance());
        otherEmployeeDetails.setTotalPersonalAllowanceInCompany(otherEmployeeDetailsDTO.getTotalPersonalAllowanceInCompany());
        otherEmployeeDetails.setUsedPersonalAllowance(otherEmployeeDetailsDTO.getUsedPersonalAllowance());
        otherEmployeeDetails.setTotalUsedPersonalAllowance(otherEmployeeDetailsDTO.getTotalUsedPersonalAllowance());
        otherEmployeeDetails.setRemainingPersonalAllowance(otherEmployeeDetailsDTO.getRemainingPersonalAllowance());
        otherEmployeeDetails.setIncomeTaxPaid(otherEmployeeDetailsDTO.getIncomeTaxPaid());
        otherEmployeeDetails.setTotalIncomeTaxPaidInCompany(otherEmployeeDetailsDTO.getTotalIncomeTaxPaidInCompany());
        otherEmployeeDetails.setNumberOfMonthsOfIncomeTaxPaid(otherEmployeeDetailsDTO.getNumberOfMonthsOfIncomeTaxPaid());
        otherEmployeeDetails.setNumberOfYearsOfIncomeTaxPaid(otherEmployeeDetailsDTO.getNumberOfYearsOfIncomeTaxPaid());
        otherEmployeeDetails.setNumberOfWeeksOfIncomeTaxPaid(otherEmployeeDetailsDTO.getNumberOfWeeksOfIncomeTaxPaid());
        otherEmployeeDetails.setTotalEmployeeNIContributionInCompany(otherEmployeeDetailsDTO.getTotalEmployeeNIContributionInCompany());
        otherEmployeeDetails.setEmployeeNIContribution(otherEmployeeDetailsDTO.getEmployeeNIContribution());
        otherEmployeeDetails.setNumberOfMonthsOfNIContributions(otherEmployeeDetailsDTO.getNumberOfMonthsOfNIContributions());
        otherEmployeeDetails.setNumberOfWeeksOfNIContributions(otherEmployeeDetailsDTO.getNumberOfWeeksOfNIContributions());
        otherEmployeeDetails.setNumberOfYearsOfNIContributions(otherEmployeeDetailsDTO.getNumberOfYearsOfNIContributions());
        return otherEmployeeDetails;

    }


}


