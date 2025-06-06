package com.payroll.uk.payroll_processing.service.payslip;

import com.payroll.uk.payroll_processing.dto.PaySlipCreateDto;
import com.payroll.uk.payroll_processing.dto.mapper.PaySlipCreateDtoMapper;
import com.payroll.uk.payroll_processing.entity.PaySlip;
import com.payroll.uk.payroll_processing.entity.employee.EmployeeDetails;
import com.payroll.uk.payroll_processing.entity.employee.OtherEmployeeDetails;
import com.payroll.uk.payroll_processing.entity.employer.EmployerDetails;
import com.payroll.uk.payroll_processing.repository.EmployeeDetailsRepository;
import com.payroll.uk.payroll_processing.repository.EmployerDetailsRepository;
import com.payroll.uk.payroll_processing.repository.PaySlipRepository;
import com.payroll.uk.payroll_processing.service.NationalInsuranceCalculation;
import com.payroll.uk.payroll_processing.service.PersonalAllowanceCalculation;
import com.payroll.uk.payroll_processing.service.incometax.TaxCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.security.SecureRandom;
import java.time.LocalDate;

@Service
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
    private PaySlipCreateDtoMapper paySlipCreateDtoMapper;



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
        paySlipCreate.setPayPeriod(String.valueOf(employerDetails.getPayPeriod()));
        paySlipCreate.setPayDate(LocalDate.now());
        paySlipCreate.setPeriodEnd(getPreviousMonthEndDate());
        paySlipCreate.setGrossPayTotal(employeeDetails.getGrossIncome());
        BigDecimal personal = personalAllowanceCalculation.calculatePersonalAllowance(
                paySlipCreate.getGrossPayTotal(), paySlipCreate.getTaxCode()
        );
        BigDecimal personalAllowance=calculateIncomeTaxBasedOnPayPeriod(personal, paySlipCreate.getPayPeriod());

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


        // Calculate National Insurance contributions
        BigDecimal NI=nationalInsuranceCalculation.calculateNationalInsurance(
                paySlipCreate.getGrossPayTotal(),paySlipCreate.getTaxYear(),
                paySlipCreate.getRegion(),paySlipCreate.getPayPeriod()
        );


        paySlipCreate.setNationalInsurance(NI);
        paySlipCreate.setEmployersNationalInsurance(
                nationalInsuranceCalculation.calculateEmploymentAllowance(
                        paySlipCreate.getGrossPayTotal(), paySlipCreate.getTaxYear(),
                        paySlipCreate.getRegion(), paySlipCreate.getPayPeriod()
                ));
        BigDecimal deduction=incomeTax.add(NI);
        paySlipCreate.setDeductionsTotal(deduction);
        BigDecimal netPay=paySlipCreate.getGrossPayTotal().subtract(deduction);
        paySlipCreate.setTakeHomePayTotal(netPay);
        paySlipCreate.setPaySlipReference(generatePayslipReference(paySlipCreate.getEmployeeId()));

        PaySlip savedPaySlip=paySlipRepository.save(paySlipCreate);
        System.out.println("Successfully saved PaySlip: " + savedPaySlip);

        BigDecimal totalUsedPersonalAllowance = employeeDetailsRepository.findByTotalUsedPersonalAllowance(employeeDetails.getEmployeeId());
        BigDecimal remainingPersonalAllowance = employeeDetailsRepository.findByRemainingPersonalAllowance(employeeDetails.getEmployeeId());
        if (remainingPersonalAllowance.compareTo(BigDecimal.ZERO)==0){
            remainingPersonalAllowance=employeeDetails.getTotalPersonalAllowanceInCompany().subtract(employeeDetails.getPreviouslyUsedPersonalAllowance());
        }
        OtherEmployeeDetails otherEmployeeDetails = employeeDetails.getOtherEmployeeDetails();
        otherEmployeeDetails.setUsedPersonalAllowance(personalAllowance);
        otherEmployeeDetails.setTotalUsedPersonalAllowance(totalUsedPersonalAllowance.add(personalAllowance));
        otherEmployeeDetails.setRemainingPersonalAllowance(remainingPersonalAllowance.subtract(personalAllowance));



        // Update OtherEmployeeDetails with income tax information
        otherEmployeeDetails.setIncomeTaxPaid(incomeTax);
    BigDecimal totalIncomeTaxPaidInCompany = employeeDetailsRepository.findByTotalIncomeTaxPaidInCompany(employeeDetails.getEmployeeId());

        otherEmployeeDetails.setTotalIncomeTaxPaidInCompany(totalIncomeTaxPaidInCompany.add(incomeTax));
        if("Yearly".equalsIgnoreCase(String.valueOf(employeeDetails.getPayPeriod()))){
        BigDecimal yearCount=employeeDetailsRepository.findByNumberOfYearsOfIncomeTaxPaid(employeeDetails.getEmployeeId());
        otherEmployeeDetails.setNumberOfYearsOfIncomeTaxPaid(yearCount.add(BigDecimal.ONE));
    } else if ("Monthly".equalsIgnoreCase(String.valueOf(employeeDetails.getPayPeriod()))) {
        BigDecimal monthCount=employeeDetailsRepository.findByNumberOfMonthsOfIncomeTaxPaid(employeeDetails.getEmployeeId());
        otherEmployeeDetails.setNumberOfMonthsOfIncomeTaxPaid(monthCount.add(BigDecimal.ONE));
    }
        else if ("Weekly".equalsIgnoreCase(String.valueOf(employeeDetails.getPayPeriod()))) {
        BigDecimal weekCount=employeeDetailsRepository.findByNumberOfWeeksOfIncomeTaxPaid(employeeDetails.getEmployeeId());
        otherEmployeeDetails.setNumberOfWeeksOfIncomeTaxPaid(weekCount.add(BigDecimal.ONE));
    }
        // Update OtherEmployeeDetails with National Insurance contributions
        otherEmployeeDetails.setEmployeeNIContribution(NI);
    BigDecimal totalEmployeeNIContributionInCompany=employeeDetailsRepository.findByTotalEmployeeNIContributionInCompany(employeeDetails.getEmployeeId());
        otherEmployeeDetails.setTotalEmployeeNIContributionInCompany(
                totalEmployeeNIContributionInCompany.add(NI)
            );

