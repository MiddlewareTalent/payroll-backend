package com.payroll.uk.payroll_processing.service.payslip;

import com.payroll.uk.payroll_processing.dto.LoanCalculationPaySlipDTO;
import com.payroll.uk.payroll_processing.dto.PaySlipCreateDto;
import com.payroll.uk.payroll_processing.dto.PensionCalculationPaySlipDTO;
import com.payroll.uk.payroll_processing.dto.customdto.EmployeesSummaryInEmployerDTO;
import com.payroll.uk.payroll_processing.dto.mapper.PaySlipCreateDTOMapper;
import com.payroll.uk.payroll_processing.entity.PaySlip;
import com.payroll.uk.payroll_processing.entity.employee.EmployeeDetails;
import com.payroll.uk.payroll_processing.entity.employer.EmployerDetails;
import com.payroll.uk.payroll_processing.repository.EmployeeDetailsRepository;
import com.payroll.uk.payroll_processing.repository.EmployerDetailsRepository;
import com.payroll.uk.payroll_processing.repository.PaySlipRepository;
import com.payroll.uk.payroll_processing.service.LoanPaySlipCalculation;
import com.payroll.uk.payroll_processing.service.PensionCalculation;
import com.payroll.uk.payroll_processing.service.PersonalAllowanceCalculation;
import com.payroll.uk.payroll_processing.service.StudentLoanCalculation;
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
import java.util.List;

@Service
public class AutoPaySlip {
    private static final Logger logger = LoggerFactory.getLogger(AutoPaySlip.class);
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


