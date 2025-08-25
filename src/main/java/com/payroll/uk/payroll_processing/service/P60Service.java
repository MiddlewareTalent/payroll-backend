package com.payroll.uk.payroll_processing.service;

import com.payroll.uk.payroll_processing.dto.customdto.NIContributionDTO;
import com.payroll.uk.payroll_processing.dto.customdto.P60DTO;
import com.payroll.uk.payroll_processing.dto.customdto.StatutoryPaymentDTO;
import com.payroll.uk.payroll_processing.entity.employee.EmployeeDetails;
import com.payroll.uk.payroll_processing.entity.employer.EmployerDetails;
import com.payroll.uk.payroll_processing.entity.employmentHistory.EmploymentHistory;
import com.payroll.uk.payroll_processing.exception.DataValidationException;
import com.payroll.uk.payroll_processing.repository.EmployeeDetailsRepository;
import com.payroll.uk.payroll_processing.repository.EmployerDetailsRepository;
import com.payroll.uk.payroll_processing.repository.EmploymentHistoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service

public class P60Service {

    private static  final Logger logger = LoggerFactory.getLogger(P60Service.class);

    @Autowired
    private EmployerDetailsRepository employerDetailsRepository;
    @Autowired
    private EmployeeDetailsRepository employeeDetailsRepository;
    @Autowired
    private ValidateData validateData;
    @Autowired
    private EmploymentHistoryRepository employmentHistoryRepository;

