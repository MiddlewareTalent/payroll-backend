package com.payroll.uk.payroll_processing.dto.mapper.employerdtomapper;

import com.payroll.uk.payroll_processing.dto.employerdto.EmployerDetailsDto;
import com.payroll.uk.payroll_processing.dto.employerdto.TaxOfficeDto;
import com.payroll.uk.payroll_processing.dto.employerdto.TermsDto;
import com.payroll.uk.payroll_processing.entity.employer.EmployerDetails;
import com.payroll.uk.payroll_processing.entity.employer.TaxOffice;
import com.payroll.uk.payroll_processing.entity.employer.Terms;
import org.springframework.stereotype.Component;

@Component
public class EmployerDetailsDtoMapper {

    public EmployerDetailsDto mapToDto(EmployerDetails employerDetails){
        EmployerDetailsDto dto = new EmployerDetailsDto();
        dto.setEmployerName(employerDetails.getEmployerName());
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
        dto.setTaxOfficeDto(mapToTaxOfficeDto(employerDetails.getTaxOffice()));
        dto.setTermsDto(mapToTermsDto(employerDetails.getTerms()));
        dto.setCompanyName(employerDetails.getCompanyName());
        dto.setPayPeriod(employerDetails.getPayPeriod());
        dto.setRegion(employerDetails.getRegion());
        dto.setTaxYear(employerDetails.getTaxYear());

        return dto;
    }
    public EmployerDetails changeToEmployerDetails(EmployerDetailsDto employerDto){
        EmployerDetails employerDetails = new EmployerDetails();
        employerDetails.setEmployerName(employerDto.getEmployerName());
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
        employerDetails.setTaxOffice(changeToTaxOffice(employerDto.getTaxOfficeDto()));
        employerDetails.setTerms(changeToTerms(employerDto.getTermsDto()));
        employerDetails.setCompanyName(employerDto.getCompanyName());
        employerDetails.setPayPeriod(employerDto.getPayPeriod());
        employerDetails.setRegion(employerDto.getRegion());
        employerDetails.setTaxYear(employerDto.getTaxYear());
        return employerDetails;
    }

    public TaxOfficeDto mapToTaxOfficeDto(TaxOffice taxOffice) {
        TaxOfficeDto dto = new TaxOfficeDto();
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
    public TaxOffice changeToTaxOffice(TaxOfficeDto taxOfficeDto) {
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
    public TermsDto mapToTermsDto(Terms terms) {
        TermsDto dto = new TermsDto();
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
    public Terms changeToTerms(TermsDto termsDto) {
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
}

