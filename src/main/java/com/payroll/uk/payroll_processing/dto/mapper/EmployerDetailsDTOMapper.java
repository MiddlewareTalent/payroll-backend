package com.payroll.uk.payroll_processing.dto.mapper;

import com.payroll.uk.payroll_processing.dto.BankDetailsDTO;
import com.payroll.uk.payroll_processing.dto.employerdto.*;
import com.payroll.uk.payroll_processing.entity.BankDetails;
import com.payroll.uk.payroll_processing.entity.employer.*;
import org.springframework.stereotype.Component;

@Component
public class EmployerDetailsDTOMapper {

    public EmployerDetailsDTO mapToEmployerDetailsDTO(EmployerDetails employerDetails){
        EmployerDetailsDTO dto = new EmployerDetailsDTO();
        dto.setId(employerDetails.getId());
        dto.setEmployerName(employerDetails.getEmployerName());
        dto.setEmployerId(employerDetails.getEmployerId());
        dto.setEmployerAddress(employerDetails.getEmployerAddress());
        dto.setEmployerTelephone(employerDetails.getEmployerTelephone());
        dto.setEmployerEmail(employerDetails.getEmployerEmail());
        dto.setEmployerPostCode(employerDetails.getEmployerPostCode());

        if (employerDetails.getCompanyDetails()!=null){
            dto.setCompanyDetailsDTO(mapToCompanyDetailsDTO(employerDetails.getCompanyDetails()));
        }
        if (employerDetails.getTaxOffice() != null) {
            dto.setTaxOfficeDTO(mapToTaxOfficeDto(employerDetails.getTaxOffice()));
        }

        if (employerDetails.getTerms() != null) {
            dto.setTermsDTO(mapToTermsDto(employerDetails.getTerms()));
        }

        if (employerDetails.getBankDetails() != null) {
            dto.setBankDetailsDTO(mapToBankDetailsDTO(employerDetails.getBankDetails()));
        }
        if (employerDetails.getOtherEmployerDetails() !=null){
            dto.setOtherEmployerDetailsDTO(mapToOtherEmployerDetailsDTO(employerDetails.getOtherEmployerDetails()));
        }

        return dto;
    }
    public EmployerDetails changeToEmployerDetails(EmployerDetailsDTO employerDto){
        EmployerDetails employerDetails = new EmployerDetails();
//        employerDetails.setId(employerDto.getId());
        employerDetails.setEmployerName(employerDto.getEmployerName());
        employerDetails.setEmployerId(employerDto.getEmployerId());
        employerDetails.setEmployerAddress(employerDto.getEmployerAddress());
        employerDetails.setEmployerTelephone(employerDto.getEmployerTelephone());
        employerDetails.setEmployerEmail(employerDto.getEmployerEmail());
        employerDetails.setEmployerPostCode(employerDto.getEmployerPostCode());
        if (employerDto.getCompanyDetailsDTO()!=null){
            employerDetails.setCompanyDetails(changeToCompanyDetails(employerDto.getCompanyDetailsDTO()));

        }
        if (employerDto.getTaxOfficeDTO() != null) {
            employerDetails.setTaxOffice(changeToTaxOffice(employerDto.getTaxOfficeDTO()));
        }

        if (employerDto.getTermsDTO() != null) {
            employerDetails.setTerms(changeToTerms(employerDto.getTermsDTO()));
        }

        if (employerDto.getBankDetailsDTO() != null) {
            employerDetails.setBankDetails(changeToBankDetails(employerDto));
        }
        /*if(employerDto.getOtherEmployerDetailsDto() !=null){
            employerDetails.setOtherEmployerDetails(changeToOtherEmployerDetails(employerDto.getOtherEmployerDetailsDto()));
        }*/
        OtherEmployerDetails otherEmployerDetails;

        if (employerDto.getOtherEmployerDetailsDTO() != null) {
            otherEmployerDetails = changeToOtherEmployerDetails(employerDto.getOtherEmployerDetailsDTO());
        } else {
            otherEmployerDetails = new OtherEmployerDetails();
            otherEmployerDetails.setDefaults(); // assuming you have this method in entity
        }

        employerDetails.setOtherEmployerDetails(otherEmployerDetails);

        return employerDetails;
    }