    @Transactional
    public PaySlipCreateDto fillPaySlip(String employeeId){
        if (employeeId.isBlank()) {
            throw new IllegalArgumentException("Employee ID cannot be null or empty");
        }
        EmployeeDetails employeeDetails = employeeDetailsRepository.findByEmployeeId(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found with ID: " + employeeId));

        EmployerDetails  employerDetails = employerDetailsRepository.findByEmployerId(employeeDetails.getEmployerId())
                .orElseThrow(() -> new RuntimeException("Employer not found with ID: " + employeeDetails.getEmployerId()));

        validateEmployeeDetails(employeeDetails);
        validateEmployerDetails(employerDetails);
        PaySlip paySlipCreate = new PaySlip();
        try{
            paySlipCreate.setFirstName(employeeDetails.getFirstName());
            paySlipCreate.setLastName(employeeDetails.getLastName());
            paySlipCreate.setAddress(employeeDetails.getAddress());
            paySlipCreate.setPostCode(employeeDetails.getPostCode());
            paySlipCreate.setEmployeeId(employeeDetails.getEmployeeId());
            paySlipCreate.setRegion(employeeDetails.getRegion());
            paySlipCreate.setTaxYear(employerDetails.getTaxYear());
            paySlipCreate.setTaxCode(employeeDetails.getTaxCode());
            paySlipCreate.setNI_Number(employeeDetails.getNationalInsuranceNumber());
            paySlipCreate.setNiLetter(employeeDetails.getNiLetter());
            paySlipCreate.setWorkingCompanyName(employeeDetails.getWorkingCompanyName());
            paySlipCreate.setPayPeriod(String.valueOf(employeeDetails.getPayPeriod()));
            paySlipCreate.setPayDate(employerDetails.getPayDate());
            paySlipCreate.setPeriodEnd(getPeriodEndMonthYear(paySlipCreate.getPayDate()));
            logger.info("successfully completed  up to basic details");
        }
        catch (Exception e){
            logger.error("Error occurred while setting basic details: {}", e.getMessage());
            throw new RuntimeException("Failed to set basic details for payslip", e);
        }

        paySlipCreate.setGrossPayTotal(employeeDetails.getPayPeriodOfIncomeOfEmployee());
        try{
            BigDecimal personalAllowance=BigDecimal.ZERO;
            if(!employeeDetails.isHasEmergencyCode()){
                BigDecimal personal = personalAllowanceCalculation.calculatePersonalAllowance(
                        paySlipCreate.getGrossPayTotal(), paySlipCreate.getTaxCode(),paySlipCreate.getPayPeriod()
                );
                personalAllowance=calculateIncomeTaxBasedOnPayPeriod(personal, paySlipCreate.getPayPeriod());
            }

            else if (employeeDetails.isHasEmergencyCode()){
                personalAllowance=personalAllowanceCalculation.getPersonalAllowanceFromEmergencyTaxCode(paySlipCreate.getTaxCode(),paySlipCreate.getPayPeriod());

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
            throw new RuntimeException("Failed to calculate personal allowance for payslip", e);
        }

        paySlipCreate.setTaxableIncome(
                taxCodeService.calculateTaxableIncome(
                        paySlipCreate.getGrossPayTotal(), paySlipCreate.getPersonalAllowance()
                ));
        try {
            BigDecimal incomeTax = taxCodeService.calculateIncomeBasedOnTaxCode(
                    paySlipCreate.getGrossPayTotal(), paySlipCreate.getPersonalAllowance(), paySlipCreate.getTaxableIncome(), paySlipCreate.getTaxYear(),
                    paySlipCreate.getRegion(), paySlipCreate.getTaxCode(), paySlipCreate.getPayPeriod()
            );
            logger.info("incomeTax {} ", incomeTax);
            paySlipCreate.setIncomeTaxTotal(incomeTax);
            logger.info("successfully completed the income tax calculation");
        }
        catch (Exception e){
            logger.error("Error occurred while calculating income tax: {}", e.getMessage());
            throw new RuntimeException("Failed to calculate income tax for payslip", e);
        }
        try {


            BigDecimal NI = nationalInsuranceCalculator.calculateEmployeeNIContribution(paySlipCreate.getGrossPayTotal(), paySlipCreate.getTaxYear(),
                    paySlipCreate.getPayPeriod(), paySlipCreate.getNiLetter());
            paySlipCreate.setEmployeeNationalInsurance(NI);
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
            throw new RuntimeException("Failed to calculate National Insurance for payslip", e);
        }
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
            throw new RuntimeException("Failed to calculate student or postgraduate loan for payslip", e);
        }
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
            throw new RuntimeException("Failed to calculate pension contributions for payslip", e);
        }
        try {
            BigDecimal deduction = paySlipCreate.getIncomeTaxTotal().add(paySlipCreate.getEmployeeNationalInsurance()).add(paySlipCreate.getStudentLoanDeductionAmount()).add(paySlipCreate.getPostgraduateDeductionAmount()).add(paySlipCreate.getEmployeePensionContribution());
            paySlipCreate.setDeductionsTotal(deduction);
            BigDecimal netPay = paySlipCreate.getGrossPayTotal().subtract(deduction);
            paySlipCreate.setTakeHomePayTotal(netPay);
            paySlipCreate.setPaySlipReference(generatePayslipReference(paySlipCreate.getEmployeeId()));
        }
        catch (Exception e){
            logger.error("Error occurred while calculating deductions or net pay: {}", e.getMessage());
            throw new RuntimeException("Failed to calculate deductions or net pay for payslip", e);
        }
        PaySlip savedPaySlip=paySlipRepository.save(paySlipCreate);
        logger.info("successful payslip is created: ");
        System.out.println("created PaySlip: " + savedPaySlip);

        try {
            updatingDetails.updatingOtherEmployeeDetails(savedPaySlip);
            logger.info("Successful updated the other employee Details");
        }
        catch (Exception e){
            logger.error("Error occurred while updating other employee details: {}", e.getMessage());
            throw new RuntimeException("Failed to update other employee details for payslip", e);
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
              throw new RuntimeException("Failed to calculate loan or pension contributions for payslip", e);
       }


        /*if(savedPaySlip.getHasStudentLoanStart()){
            updatingDetails.updatingStudentLoanInEmployeeDetails(savedPaySlip);
            log.info("successfully updated the student loan details in employee Details");
        }
        if (savedPaySlip.getHasPostGraduateLoanStart()){
            updatingDetails.updatingPostGraduateLoanInEmployeeDetails(savedPaySlip);
            log.info("successfully updated the post graduate loan details in employee Details");
        }*/

        try {
            updatingDetails.updatingOtherEmployerDetails(savedPaySlip);
            logger.info("successfully updated the employer details of the total PAYE,NI");
        }
        catch (Exception e){
            logger.error("Error occurred while updating employer details: {}", e.getMessage());
            throw new RuntimeException("Failed to update employer details for payslip", e);
        }





        return  paySlipCreateDtoMapper.mapToDto(savedPaySlip);
    }
    public BigDecimal  calculateIncomeTaxBasedOnPayPeriod(BigDecimal incomeTax,String payPeriod){
        return switch (payPeriod.toUpperCase()) {
            case "WEEKLY" -> incomeTax.divide(BigDecimal.valueOf(52), 2, RoundingMode.HALF_UP);
            case "MONTHLY" -> incomeTax.divide(BigDecimal.valueOf(12), 2, RoundingMode.HALF_UP);
            case "QUARTERLY" -> incomeTax.divide(BigDecimal.valueOf(4), 2, RoundingMode.HALF_UP);
            case "YEARLY" -> incomeTax;
            default -> throw new IllegalArgumentException("Invalid pay period. Must be WEEKLY, MONTHLY or YEARLY");
        };
    }
    public String generatePayslipReference(String employeeId) {
        String random = new BigInteger(48, new SecureRandom()).toString(36).toUpperCase();
        return String.format("%s-%s", random.substring(0, 6), employeeId);
    }


    public String getPeriodEndMonthYear(LocalDate payDate) {
        if (payDate == null) {
            throw new IllegalArgumentException("Pay date cannot be null");
        }

        YearMonth yearMonth = YearMonth.from(payDate);

        // Choose your format: "MMMM yyyy" => "June 2025", or "MM-yyyy" => "06-2025"
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy"); // or "MM-yyyy"

        return yearMonth.format(formatter);
    }

