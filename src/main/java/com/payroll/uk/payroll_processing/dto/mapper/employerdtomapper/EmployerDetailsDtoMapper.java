package com.payroll.uk.payroll_processing.dto.mapper.employerdtomapper;

import com.payroll.uk.payroll_processing.dto.employerdto.EmployerDetailsDto;
import com.payroll.uk.payroll_processing.entity.employer.EmployerDetails;
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
        dto.setTaxOffice(employerDetails.getTaxOffice());
        dto.setTerms(employerDetails.getTerms());

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
        employerDetails.setTaxOffice(employerDto.getTaxOffice());
        employerDetails.setTerms(employerDto.getTerms());
        return employerDetails;
    }
}

