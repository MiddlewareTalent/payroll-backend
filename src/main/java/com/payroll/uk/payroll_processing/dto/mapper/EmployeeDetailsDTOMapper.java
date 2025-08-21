package com.payroll.uk.payroll_processing.dto.mapper;

import com.payroll.uk.payroll_processing.dto.BankDetailsDTO;
import com.payroll.uk.payroll_processing.dto.employeedto.*;
import com.payroll.uk.payroll_processing.entity.BankDetails;
import com.payroll.uk.payroll_processing.entity.PayPeriod;
import com.payroll.uk.payroll_processing.entity.employee.*;
import com.payroll.uk.payroll_processing.exception.ResourceNotFoundException;
import com.payroll.uk.payroll_processing.repository.EmployeeDetailsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
public class EmployeeDetailsDTOMapper {

    private static final Logger logger= LoggerFactory.getLogger(EmployeeDetailsDTOMapper.class);
    @Autowired
    private EmployeeDetailsRepository employeeDetailsRepository;

    // Method to map EmployeeDetails entity to EmployeeDetailsDTO

    public EmployeeDetailsDTO mapToEmployeeDetailsDTO(EmployeeDetails employeeDetails){
        EmployeeDetailsDTO employeeDetailsDTO = new EmployeeDetailsDTO();
        employeeDetailsDTO.setId(employeeDetails.getId());
        employeeDetailsDTO.setFirstName(employeeDetails.getFirstName());
        employeeDetailsDTO.setLastName(employeeDetails.getLastName());
        employeeDetailsDTO.setEmail(employeeDetails.getEmail());
        employeeDetailsDTO.setRegion(employeeDetails.getRegion());
        employeeDetailsDTO.setDateOfBirth(employeeDetails.getDateOfBirth());
        employeeDetailsDTO.setEmployeeId(employeeDetails.getEmployeeId());
        employeeDetailsDTO.setAddress(employeeDetails.getAddress());
        employeeDetailsDTO.setPostCode(employeeDetails.getPostCode());
        employeeDetailsDTO.setTaxYear(employeeDetails.getTaxYear());
        employeeDetailsDTO.setEmploymentType(employeeDetails.getEmploymentType());
//        employeeDetailsDTO.setDirector(employeeDetails.isDirector());
        employeeDetailsDTO.setGender(employeeDetails.getGender());
        employeeDetailsDTO.setWorkingCompanyName(employeeDetails.getWorkingCompanyName());
        employeeDetailsDTO.setEmployeeDepartment(employeeDetails.getEmployeeDepartment());
        employeeDetailsDTO.setEmploymentStartedDate(employeeDetails.getEmploymentStartedDate());
        employeeDetailsDTO.setEmploymentEndDate(employeeDetails.getEmploymentEndDate());
        employeeDetailsDTO.setAnnualIncomeOfEmployee(employeeDetails.getAnnualIncomeOfEmployee());
        employeeDetailsDTO.setTaxCode(employeeDetails.getTaxCode());
        employeeDetailsDTO.setHasEmergencyCode(employeeDetails.isHasEmergencyCode());
        employeeDetailsDTO.setPayPeriod(employeeDetails.getPayPeriod());
        employeeDetailsDTO.setNationalInsuranceNumber(employeeDetails.getNationalInsuranceNumber());
        employeeDetailsDTO.setNiLetter(employeeDetails.getNiLetter());
        employeeDetailsDTO.setPayrollId(employeeDetails.getPayrollId());

//        employeeDetailsDTO.setEmployerId(employeeDetails.getEmployerId());

        employeeDetailsDTO.setPayPeriodOfIncomeOfEmployee(employeeDetails.getPayPeriodOfIncomeOfEmployee());
        employeeDetailsDTO.setHasMarriedEmployee(employeeDetails.isHasMarriedEmployee());
        //KCode Taxable Adjustment
        employeeDetailsDTO.setKCodeTaxableAdjustmentAnnual(employeeDetails.getKCodeTaxableAdjustmentAnnual());
        //pension eligibility
         employeeDetailsDTO.setHasPensionEligible(employeeDetails.isHasPensionEligible());
        //Documents upload
        employeeDetailsDTO.setP45Document(employeeDetails.getP45Document());
        employeeDetailsDTO.setHasP45DocumentSubmitted(employeeDetails.isHasP45DocumentSubmitted());
        employeeDetailsDTO.setStarterChecklistDocument(employeeDetails.getStarterChecklistDocument());
        employeeDetailsDTO.setHasStarterChecklistDocumentSubmitted(employeeDetails.isHasStarterChecklistDocumentSubmitted());

        // Null check before mapping nested objects
        if (employeeDetails.getBankDetails() != null) {
            employeeDetailsDTO.setBankDetailsDTO(mapToBanKDetailsDTO(employeeDetails.getBankDetails()));
        }

        if (employeeDetails.getOtherEmployeeDetails() != null) {
            employeeDetailsDTO.setOtherEmployeeDetailsDTO(mapToOtherEmployeeDetailsDTO(employeeDetails.getOtherEmployeeDetails()));
        }
        if (employeeDetails.getStudentLoan()!=null){
            employeeDetailsDTO.setStudentLoanDto(mapToStudentLoanDto(employeeDetails.getStudentLoan()));
        }
        if (employeeDetails.getPostGraduateLoan()!=null){
            employeeDetailsDTO.setPostGraduateLoanDto(mapToPostGraduateLoanDto(employeeDetails.getPostGraduateLoan()));
        }
        if(employeeDetails.getPreviousEmploymentData() !=null){
            employeeDetailsDTO.setPreviousEmploymentDataDTO(changeToEmployeeDetailsDTO(employeeDetails.getPreviousEmploymentData()));
        }
        employeeDetailsDTO.setPreviouslyUsedPersonalAllowance(employeeDetails.getPreviouslyUsedPersonalAllowance());
        return employeeDetailsDTO;

    }
    public BankDetailsDTO mapToBanKDetailsDTO(BankDetails bankDetails) {
        if (bankDetails == null) {
            return null;
        }
        BankDetailsDTO bankDetailsDTO = new BankDetailsDTO();
//        bankDetailsDTO.setId(employeeDetails.getBankDetails().getId());
        bankDetailsDTO.setAccountName(bankDetails.getAccountName());
        bankDetailsDTO.setAccountNumber(bankDetails.getAccountNumber());
        bankDetailsDTO.setBankName(bankDetails.getBankName());
        bankDetailsDTO.setSortCode(bankDetails.getSortCode());
        bankDetailsDTO.setBankAddress(bankDetails.getBankAddress());
        bankDetailsDTO.setBankPostCode(bankDetails.getBankPostCode());

        return bankDetailsDTO;

    }
    public OtherEmployeeDetailsDTO mapToOtherEmployeeDetailsDTO(OtherEmployeeDetails otherEmployeeDetails) {
        OtherEmployeeDetailsDTO otherEmployeeDetailsDTO = new OtherEmployeeDetailsDTO();
        otherEmployeeDetailsDTO.setTotalTaxablePayInThisEmployment(otherEmployeeDetails.getTotalTaxablePayInThisEmployment() == null ? BigDecimal.ZERO : otherEmployeeDetails.getTotalTaxablePayInThisEmployment());
        otherEmployeeDetailsDTO.setNumberOfPayPeriodsEmergencyTaxCodeUsed(otherEmployeeDetails.getNumberOfPayPeriodsEmergencyTaxCodeUsed()== null ? BigDecimal.ZERO : otherEmployeeDetails.getNumberOfPayPeriodsEmergencyTaxCodeUsed());
        otherEmployeeDetailsDTO.setTotalAllowanceUsedDuringEmergencyCode(otherEmployeeDetails.getTotalAllowanceUsedDuringEmergencyCode());

        otherEmployeeDetailsDTO.setUsedPersonalAllowance(otherEmployeeDetails.getUsedPersonalAllowance());
        otherEmployeeDetailsDTO.setTotalUsedPersonalAllowance(otherEmployeeDetails.getTotalUsedPersonalAllowance());
        otherEmployeeDetailsDTO.setRemainingPersonalAllowance(otherEmployeeDetails.getRemainingPersonalAllowance());
        otherEmployeeDetailsDTO.setTotalEarningsAmountYTD(otherEmployeeDetails.getTotalEarningsAmountYTD());
        otherEmployeeDetailsDTO.setTotalEarningsAmountInThisEmployment(otherEmployeeDetails.getTotalEarningsAmountInThisEmployment());
        otherEmployeeDetailsDTO.setTotalTaxPayToDate(otherEmployeeDetails.getTotalTaxPayToDate());
        otherEmployeeDetailsDTO.setIncomeTaxPaid(otherEmployeeDetails.getIncomeTaxPaid());
        otherEmployeeDetailsDTO.setTotalIncomeTaxPaidInThisEmployment(otherEmployeeDetails.getTotalIncomeTaxPaidInThisEmployment());
        otherEmployeeDetailsDTO.setNumberOfPayPeriodsIncomeTaxPaid(otherEmployeeDetails.getNumberOfPayPeriodsIncomeTaxPaid() == null ? BigDecimal.ZERO : otherEmployeeDetails.getNumberOfPayPeriodsIncomeTaxPaid());

        otherEmployeeDetailsDTO.setEmployeeNIContribution(otherEmployeeDetails.getEmployeeNIContribution());
        otherEmployeeDetailsDTO.setTotalEmployeeNIContributionInCompany(otherEmployeeDetails.getTotalEmployeeNIContributionInCompany());
        otherEmployeeDetailsDTO.setNumberOfPayPeriodsNIContributions(otherEmployeeDetails.getNumberOfPayPeriodsNIContributions() == null ? BigDecimal.ZERO : otherEmployeeDetails.getNumberOfPayPeriodsNIContributions());
        otherEmployeeDetailsDTO.setEarningsAtLELYtd(otherEmployeeDetails.getEarningsAtLELYtd() == null ? BigDecimal.ZERO : otherEmployeeDetails.getEarningsAtLELYtd());
        otherEmployeeDetailsDTO.setEarningsLelToPtYtd(otherEmployeeDetails.getEarningsLelToPtYtd() == null ? BigDecimal.ZERO : otherEmployeeDetails.getEarningsLelToPtYtd());
        otherEmployeeDetailsDTO.setEarningsPtToUelYtd(otherEmployeeDetails.getEarningsPtToUelYtd() == null ? BigDecimal.ZERO : otherEmployeeDetails.getEarningsPtToUelYtd());

        otherEmployeeDetailsDTO.setNumberOfPayPeriodsPensionContribution(otherEmployeeDetails.getNumberOfPayPeriodsPensionContribution()== null ? BigDecimal.ZERO : otherEmployeeDetails.getNumberOfPayPeriodsPensionContribution());
        otherEmployeeDetailsDTO.setTotalAmountPensionContribution(otherEmployeeDetails.getTotalAmountPensionContribution());
        otherEmployeeDetailsDTO.setPensionContributeAmount(otherEmployeeDetails.getPensionContributeAmount());
        otherEmployeeDetailsDTO.setRemainingKCodeAmount(otherEmployeeDetails.getRemainingKCodeAmount());
        return otherEmployeeDetailsDTO;

    }
    //DTO to Entity mapping methods
    public EmployeeDetails mapToEmployeeDetails(EmployeeDetailsDTO employeeDetailsDTO) {
        EmployeeDetails employeeDetails = new EmployeeDetails();
        // Map DTO to Entity
//        employeeDetails.setId(employeeDetailsDTO.getId());
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
        employeeDetails.setTaxYear(employeeDetailsDTO.getTaxYear());
//        employeeDetails.setDirector(employeeDetailsDTO.isDirector());
        employeeDetails.setGender(employeeDetailsDTO.getGender());
        employeeDetails.setEmployeeDepartment(employeeDetailsDTO.getEmployeeDepartment());
        employeeDetails.setEmploymentStartedDate(employeeDetailsDTO.getEmploymentStartedDate());
        employeeDetails.setPayPeriod(employeeDetailsDTO.getPayPeriod());
        employeeDetails.setEmploymentEndDate(employeeDetailsDTO.getEmploymentEndDate());
        employeeDetails.setAnnualIncomeOfEmployee(employeeDetailsDTO.getAnnualIncomeOfEmployee());
        employeeDetails.setHasMarriedEmployee(employeeDetailsDTO.isHasMarriedEmployee());

        employeeDetails.setPayPeriodOfIncomeOfEmployee(calculateIncomeTaxBasedOnPayPeriod(employeeDetailsDTO.getAnnualIncomeOfEmployee(),employeeDetailsDTO.getPayPeriod()));
        employeeDetails.setTaxCode(employeeDetailsDTO.getTaxCode());
        if(!employeeDetailsDTO.isHasEmergencyCode()){
            employeeDetails.setHasEmergencyCode(checkIfEmergencyTaxCode(employeeDetails.getTaxCode()));
        }else {
            employeeDetails.setHasEmergencyCode(employeeDetailsDTO.isHasEmergencyCode());
        }


        employeeDetails.setNationalInsuranceNumber(employeeDetailsDTO.getNationalInsuranceNumber());
        employeeDetails.setNiLetter(employeeDetailsDTO.getNiLetter());
//        employeeDetails.setEmployerId(employeeDetailsDTO.getEmployerId());

        if (employeeDetailsDTO.getPreviouslyUsedPersonalAllowance()==null){
            employeeDetails.setPreviouslyUsedPersonalAllowance(BigDecimal.ZERO);
        }else {
            employeeDetails.setPreviouslyUsedPersonalAllowance(employeeDetailsDTO.getPreviouslyUsedPersonalAllowance());
        }

        employeeDetails.setHasPensionEligible(employeeDetailsDTO.isHasPensionEligible());

        //K Code Taxable Adjustment
        if(isKTaxCode(employeeDetails.getTaxCode())) {
            BigDecimal kCodeAmount = calculateTaxWithKCode(employeeDetails.getTaxCode());
            employeeDetails.setKCodeTaxableAdjustmentAnnual(kCodeAmount);
        }
        else {
            employeeDetails.setKCodeTaxableAdjustmentAnnual(BigDecimal.ZERO);
        }

//        employeeDetails.setRemainingPersonalAllowanceInYear(employeeDetails.getTotalPersonalAllowance().subtract(employeeDetails.getPreviouslyUsedPersonalAllowance()));
        //Document Upload
        employeeDetails.setP45Document(employeeDetailsDTO.getP45Document());
        if(employeeDetails.getP45Document().isEmpty()){
            employeeDetails.setHasP45DocumentSubmitted(false);
        }
        else {
            employeeDetails.setHasP45DocumentSubmitted(employeeDetailsDTO.isHasP45DocumentSubmitted());
        }
        employeeDetails.setStarterChecklistDocument(employeeDetailsDTO.getStarterChecklistDocument());
        if(employeeDetails.getStarterChecklistDocument().isEmpty()){
            employeeDetails.setHasStarterChecklistDocumentSubmitted(false);
        }
        else{
            employeeDetails.setHasStarterChecklistDocumentSubmitted(employeeDetailsDTO.isHasStarterChecklistDocumentSubmitted());
        }

        // Null check before mapping nested DTOs
        if (employeeDetailsDTO.getBankDetailsDTO() != null) {
            employeeDetails.setBankDetails(mapToBankDetails(employeeDetailsDTO));
        }

        OtherEmployeeDetails otherEmployeeDetails;

        if (employeeDetailsDTO.getOtherEmployeeDetailsDTO() != null) {
            otherEmployeeDetails = mapToOtherEmployeeDetails(employeeDetailsDTO);
        } else {
            otherEmployeeDetails = new OtherEmployeeDetails();
            otherEmployeeDetails.setDefaultsIfNull(); // Now it's safe
//            otherEmployeeDetails.setRemainingPersonalAllowance(employeeDetails.getTotalPersonalAllowance().subtract(employeeDetails.getPreviouslyUsedPersonalAllowance()));
            otherEmployeeDetails.setRemainingKCodeAmount(employeeDetails.getKCodeTaxableAdjustmentAnnual());
        }

        employeeDetails.setOtherEmployeeDetails(otherEmployeeDetails); // Always set

        StudentLoan studentLoan;
        if (employeeDetailsDTO.getStudentLoanDto() != null) {
            studentLoan = mapToStudentLoan(employeeDetailsDTO.getStudentLoanDto());
        } else {
            studentLoan = new StudentLoan();
            studentLoan.setStudentLoanDefaults(); // Now it's safe
        }
        employeeDetails.setStudentLoan(studentLoan); // Always set
        PostGraduateLoan postGraduateLoan;
        if (employeeDetailsDTO.getPostGraduateLoanDto() != null) {
            postGraduateLoan = mapToPostGraduateLoan(employeeDetailsDTO.getPostGraduateLoanDto());
        } else {
            postGraduateLoan = new PostGraduateLoan();
            postGraduateLoan.setPostgraduateDefaults();
        }
        employeeDetails.setPostGraduateLoan(postGraduateLoan);
        PreviousEmploymentData previousEmploymentData;
        if (employeeDetailsDTO.getPreviousEmploymentDataDTO()!=null){
            previousEmploymentData=mapToPreviousEmploymentData(employeeDetailsDTO.getPreviousEmploymentDataDTO());
        }
        else {
            previousEmploymentData = new PreviousEmploymentData();
            previousEmploymentData.setDefaultsIfNull(); // Now it's safe
        }
        employeeDetails.setPreviousEmploymentData(previousEmploymentData);


        return employeeDetails;
    }
    public EmployeeDetails mapToUpdateEmployeeDetails(EmployeeDetailsDTO employeeDetailsDTO) {
        EmployeeDetails employeeDetails = employeeDetailsRepository.findByEmployeeId(employeeDetailsDTO.getEmployeeId())
                .orElseThrow(() -> new ResourceNotFoundException("Employee with ID " + employeeDetailsDTO.getEmployeeId() + " not found"));
        // Map DTO to Entity
//        employeeDetails.setId(employeeDetailsDTO.getId());
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
        employeeDetails.setTaxYear(employeeDetailsDTO.getTaxYear());
//        employeeDetails.setDirector(employeeDetailsDTO.isDirector());
        employeeDetails.setGender(employeeDetailsDTO.getGender());
        employeeDetails.setEmployeeDepartment(employeeDetailsDTO.getEmployeeDepartment());
        employeeDetails.setEmploymentStartedDate(employeeDetailsDTO.getEmploymentStartedDate());
        employeeDetails.setPayPeriod(employeeDetailsDTO.getPayPeriod());
        employeeDetails.setEmploymentEndDate(employeeDetailsDTO.getEmploymentEndDate());
        employeeDetails.setAnnualIncomeOfEmployee(employeeDetailsDTO.getAnnualIncomeOfEmployee());
        employeeDetails.setHasMarriedEmployee(employeeDetailsDTO.isHasMarriedEmployee());

        employeeDetails.setPayPeriodOfIncomeOfEmployee(calculateIncomeTaxBasedOnPayPeriod(employeeDetailsDTO.getAnnualIncomeOfEmployee(),employeeDetailsDTO.getPayPeriod()));
        employeeDetails.setTaxCode(employeeDetailsDTO.getTaxCode());
        if(!employeeDetailsDTO.isHasEmergencyCode()){
            employeeDetails.setHasEmergencyCode(checkIfEmergencyTaxCode(employeeDetails.getTaxCode()));
        }else {
            employeeDetails.setHasEmergencyCode(employeeDetailsDTO.isHasEmergencyCode());
        }


        employeeDetails.setNationalInsuranceNumber(employeeDetailsDTO.getNationalInsuranceNumber());
        employeeDetails.setNiLetter(employeeDetailsDTO.getNiLetter());
//        employeeDetails.setEmployerId(employeeDetailsDTO.getEmployerId());

        if (employeeDetailsDTO.getPreviouslyUsedPersonalAllowance()==null){
            employeeDetails.setPreviouslyUsedPersonalAllowance(BigDecimal.ZERO);
        }else {
            employeeDetails.setPreviouslyUsedPersonalAllowance(employeeDetailsDTO.getPreviouslyUsedPersonalAllowance());
        }

        employeeDetails.setHasPensionEligible(employeeDetailsDTO.isHasPensionEligible());

        //K Code Taxable Adjustment
        if(isKTaxCode(employeeDetails.getTaxCode())) {
            BigDecimal kCodeAmount = calculateTaxWithKCode(employeeDetails.getTaxCode());
            employeeDetails.setKCodeTaxableAdjustmentAnnual(kCodeAmount);
        }
        else {
            employeeDetails.setKCodeTaxableAdjustmentAnnual(BigDecimal.ZERO);
        }

        //Document Upload
        employeeDetails.setP45Document(employeeDetailsDTO.getP45Document());
        if(employeeDetails.getP45Document().isEmpty()){
            employeeDetails.setHasP45DocumentSubmitted(false);
        }
        else {
            employeeDetails.setHasP45DocumentSubmitted(employeeDetailsDTO.isHasP45DocumentSubmitted());
        }
        employeeDetails.setStarterChecklistDocument(employeeDetailsDTO.getStarterChecklistDocument());
        if(employeeDetails.getStarterChecklistDocument().isEmpty()){
            employeeDetails.setHasStarterChecklistDocumentSubmitted(false);
        }
        else{
            employeeDetails.setHasStarterChecklistDocumentSubmitted(employeeDetailsDTO.isHasStarterChecklistDocumentSubmitted());
        }

        // Null check before mapping nested DTOs
        if (employeeDetailsDTO.getBankDetailsDTO() != null) {
            employeeDetails.setBankDetails(mapToBankDetails(employeeDetailsDTO));
        }

      /*  OtherEmployeeDetails otherEmployeeDetails;

        if (employeeDetailsDTO.getOtherEmployeeDetailsDTO() != null) {
            otherEmployeeDetails = mapToOtherEmployeeDetails(employeeDetailsDTO);
        } else {
            otherEmployeeDetails = new OtherEmployeeDetails();
            otherEmployeeDetails.setDefaultsIfNull(); // Now it's safe
//            otherEmployeeDetails.setRemainingPersonalAllowance(employeeDetails.getTotalPersonalAllowance().subtract(employeeDetails.getPreviouslyUsedPersonalAllowance()));
            otherEmployeeDetails.setRemainingKCodeAmount(employeeDetails.getKCodeTaxableAdjustmentAnnual());
        }

        employeeDetails.setOtherEmployeeDetails(otherEmployeeDetails); // Always set*/

        StudentLoan studentLoan;
        if (employeeDetailsDTO.getStudentLoanDto() != null) {
            studentLoan = mapToStudentLoan(employeeDetailsDTO.getStudentLoanDto());
        } else {
            studentLoan = new StudentLoan();
            studentLoan.setStudentLoanDefaults(); // Now it's safe
        }
        employeeDetails.setStudentLoan(studentLoan); // Always set
        PostGraduateLoan postGraduateLoan;
        if (employeeDetailsDTO.getPostGraduateLoanDto() != null) {
            postGraduateLoan = mapToPostGraduateLoan(employeeDetailsDTO.getPostGraduateLoanDto());
        } else {
            postGraduateLoan = new PostGraduateLoan();
            postGraduateLoan.setPostgraduateDefaults();
        }
        employeeDetails.setPostGraduateLoan(postGraduateLoan);

        PreviousEmploymentData previousEmploymentData;
        if (employeeDetailsDTO.getPreviousEmploymentDataDTO()!=null){
            previousEmploymentData=mapToPreviousEmploymentData(employeeDetailsDTO.getPreviousEmploymentDataDTO());
        }
        else {
            previousEmploymentData = new PreviousEmploymentData();
            previousEmploymentData.setDefaultsIfNull(); // Now it's safe
        }
        employeeDetails.setPreviousEmploymentData(previousEmploymentData);


        return employeeDetails;
    }
    public BankDetails mapToBankDetails(EmployeeDetailsDTO employeeDetailsDTO) {
        BankDetails bankDetails = new BankDetails();
        bankDetails.setAccountName(employeeDetailsDTO.getBankDetailsDTO().getAccountName());
        bankDetails.setAccountNumber(employeeDetailsDTO.getBankDetailsDTO().getAccountNumber());

        bankDetails.setBankName(employeeDetailsDTO.getBankDetailsDTO().getBankName());
        bankDetails.setSortCode(employeeDetailsDTO.getBankDetailsDTO().getSortCode());
        bankDetails.setBankAddress(employeeDetailsDTO.getBankDetailsDTO().getBankAddress());
        bankDetails.setBankPostCode(employeeDetailsDTO.getBankDetailsDTO().getBankPostCode());
        return bankDetails;
    }
    public OtherEmployeeDetails mapToOtherEmployeeDetails(EmployeeDetailsDTO employeeDetailsDTO) {
        OtherEmployeeDetails otherEmployeeDetails = new OtherEmployeeDetails();
        otherEmployeeDetails.setTotalTaxablePayInThisEmployment(employeeDetailsDTO.getOtherEmployeeDetailsDTO().getTotalTaxablePayInThisEmployment() == null ? BigDecimal.ZERO : employeeDetailsDTO.getOtherEmployeeDetailsDTO().getTotalTaxablePayInThisEmployment());
        otherEmployeeDetails.setTotalAllowanceUsedDuringEmergencyCode(employeeDetailsDTO.getOtherEmployeeDetailsDTO().getTotalAllowanceUsedDuringEmergencyCode());
        otherEmployeeDetails.setNumberOfPayPeriodsEmergencyTaxCodeUsed(employeeDetailsDTO.getOtherEmployeeDetailsDTO().getNumberOfPayPeriodsEmergencyTaxCodeUsed());

        otherEmployeeDetails.setUsedPersonalAllowance(employeeDetailsDTO.getOtherEmployeeDetailsDTO().getUsedPersonalAllowance());
        otherEmployeeDetails.setTotalUsedPersonalAllowance(employeeDetailsDTO.getOtherEmployeeDetailsDTO().getTotalUsedPersonalAllowance());
        otherEmployeeDetails.setRemainingPersonalAllowance(employeeDetailsDTO.getTotalPersonalAllowance().subtract(employeeDetailsDTO.getPreviouslyUsedPersonalAllowance()));
        otherEmployeeDetails.setTotalEarningsAmountYTD(employeeDetailsDTO.getPreviousEmploymentDataDTO().getPreviousTotalPayToDate());

        otherEmployeeDetails.setTotalEarningsAmountInThisEmployment(employeeDetailsDTO.getOtherEmployeeDetailsDTO().getTotalEarningsAmountInThisEmployment());
        otherEmployeeDetails.setIncomeTaxPaid(employeeDetailsDTO.getOtherEmployeeDetailsDTO().getIncomeTaxPaid());

        otherEmployeeDetails.setTotalIncomeTaxPaidInThisEmployment(employeeDetailsDTO.getOtherEmployeeDetailsDTO().getTotalIncomeTaxPaidInThisEmployment());
        otherEmployeeDetails.setTotalTaxPayToDate(employeeDetailsDTO.getPreviousEmploymentDataDTO().getPreviousTotalTaxToDate());

        otherEmployeeDetails.setNumberOfPayPeriodsIncomeTaxPaid(employeeDetailsDTO.getOtherEmployeeDetailsDTO().getNumberOfPayPeriodsIncomeTaxPaid() == null ? BigDecimal.ZERO : employeeDetailsDTO.getOtherEmployeeDetailsDTO().getNumberOfPayPeriodsIncomeTaxPaid());

        otherEmployeeDetails.setTotalEmployeeNIContributionInCompany(employeeDetailsDTO.getOtherEmployeeDetailsDTO().getTotalEmployeeNIContributionInCompany());
        otherEmployeeDetails.setEmployeeNIContribution(employeeDetailsDTO.getOtherEmployeeDetailsDTO().getEmployeeNIContribution());
        otherEmployeeDetails.setNumberOfPayPeriodsNIContributions(employeeDetailsDTO.getOtherEmployeeDetailsDTO().getNumberOfPayPeriodsNIContributions()== null ? BigDecimal.ZERO : employeeDetailsDTO.getOtherEmployeeDetailsDTO().getNumberOfPayPeriodsNIContributions());
        otherEmployeeDetails.setEarningsAtLELYtd(employeeDetailsDTO.getOtherEmployeeDetailsDTO().getEarningsAtLELYtd() == null ? BigDecimal.ZERO : employeeDetailsDTO.getOtherEmployeeDetailsDTO().getEarningsAtLELYtd());
        otherEmployeeDetails.setEarningsLelToPtYtd(employeeDetailsDTO.getOtherEmployeeDetailsDTO().getEarningsLelToPtYtd() == null ? BigDecimal.ZERO : employeeDetailsDTO.getOtherEmployeeDetailsDTO().getEarningsLelToPtYtd());
        otherEmployeeDetails.setEarningsPtToUelYtd(employeeDetailsDTO.getOtherEmployeeDetailsDTO().getEarningsPtToUelYtd() == null ? BigDecimal.ZERO : employeeDetailsDTO.getOtherEmployeeDetailsDTO().getEarningsPtToUelYtd());

        otherEmployeeDetails.setTotalAmountPensionContribution(employeeDetailsDTO.getOtherEmployeeDetailsDTO().getTotalAmountPensionContribution());
        otherEmployeeDetails.setNumberOfPayPeriodsPensionContribution(employeeDetailsDTO.getOtherEmployeeDetailsDTO().getNumberOfPayPeriodsPensionContribution()== null ? BigDecimal.ZERO : employeeDetailsDTO.getOtherEmployeeDetailsDTO().getNumberOfPayPeriodsPensionContribution());
        otherEmployeeDetails.setPensionContributeAmount(employeeDetailsDTO.getOtherEmployeeDetailsDTO().getPensionContributeAmount());
        otherEmployeeDetails.setRemainingKCodeAmount(employeeDetailsDTO.getKCodeTaxableAdjustmentAnnual());
        return otherEmployeeDetails;

    }