    public P60DTO generateP60File(String employeeId){
        logger.info("Generating P60 for employee ID: {}", employeeId);

        if (employeeId==null || employeeId.isEmpty()) {
            throw new DataValidationException("Employee ID cannot be null or empty");
        }
        EmployeeDetails employeeData = employeeDetailsRepository.findByEmployeeId(employeeId).orElseThrow(() -> new DataValidationException("Employee not found with ID: " + employeeId));
        EmployerDetails employerData = employerDetailsRepository.findAll().getFirst();
        if (employerData == null) {
            throw new DataValidationException("Employer details not found");
        }
        logger.info("Validating data for employee ID: {}", employeeId);
        P60DTO dtoData= new P60DTO();
        dtoData.setEmployeeId(employeeData.getEmployeeId());
        dtoData.setFirstName(employeeData.getFirstName());
        dtoData.setLastName(employeeData.getLastName());
        dtoData.setEmployeeNationalInsuranceNumber(employeeData.getNationalInsuranceNumber());
        dtoData.setTaxYear(employeeData.getTaxYear());
        dtoData.setPayrollId(employeeData.getPayrollId());
        dtoData.setPreviousEmploymentPay(employeeData.getPreviousEmploymentData().getPreviousTotalPayToDate()==null? BigDecimal.ZERO:employeeData.getPreviousEmploymentData().getPreviousTotalPayToDate());
        dtoData.setPreviousEmploymentTaxDeducted(employeeData.getPreviousEmploymentData().getPreviousTotalTaxToDate() ==null? BigDecimal.ZERO:employeeData.getPreviousEmploymentData().getPreviousTotalTaxToDate());
        dtoData.setCurrentEmploymentPay(employeeData.getOtherEmployeeDetails().getTotalEarningsAmountInThisEmployment() ==null? BigDecimal.ZERO:employeeData.getOtherEmployeeDetails().getTotalEarningsAmountInThisEmployment());
        dtoData.setCurrentEmploymentTaxDeducted(employeeData.getOtherEmployeeDetails().getTotalIncomeTaxPaidInThisEmployment() ==null? BigDecimal.ZERO:employeeData.getOtherEmployeeDetails().getTotalIncomeTaxPaidInThisEmployment());
        dtoData.setTotalForYearPay(dtoData.getPreviousEmploymentPay().add(dtoData.getCurrentEmploymentPay()));
        dtoData.setTotalForYearTaxDeducted(dtoData.getPreviousEmploymentTaxDeducted().add(dtoData.getCurrentEmploymentTaxDeducted()));
        dtoData.setFinalTaxCode(employeeData.getTaxCode());


        logger.info("basic details set for P60 of employee ID: {}", employeeId);
        // --- Fetch NI letters ---
        List<NIContributionDTO> niContributions = new ArrayList<>(); // mutable list

        List<EmploymentHistory> niChanges = employmentHistoryRepository.findByEmployeeIdAndNiLetterChangedTrue(employeeId);
        logger.info("NI changes found: {}", niChanges);

        if (!niChanges.isEmpty()) {
            niContributions = niChanges.stream()
                    .map(eh -> new NIContributionDTO(
                            eh.getNiLetter().name(),
                            eh.getEarningsAtLELYtd(),
                            eh.getEarningsLelToPtYtd(),
                            eh.getEarningsPtToUelYtd(),
                            eh.getEmployeeNIContribution()
                    ))
                    .collect(Collectors.toCollection(ArrayList::new)); // ensures mutable list
        }

// Add current NI letter if not already in the list
        boolean currentNiLetterExists = niContributions.stream()
                .anyMatch(n -> n.getNiLetter().equals(employeeData.getNiLetter().name()));

        if (!currentNiLetterExists) {
            NIContributionDTO currentNi = new NIContributionDTO(
                    employeeData.getNiLetter().name(),
                    employeeData.getOtherEmployeeDetails().getEarningsAtLELYtd(),
                    employeeData.getOtherEmployeeDetails().getEarningsLelToPtYtd(),
                    employeeData.getOtherEmployeeDetails().getEarningsPtToUelYtd(),
                    employeeData.getOtherEmployeeDetails().getEmployeeNIContribution()
            );
            logger.info("Adding current NI letter to contributions: {}", currentNi);
            niContributions.add(currentNi); // now works
        }

        dtoData.setNiContributions(niContributions);
        logger.info("NI contributions set for P60 of employee ID: {}", employeeId);



        StatutoryPaymentDTO statutoryPayments = new StatutoryPaymentDTO();
        statutoryPayments.setStatutoryMaternityPay(BigDecimal.ZERO);
        statutoryPayments.setStatutoryPaternityPay(BigDecimal.ZERO);
        statutoryPayments.setStatutorySharedParentalPay(BigDecimal.ZERO);
        statutoryPayments.setStatutoryAdoptionPay(BigDecimal.ZERO);
        statutoryPayments.setStatutoryParentalBereavementPay(BigDecimal.ZERO);
        statutoryPayments.setStatutoryNeonatalCarePay(BigDecimal.ZERO);

        dtoData.setStatutoryPayments(statutoryPayments);
        logger.info("Statutory payments set for P60 of employee ID: {}", employeeId);

        dtoData.setStudentLoanDeducted(employeeData.getStudentLoan().getTotalDeductionAmountInStudentLoan() ==null? BigDecimal.ZERO:employeeData.getStudentLoan().getTotalDeductionAmountInStudentLoan());
        dtoData.setPostgraduateLoanDeducted(employeeData.getPostGraduateLoan().getTotalDeductionAmountInPostgraduateLoan() ==null? BigDecimal.ZERO:employeeData.getPostGraduateLoan().getTotalDeductionAmountInPostgraduateLoan());
        logger.info("Loan details set for P60 of employee ID: {}", employeeId);
        dtoData.setEmployeeAddress(employeeData.getAddress());
        dtoData.setEmployeePostCode(employeeData.getPostCode());
        logger.info("Address details set for P60 of employee ID: {}", employeeId);
        dtoData.setEmployerPAYEReference(employerData.getTaxOffice().getPayeReference());
        dtoData.setCompanyName(employerData.getCompanyDetails().getCompanyName());
        dtoData.setCompanyAddress(employerData.getCompanyDetails().getCompanyAddress());
        dtoData.setCompanyPostCode(employerData.getCompanyDetails().getCompanyPostCode());
        logger.info("Employer details set for P60 of employee ID: {}", employeeId);



        return dtoData;


    }

}
