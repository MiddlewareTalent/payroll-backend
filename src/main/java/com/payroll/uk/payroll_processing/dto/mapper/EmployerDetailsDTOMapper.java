package com.payroll.uk.payroll_processing.dto.mapper;

import com.payroll.uk.payroll_processing.dto.BankDetailsDTO;
import com.payroll.uk.payroll_processing.dto.employerdto.EmployerDetailsDTO;
import com.payroll.uk.payroll_processing.dto.employerdto.OtherEmployerDetailsDTO;
import com.payroll.uk.payroll_processing.dto.employerdto.TaxOfficeDTO;
import com.payroll.uk.payroll_processing.dto.employerdto.TermsDTO;
import com.payroll.uk.payroll_processing.entity.BankDetails;
import com.payroll.uk.payroll_processing.entity.employer.EmployerDetails;
import com.payroll.uk.payroll_processing.entity.employer.OtherEmployerDetails;
import com.payroll.uk.payroll_processing.entity.employer.TaxOffice;
import com.payroll.uk.payroll_processing.entity.employer.Terms;
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
        dto.setContactForename(employerDetails.getContactForename());
        dto.setContactSurname(employerDetails.getContactSurname());
        dto.setPdfPassword(employerDetails.getPdfPassword());
        dto.setUserReference(employerDetails.getUserReference());
        dto.setDatePAYESchemeStarted(employerDetails.getDatePAYESchemeStarted());
        dto.setDatePAYESchemeCeased(employerDetails.getDatePAYESchemeCeased());
        dto.setRtiBatchProcessing(employerDetails.getRtiBatchProcessing());
        dto.setPreviousWorksNumberUnknown(employerDetails.getPreviousWorksNumberUnknown());
        dto.setEnsureUniqueWorksNumber(employerDetails.getEnsureUniqueWorksNumber());
        dto.setWarnBelowNationalMinimumWage(employerDetails.getWarnBelowNationalMinimumWage());
        dto.setShowAgeOnHourlyTab(employerDetails.getShowAgeOnHourlyTab());
        dto.setCompanyLogo(employerDetails.getCompanyLogo());
        dto.setCompanyName(employerDetails.getCompanyName());
        dto.setPayPeriod(employerDetails.getPayPeriod());
        dto.setRegion(employerDetails.getRegion());
        dto.setTaxYear(employerDetails.getTaxYear());
        dto.setPayDate(employerDetails.getPayDate());