    public List<PaySlipCreateDto> getAllEmployeeId(String employeeId){
        if(employeeId==null){
            throw new IllegalArgumentException("Employee Cannot be null or empty");
        }
        if(!paySlipRepository.existsByEmployeeId(employeeId)){
            throw new IllegalArgumentException("Employee Id is not found");
        }

        List<PaySlip> listOfPayslips = paySlipRepository.findByEmployeeId(employeeId);
        List<PaySlipCreateDto> listOfPayslipsData = listOfPayslips.stream().map(paySlipCreateDtoMapper::mapToDto).toList();

        if (listOfPayslipsData.isEmpty()){
            throw new IllegalArgumentException("Payslip data is not found");
        }
        return listOfPayslipsData;
    }
    public PaySlipCreateDto getPaySlipByReferences(String paySlipReference){
        if(paySlipReference ==null){
            throw new IllegalArgumentException("paySlipReference Number cannot be Null");
        }
        if(!paySlipRepository.existsByPaySlipReference(paySlipReference)){
            throw  new IllegalArgumentException("paySlipReference number is not found");
        }
        PaySlip paySlipData = paySlipRepository.findByPaySlipReference(paySlipReference);
        if(paySlipData==null){
            throw new IllegalArgumentException("payslip data is null");
        }
        return paySlipCreateDtoMapper.mapToDto(paySlipData);
    }

    private void validateEmployeeDetails(EmployeeDetails employeeDetails) {

        // Name validations
        if (employeeDetails.getFirstName() == null || employeeDetails.getFirstName().trim().isEmpty()) {
            throw new IllegalArgumentException("First name is not found");
        }
        if (employeeDetails.getLastName() == null || employeeDetails.getLastName().trim().isEmpty()) {
            throw new IllegalArgumentException("Last name is not found");
        }
        if (employeeDetails.getAddress() == null || employeeDetails.getAddress().trim().isEmpty()) {
            throw new IllegalArgumentException("Address is not found");
        }
        if (employeeDetails.getPostCode() == null || employeeDetails.getPostCode().trim().isEmpty()) {
            throw new IllegalArgumentException("PostCode is not found");
        }
        if (employeeDetails.getRegion() == null ) {
            throw new IllegalArgumentException("Region is not found");
        }
        if (employeeDetails.getTaxCode() == null || employeeDetails.getTaxCode().trim().isEmpty()) {
            throw new IllegalArgumentException("tax code is not found");
        }
        if (employeeDetails.getWorkingCompanyName() == null || employeeDetails.getWorkingCompanyName().trim().isEmpty()) {
            throw new IllegalArgumentException("Working Company Name is not found");
        }

        // Email validation
        if (employeeDetails.getEmail() == null || employeeDetails.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Email is not found");
        }


        // Employee ID validation
        if (employeeDetails.getEmployeeId() == null || employeeDetails.getEmployeeId().trim().isEmpty()) {
            throw new IllegalArgumentException("Employee ID is not found");
        }

        // National Insurance validation
        if (employeeDetails.getNationalInsuranceNumber() != null &&
                !employeeDetails.getNationalInsuranceNumber().matches("^[A-Z]{2}[0-9]{6}[A-Z]$")) {
            throw new IllegalArgumentException("National Insurance number is not found");
        }

        if (employeeDetails.getNiLetter() ==null){
            throw new IllegalArgumentException("NI Category Letter is not found");
        }

        // Financial validations
        if (employeeDetails.getPayPeriodOfIncomeOfEmployee() == null) {
            throw new IllegalArgumentException("Gross income is not found");
        }
        if (employeeDetails.getPayPeriodOfIncomeOfEmployee().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Gross income cannot be negative");
        }

        if (employeeDetails.getPayPeriod() == null) {
            throw new IllegalArgumentException("Pay period is not found");
        }
        if(employeeDetails.getStudentLoan().getHasStudentLoan()==null){
            throw new IllegalArgumentException("student loan cannot be null and it can true or false");

        }
        if(employeeDetails.getStudentLoan().getStudentLoanPlanType()==null){
            throw new IllegalArgumentException("student loan plan Type cannot be null");
        }



    }
    private void validateEmployerDetails(EmployerDetails employerDetails){
        if(employerDetails.getTaxYear()==null){
            throw  new IllegalArgumentException("tax year is not found");
        }
        if (employerDetails.getPayDate()==null){
            throw new IllegalArgumentException("pay date is not found");
        }
    }

    public List<PaySlipCreateDto> getAllPaySlips() {
        List<PaySlip> paySlips = paySlipRepository.findAll();
        if (paySlips.isEmpty()) {
            throw new IllegalArgumentException("No payslips found");
        }
        return paySlips.stream().map(paySlipCreateDtoMapper::mapToDto).toList();
    }

}

