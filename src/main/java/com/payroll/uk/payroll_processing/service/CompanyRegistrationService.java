package com.payroll.uk.payroll_processing.service;


import com.payroll.uk.payroll_processing.dto.companydto.CompanyRegistrationDto;
import com.payroll.uk.payroll_processing.dto.mapper.CompanyRegistrationDtoMapper;
import com.payroll.uk.payroll_processing.entity.CompanyRegistration;
//import com.payroll.uk.payroll_processing.exception.RegistrationNotFoundException;
import com.payroll.uk.payroll_processing.repository.CompanyRegistrationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CompanyRegistrationService {
    private final CompanyRegistrationRepository companyRegistrationRepository;
    private final CompanyRegistrationDtoMapper dtoMapper;

    public CompanyRegistrationService(CompanyRegistrationRepository companyRegistrationRepository, CompanyRegistrationDtoMapper dtoMapper) {
        this.companyRegistrationRepository = companyRegistrationRepository;
        this.dtoMapper = dtoMapper;
    }
    public CompanyRegistrationDto registerCompany(CompanyRegistrationDto companyRegistrationDto) {
        try{
            CompanyRegistration companyRegistration=new CompanyRegistration();
            companyRegistration.setCompanyName(companyRegistrationDto.getCompanyName());
            companyRegistration.setTaxYear(companyRegistrationDto.getTaxYear());
            companyRegistration.setPayDates(companyRegistrationDto.getPayDates());

            return dtoMapper.mapToDto(companyRegistrationRepository.save(companyRegistration));
        }
//        catch (RegistrationNotFoundException t){
//            throw  new RegistrationNotFoundException(t.getMessage());
//        }
        catch (Exception e){
            throw new RuntimeException("Error registering company: " + e.getMessage());
        }

    }
    public CompanyRegistrationDto getCompanyByName(String companyName) {
        Optional<CompanyRegistration>companyRegistration = Optional.ofNullable(companyRegistrationRepository.findByCompanyName(companyName).orElseThrow(() -> new RuntimeException("Company not found")));
        return dtoMapper.mapToDto(companyRegistration.get());

    }

    public List<String> getAllTaxYears() {
        return companyRegistrationRepository.findAll().stream()
                .map(CompanyRegistration::getTaxYear)
                .distinct()
                .toList();
    }
}
