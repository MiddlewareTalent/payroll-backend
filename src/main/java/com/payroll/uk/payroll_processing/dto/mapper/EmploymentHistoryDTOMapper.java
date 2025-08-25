package com.payroll.uk.payroll_processing.dto.mapper;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.payroll.uk.payroll_processing.dto.historydto.EmploymentHistoryDTO;
import com.payroll.uk.payroll_processing.entity.NICategoryLetters;
import com.payroll.uk.payroll_processing.entity.employee.EmployeeDetails;
import com.payroll.uk.payroll_processing.entity.employee.PostGraduateLoan;
import com.payroll.uk.payroll_processing.entity.employee.StudentLoan;
import com.payroll.uk.payroll_processing.entity.employmentHistory.EmploymentHistory;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

@Component
public class EmploymentHistoryDTOMapper {


    public EmploymentHistory changeToEmploymentHistory(EmploymentHistoryDTO employmentHistoryDTO) {
        EmploymentHistory employmentHistory = new EmploymentHistory();
     // Basic Employee Details
        employmentHistory.setId(employmentHistoryDTO.getId());
        employmentHistory.setEmployeeId(employmentHistoryDTO.getEmployeeId());
        employmentHistory.setDateOfBirth(employmentHistoryDTO.getDateOfBirth());
        employmentHistory.setAddress(employmentHistoryDTO.getAddress());
        employmentHistory.setPostCode(employmentHistoryDTO.getPostCode());
        employmentHistory.setAnnualIncomeOfEmployee(employmentHistoryDTO.getAnnualIncomeOfEmployee());
        employmentHistory.setTaxCode(employmentHistoryDTO.getTaxCode());
        employmentHistory.setHasEmergencyCode(employmentHistoryDTO.isHasEmergencyCode());
        employmentHistory.setNationalInsuranceNumber(employmentHistoryDTO.getNationalInsuranceNumber());
        employmentHistory.setNiLetter(employmentHistoryDTO.getNiLetter());
        employmentHistory.setPayrollId(employmentHistoryDTO.getPayrollId());
        // Other Employee Details
        employmentHistory.setTotalTaxablePayInThisEmployment(employmentHistoryDTO.getTotalTaxablePayInThisEmployment());
        employmentHistory.setEarningsAtLELYtd(employmentHistoryDTO.getEarningsAtLELYtd());
        employmentHistory.setEarningsLelToPtYtd(employmentHistoryDTO.getEarningsLelToPtYtd());
        employmentHistory.setEarningsPtToUelYtd(employmentHistoryDTO.getEarningsPtToUelYtd());
        employmentHistory.setNumberOfPayPeriodsEmergencyTaxCodeUsed(employmentHistoryDTO.getNumberOfPayPeriodsEmergencyTaxCodeUsed());
        employmentHistory.setTotalAllowanceUsedDuringEmergencyCode(employmentHistoryDTO.getTotalAllowanceUsedDuringEmergencyCode());
        employmentHistory.setUsedPersonalAllowance(employmentHistoryDTO.getUsedPersonalAllowance());
        employmentHistory.setTotalUsedPersonalAllowance(employmentHistoryDTO.getTotalUsedPersonalAllowance());
        employmentHistory.setRemainingPersonalAllowance(employmentHistoryDTO.getRemainingPersonalAllowance());
        employmentHistory.setTotalEarningsAmountYTD(employmentHistoryDTO.getTotalEarningsAmountYTD());
        employmentHistory.setTotalEarningsAmountInThisEmployment(employmentHistoryDTO.getTotalEarningsAmountInThisEmployment());
        employmentHistory.setTotalIncomeTaxYTD(employmentHistoryDTO.getTotalIncomeTaxYTD());
        employmentHistory.setIncomeTaxPaid(employmentHistoryDTO.getIncomeTaxPaid());
        employmentHistory.setTotalIncomeTaxPaidInThisEmployment(employmentHistoryDTO.getTotalIncomeTaxPaidInThisEmployment());
        employmentHistory.setNumberOfPayPeriodsIncomeTaxPaid(employmentHistoryDTO.getNumberOfPayPeriodsIncomeTaxPaid());
        employmentHistory.setTotalEmployeeNIContributionInCompany(employmentHistoryDTO.getTotalEmployeeNIContributionInCompany());
        employmentHistory.setEmployeeNIContribution(employmentHistoryDTO.getEmployeeNIContribution());
        employmentHistory.setNumberOfPayPeriodsNIContributions(employmentHistoryDTO.getNumberOfPayPeriodsNIContributions());
        employmentHistory.setTotalAmountPensionContribution(employmentHistoryDTO.getTotalAmountPensionContribution());
        employmentHistory.setNumberOfPayPeriodsPensionContribution(employmentHistoryDTO.getNumberOfPayPeriodsPensionContribution());
        employmentHistory.setPensionContributeAmount(employmentHistoryDTO.getPensionContributeAmount());
        employmentHistory.setRemainingKCodeAmount(employmentHistoryDTO.getRemainingKCodeAmount());
        // Student Loan Details
        employmentHistory.setHasStudentLoan(employmentHistoryDTO.getHasStudentLoan());
        employmentHistory.setDeductionAmountInStudentLoan(employmentHistoryDTO.getDeductionAmountInStudentLoan());
        employmentHistory.setNumberOfPayPeriodsOfStudentLoan(employmentHistoryDTO.getNumberOfPayPeriodsOfStudentLoan());
        employmentHistory.setTotalDeductionAmountInStudentLoan(employmentHistoryDTO.getTotalDeductionAmountInStudentLoan());
        employmentHistory.setStudentLoanPlanType(employmentHistoryDTO.getStudentLoanPlanType());
        // Postgraduate Loan Details
        employmentHistory.setHasPostgraduateLoan(employmentHistoryDTO.getHasPostgraduateLoan());
        employmentHistory.setNumberOfPayPeriodsOfPostgraduateLoan(employmentHistoryDTO.getNumberOfPayPeriodsOfPostgraduateLoan());
        employmentHistory.setDeductionAmountInPostgraduateLoan(employmentHistoryDTO.getDeductionAmountInPostgraduateLoan());
        employmentHistory.setTotalDeductionAmountInPostgraduateLoan(employmentHistoryDTO.getTotalDeductionAmountInPostgraduateLoan());
        employmentHistory.setPostgraduateLoanPlanType(employmentHistoryDTO.getPostgraduateLoanPlanType());
        // Change Tracking Flags
        employmentHistory.setTaxCodeChanged(employmentHistoryDTO.isTaxCodeChanged());
        employmentHistory.setNiLetterChanged(employmentHistoryDTO.isNiLetterChanged());
        employmentHistory.setAddressChanged(employmentHistoryDTO.isAddressChanged());
        employmentHistory.setPostCodeChanged(employmentHistoryDTO.isPostCodeChanged());
        employmentHistory.setAnnualIncomeChanged(employmentHistoryDTO.isAnnualIncomeChanged());
        employmentHistory.setDateOfBirthChanged(employmentHistoryDTO.isDateOfBirthChanged());
        employmentHistory.setNationalInsuranceNumberChanged(employmentHistoryDTO.isNationalInsuranceNumberChanged());
        employmentHistory.setPayrollIdChanged(employmentHistoryDTO.isPayrollIdChanged());
        employmentHistory.setStudentLoanPlanTypeChanged(employmentHistoryDTO.isStudentLoanPlanTypeChanged());
        employmentHistory.setPostgraduateLoanPlanTypeChanged(employmentHistoryDTO.isPostgraduateLoanPlanTypeChanged());

        return employmentHistory;
    }
    public EmploymentHistoryDTO mapToEmploymentHistoryDTO(EmploymentHistory employmentHistory){
        EmploymentHistoryDTO employmentHistoryDTO=new EmploymentHistoryDTO();
        employmentHistoryDTO.setId(employmentHistory.getId());
        employmentHistoryDTO.setEmployeeId(employmentHistory.getEmployeeId());
        employmentHistoryDTO.setDateOfBirth(employmentHistory.getDateOfBirth());
        employmentHistoryDTO.setAddress(employmentHistory.getAddress());
        employmentHistoryDTO.setPostCode(employmentHistory.getPostCode());
        employmentHistoryDTO.setAnnualIncomeOfEmployee(employmentHistory.getAnnualIncomeOfEmployee());
        employmentHistoryDTO.setTaxCode(employmentHistory.getTaxCode());
        employmentHistoryDTO.setHasEmergencyCode(employmentHistory.isHasEmergencyCode());
        employmentHistoryDTO.setNationalInsuranceNumber(employmentHistory.getNationalInsuranceNumber());
        employmentHistoryDTO.setNiLetter(employmentHistory.getNiLetter());
        employmentHistoryDTO.setPayrollId(employmentHistory.getPayrollId());
        // Other Employee Details
        employmentHistoryDTO.setTotalTaxablePayInThisEmployment(employmentHistory.getTotalTaxablePayInThisEmployment());
        employmentHistoryDTO.setEarningsAtLELYtd(employmentHistory.getEarningsAtLELYtd());
        employmentHistoryDTO.setEarningsLelToPtYtd(employmentHistory.getEarningsLelToPtYtd());
        employmentHistoryDTO.setEarningsPtToUelYtd(employmentHistory.getEarningsPtToUelYtd());
        employmentHistoryDTO.setNumberOfPayPeriodsEmergencyTaxCodeUsed(employmentHistory.getNumberOfPayPeriodsEmergencyTaxCodeUsed());
        employmentHistoryDTO.setTotalAllowanceUsedDuringEmergencyCode(employmentHistory.getTotalAllowanceUsedDuringEmergencyCode());
        employmentHistoryDTO.setUsedPersonalAllowance(employmentHistory.getUsedPersonalAllowance());
        employmentHistoryDTO.setTotalUsedPersonalAllowance(employmentHistory.getTotalUsedPersonalAllowance());
        employmentHistoryDTO.setRemainingPersonalAllowance(employmentHistory.getRemainingPersonalAllowance());
        employmentHistoryDTO.setTotalEarningsAmountYTD(employmentHistory.getTotalEarningsAmountYTD());
        employmentHistoryDTO.setTotalEarningsAmountInThisEmployment(employmentHistory.getTotalEarningsAmountInThisEmployment());
        employmentHistoryDTO.setTotalIncomeTaxYTD(employmentHistory.getTotalIncomeTaxYTD());
        employmentHistoryDTO.setIncomeTaxPaid(employmentHistory.getIncomeTaxPaid());
        employmentHistoryDTO.setTotalIncomeTaxPaidInThisEmployment(employmentHistory.getTotalIncomeTaxPaidInThisEmployment());
        employmentHistoryDTO.setNumberOfPayPeriodsIncomeTaxPaid(employmentHistory.getNumberOfPayPeriodsIncomeTaxPaid());
        employmentHistoryDTO.setTotalEmployeeNIContributionInCompany(employmentHistory.getTotalEmployeeNIContributionInCompany());
        employmentHistoryDTO.setEmployeeNIContribution(employmentHistory.getEmployeeNIContribution());
        employmentHistoryDTO.setNumberOfPayPeriodsNIContributions(employmentHistory.getNumberOfPayPeriodsNIContributions());
        employmentHistoryDTO.setTotalAmountPensionContribution(employmentHistory.getTotalAmountPensionContribution());
        employmentHistoryDTO.setNumberOfPayPeriodsPensionContribution(employmentHistory.getNumberOfPayPeriodsPensionContribution());
        employmentHistoryDTO.setPensionContributeAmount(employmentHistory.getPensionContributeAmount());
        employmentHistoryDTO.setRemainingKCodeAmount(employmentHistory.getRemainingKCodeAmount());
        // Student Loan Details
        employmentHistoryDTO.setHasStudentLoan(employmentHistory.getHasStudentLoan());
        employmentHistoryDTO.setDeductionAmountInStudentLoan(employmentHistory.getDeductionAmountInStudentLoan());
        employmentHistoryDTO.setNumberOfPayPeriodsOfStudentLoan(employmentHistory.getNumberOfPayPeriodsOfStudentLoan());
        employmentHistoryDTO.setTotalDeductionAmountInStudentLoan(employmentHistory.getTotalDeductionAmountInStudentLoan());
        employmentHistoryDTO.setStudentLoanPlanType(employmentHistory.getStudentLoanPlanType());
        // Postgraduate Loan Details
        employmentHistoryDTO.setHasPostgraduateLoan(employmentHistory.getHasPostgraduateLoan());
        employmentHistoryDTO.setNumberOfPayPeriodsOfPostgraduateLoan(employmentHistory.getNumberOfPayPeriodsOfPostgraduateLoan());
        employmentHistoryDTO.setDeductionAmountInPostgraduateLoan(employmentHistory.getDeductionAmountInPostgraduateLoan());
        employmentHistoryDTO.setTotalDeductionAmountInPostgraduateLoan(employmentHistory.getTotalDeductionAmountInPostgraduateLoan());
        employmentHistoryDTO.setPostgraduateLoanPlanType(employmentHistory.getPostgraduateLoanPlanType());

        // Change Tracking Flags
        employmentHistoryDTO.setTaxCodeChanged(employmentHistory.isTaxCodeChanged());
        employmentHistoryDTO.setNiLetterChanged(employmentHistory.isNiLetterChanged());
        employmentHistoryDTO.setAddressChanged(employmentHistory.isAddressChanged());
        employmentHistoryDTO.setPostCodeChanged(employmentHistory.isPostCodeChanged());
        employmentHistoryDTO.setAnnualIncomeChanged(employmentHistory.isAnnualIncomeChanged());
        employmentHistoryDTO.setDateOfBirthChanged(employmentHistory.isDateOfBirthChanged());
        employmentHistoryDTO.setNationalInsuranceNumberChanged(employmentHistory.isNationalInsuranceNumberChanged());
        employmentHistoryDTO.setPayrollIdChanged(employmentHistory.isPayrollIdChanged());
        employmentHistoryDTO.setStudentLoanPlanTypeChanged(employmentHistory.isStudentLoanPlanTypeChanged());
        employmentHistoryDTO.setPostgraduateLoanPlanTypeChanged(employmentHistory.isPostgraduateLoanPlanTypeChanged());

        return employmentHistoryDTO;
    }