    public StudentLoanDTO mapToStudentLoanDto(StudentLoan studentLoan) {
        if (studentLoan == null) {
            return null;
        }
        StudentLoanDTO studentLoanDto = new StudentLoanDTO();
        studentLoanDto.setHasStudentLoan(studentLoan.getHasStudentLoan());
        studentLoanDto.setNumberOfPayPeriodsOfStudentLoan(studentLoan.getNumberOfPayPeriodsOfStudentLoan()== null ? BigDecimal.ZERO : studentLoan.getNumberOfPayPeriodsOfStudentLoan());
        studentLoanDto.setDeductionAmountInStudentLoan(studentLoan.getDeductionAmountInStudentLoan());
        studentLoanDto.setTotalDeductionAmountInStudentLoan(studentLoan.getTotalDeductionAmountInStudentLoan());
        studentLoanDto.setStudentLoanPlanType(studentLoan.getStudentLoanPlanType());
//        studentLoanDto.setMonthlyDeductionAmountInStudentLoan(studentLoan.getMonthlyDeductionAmountInStudentLoan());
//        studentLoanDto.setWeeklyDeductionAmountInStudentLoan(studentLoanDto.getWeeklyDeductionAmountInStudentLoan());
//        studentLoanDto.setYearlyDeductionAmountInStudentLoan(studentLoanDto.getYearlyDeductionAmountInStudentLoan());

        return studentLoanDto;
    }
    public StudentLoan mapToStudentLoan(StudentLoanDTO studentLoanDto) {
        if (studentLoanDto == null) {
            return null;
        }
        StudentLoan studentLoan = new StudentLoan();
        studentLoan.setStudentLoanPlanType(studentLoanDto.getStudentLoanPlanType());
        if(studentLoanDto.getStudentLoanPlanType()==StudentLoan.StudentLoanPlan.NONE ||studentLoanDto.getStudentLoanPlanType()==null){
            studentLoan.setHasStudentLoan(false);
        }
        else {
            studentLoan.setHasStudentLoan(studentLoanDto.getHasStudentLoan());
        }
        studentLoan.setNumberOfPayPeriodsOfStudentLoan(studentLoanDto.getNumberOfPayPeriodsOfStudentLoan()== null ? BigDecimal.ZERO : studentLoanDto.getNumberOfPayPeriodsOfStudentLoan());
       studentLoan.setDeductionAmountInStudentLoan(studentLoanDto.getDeductionAmountInStudentLoan());

//       studentLoan.setMonthlyDeductionAmountInStudentLoan(studentLoanDto.getMonthlyDeductionAmountInStudentLoan());
//       studentLoan.setWeeklyDeductionAmountInStudentLoan(studentLoanDto.getWeeklyDeductionAmountInStudentLoan());
//       studentLoan.setYearlyDeductionAmountInStudentLoan(studentLoanDto.getYearlyDeductionAmountInStudentLoan());

       studentLoan.setTotalDeductionAmountInStudentLoan(studentLoanDto.getTotalDeductionAmountInStudentLoan());
       return studentLoan;


    }