    public TaxOfficeDTO mapToTaxOfficeDto(TaxOffice taxOffice) {

        TaxOfficeDTO dto = new TaxOfficeDTO();
        dto.setDatePAYESchemeStarted(taxOffice.getDatePAYESchemeStarted());
        dto.setDatePAYESchemeCeased(taxOffice.getDatePAYESchemeCeased());
        dto.setPayeReference(taxOffice.getPayeReference());
        dto.setAccountsOfficeReference(taxOffice.getAccountsOfficeReference());
        dto.setPaymentMethod(taxOffice.getPaymentMethod());
        dto.setUniqueTaxRef(taxOffice.getUniqueTaxRef());
        dto.setCorporationTaxRef(taxOffice.getCorporationTaxRef());
        dto.setPayrollGivingRef(taxOffice.getPayrollGivingRef());
        dto.setClaimEmploymentAllowance(taxOffice.getClaimEmploymentAllowance());


        return dto;

    }
    public TaxOffice changeToTaxOffice(TaxOfficeDTO taxOfficeDto) {
        TaxOffice taxOffice = new TaxOffice();
        taxOffice.setPayeReference(taxOfficeDto.getPayeReference());
        taxOffice.setAccountsOfficeReference(taxOfficeDto.getAccountsOfficeReference());
        taxOffice.setPaymentMethod(taxOfficeDto.getPaymentMethod());
        taxOffice.setUniqueTaxRef(taxOfficeDto.getUniqueTaxRef());
        taxOffice.setCorporationTaxRef(taxOfficeDto.getCorporationTaxRef());
        taxOffice.setPayrollGivingRef(taxOfficeDto.getPayrollGivingRef());
        taxOffice.setClaimEmploymentAllowance(taxOfficeDto.getClaimEmploymentAllowance());

        return taxOffice;


    }
    public TermsDTO mapToTermsDto(Terms terms) {
        TermsDTO dto = new TermsDTO();
        dto.setHoursWorkedPerWeek(terms.getHoursWorkedPerWeek());
        dto.setWeeksNoticeRequired(terms.getWeeksNoticeRequired());
        dto.setDaysSicknessOnFullPay(terms.getDaysSicknessOnFullPay());
        dto.setMaleRetirementAge(terms.getMaleRetirementAge());
        dto.setFemaleRetirementAge(terms.getFemaleRetirementAge());
        dto.setDaysHolidayPerYear(terms.getDaysHolidayPerYear());
        dto.setMaxDaysToCarryOver(terms.getMaxDaysToCarryOver());

        return dto;
    }
    public Terms changeToTerms(TermsDTO termsDto) {
        Terms terms = new Terms();
        terms.setHoursWorkedPerWeek(termsDto.getHoursWorkedPerWeek());
        terms.setWeeksNoticeRequired(termsDto.getWeeksNoticeRequired());
        terms.setDaysSicknessOnFullPay(termsDto.getDaysSicknessOnFullPay());
        terms.setMaleRetirementAge(termsDto.getMaleRetirementAge());
        terms.setFemaleRetirementAge(termsDto.getFemaleRetirementAge());
        terms.setDaysHolidayPerYear(termsDto.getDaysHolidayPerYear());
        terms.setMaxDaysToCarryOver(termsDto.getMaxDaysToCarryOver());

        return terms;
    }

