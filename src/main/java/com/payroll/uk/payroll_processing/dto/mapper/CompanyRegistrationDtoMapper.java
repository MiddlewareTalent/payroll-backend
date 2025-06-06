package com.payroll.uk.payroll_processing.dto.mapper;


import com.payroll.uk.payroll_processing.dto.companydto.CompanyRegistrationDto;
import com.payroll.uk.payroll_processing.entity.CompanyRegistration;
import org.springframework.stereotype.Component;

@Component
public class CompanyRegistrationDtoMapper {

    public CompanyRegistrationDto mapToDto(CompanyRegistration companyRegistration) {
        CompanyRegistrationDto dto = new CompanyRegistrationDto();
        dto.setCompanyName(companyRegistration.getCompanyName());
        dto.setTaxYear(companyRegistration.getTaxYear());
        dto.setPayPeriod(companyRegistration.getPayPeriod());
        dto.setRegion(companyRegistration.getRegion());
        dto.setId(companyRegistration.getId());
        return dto;
    }
    public CompanyRegistration changeToCompanyRegistration(CompanyRegistrationDto companyRegistrationDto) {
        CompanyRegistration companyRegistration = new CompanyRegistration();
        companyRegistration.setCompanyName(companyRegistrationDto.getCompanyName());
        companyRegistration.setTaxYear(companyRegistrationDto.getTaxYear());
        companyRegistration.setPayPeriod(companyRegistrationDto.getPayPeriod());
        companyRegistration.setRegion(companyRegistrationDto.getRegion());
        return companyRegistration;
    }
}