    public PostGraduateLoanDTO mapToPostGraduateLoanDto(PostGraduateLoan postGraduateLoan){
        if(postGraduateLoan==null){
            return null;
        }
        PostGraduateLoanDTO postGraduateLoanDto=new PostGraduateLoanDTO();
        postGraduateLoanDto.setHasPostgraduateLoan(postGraduateLoan.getHasPostgraduateLoan());
        postGraduateLoanDto.setPostgraduateLoanPlanType(postGraduateLoan.getPostgraduateLoanPlanType());
        postGraduateLoanDto.setDeductionAmountInPostgraduateLoan(postGraduateLoan.getDeductionAmountInPostgraduateLoan());
        postGraduateLoanDto.setNumberOfPayPeriodsOfPostgraduateLoan(postGraduateLoan.getNumberOfPayPeriodsOfPostgraduateLoan()== null ? BigDecimal.ZERO : postGraduateLoan.getNumberOfPayPeriodsOfPostgraduateLoan());
        postGraduateLoanDto.setTotalDeductionAmountInPostgraduateLoan(postGraduateLoan.getTotalDeductionAmountInPostgraduateLoan());
        /*postGraduateLoanDto.setMonthlyDeductionAmountInPostgraduateLoan(postGraduateLoan.getMonthlyDeductionAmountInPostgraduateLoan());
        postGraduateLoanDto.setWeeklyDeductionAmountInPostgraduateLoan(postGraduateLoan.getWeeklyDeductionAmountInPostgraduateLoan());
        postGraduateLoanDto.setYearlyDeductionAmountInPostgraduateLoan(postGraduateLoan.getYearlyDeductionAmountInPostgraduateLoan());*/
        return  postGraduateLoanDto;
    }

