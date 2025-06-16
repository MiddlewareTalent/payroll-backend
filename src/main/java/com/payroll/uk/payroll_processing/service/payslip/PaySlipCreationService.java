package com.payroll.uk.payroll_processing.service.payslip;

import com.payroll.uk.payroll_processing.dto.PaySlipCreateDto;
import com.payroll.uk.payroll_processing.dto.mapper.PaySlipCreateDTOMapper;
import com.payroll.uk.payroll_processing.entity.PaySlip;
import com.payroll.uk.payroll_processing.repository.PaySlipRepository;
import com.payroll.uk.payroll_processing.service.ni.NationalInsuranceCalculation;
import com.payroll.uk.payroll_processing.service.PersonalAllowanceCalculation;
import com.payroll.uk.payroll_processing.service.incometax.TaxCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

@Service
public class PaySlipCreationService {
    @Autowired
    private  PaySlipRepository paySlipRepository;
    @Autowired
    private TaxCodeService taxCodeService;
    @Autowired
    private PersonalAllowanceCalculation personalAllowanceCalculation;
    @Autowired
    private NationalInsuranceCalculation nationalInsuranceCalculation;
    @Autowired
    private PaySlipCreateDTOMapper paySlipCreateDtoMapper;
//    @Autowired
//    private EmployeeDetailsDTO employeeDetailsDTO;
//    @Autowired
//    private EmployeeDetailsRepository employeeDetailsRepository;




    // Method to create a payslip
    public PaySlipCreateDto createPaySlip(PaySlipCreateDto paySlipCreateDto) {

        PaySlip paySlip=new PaySlip();
        paySlip.setFirstName(paySlipCreateDto.getFirstName());
        paySlip.setLastName(paySlipCreateDto.getLastName());
        paySlip.setAddress(paySlipCreateDto.getAddress());
        paySlip.setPostCode(paySlipCreateDto.getPostCode());
        paySlip.setEmployeeId(paySlipCreateDto.getEmployeeId());
        paySlip.setRegion(paySlipCreateDto.getRegion());
        paySlip.setTaxYear(paySlipCreateDto.getTaxYear());
        paySlip.setTaxCode(paySlipCreateDto.getTaxCode());
        paySlip.setNI_Number(paySlipCreateDto.getNI_Number());
        paySlip.setPayPeriod(paySlipCreateDto.getPayPeriod());
        paySlip.setPayDate(LocalDate.now());
        paySlip.setPeriodEnd(getPeriodEndMonthYear(paySlip.getPayDate()));
        paySlip.setGrossPayTotal(paySlipCreateDto.getGrossPayTotal());
        BigDecimal personal = personalAllowanceCalculation.calculatePersonalAllowance(
                paySlipCreateDto.getGrossPayTotal(), paySlipCreateDto.getTaxCode(),paySlipCreateDto.getPayPeriod()
        );
        BigDecimal personalAllowance=calculateIncomeTaxBasedOnPayPeriod(personal, paySlipCreateDto.getPayPeriod());

        paySlip.setPersonalAllowance(personalAllowance);
        paySlip.setTaxableIncome(
                taxCodeService.calculateTaxableIncome(
                        paySlipCreateDto.getGrossPayTotal(), personalAllowance
                ));
        BigDecimal incomeTax = taxCodeService.calculateIncomeBasedOnTaxCode(
                paySlipCreateDto.getGrossPayTotal(), paySlipCreateDto.getTaxYear(),
                paySlipCreateDto.getRegion(), paySlipCreateDto.getTaxCode(), paySlipCreateDto.getPayPeriod()
        );

        paySlip.setIncomeTaxTotal(incomeTax);


        // Calculate National Insurance contributions
       BigDecimal NI=nationalInsuranceCalculation.calculateNationalInsurance(
                paySlipCreateDto.getGrossPayTotal(),paySlipCreateDto.getTaxYear(),
                paySlipCreateDto.getRegion(),paySlipCreateDto.getPayPeriod()
        );


        paySlip.setEmployeeNationalInsurance(NI);
        paySlip.setEmployersNationalInsurance(
                nationalInsuranceCalculation.calculateEmploymentAllowance(
                        paySlipCreateDto.getGrossPayTotal(), paySlipCreateDto.getTaxYear(),
                        paySlipCreateDto.getRegion(), paySlipCreateDto.getPayPeriod()
                ));
        BigDecimal deduction=incomeTax.add(NI);
        paySlip.setDeductionsTotal(deduction);
        BigDecimal netPay=paySlipCreateDto.getGrossPayTotal().subtract(deduction);
        paySlip.setTakeHomePayTotal(netPay);
        paySlip.setPaySlipReference(generatePayslipReference(paySlipCreateDto.getEmployeeId()));

        PaySlip savedPaySlip=paySlipRepository.save(paySlip);
        System.out.println("Successfully saved PaySlip: " + savedPaySlip);
//          employeeDetailsRepository.findByEmployeeId()


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
    //Example: "7HJK9P-100"
// Example: "P-7HJK9P-100" (11 chars)
    // 8-character base36 (36^8 = ~2.8 trillion combinations)

}