    public BankDetails changeToBankDetails(EmployerDetailsDTO employerDetailsDto) {
        BankDetails bankDetails = new BankDetails();
        bankDetails.setAccountName(employerDetailsDto.getBankDetailsDTO().getAccountName());
        bankDetails.setAccountNumber(employerDetailsDto.getBankDetailsDTO().getAccountNumber());

        bankDetails.setBankName(employerDetailsDto.getBankDetailsDTO().getBankName());
        bankDetails.setSortCode(employerDetailsDto.getBankDetailsDTO().getSortCode());
        bankDetails.setBankAddress(employerDetailsDto.getBankDetailsDTO().getBankAddress());
        bankDetails.setBankPostCode(employerDetailsDto.getBankDetailsDTO().getBankPostCode());
        /*bankDetails.setPaymentReference(employerDetailsDto.getBankDetailsDTO().getPaymentReference());
        bankDetails.setTelephone(employerDetailsDto.getBankDetailsDTO().getTelephone());
        bankDetails.setPaymentLeadDays(employerDetailsDto.getBankDetailsDTO().getPaymentLeadDays());
        bankDetails.setIsRTIReturnsIncluded(employerDetailsDto.getBankDetailsDTO().getIsRTIReturnsIncluded());*/
        return bankDetails;
    }
    public BankDetailsDTO mapToBankDetailsDTO(BankDetails bankDetails) {
        if (bankDetails == null) {
            throw new IllegalArgumentException("Bank details cannot be null");
        }
        BankDetailsDTO bankDetailsDTO = new BankDetailsDTO();
//        bankDetailsDTO.setId(employeeDetails.getBankDetails().getId());
        bankDetailsDTO.setAccountName(bankDetails.getAccountName());
        bankDetailsDTO.setAccountNumber(bankDetails.getAccountNumber());

        bankDetailsDTO.setBankName(bankDetails.getBankName());
        bankDetailsDTO.setSortCode(bankDetails.getSortCode());
        bankDetailsDTO.setBankAddress(bankDetails.getBankAddress());
        bankDetailsDTO.setBankPostCode(bankDetails.getBankPostCode());
       /* bankDetailsDTO.setPaymentReference(bankDetails.getPaymentReference());
        bankDetailsDTO.setTelephone(bankDetails.getTelephone());
        bankDetailsDTO.setPaymentLeadDays(bankDetails.getPaymentLeadDays());
        bankDetailsDTO.setIsRTIReturnsIncluded(bankDetails.getIsRTIReturnsIncluded());*/
        return bankDetailsDTO;

    }