    public PostGraduateLoan mapToPostGraduateLoan(PostGraduateLoanDTO postGraduateLoanDto){
        if(postGraduateLoanDto==null){
            return null;
        }
        PostGraduateLoan postGraduateLoan=new PostGraduateLoan();
        postGraduateLoan.setHasPostgraduateLoan(postGraduateLoanDto.getHasPostgraduateLoan());
        postGraduateLoan.setPostgraduateLoanPlanType(postGraduateLoanDto.getPostgraduateLoanPlanType());
        postGraduateLoan.setTotalDeductionAmountInPostgraduateLoan(postGraduateLoanDto.getTotalDeductionAmountInPostgraduateLoan());
        postGraduateLoan.setDeductionAmountInPostgraduateLoan(postGraduateLoanDto.getDeductionAmountInPostgraduateLoan());
        postGraduateLoan.setNumberOfPayPeriodsOfPostgraduateLoan(postGraduateLoanDto.getNumberOfPayPeriodsOfPostgraduateLoan()== null ? BigDecimal.ZERO : postGraduateLoanDto.getNumberOfPayPeriodsOfPostgraduateLoan());
        /*postGraduateLoan.setMonthlyDeductionAmountInPostgraduateLoan(postGraduateLoanDto.getMonthlyDeductionAmountInPostgraduateLoan());
        postGraduateLoan.setWeeklyDeductionAmountInPostgraduateLoan(postGraduateLoanDto.getWeeklyDeductionAmountInPostgraduateLoan());
        postGraduateLoan.setYearlyDeductionAmountInPostgraduateLoan(postGraduateLoanDto.getYearlyDeductionAmountInPostgraduateLoan());*/
        return  postGraduateLoan;
    }

