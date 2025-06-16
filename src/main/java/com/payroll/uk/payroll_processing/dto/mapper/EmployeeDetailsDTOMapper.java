package com.payroll.uk.payroll_processing.dto.mapper;

import com.payroll.uk.payroll_processing.dto.BankDetailsDTO;
import com.payroll.uk.payroll_processing.dto.employeedto.EmployeeDetailsDTO;
import com.payroll.uk.payroll_processing.dto.employeedto.OtherEmployeeDetailsDTO;
import com.payroll.uk.payroll_processing.dto.employeedto.PostGraduateLoanDTO;
import com.payroll.uk.payroll_processing.dto.employeedto.StudentLoanDTO;
import com.payroll.uk.payroll_processing.entity.BankDetails;
import com.payroll.uk.payroll_processing.entity.PayPeriod;
import com.payroll.uk.payroll_processing.entity.employee.EmployeeDetails;
import com.payroll.uk.payroll_processing.entity.employee.OtherEmployeeDetails;
import com.payroll.uk.payroll_processing.entity.employee.PostGraduateLoan;
import com.payroll.uk.payroll_processing.entity.employee.StudentLoan;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
public class EmployeeDetailsDTOMapper {

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
        employeeDetailsDTO.setEmploymentType(employeeDetails.getEmploymentType());
        employeeDetailsDTO.setIsDirector(employeeDetails.getIsDirector());
        employeeDetailsDTO.setGender(employeeDetails.getGender());
        employeeDetailsDTO.setWorkingCompanyName(employeeDetails.getWorkingCompanyName());
        employeeDetailsDTO.setEmployeeDepartment(employeeDetails.getEmployeeDepartment());
        employeeDetailsDTO.setEmploymentStartedDate(employeeDetails.getEmploymentStartedDate());
        employeeDetailsDTO.setEmploymentEndDate(employeeDetails.getEmploymentEndDate());
        employeeDetailsDTO.setAnnualIncomeOfEmployee(employeeDetails.getAnnualIncomeOfEmployee());
        employeeDetailsDTO.setTaxCode(employeeDetails.getTaxCode());
        employeeDetailsDTO.setIsEmergencyCode(employeeDetails.getIsEmergencyCode());
//        employeeDetailsDTO.setIsPostgraduateLoan(employeeDetails.getIsPostgraduateLoan());
//        employeeDetailsDTO.setStudentLoan(employeeDetails.getStudentLoan());
        employeeDetailsDTO.setPayPeriod(employeeDetails.getPayPeriod());
        employeeDetailsDTO.setNationalInsuranceNumber(employeeDetails.getNationalInsuranceNumber());
        employeeDetailsDTO.setNiLetter(employeeDetails.getNiLetter());

//        employeeDetailsDTO.setBankDetailsDTO(mapToBanKDetailsDTO(employeeDetails.getBankDetails()));
//        employeeDetailsDTO.setOtherEmployeeDetailsDTO(mapToOtherEmployeeDetailsDTO(employeeDetails.getOtherEmployeeDetails()));
        employeeDetailsDTO.setEmployerId(employeeDetails.getEmployerId());
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
    public OtherEmployeeDetailsDTO mapToOtherEmployeeDetailsDTO(OtherEmployeeDetails otherEmployeeDetails) {
        OtherEmployeeDetailsDTO otherEmployeeDetailsDTO = new OtherEmployeeDetailsDTO();
//        otherEmployeeDetailsDTO.setPreviouslyUsedPersonalAllowance(otherEmployeeDetails.getPreviouslyUsedPersonalAllowance());
//        otherEmployeeDetailsDTO.setTotalPersonalAllowanceInCompany(otherEmployeeDetails.getTotalPersonalAllowanceInCompany());
        otherEmployeeDetailsDTO.setUsedPersonalAllowance(otherEmployeeDetails.getUsedPersonalAllowance());
        otherEmployeeDetailsDTO.setTotalUsedPersonalAllowance(otherEmployeeDetails.getTotalUsedPersonalAllowance());
        otherEmployeeDetailsDTO.setRemainingPersonalAllowance(otherEmployeeDetails.getRemainingPersonalAllowance());
        otherEmployeeDetailsDTO.setIncomeTaxPaid(otherEmployeeDetails.getIncomeTaxPaid());
        otherEmployeeDetailsDTO.setTotalIncomeTaxPaidInCompany(otherEmployeeDetails.getTotalIncomeTaxPaidInCompany());
        otherEmployeeDetailsDTO.setNumberOfMonthsOfIncomeTaxPaid(otherEmployeeDetails.getNumberOfMonthsOfIncomeTaxPaid());
        otherEmployeeDetailsDTO.setNumberOfYearsOfIncomeTaxPaid(otherEmployeeDetails.getNumberOfYearsOfIncomeTaxPaid());
        otherEmployeeDetailsDTO.setNumberOfWeeksOfIncomeTaxPaid(otherEmployeeDetails.getNumberOfWeeksOfIncomeTaxPaid());
        otherEmployeeDetailsDTO.setEmployeeNIContribution(otherEmployeeDetails.getEmployeeNIContribution());
        otherEmployeeDetailsDTO.setTotalEmployeeNIContributionInCompany(otherEmployeeDetails.getTotalEmployeeNIContributionInCompany());
        otherEmployeeDetailsDTO.setNumberOfMonthsOfNIContributions(otherEmployeeDetails.getNumberOfMonthsOfNIContributions());
        otherEmployeeDetailsDTO.setNumberOfWeeksOfNIContributions(otherEmployeeDetails.getNumberOfWeeksOfNIContributions());
        otherEmployeeDetailsDTO.setNumberOfYearsOfNIContributions(otherEmployeeDetails.getNumberOfYearsOfNIContributions());
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
        employeeDetails.setIsDirector(employeeDetailsDTO.getIsDirector());
        employeeDetails.setGender(employeeDetailsDTO.getGender());
        employeeDetails.setEmployeeDepartment(employeeDetailsDTO.getEmployeeDepartment());
        employeeDetails.setEmploymentStartedDate(employeeDetailsDTO.getEmploymentStartedDate());
        employeeDetails.setPayPeriod(employeeDetailsDTO.getPayPeriod());
        employeeDetails.setEmploymentEndDate(employeeDetailsDTO.getEmploymentEndDate());
        employeeDetails.setAnnualIncomeOfEmployee(employeeDetailsDTO.getAnnualIncomeOfEmployee());
        employeeDetails.setPayPeriodOfIncomeOfEmployee(calculateIncomeTaxBasedOnPayPeriod(employeeDetailsDTO.getAnnualIncomeOfEmployee(),employeeDetailsDTO.getPayPeriod()));
        employeeDetails.setTaxCode(employeeDetailsDTO.getTaxCode());
        if(!employeeDetailsDTO.getIsEmergencyCode()){
            employeeDetails.setIsEmergencyCode(checkIfEmergencyTaxCode(employeeDetails.getTaxCode()));
        }else {
            employeeDetails.setIsEmergencyCode(employeeDetailsDTO.getIsEmergencyCode());
        }
//        employeeDetails.setIsPostgraduateLoan(employeeDetailsDTO.getIsPostgraduateLoan());
//        employeeDetails.setStudentLoan(employeeDetailsDTO.getStudentLoan());

        employeeDetails.setNationalInsuranceNumber(employeeDetailsDTO.getNationalInsuranceNumber());
        employeeDetails.setNiLetter(employeeDetailsDTO.getNiLetter());
        employeeDetails.setEmployerId(employeeDetailsDTO.getEmployerId());
        employeeDetails.setPreviouslyUsedPersonalAllowance(employeeDetailsDTO.getPreviouslyUsedPersonalAllowance());
        employeeDetails.setTotalPersonalAllowance(employeeDetailsDTO.getTotalPersonalAllowance());


//        BankDetails bankDetails = mapToBankDetails(employeeDetailsDTO);
//        employeeDetails.setBankDetails(bankDetails);

//        OtherEmployeeDetailsDTO otherEmployeeDetailsDTO = employeeDetailsDTO.getOtherEmployeeDetailsDTO();
//        OtherEmployeeDetails otherEmployeeDetails = getOtherEmployeeDetails(otherEmployeeDetailsDTO);
//        employeeDetails.setOtherEmployeeDetails(otherEmployeeDetails);

        // Null check before mapping nested DTOs
        if (employeeDetailsDTO.getBankDetailsDTO() != null) {
            employeeDetails.setBankDetails(mapToBankDetails(employeeDetailsDTO));
        }

        if (employeeDetailsDTO.getOtherEmployeeDetailsDTO() != null) {
            employeeDetails.setOtherEmployeeDetails(mapToOtherEmployeeDetails(employeeDetailsDTO.getOtherEmployeeDetailsDTO()));
        }
        if (employeeDetailsDTO.getStudentLoanDto()!=null){
            employeeDetails.setStudentLoan(mapToStudentLoan(employeeDetailsDTO.getStudentLoanDto()));
        }
        if (employeeDetailsDTO.getPostGraduateLoanDto()!=null){
            employeeDetails.setPostGraduateLoan(mapToPostGraduateLoan(employeeDetailsDTO.getPostGraduateLoanDto()));
        }


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
    public OtherEmployeeDetails mapToOtherEmployeeDetails(OtherEmployeeDetailsDTO otherEmployeeDetailsDTO) {
        OtherEmployeeDetails otherEmployeeDetails = new OtherEmployeeDetails();
//        otherEmployeeDetails.setPreviouslyUsedPersonalAllowance(otherEmployeeDetailsDTO.getPreviouslyUsedPersonalAllowance());
//        otherEmployeeDetails.setTotalPersonalAllowanceInCompany(otherEmployeeDetailsDTO.getTotalPersonalAllowanceInCompany());
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
    public StudentLoanDTO mapToStudentLoanDto(StudentLoan studentLoan) {
        if (studentLoan == null) {
            return null;
        }
        StudentLoanDTO studentLoanDto = new StudentLoanDTO();
        studentLoanDto.setHasStudentLoan(studentLoan.getHasStudentLoan());
        studentLoanDto.setMonthlyDeductionAmountInStudentLoan(studentLoan.getMonthlyDeductionAmountInStudentLoan());
        studentLoanDto.setWeeklyDeductionAmountInStudentLoan(studentLoanDto.getWeeklyDeductionAmountInStudentLoan());
        studentLoanDto.setYearlyDeductionAmountInStudentLoan(studentLoanDto.getYearlyDeductionAmountInStudentLoan());
        studentLoanDto.setTotalDeductionAmountInStudentLoan(studentLoan.getTotalDeductionAmountInStudentLoan());
        studentLoanDto.setStudentLoanPlanType(studentLoan.getStudentLoanPlanType());

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
       studentLoan.setMonthlyDeductionAmountInStudentLoan(studentLoanDto.getMonthlyDeductionAmountInStudentLoan());
       studentLoan.setWeeklyDeductionAmountInStudentLoan(studentLoanDto.getWeeklyDeductionAmountInStudentLoan());
       studentLoan.setYearlyDeductionAmountInStudentLoan(studentLoanDto.getYearlyDeductionAmountInStudentLoan());
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
        postGraduateLoanDto.setMonthlyDeductionAmountInPostgraduateLoan(postGraduateLoan.getMonthlyDeductionAmountInPostgraduateLoan());
        postGraduateLoanDto.setWeeklyDeductionAmountInPostgraduateLoan(postGraduateLoan.getWeeklyDeductionAmountInPostgraduateLoan());
        postGraduateLoanDto.setYearlyDeductionAmountInPostgraduateLoan(postGraduateLoan.getYearlyDeductionAmountInPostgraduateLoan());
        postGraduateLoanDto.setTotalDeductionAmountInPostgraduateLoan(postGraduateLoan.getTotalDeductionAmountInPostgraduateLoan());
        return  postGraduateLoanDto;
    }

    public PostGraduateLoan mapToPostGraduateLoan(PostGraduateLoanDTO postGraduateLoanDto){
        if(postGraduateLoanDto==null){
            return null;
        }
        PostGraduateLoan postGraduateLoan=new PostGraduateLoan();
        postGraduateLoan.setHasPostgraduateLoan(postGraduateLoanDto.getHasPostgraduateLoan());
        postGraduateLoan.setPostgraduateLoanPlanType(postGraduateLoanDto.getPostgraduateLoanPlanType());
        postGraduateLoan.setMonthlyDeductionAmountInPostgraduateLoan(postGraduateLoanDto.getMonthlyDeductionAmountInPostgraduateLoan());
        postGraduateLoan.setWeeklyDeductionAmountInPostgraduateLoan(postGraduateLoanDto.getWeeklyDeductionAmountInPostgraduateLoan());
        postGraduateLoan.setYearlyDeductionAmountInPostgraduateLoan(postGraduateLoanDto.getYearlyDeductionAmountInPostgraduateLoan());
        postGraduateLoan.setTotalDeductionAmountInPostgraduateLoan(postGraduateLoanDto.getTotalDeductionAmountInPostgraduateLoan());
        return  postGraduateLoan;
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


}