//        dto.setTermsDto(mapToTermsDto(employerDetails.getTerms()));
//        dto.setTaxOfficeDto(mapToTaxOfficeDto(employerDetails.getTaxOffice()));
//        dto.setBankDetailsDTO(mapToBanKDetailsDTO(employerDetails.getBankDetails()));
        if (employerDetails.getTaxOffice() != null) {
            dto.setTaxOfficeDto(mapToTaxOfficeDto(employerDetails.getTaxOffice()));
        }

        if (employerDetails.getTerms() != null) {
            dto.setTermsDto(mapToTermsDto(employerDetails.getTerms()));
        }

        if (employerDetails.getBankDetails() != null) {
            dto.setBankDetailsDTO(mapToBankDetailsDTO(employerDetails.getBankDetails()));
        }
        if (employerDetails.getOtherEmployerDetails() !=null){
            dto.setOtherEmployerDetailsDto(mapToOtherEmployerDetailsDTO(employerDetails.getOtherEmployerDetails()));
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
        employerDetails.setContactForename(employerDto.getContactForename());
        employerDetails.setContactSurname(employerDto.getContactSurname());
        employerDetails.setPdfPassword(employerDto.getPdfPassword());
        employerDetails.setUserReference(employerDto.getUserReference());
        employerDetails.setDatePAYESchemeStarted(employerDto.getDatePAYESchemeStarted());
        employerDetails.setDatePAYESchemeCeased(employerDto.getDatePAYESchemeCeased());
        employerDetails.setRtiBatchProcessing(employerDto.getRtiBatchProcessing());
        employerDetails.setPreviousWorksNumberUnknown(employerDto.getPreviousWorksNumberUnknown());
        employerDetails.setEnsureUniqueWorksNumber(employerDto.getEnsureUniqueWorksNumber());
        employerDetails.setWarnBelowNationalMinimumWage(employerDto.getWarnBelowNationalMinimumWage());
        employerDetails.setShowAgeOnHourlyTab(employerDto.getShowAgeOnHourlyTab());
        employerDetails.setCompanyLogo(employerDto.getCompanyLogo());
        employerDetails.setPayDate(employerDto.getPayDate());
//        employerDetails.setTaxOffice(changeToTaxOffice(employerDto.getTaxOfficeDto()));
//        employerDetails.setTerms(changeToTerms(employerDto.getTermsDto()));
        employerDetails.setCompanyName(employerDto.getCompanyName());
        employerDetails.setPayPeriod(employerDto.getPayPeriod());
        employerDetails.setRegion(employerDto.getRegion());
        employerDetails.setTaxYear(employerDto.getTaxYear());
//        BankDetails bankDetails = mapToBankDetails(employerDto);
//        employerDetails.setBankDetails(bankDetails);
        if (employerDto.getTaxOfficeDto() != null) {
            employerDetails.setTaxOffice(changeToTaxOffice(employerDto.getTaxOfficeDto()));
        }

        if (employerDto.getTermsDto() != null) {
            employerDetails.setTerms(changeToTerms(employerDto.getTermsDto()));
        }

        if (employerDto.getBankDetailsDTO() != null) {
            employerDetails.setBankDetails(changeToBankDetails(employerDto));
        }
        /*if(employerDto.getOtherEmployerDetailsDto() !=null){
            employerDetails.setOtherEmployerDetails(changeToOtherEmployerDetails(employerDto.getOtherEmployerDetailsDto()));
        }*/
        OtherEmployerDetails otherEmployerDetails;

        if (employerDto.getOtherEmployerDetailsDto() != null) {
            otherEmployerDetails = changeToOtherEmployerDetails(employerDto.getOtherEmployerDetailsDto());
        } else {
            otherEmployerDetails = new OtherEmployerDetails();
            otherEmployerDetails.setDefaults(); // assuming you have this method in entity
        }

        employerDetails.setOtherEmployerDetails(otherEmployerDetails);

        return employerDetails;
    }

    public TaxOfficeDTO mapToTaxOfficeDto(TaxOffice taxOffice) {

        TaxOfficeDTO dto = new TaxOfficeDTO();
        dto.setPayeReference(taxOffice.getPayeReference());
        dto.setAccountsOfficeReference(taxOffice.getAccountsOfficeReference());
        dto.setPaymentMethod(taxOffice.getPaymentMethod());
        dto.setUniqueTaxRef(taxOffice.getUniqueTaxRef());
        dto.setCorporationTaxRef(taxOffice.getCorporationTaxRef());
        dto.setPayrollGivingRef(taxOffice.getPayrollGivingRef());
        dto.setSerQualifiedThisYear(taxOffice.getSerQualifiedThisYear());
        dto.setSerQualifiedLastYear(taxOffice.getSerQualifiedLastYear());
        dto.setNoRtiDueWarnings(taxOffice.getNoRtiDueWarnings());
        dto.setClaimNICAllowance(taxOffice.getClaimNICAllowance());
        dto.setClaimEmploymentAllowance(taxOffice.getClaimEmploymentAllowance());
        dto.setChildSupportRef(taxOffice.getChildSupportRef());

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
        taxOffice.setSerQualifiedThisYear(taxOfficeDto.getSerQualifiedThisYear());
        taxOffice.setSerQualifiedLastYear(taxOfficeDto.getSerQualifiedLastYear());
        taxOffice.setNoRtiDueWarnings(taxOfficeDto.getNoRtiDueWarnings());
        taxOffice.setClaimNICAllowance(taxOfficeDto.getClaimNICAllowance());
        taxOffice.setClaimEmploymentAllowance(taxOfficeDto.getClaimEmploymentAllowance());
        taxOffice.setChildSupportRef(taxOfficeDto.getChildSupportRef());

        return taxOffice;


    }
    public TermsDTO mapToTermsDto(Terms terms) {
        TermsDTO dto = new TermsDTO();
        dto.setHoursWorkedPerWeek(terms.getHoursWorkedPerWeek());
        dto.setIsPaidOvertime(terms.getIsPaidOvertime());
        dto.setWeeksNoticeRequired(terms.getWeeksNoticeRequired());
        dto.setDaysSicknessOnFullPay(terms.getDaysSicknessOnFullPay());
        dto.setMaleRetirementAge(terms.getMaleRetirementAge());
        dto.setFemaleRetirementAge(terms.getFemaleRetirementAge());
        dto.setMayJoinPensionScheme(terms.getMayJoinPensionScheme());
        dto.setDaysHolidayPerYear(terms.getDaysHolidayPerYear());
        dto.setMaxDaysToCarryOver(terms.getMaxDaysToCarryOver());

        return dto;
    }
    public Terms changeToTerms(TermsDTO termsDto) {
        Terms terms = new Terms();
        terms.setHoursWorkedPerWeek(termsDto.getHoursWorkedPerWeek());
        terms.setIsPaidOvertime(termsDto.getIsPaidOvertime());
        terms.setWeeksNoticeRequired(termsDto.getWeeksNoticeRequired());
        terms.setDaysSicknessOnFullPay(termsDto.getDaysSicknessOnFullPay());
        terms.setMaleRetirementAge(termsDto.getMaleRetirementAge());
        terms.setFemaleRetirementAge(termsDto.getFemaleRetirementAge());
        terms.setMayJoinPensionScheme(termsDto.getMayJoinPensionScheme());
        terms.setDaysHolidayPerYear(termsDto.getDaysHolidayPerYear());
        terms.setMaxDaysToCarryOver(termsDto.getMaxDaysToCarryOver());

        return terms;
    }

    public BankDetails changeToBankDetails(EmployerDetailsDTO employerDetailsDto) {
        BankDetails bankDetails = new BankDetails();
        bankDetails.setAccountName(employerDetailsDto.getBankDetailsDTO().getAccountName());
        bankDetails.setAccountNumber(employerDetailsDto.getBankDetailsDTO().getAccountNumber());
        bankDetails.setPaymentReference(employerDetailsDto.getBankDetailsDTO().getPaymentReference());
        bankDetails.setBankName(employerDetailsDto.getBankDetailsDTO().getBankName());
        bankDetails.setSortCode(employerDetailsDto.getBankDetailsDTO().getSortCode());
        bankDetails.setBankAddress(employerDetailsDto.getBankDetailsDTO().getBankAddress());
        bankDetails.setBankPostCode(employerDetailsDto.getBankDetailsDTO().getBankPostCode());
        bankDetails.setTelephone(employerDetailsDto.getBankDetailsDTO().getTelephone());
        bankDetails.setPaymentLeadDays(employerDetailsDto.getBankDetailsDTO().getPaymentLeadDays());
        bankDetails.setIsRTIReturnsIncluded(employerDetailsDto.getBankDetailsDTO().getIsRTIReturnsIncluded());
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
}