    public PreviousEmploymentData mapToPreviousEmploymentData(PreviousEmploymentDataDTO previousEmploymentDataDTO) {
        PreviousEmploymentData previousEmployment= new PreviousEmploymentData();
        previousEmployment.setPreviousEmploymentEndDate(previousEmploymentDataDTO.getPreviousEmploymentEndDate());
        previousEmployment.setPreviousTaxCode(previousEmploymentDataDTO.getPreviousTaxCode());
        previousEmployment.setPreviousTotalTaxToDate(previousEmploymentDataDTO.getPreviousTotalTaxToDate());
        previousEmployment.setPreviousTotalPayToDate(previousEmploymentDataDTO.getPreviousTotalPayToDate());
        return previousEmployment;

    }
    public PreviousEmploymentDataDTO changeToEmployeeDetailsDTO(PreviousEmploymentData previousEmploymentData) {
        PreviousEmploymentDataDTO previousEmploymentDataDTO = new PreviousEmploymentDataDTO();
        previousEmploymentDataDTO.setPreviousEmploymentEndDate(previousEmploymentData.getPreviousEmploymentEndDate());
        previousEmploymentDataDTO.setPreviousTaxCode(previousEmploymentData.getPreviousTaxCode());
        previousEmploymentDataDTO.setPreviousTotalTaxToDate(previousEmploymentData.getPreviousTotalTaxToDate());
        previousEmploymentDataDTO.setPreviousTotalPayToDate(previousEmploymentData.getPreviousTotalPayToDate());
        return previousEmploymentDataDTO;
    }

