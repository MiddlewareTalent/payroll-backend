package com.payroll.uk.payroll_processing.dto.mapper;

import com.payroll.uk.payroll_processing.dto.PaySlipCreateDto;
import com.payroll.uk.payroll_processing.entity.PaySlip;
import org.springframework.stereotype.Component;

@Component
public class PaySlipCreateDTOMapper {
    public PaySlipCreateDto mapToDto(PaySlip paySlip) {
        PaySlipCreateDto dto = new PaySlipCreateDto();
        dto.setFirstName(paySlip.getFirstName());
        dto.setLastName(paySlip.getLastName());
        dto.setAddress(paySlip.getAddress());
        dto.setPostCode(paySlip.getPostCode());
        dto.setEmployeeId(paySlip.getEmployeeId());
        dto.setNiCategoryLetter(paySlip.getNiLetter());
        dto.setWorkingCompanyName(paySlip.getWorkingCompanyName());
        dto.setRegion(paySlip.getRegion());
        dto.setTaxYear(paySlip.getTaxYear());
        dto.setTaxCode(paySlip.getTaxCode());
        dto.setNI_Number(paySlip.getNI_Number());
        dto.setPayPeriod(paySlip.getPayPeriod());
        dto.setPayDate(paySlip.getPayDate());
        dto.setPeriodEnd(paySlip.getPeriodEnd());
        dto.setGrossPayTotal(paySlip.getGrossPayTotal());
        dto.setTaxableIncome(paySlip.getTaxableIncome());
        dto.setPersonalAllowance(paySlip.getPersonalAllowance());
        dto.setIncomeTaxTotal(paySlip.getIncomeTaxTotal());
        dto.setEmployeeNationalInsurance(paySlip.getEmployeeNationalInsurance());
        dto.setEmployersNationalInsurance(paySlip.getEmployersNationalInsurance());
        dto.setHasStudentLoanStart(paySlip.getHasStudentLoanStart());
        dto.setHasPostGraduateLoanStart(paySlip.getHasPostGraduateLoanStart());
        dto.setStudentLoanPlanType(paySlip.getStudentLoanPlanType());
        dto.setPostgraduateLoanPlanType(paySlip.getPostgraduateLoanPlanType());
        dto.setStudentLoanDeductionAmount(paySlip.getStudentLoanDeductionAmount());
        dto.setPostgraduateDeductionAmount(paySlip.getPostgraduateDeductionAmount());
        dto.setDeductionsTotal(paySlip.getDeductionsTotal());
        dto.setTakeHomePayTotal(paySlip.getTakeHomePayTotal());
        dto.setPaySlipReference(paySlip.getPaySlipReference());
        return dto;
    }
    /*public PaySlip changeToPaySlip(PaySlipCreateDto paySlipCreateDto) {
        PaySlip paySlip = new PaySlip();
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
        paySlip.setPayDate(paySlipCreateDto.getPayDate());
        paySlip.setPeriodEnd(paySlipCreateDto.getPeriodEnd());
        paySlip.setGrossPayTotal(paySlipCreateDto.getGrossPayTotal());
//        paySlip.setTaxableIncome(paySlipCreateDto.getTaxableIncome());
        paySlip.setPersonalAllowance(paySlipCreateDto.getPersonalAllowance());
        paySlip.setIncomeTaxTotal(paySlipCreateDto.getIncomeTaxTotal());
        paySlip.setNationalInsurance(paySlipCreateDto.getNationalInsurance());
        paySlip.setEmployersNationalInsurance(paySlipCreateDto.getEmployersNationalInsurance());
        paySlip.setDeductionsTotal(paySlipCreateDto.getDeductionsTotal());
        paySlip.setTakeHomePayTotal(paySlipCreateDto.getTakeHomePayTotal());
        return paySlip;
    }*/
}


