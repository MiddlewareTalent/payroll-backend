package com.payroll.uk.payroll_processing.service.payslip;

import com.payroll.uk.payroll_processing.dto.LoanCalculationPaySlipDTO;
import com.payroll.uk.payroll_processing.dto.PaySlipCreateDto;
import com.payroll.uk.payroll_processing.dto.PensionCalculationPaySlipDTO;
import com.payroll.uk.payroll_processing.dto.mapper.PaySlipCreateDTOMapper;
import com.payroll.uk.payroll_processing.entity.PaySlip;
import com.payroll.uk.payroll_processing.entity.employee.EmployeeDetails;
import com.payroll.uk.payroll_processing.entity.employer.EmployerDetails;
import com.payroll.uk.payroll_processing.exception.*;
import com.payroll.uk.payroll_processing.repository.EmployeeDetailsRepository;
import com.payroll.uk.payroll_processing.repository.EmployerDetailsRepository;
import com.payroll.uk.payroll_processing.repository.PaySlipRepository;
import com.payroll.uk.payroll_processing.service.*;
import com.payroll.uk.payroll_processing.service.incometax.TaxCodeService;
import com.payroll.uk.payroll_processing.service.ni.NationalInsuranceCalculation;
import com.payroll.uk.payroll_processing.service.ni.NationalInsuranceCalculator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

@Service
public class PaySlipGeneration {
    private static final Logger logger = LoggerFactory.getLogger(PaySlipGeneration.class);
    @Autowired
    private PaySlipRepository paySlipRepository;
    @Autowired
    private EmployeeDetailsRepository employeeDetailsRepository;
    @Autowired
    private EmployerDetailsRepository employerDetailsRepository;
    @Autowired
    private TaxCodeService taxCodeService;
    @Autowired
    private PersonalAllowanceCalculation personalAllowanceCalculation;
    @Autowired
    private NationalInsuranceCalculation nationalInsuranceCalculation;
    @Autowired
    private PaySlipCreateDTOMapper paySlipCreateDtoMapper;
    @Autowired
    private NationalInsuranceCalculator nationalInsuranceCalculator;
    @Autowired
    private StudentLoanCalculation studentLoanCalculation;

    @Autowired
    private UpdatingDetails updatingDetails;
    @Autowired
    private LoanPaySlipCalculation loanPaySlipCalculation;

    @Autowired
    private PensionCalculation pensionCalculation;

    @Autowired
    private ValidateData validateData;



