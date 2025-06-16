package com.payroll.uk.payroll_processing.service.payslip;

import com.payroll.uk.payroll_processing.dto.PaySlipCreateDto;
import com.payroll.uk.payroll_processing.dto.mapper.PaySlipCreateDTOMapper;
import com.payroll.uk.payroll_processing.entity.PaySlip;
import com.payroll.uk.payroll_processing.entity.employee.EmployeeDetails;
import com.payroll.uk.payroll_processing.entity.employer.EmployerDetails;
import com.payroll.uk.payroll_processing.repository.EmployeeDetailsRepository;
import com.payroll.uk.payroll_processing.repository.EmployerDetailsRepository;
import com.payroll.uk.payroll_processing.repository.PaySlipRepository;
import com.payroll.uk.payroll_processing.service.PersonalAllowanceCalculation;
import com.payroll.uk.payroll_processing.service.StudentLoanCalculation;
import com.payroll.uk.payroll_processing.service.incometax.TaxCodeService;
import com.payroll.uk.payroll_processing.service.ni.NationalInsuranceCalculation;
import com.payroll.uk.payroll_processing.service.ni.NationalInsuranceCalculator;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class AutoPaySlip {
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



    @Transactional
    public PaySlipCreateDto fillPaySlip(String employeeId){
        if (employeeId.isBlank()) {
            throw new IllegalArgumentException("Employee ID cannot be null or empty");
        }
        EmployeeDetails employeeDetails = employeeDetailsRepository.findByEmployeeId(employeeId)
                .orElseThrow(() -> new RuntimeException("Employer not found with ID: " + employeeId));

        EmployerDetails  employerDetails = employerDetailsRepository.findByEmployerId(employeeDetails.getEmployerId())
                .orElseThrow(() -> new RuntimeException("Employer not found with ID: " + employeeDetails.getEmployerId()));

        PaySlip paySlipCreate = new PaySlip();
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
        log.info("successfully completed  up to basic details");
        paySlipCreate.setGrossPayTotal(employeeDetails.getPayPeriodOfIncomeOfEmployee());
        BigDecimal personalAllowance=BigDecimal.ZERO;
        if(!employeeDetails.getIsEmergencyCode()){
            BigDecimal personal = personalAllowanceCalculation.calculatePersonalAllowance(
                    paySlipCreate.getGrossPayTotal(), paySlipCreate.getTaxCode(),paySlipCreate.getPayPeriod()
            );
            personalAllowance=calculateIncomeTaxBasedOnPayPeriod(personal, paySlipCreate.getPayPeriod());
        }
        else if (employeeDetails.getIsEmergencyCode()){
            personalAllowance=personalAllowanceCalculation.getPersonalAllowanceFromEmergencyTaxCode(paySlipCreate.getTaxCode(),paySlipCreate.getPayPeriod());

        }


        paySlipCreate.setPersonalAllowance(personalAllowance);
        paySlipCreate.setTaxableIncome(
                taxCodeService.calculateTaxableIncome(
                        paySlipCreate.getGrossPayTotal(), personalAllowance
                ));
        BigDecimal incomeTax = taxCodeService.calculateIncomeBasedOnTaxCode(
                paySlipCreate.getGrossPayTotal(), paySlipCreate.getTaxYear(),
                paySlipCreate.getRegion(), paySlipCreate.getTaxCode(), paySlipCreate.getPayPeriod()
        );

        paySlipCreate.setIncomeTaxTotal(incomeTax);
        log.info("successfully completed the income tax calculation");


        // Calculate National Insurance contributions
//        BigDecimal NI=nationalInsuranceCalculation.calculateNationalInsurance(
//                paySlipCreate.getGrossPayTotal(),paySlipCreate.getTaxYear(),
//                paySlipCreate.getRegion(),paySlipCreate.getPayPeriod()
//        );


        BigDecimal NI= nationalInsuranceCalculator.calculateEmployeeNIContribution( paySlipCreate.getGrossPayTotal(),paySlipCreate.getTaxYear(),
                paySlipCreate.getPayPeriod(),paySlipCreate.getNiLetter());



        paySlipCreate.setEmployeeNationalInsurance(NI);
//        paySlipCreate.setEmployersNationalInsurance(
//                nationalInsuranceCalculation.calculateEmploymentAllowance(
//                        paySlipCreate.getGrossPayTotal(), paySlipCreate.getTaxYear(),
//                        paySlipCreate.getRegion(), paySlipCreate.getPayPeriod()
//                ));
        log.info("successfully completed the Employee NI calculation ");

        paySlipCreate.setEmployersNationalInsurance(
                nationalInsuranceCalculator.calculateEmployerNIContribution(
                        paySlipCreate.getGrossPayTotal(), paySlipCreate.getTaxYear(),
                        paySlipCreate.getPayPeriod(),paySlipCreate.getNiLetter()
                ));
        log.info("successfully completed the Employers NI Calculation");
       paySlipCreate.setHasStudentLoanStart(employeeDetails.getStudentLoan().getHasStudentLoan());
       paySlipCreate.setStudentLoanPlanType(employeeDetails.getStudentLoan().getStudentLoanPlanType());
       if(paySlipCreate.getHasStudentLoanStart()){
           paySlipCreate.setStudentLoanDeductionAmount
                   (studentLoanCalculation.calculateStudentLoan(paySlipCreate.getGrossPayTotal(), paySlipCreate.getHasStudentLoanStart(),paySlipCreate.getStudentLoanPlanType(),paySlipCreate.getTaxYear(),paySlipCreate.getPayPeriod()));
       }
       else {
           paySlipCreate.setStudentLoanDeductionAmount(BigDecimal.ZERO);
       }
       paySlipCreate.setHasPostGraduateLoanStart(employeeDetails.getPostGraduateLoan().getHasPostgraduateLoan());
       paySlipCreate.setPostgraduateLoanPlanType(employeeDetails.getPostGraduateLoan().getPostgraduateLoanPlanType());
       if(paySlipCreate.getHasPostGraduateLoanStart() ){
           paySlipCreate.setPostgraduateDeductionAmount(
                   studentLoanCalculation.calculatePostGraduateLoan(paySlipCreate.getGrossPayTotal(), paySlipCreate.getHasPostGraduateLoanStart(),paySlipCreate.getPostgraduateLoanPlanType(),paySlipCreate.getTaxYear(),paySlipCreate.getPayPeriod())
           );
       }
       else {
           paySlipCreate.setPostgraduateDeductionAmount(BigDecimal.ZERO);
       }



        BigDecimal deduction=incomeTax.add(NI).add(paySlipCreate.getStudentLoanDeductionAmount()).add(paySlipCreate.getPostgraduateDeductionAmount());
        paySlipCreate.setDeductionsTotal(deduction);
        BigDecimal netPay=paySlipCreate.getGrossPayTotal().subtract(deduction);
        paySlipCreate.setTakeHomePayTotal(netPay);
        paySlipCreate.setPaySlipReference(generatePayslipReference(paySlipCreate.getEmployeeId()));

        PaySlip savedPaySlip=paySlipRepository.save(paySlipCreate);

        System.out.println("created PaySlip: " + savedPaySlip);

        updatingDetails.updatingOtherEmployeeDetails(savedPaySlip);
        log.info("Successful updated the other employee Details");


        if(savedPaySlip.getHasStudentLoanStart()){
            updatingDetails.updatingStudentLoanInEmployeeDetails(savedPaySlip);
            log.info("successfully updated the student loan details in employee Details");
        }
        if (savedPaySlip.getHasPostGraduateLoanStart()){
            updatingDetails.updatingPostGraduateLoanInEmployeeDetails(savedPaySlip);
            log.info("successfully updated the post graduate loan details in employee Details");
        }

        updatingDetails.updatingOtherEmployerDetails(savedPaySlip);
        log.info("successfully updated the employer details of the total PAYE,NI");






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

}