    private boolean checkIfEmergencyTaxCode(String code) {
        if (code == null) return false;
        if(code.matches("1257LM1")||code.matches("1257LW1")||code.matches("1257LX")){
            throw new IllegalArgumentException("Wrong Emergency Code : "+code);
        }

        // Match: e.g. 1257LM1, 1257LW1, 1257LX
        return code.matches("1257L M1")||code.matches("1257L W1")||code.matches("1257L X");
    }

    private BigDecimal  calculateIncomeTaxBasedOnPayPeriod(BigDecimal incomeTax,PayPeriod payPeriod){
        return switch (payPeriod) {
            case WEEKLY -> incomeTax.divide(BigDecimal.valueOf(52), 4, RoundingMode.HALF_UP);
            case MONTHLY -> incomeTax.divide(BigDecimal.valueOf(12), 4, RoundingMode.HALF_UP);
            case YEARLY -> incomeTax;
            default -> throw new IllegalArgumentException("Invalid pay period. Must be WEEKLY, MONTHLY or YEARLY");
        };
    }

    public boolean autoEnrollmentEligibilityForPension(int age, BigDecimal income) {
        return age >= 22 && age <= 65 && income.compareTo(BigDecimal.valueOf(10000)) > 0;
    }

    public boolean isKTaxCode(String normalizedTaxCode){
        if (normalizedTaxCode == null || normalizedTaxCode.isEmpty()) {
            return false;
        }
        normalizedTaxCode = normalizedTaxCode.trim().toUpperCase();
        // Check if the tax code starts with 'K', 'SK', or 'CK'
        return normalizedTaxCode.matches("^K\\d+$") || normalizedTaxCode.matches("^SK\\d+$") || normalizedTaxCode.matches("^CK\\d+$"); // Valid K code format
    }