    public OtherEmployerDetails changeToOtherEmployerDetails(OtherEmployerDetailsDTO otherEmployerDetailsDTO){
        OtherEmployerDetails otherDetails=new OtherEmployerDetails();
        otherDetails.setTotalPaidGrossAmountYTD(otherEmployerDetailsDTO.getTotalPaidGrossAmountYTD());
        otherDetails.setCurrentPayPeriodPaidGrossPay(otherEmployerDetailsDTO.getCurrentPayPeriodPaidGrossPay());

        otherDetails.setTotalPAYEYTD(otherEmployerDetailsDTO.getTotalPAYEYTD());
        otherDetails.setCurrentPayPeriodPAYE(otherEmployerDetailsDTO.getCurrentPayPeriodPAYE());

        otherDetails.setTotalEmployeesNIYTD(otherEmployerDetailsDTO.getTotalEmployeesNIYTD());
        otherDetails.setCurrentPayPeriodEmployeesNI(otherEmployerDetailsDTO.getCurrentPayPeriodEmployeesNI());

        otherDetails.setTotalEmployersNIYTD(otherEmployerDetailsDTO.getTotalEmployersNIYTD());
        otherDetails.setCurrentPayPeriodEmployersNI(otherEmployerDetailsDTO.getCurrentPayPeriodEmployersNI());

        otherDetails.setTotalEmployerPensionContribution(otherEmployerDetailsDTO.getTotalEmployerPensionContribution());
        otherDetails.setCurrentPayPeriodEmployerPensionContribution(otherEmployerDetailsDTO.getCurrentPayPeriodEmployerPensionContribution());

        otherDetails.setCurrentPayPeriodEmployeePensionContribution(otherEmployerDetailsDTO.getCurrentPayPeriodEmployeePensionContribution());
        otherDetails.setTotalEmployeePensionContribution(otherEmployerDetailsDTO.getTotalEmployeePensionContribution());
        return otherDetails;
    }
    public OtherEmployerDetailsDTO mapToOtherEmployerDetailsDTO(OtherEmployerDetails otherEmployerDetails){
        OtherEmployerDetailsDTO otherDetailsDTO=new OtherEmployerDetailsDTO();
        otherDetailsDTO.setTotalPaidGrossAmountYTD(otherEmployerDetails.getTotalPaidGrossAmountYTD());
        otherDetailsDTO.setCurrentPayPeriodPaidGrossPay(otherEmployerDetails.getCurrentPayPeriodPaidGrossPay());

        otherDetailsDTO.setTotalPAYEYTD(otherEmployerDetails.getTotalPAYEYTD());
        otherDetailsDTO.setCurrentPayPeriodPAYE(otherEmployerDetails.getCurrentPayPeriodPAYE());

        otherDetailsDTO.setTotalEmployeesNIYTD(otherEmployerDetails.getTotalEmployeesNIYTD());
        otherDetailsDTO.setCurrentPayPeriodEmployeesNI(otherEmployerDetails.getCurrentPayPeriodEmployeesNI());

        otherDetailsDTO.setTotalEmployersNIYTD(otherEmployerDetails.getTotalEmployersNIYTD());
        otherDetailsDTO.setCurrentPayPeriodEmployersNI(otherEmployerDetails.getCurrentPayPeriodEmployersNI());

        otherDetailsDTO.setTotalEmployerPensionContribution(otherEmployerDetails.getTotalEmployerPensionContribution());
        otherDetailsDTO.setCurrentPayPeriodEmployerPensionContribution(otherEmployerDetails.getCurrentPayPeriodEmployerPensionContribution());

        otherDetailsDTO.setCurrentPayPeriodEmployeePensionContribution(otherEmployerDetails.getCurrentPayPeriodEmployeePensionContribution());
        otherDetailsDTO.setTotalEmployeePensionContribution(otherEmployerDetails.getTotalEmployeePensionContribution());
        return otherDetailsDTO;
    }

    public CompanyDetails changeToCompanyDetails(CompanyDetailsDTO companyDetailsDTO) {
        CompanyDetails companyDetails = new CompanyDetails();
        companyDetails.setCompanyName(companyDetailsDTO.getCompanyName());
        companyDetails.setCompanyLogo(companyDetailsDTO.getCompanyLogo());
        companyDetails.setCompanyAddress(companyDetailsDTO.getCompanyAddress());
        companyDetails.setCompanyPostCode(companyDetailsDTO.getCompanyPostCode());
        companyDetails.setRegion(companyDetailsDTO.getRegion());
        companyDetails.setCurrentPayPeriod(companyDetailsDTO.getCurrentPayPeriod());
        companyDetails.setPayDate(companyDetailsDTO.getPayDate());
        companyDetails.setCurrentTaxYear(companyDetailsDTO.getCurrentTaxYear());
        return companyDetails;
    }
    public CompanyDetailsDTO mapToCompanyDetailsDTO(CompanyDetails companyDetails) {
        CompanyDetailsDTO companyDetailsDTO = new CompanyDetailsDTO();
        companyDetailsDTO.setCompanyName(companyDetails.getCompanyName());
        companyDetailsDTO.setCompanyLogo(companyDetails.getCompanyLogo());
        companyDetailsDTO.setCompanyAddress(companyDetails.getCompanyAddress());
        companyDetailsDTO.setCompanyPostCode(companyDetails.getCompanyPostCode());
        companyDetailsDTO.setRegion(companyDetails.getRegion());
        companyDetailsDTO.setCurrentPayPeriod(companyDetails.getCurrentPayPeriod());
        companyDetailsDTO.setPayDate(companyDetails.getPayDate());
        companyDetailsDTO.setCurrentTaxYear(companyDetails.getCurrentTaxYear());
        return companyDetailsDTO;
    }
}