    @Transactional
     PaySlipCreateDto fillPaySlip(String employeeId){
        if (employeeId.isBlank()) {
            throw new IllegalArgumentException("Employee ID cannot be null or empty");
        }
        EmployeeDetails employeeDetails = employeeDetailsRepository.findByEmployeeId(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with ID: " + employeeId));

        logger.info("employeeDetails: {}", employeeDetails);
        EmployerDetails  employerDetails = employerDetailsRepository.findAll().getFirst();
        logger.info("employerDetails: {}", employerDetails);
        if (employerDetails == null) {
            throw new DataValidationException("Employer details not found");
        }
        String periodEnd = getPeriodEndMonthYear(employerDetails.getCompanyDetails().getPayDate());

//        boolean exists = paySlipRepository.existsByEmployeeIdAndPeriodEnd(employeeDetails.getEmployeeId(), periodEnd);
//        logger.info("exists: {}", exists);
//        if (exists) {
//            logger.error("Payslip already exists for employee ID: {} and period end: {}", employeeId, periodEnd);
//            throw new ResourceConflictException("Payslip already exists for " + periodEnd + " for employee ID " + employeeId);
//        }

         validateData.validateEmployeeDetails(employeeDetails);
         validateData.validateEmployerDetails(employerDetails);
         validateTaxCodeAndEmergencyFlag(employeeDetails);

         PaySlip paySlipCreate = new PaySlip();
        try{
            paySlipCreate.setFirstName(employeeDetails.getFirstName());
            paySlipCreate.setLastName(employeeDetails.getLastName());
            paySlipCreate.setAddress(employeeDetails.getAddress());
            paySlipCreate.setPostCode(employeeDetails.getPostCode());
            paySlipCreate.setEmployeeId(employeeDetails.getEmployeeId());
            paySlipCreate.setRegion(employeeDetails.getRegion());
            paySlipCreate.setTaxYear(employerDetails.getCompanyDetails().getCurrentTaxYear());
            paySlipCreate.setTaxCode(employeeDetails.getTaxCode());
            paySlipCreate.setNI_Number(employeeDetails.getNationalInsuranceNumber());
            paySlipCreate.setNiLetter(employeeDetails.getNiLetter());
            paySlipCreate.setWorkingCompanyName(employeeDetails.getWorkingCompanyName());
            paySlipCreate.setPayPeriod(String.valueOf(employeeDetails.getPayPeriod()));
            paySlipCreate.setPayDate(employerDetails.getCompanyDetails().getPayDate());
            paySlipCreate.setPeriodEnd(getPeriodEndMonthYear(paySlipCreate.getPayDate()));
            logger.info("successfully completed  up to basic details");
        }
        catch (Exception e){
            logger.error("Error occurred while setting basic details: {}", e.getMessage());
            throw new DataValidationException("Failed to set basic details for payslip", e);
        }
        paySlipCreate.setGrossPayTotal(employeeDetails.getPayPeriodOfIncomeOfEmployee());



        try{
            BigDecimal personalAllowance=BigDecimal.ZERO;
            if(!employeeDetails.isHasEmergencyCode()){
                BigDecimal personal = personalAllowanceCalculation.calculatePersonalAllowance(
                         paySlipCreate.getTaxCode(),paySlipCreate.getPayPeriod()
                );
                personalAllowance=calculateIncomeTaxBasedOnPayPeriod(personal, paySlipCreate.getPayPeriod());

            }

            else {
                String taxCode = getBaseTaxCode(paySlipCreate.getTaxCode());
                BigDecimal personal=personalAllowanceCalculation.calculatePersonalAllowance(taxCode,paySlipCreate.getPayPeriod());
                personalAllowance=calculateIncomeTaxBasedOnPayPeriod(personal, paySlipCreate.getPayPeriod());

            }

            BigDecimal remainingPersonalAllowance = employeeDetailsRepository.findByRemainingPersonalAllowance(employeeDetails.getEmployeeId());


            if (remainingPersonalAllowance.compareTo(BigDecimal.ZERO) <= 0) {
                // If no personal allowance left
                remainingPersonalAllowance = BigDecimal.ZERO;
                paySlipCreate.setPersonalAllowance(remainingPersonalAllowance);
            }
            else if (personalAllowance.compareTo(remainingPersonalAllowance) >= 0) {
                // If this month's allowance is more than what's remaining
                paySlipCreate.setPersonalAllowance(remainingPersonalAllowance);
            }
            else {
                // Apply the full amount scheduled for this pay period
                paySlipCreate.setPersonalAllowance(personalAllowance);
            }
            logger.info("personalAllowance: {} ", paySlipCreate.getPersonalAllowance());
        }
        catch (Exception e){
            logger.error("Error occurred while calculating personal allowance: {}", e.getMessage());
            throw new InvalidComputationException("Failed to calculate personal allowance for payslip", e);
        }
        // Check if K Code adjustment is needed
        BigDecimal currentKCodeAmount=BigDecimal.ZERO;
        if (updatingDetails.isKTaxCode(paySlipCreate.getTaxCode())) {
             currentKCodeAmount = updatingDetails.applyKCodeAdjustment(employeeDetails, paySlipCreate.getPayPeriod());

            logger.info("Current K Code Amount: {}", currentKCodeAmount);
        }

        //taxable income calculation
        try {

            if (updatingDetails.isKTaxCode(paySlipCreate.getTaxCode())) {
                logger.info("K code tax code in taxable income: {}",paySlipCreate.getGrossPayTotal().add(currentKCodeAmount));
                paySlipCreate.setTaxableIncome(
                        taxCodeService.calculateTaxableIncome(
                                paySlipCreate.getGrossPayTotal().add(currentKCodeAmount), paySlipCreate.getPersonalAllowance()
                        ));

            }
            else {
                paySlipCreate.setTaxableIncome(
                        taxCodeService.calculateTaxableIncome(
                                paySlipCreate.getGrossPayTotal(), paySlipCreate.getPersonalAllowance()
                        ));
            }

        }
        catch (Exception e){
            logger.error("Error occurred while calculating taxable income: {}", e.getMessage());
            throw new InvalidComputationException("Failed to calculate taxable income for payslip", e);
        }
        // income tax calculation
        try {
            BigDecimal incomeTax;
            if (updatingDetails.isKTaxCode(paySlipCreate.getTaxCode())) {
                logger.info("K code tax code in income tax: {} ",paySlipCreate.getGrossPayTotal().add(currentKCodeAmount));
                 incomeTax = taxCodeService.calculateIncomeBasedOnTaxCode(
                        paySlipCreate.getGrossPayTotal().add(currentKCodeAmount), paySlipCreate.getPersonalAllowance(), paySlipCreate.getTaxableIncome(), paySlipCreate.getTaxYear(),
                        paySlipCreate.getRegion(), paySlipCreate.getTaxCode(), paySlipCreate.getPayPeriod()
                );
            }
            else {

                if (employeeDetails.isHasEmergencyCode()){

                String taxCode=getBaseTaxCode(paySlipCreate.getTaxCode());
                    logger.info("Emergency tax code in income tax: {} ",taxCode);
                    incomeTax = taxCodeService.calculateIncomeBasedOnTaxCode(
                            paySlipCreate.getGrossPayTotal(), paySlipCreate.getPersonalAllowance(), paySlipCreate.getTaxableIncome(), paySlipCreate.getTaxYear(),
                            paySlipCreate.getRegion(), taxCode, paySlipCreate.getPayPeriod()
                    );
                }
                else {
                    logger.info("Normal tax code in income tax: {} ",paySlipCreate.getTaxCode());
                    incomeTax = taxCodeService.calculateIncomeBasedOnTaxCode(
                            paySlipCreate.getGrossPayTotal(), paySlipCreate.getPersonalAllowance(), paySlipCreate.getTaxableIncome(), paySlipCreate.getTaxYear(),
                            paySlipCreate.getRegion(), paySlipCreate.getTaxCode(), paySlipCreate.getPayPeriod()
                    );
                }
            }


            logger.info("incomeTax {} ", incomeTax);
            paySlipCreate.setIncomeTaxTotal(incomeTax);
            logger.info("successfully completed the income tax calculation");
        }
        catch (Exception e){
            logger.error("Error occurred while calculating income tax: {}", e.getMessage());
            throw new InvalidComputationException("Failed to calculate income tax for payslip", e);
        }
        // National Insurance calculation

         HashMap<String,BigDecimal> niContributionData;
        try {
            niContributionData = nationalInsuranceCalculator.calculateEmployeeNIContribution(paySlipCreate.getGrossPayTotal(), paySlipCreate.getTaxYear(),
                    paySlipCreate.getPayPeriod(), paySlipCreate.getNiLetter());
            BigDecimal NI = niContributionData.get("niContribution");
            paySlipCreate.setEmployeeNationalInsurance(NI);
            paySlipCreate.setEarningsAtLEL(niContributionData.get("earningsAtLEL"));
            paySlipCreate.setEarningsLelToPt(niContributionData.get("earningsLelToPt"));
            paySlipCreate.setEarningsPtToUel(niContributionData.get("earningsPtToUel"));
            logger.info("successfully completed the Employee NI calculation ");

            paySlipCreate.setEmployersNationalInsurance(
                    nationalInsuranceCalculator.calculatingEmployerNIContribution(
                            paySlipCreate.getGrossPayTotal(), paySlipCreate.getTaxYear(),
                            paySlipCreate.getPayPeriod(), paySlipCreate.getNiLetter()
                    ));
            logger.info("successfully completed the Employers NI Calculation");
        }
        catch (Exception e){
            logger.error("Error occurred while calculating National Insurance: {}", e.getMessage());
            throw new InvalidComputationException("Failed to calculate National Insurance for payslip", e);
        }
        // Student Loan and Postgraduate Loan calculation
        try {
            paySlipCreate.setHasStudentLoanStart(employeeDetails.getStudentLoan().getHasStudentLoan());
            paySlipCreate.setStudentLoanPlanType(employeeDetails.getStudentLoan().getStudentLoanPlanType());
            if (paySlipCreate.getHasStudentLoanStart()) {
                paySlipCreate.setStudentLoanDeductionAmount
                        (studentLoanCalculation.calculateStudentLoan(paySlipCreate.getGrossPayTotal(), paySlipCreate.getHasStudentLoanStart(), paySlipCreate.getStudentLoanPlanType(), paySlipCreate.getTaxYear(), paySlipCreate.getPayPeriod()));
            } else {
                paySlipCreate.setStudentLoanDeductionAmount(BigDecimal.ZERO);
            }
            paySlipCreate.setHasPostGraduateLoanStart(employeeDetails.getPostGraduateLoan().getHasPostgraduateLoan());
            paySlipCreate.setPostgraduateLoanPlanType(employeeDetails.getPostGraduateLoan().getPostgraduateLoanPlanType());
            if (paySlipCreate.getHasPostGraduateLoanStart()) {
                paySlipCreate.setPostgraduateDeductionAmount(
                        studentLoanCalculation.calculatePostGraduateLoan(paySlipCreate.getGrossPayTotal(), paySlipCreate.getHasPostGraduateLoanStart(), paySlipCreate.getPostgraduateLoanPlanType(), paySlipCreate.getTaxYear(), paySlipCreate.getPayPeriod())
                );
            } else {
                paySlipCreate.setPostgraduateDeductionAmount(BigDecimal.ZERO);
            }
        }
        catch (Exception e){
            logger.error("Error occurred while calculating student loan or postgraduate loan: {}", e.getMessage());
            throw new InvalidComputationException("Failed to calculate student or postgraduate loan for payslip", e);
        }
        // Pension contributions calculation
        try {
            paySlipCreate.setHasPensionEligible(employeeDetails.isHasPensionEligible());
            if (paySlipCreate.isHasPensionEligible()) {
                BigDecimal employeePensionContribution = pensionCalculation.calculateEmployeePensionContribution(paySlipCreate.getGrossPayTotal(),
                        paySlipCreate.isHasPensionEligible(), paySlipCreate.getTaxYear(), paySlipCreate.getPayPeriod());
                paySlipCreate.setEmployeePensionContribution(employeePensionContribution);
                logger.info("successfully calculated the employee pension contribution: {}", employeePensionContribution);
                BigDecimal employerPensionContribution = pensionCalculation.calculateEmployerPensionContribution(paySlipCreate.getGrossPayTotal(),
                        paySlipCreate.isHasPensionEligible(), paySlipCreate.getTaxYear(), paySlipCreate.getPayPeriod());
                paySlipCreate.setEmployerPensionContribution(employerPensionContribution);
                logger.info("successfully calculated the employer pension contribution: {}", employerPensionContribution);
            } else {
                BigDecimal employeePensionContribution = BigDecimal.ZERO;
                paySlipCreate.setEmployeePensionContribution(employeePensionContribution);
                BigDecimal employerPensionContribution = BigDecimal.ZERO;
                paySlipCreate.setEmployerPensionContribution(employerPensionContribution);
            }
        }
        catch (Exception e){
            logger.error("Error occurred while calculating pension contributions: {}", e.getMessage());
            throw new InvalidComputationException("Failed to calculate pension contributions for payslip", e);
        }
        // Deductions and Net Pay calculation
        try {
            BigDecimal deduction = paySlipCreate.getIncomeTaxTotal().add(paySlipCreate.getEmployeeNationalInsurance()).add(paySlipCreate.getStudentLoanDeductionAmount()).add(paySlipCreate.getPostgraduateDeductionAmount()).add(paySlipCreate.getEmployeePensionContribution());
            paySlipCreate.setDeductionsTotal(deduction);
            BigDecimal netPay = paySlipCreate.getGrossPayTotal().subtract(deduction);
            paySlipCreate.setTakeHomePayTotal(netPay);
            paySlipCreate.setPaySlipReference(generatePayslipReference(paySlipCreate.getEmployeeId()));
        }
        catch (Exception e){
            logger.error("Error occurred while calculating deductions or net pay: {}", e.getMessage());
            throw new InvalidComputationException("Failed to calculate deductions or net pay for payslip ", e);
        }
        PaySlip savedPaySlip=paySlipRepository.save(paySlipCreate);
        logger.info("successful payslip is created: {}", savedPaySlip);

        // Update employee details
        try {
            updatingDetails.updatingOtherEmployeeDetails(savedPaySlip);
            logger.info("Successful updated the other employee Details");
        }
        catch (Exception e){
            logger.error("Error occurred while updating other employee details: {}", e.getMessage());
            throw new InvalidComputationException("Failed to update other employee details for payslip", e);
        }
       try {
           if (savedPaySlip.getHasStudentLoanStart() || savedPaySlip.getHasPostGraduateLoanStart()) {
               LoanCalculationPaySlipDTO data = loanPaySlipCalculation.calculateLoanPaySlip(savedPaySlip);
               logger.info("Successfully calculated the loan pay slip data for employee: {}", data);
           }
           if (savedPaySlip.isHasPensionEligible()) {
               PensionCalculationPaySlipDTO updatedData = pensionCalculation.calculatePensionContributionPaySlip(savedPaySlip);
               logger.info("Successfully calculated the pension contribution pay slip for employee: {}", updatedData);
           }
       }
       catch (Exception e){
           logger.error("Error occurred while calculating loan or pension contributions: {}", e.getMessage());
              throw new InvalidComputationException("Failed to calculate loan or pension contributions for payslip", e);
       }


        /*if(savedPaySlip.getHasStudentLoanStart()){
            updatingDetails.updatingStudentLoanInEmployeeDetails(savedPaySlip);
            log.info("successfully updated the student loan details in employee Details");
        }
        if (savedPaySlip.getHasPostGraduateLoanStart()){
            updatingDetails.updatingPostGraduateLoanInEmployeeDetails(savedPaySlip);
            log.info("successfully updated the post graduate loan details in employee Details");
        }*/

        // Update employer details
        try {
            updatingDetails.updatingOtherEmployerDetails(savedPaySlip);
            logger.info("successfully updated the employer details of the total PAYE,NI");
        }
        catch (Exception e){
            logger.error("Error occurred while updating employer details: {}", e.getMessage());
            throw new InvalidComputationException("Failed to update employer details for payslip", e);
        }
        return  paySlipCreateDtoMapper.mapToDto(savedPaySlip);
    }
    private BigDecimal  calculateIncomeTaxBasedOnPayPeriod(BigDecimal incomeTax,String payPeriod){
        return switch (payPeriod.toUpperCase()) {
            case "WEEKLY" -> incomeTax.divide(BigDecimal.valueOf(52), 2, RoundingMode.HALF_UP);
            case "MONTHLY" -> incomeTax.divide(BigDecimal.valueOf(12), 2, RoundingMode.HALF_UP);
            case "QUARTERLY" -> incomeTax.divide(BigDecimal.valueOf(4), 2, RoundingMode.HALF_UP);
            case "YEARLY" -> incomeTax;
            default -> throw new DataValidationException("Invalid pay period. Must be WEEKLY, MONTHLY or YEARLY");
        };
    }
    private String generatePayslipReference(String employeeId) {
        String random = new BigInteger(48, new SecureRandom()).toString(36).toUpperCase();
        return String.format("%s-%s", random.substring(0, 6), employeeId);
    }


    private String getPeriodEndMonthYear(LocalDate payDate) {
        if (payDate == null) {
            throw new DataValidationException("Pay date cannot be null");
        }

        YearMonth yearMonth = YearMonth.from(payDate);

        // Choose your format: "MMMM yyyy" => "June 2025", or "MM-yyyy" => "06-2025"
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy"); // or "MM-yyyy"

        return yearMonth.format(formatter);
    }

    public String getBaseTaxCode(String taxCode) {
        if (taxCode == null) return null;
        String code = taxCode.trim().toUpperCase();

        // Remove suffixes W1, M1, or X if present
        if (code.endsWith("W1") || code.endsWith("M1")) {
            return code.substring(0, code.length() - 2).trim();
        } else if (code.endsWith("X")) {
            return code.substring(0, code.length() - 1).trim();
        }
        return code;
    }
    public boolean isNonCumulativeTaxCode(String taxCode) {
        if (taxCode == null) return false;
        String upper = taxCode.trim().toUpperCase();
        return upper.endsWith("W1") || upper.endsWith("M1") || upper.endsWith("X");
    }

    public void validateTaxCodeAndEmergencyFlag(EmployeeDetails employeeDetails) {
        String currentTaxCode = employeeDetails.getTaxCode();
        boolean isNonCumulative = isNonCumulativeTaxCode(currentTaxCode);
        boolean hasEmergencyFlag = employeeDetails.isHasEmergencyCode();

        if (isNonCumulative && !hasEmergencyFlag) {
            throw new DataValidationException(
                    String.format("Non-cumulative tax code '%s' requires the emergency flag to be set", currentTaxCode)
            );
        }

        if (!isNonCumulative && hasEmergencyFlag) {
            throw new DataValidationException(
                    String.format("Emergency flag must not be set for cumulative tax code '%s'", currentTaxCode)
            );
        }
    }









}