//        int numberOfNIPaidYearsInCompany=employeeDetailsRepository.findByNumberOfNIPaidYearsInCompany(employeeDetails.getEmployeeId());
//        otherEmployeeDetails.setNumberOfNIPaidYearsInCompany(numberOfNIPaidYearsInCompany+1);


        if("YEARLY".equalsIgnoreCase(String.valueOf(employeeDetails.getPayPeriod()))){
        BigDecimal yearCount=employeeDetailsRepository.findByNumberOfYearsOfNIContributions(employeeDetails.getEmployeeId());
        otherEmployeeDetails.setNumberOfYearsOfNIContributions(yearCount.add(BigDecimal.ONE));
    } else if ("MONTHLY".equalsIgnoreCase(String.valueOf(employeeDetails.getPayPeriod()))) {
        BigDecimal monthCount=employeeDetailsRepository.findByNumberOfMonthsOfNIContributions(employeeDetails.getEmployeeId());
        otherEmployeeDetails.setNumberOfMonthsOfNIContributions(monthCount.add(BigDecimal.ONE));
    }
        else if ("WEEKLY".equalsIgnoreCase(String.valueOf(employeeDetails.getPayPeriod()))) {
        BigDecimal weekCount=employeeDetailsRepository.findByNumberOfWeeksOfNIContributions(employeeDetails.getEmployeeId());
        otherEmployeeDetails.setNumberOfWeeksOfNIContributions(weekCount.add(BigDecimal.ONE));
    }

        employeeDetails.setOtherEmployeeDetails(otherEmployeeDetails);

        EmployeeDetails employeeData = employeeDetailsRepository.save(employeeDetails);
        System.out.println("Successfully Updated EmployerDetails: " + employeeData);


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
    public LocalDate getPreviousMonthEndDate() {
        // Get current system date as Pay Date
        LocalDate payDate = LocalDate.now();

        // Get end of previous month
        return payDate.withDayOfMonth(1).minusDays(1);
    }
}

/*
private String firstName;
private String lastName;
private String address;
private String postCode;
private String employeeId;
@Enumerated(EnumType.STRING)
private TaxThreshold.TaxRegion region;
private String taxYear;
private String taxCode;
private String NI_Number;
private String payPeriod;
@JsonFormat(pattern = "yyyy-MM-dd")
private LocalDate payDate;
@JsonFormat(pattern = "yyyy-MM-dd")
private LocalDate periodEnd;
private BigDecimal grossPayTotal;
private BigDecimal taxableIncome;
private BigDecimal personalAllowance;
private BigDecimal incomeTaxTotal;
private BigDecimal nationalInsurance;
private BigDecimal employersNationalInsurance;
private BigDecimal deductionsTotal;
private BigDecimal takeHomePayTotal;
private String paySlipReference;
*/