    public EmploymentHistory updatedToEmploymentHistory(EmployeeDetails employeeDetails){
        EmploymentHistory employmentHistory=new EmploymentHistory();
        employmentHistory.setEmployeeId(employeeDetails.getEmployeeId());
        employmentHistory.setDateOfBirth(employeeDetails.getDateOfBirth());
        employmentHistory.setAddress(employeeDetails.getAddress());
        employmentHistory.setPostCode(employeeDetails.getPostCode());
        employmentHistory.setAnnualIncomeOfEmployee(employeeDetails.getAnnualIncomeOfEmployee());
        employmentHistory.setTaxCode(employeeDetails.getTaxCode());
        employmentHistory.setHasEmergencyCode(employeeDetails.isHasEmergencyCode());
        employmentHistory.setNationalInsuranceNumber(employeeDetails.getNationalInsuranceNumber());
        employmentHistory.setNiLetter(employeeDetails.getNiLetter());
        employmentHistory.setPayrollId(employeeDetails.getPayrollId());
        // Other Employee Details
        employmentHistory.setTotalTaxablePayInThisEmployment(employeeDetails.getOtherEmployeeDetails().getTotalTaxablePayInThisEmployment());
        employmentHistory.setEarningsAtLELYtd(employeeDetails.getOtherEmployeeDetails().getEarningsAtLELYtd());
        employmentHistory.setEarningsLelToPtYtd(employeeDetails.getOtherEmployeeDetails().getEarningsLelToPtYtd());
        employmentHistory.setEarningsPtToUelYtd(employeeDetails.getOtherEmployeeDetails().getEarningsPtToUelYtd());
        employmentHistory.setNumberOfPayPeriodsEmergencyTaxCodeUsed(employeeDetails.getOtherEmployeeDetails().getNumberOfPayPeriodsEmergencyTaxCodeUsed());
        employmentHistory.setTotalAllowanceUsedDuringEmergencyCode(employeeDetails.getOtherEmployeeDetails().getTotalAllowanceUsedDuringEmergencyCode());
        employmentHistory.setUsedPersonalAllowance(employeeDetails.getOtherEmployeeDetails().getUsedPersonalAllowance());
        employmentHistory.setTotalUsedPersonalAllowance(employeeDetails.getOtherEmployeeDetails().getTotalUsedPersonalAllowance());
        employmentHistory.setRemainingPersonalAllowance(employeeDetails.getOtherEmployeeDetails().getRemainingPersonalAllowance());
        employmentHistory.setTotalEarningsAmountYTD(employeeDetails.getOtherEmployeeDetails().getTotalEarningsAmountYTD());
        employmentHistory.setTotalEarningsAmountInThisEmployment(employeeDetails.getOtherEmployeeDetails().getTotalEarningsAmountInThisEmployment());
        employmentHistory.setTotalIncomeTaxYTD(employeeDetails.getOtherEmployeeDetails().getTotalIncomeTaxYTD());
        employmentHistory.setIncomeTaxPaid(employeeDetails.getOtherEmployeeDetails().getIncomeTaxPaid());
        employmentHistory.setTotalIncomeTaxPaidInThisEmployment(employeeDetails.getOtherEmployeeDetails().getTotalIncomeTaxPaidInThisEmployment());
        employmentHistory.setNumberOfPayPeriodsIncomeTaxPaid(employeeDetails.getOtherEmployeeDetails().getNumberOfPayPeriodsIncomeTaxPaid());
        employmentHistory.setTotalEmployeeNIContributionInCompany(employeeDetails.getOtherEmployeeDetails().getTotalEmployeeNIContributionInCompany());
        employmentHistory.setEmployeeNIContribution(employeeDetails.getOtherEmployeeDetails().getEmployeeNIContribution());
        employmentHistory.setNumberOfPayPeriodsNIContributions(employeeDetails.getOtherEmployeeDetails().getNumberOfPayPeriodsNIContributions());
        employmentHistory.setTotalAmountPensionContribution(employeeDetails.getOtherEmployeeDetails().getTotalAmountPensionContribution());
        employmentHistory.setNumberOfPayPeriodsPensionContribution(employeeDetails.getOtherEmployeeDetails().getNumberOfPayPeriodsPensionContribution());
        employmentHistory.setPensionContributeAmount(employeeDetails.getOtherEmployeeDetails().getPensionContributeAmount());
        employmentHistory.setRemainingKCodeAmount(employeeDetails.getOtherEmployeeDetails().getRemainingKCodeAmount());
        // Student Loan Details
        employmentHistory.setHasStudentLoan(employeeDetails.getStudentLoan().getHasStudentLoan());
        employmentHistory.setDeductionAmountInStudentLoan(employeeDetails.getStudentLoan().getDeductionAmountInStudentLoan());
        employmentHistory.setNumberOfPayPeriodsOfStudentLoan(employeeDetails.getStudentLoan().getNumberOfPayPeriodsOfStudentLoan());
        employmentHistory.setTotalDeductionAmountInStudentLoan(employeeDetails.getStudentLoan().getTotalDeductionAmountInStudentLoan());
        employmentHistory.setStudentLoanPlanType(employeeDetails.getStudentLoan().getStudentLoanPlanType());
        // Postgraduate Loan Details
        employmentHistory.setHasPostgraduateLoan(employeeDetails.getPostGraduateLoan().getHasPostgraduateLoan());
        employmentHistory.setNumberOfPayPeriodsOfPostgraduateLoan(employeeDetails.getPostGraduateLoan().getNumberOfPayPeriodsOfPostgraduateLoan());
        employmentHistory.setDeductionAmountInPostgraduateLoan(employeeDetails.getPostGraduateLoan().getDeductionAmountInPostgraduateLoan());
        employmentHistory.setTotalDeductionAmountInPostgraduateLoan(employeeDetails.getPostGraduateLoan().getTotalDeductionAmountInPostgraduateLoan());
        employmentHistory.setPostgraduateLoanPlanType(employeeDetails.getPostGraduateLoan().getPostgraduateLoanPlanType());
        return employmentHistory;

    }
    public EmploymentHistory updatedToEmploymentHistory(EmployeeDetails employeeDetails, Map<String, Boolean> changedFields){
        EmploymentHistory history = updatedToEmploymentHistory(employeeDetails); // your existing copy logic

        // Set flags based on Map
        history.setTaxCodeChanged(changedFields.getOrDefault("taxCodeChanged", false));
        history.setNiLetterChanged(changedFields.getOrDefault("niLetterChanged", false));
        history.setAddressChanged(changedFields.getOrDefault("addressChanged", false));
        history.setPostCodeChanged(changedFields.getOrDefault("postCodeChanged", false));
        history.setAnnualIncomeChanged(changedFields.getOrDefault("annualIncomeChanged", false));
        history.setDateOfBirthChanged(changedFields.getOrDefault("dateOfBirthChanged", false));
        history.setNationalInsuranceNumberChanged(changedFields.getOrDefault("nationalInsuranceNumberChanged", false));
        history.setPayrollIdChanged(changedFields.getOrDefault("payrollIdChanged", false));
        history.setStudentLoanPlanTypeChanged(changedFields.getOrDefault("studentLoanPlanTypeChanged", false));
        history.setPostgraduateLoanPlanTypeChanged(changedFields.getOrDefault("postgraduateLoanPlanTypeChanged", false));

        return history;
    }

}