    public BigDecimal calculateTaxWithKCode( String normalizedTaxCode) {
        // Validate inputs
        if ( normalizedTaxCode == null ) {
            throw new IllegalArgumentException("Parameters cannot be null");
        }


        if (normalizedTaxCode.startsWith("SK") || normalizedTaxCode.startsWith("CK")) {
            // Remove the first character ('S' or 'C') from the code
            String processedCode = normalizedTaxCode.substring(2);
            // Convert the processed code to a numeric value
            String numericPart = processedCode.replaceAll("[^0-9]", "");
            if (numericPart.isEmpty()) {
                return BigDecimal.ZERO; // No numeric allowance
            }
            // Calculate base allowance (numeric part × 10)
            BigDecimal baseAllowance = new BigDecimal(numericPart)
                    .multiply(new BigDecimal("10"))
                    .setScale(0, RoundingMode.HALF_UP);
            logger.info("Base Allowance for K code: {}", baseAllowance);
            return baseAllowance;


        }
        else if (normalizedTaxCode.startsWith("K")) {
            String processedCode = normalizedTaxCode.substring(1);
            // Convert the processed code to a numeric value
            String numericPart = processedCode.replaceAll("[^0-9]", "");
            if (numericPart.isEmpty()) {
                return BigDecimal.ZERO; // No numeric allowance
            }
            // Calculate base allowance (numeric part × 10)
            BigDecimal baseAllowance = new BigDecimal(numericPart)
                    .multiply(new BigDecimal("10"))
                    .setScale(0, RoundingMode.HALF_UP);
            logger.info("Base Allowance for K code: {}", baseAllowance);
            return baseAllowance;

        } else {
            throw new IllegalArgumentException("Invalid K code: " + normalizedTaxCode);

        }

    }

    public boolean hasEmergencySuffix(String taxCode) {
        if (taxCode == null) return false;
        String code = taxCode.trim().toUpperCase();
        return code.endsWith(" W1") || code.endsWith(" M1");
    }


    /*public BigDecimal calculatePreviouslyUsedPersonalAllowance(String taxCode, LocalDate leavingDate){

    }*/




}




