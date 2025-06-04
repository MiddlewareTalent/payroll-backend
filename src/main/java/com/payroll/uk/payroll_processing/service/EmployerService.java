package com.payroll.uk.payroll_processing.service;

import com.payroll.uk.payroll_processing.dto.employerdto.EmployerDetailsDto;
import com.payroll.uk.payroll_processing.dto.mapper.employerdtomapper.EmployerDetailsDtoMapper;
import com.payroll.uk.payroll_processing.entity.employer.EmployerDetails;
import com.payroll.uk.payroll_processing.repository.EmployerRegistrationRepository;
import org.springframework.stereotype.Service;

@Service
public class EmployerService {
    private final EmployerRegistrationRepository employerRegistrationRepository;
    private final EmployerDetailsDtoMapper employerDetailsDtoMapper;

    public EmployerService(EmployerRegistrationRepository employerRegistrationRepository,
                           EmployerDetailsDtoMapper employerDetailsDtoMapper) {
        this.employerRegistrationRepository = employerRegistrationRepository;
        this.employerDetailsDtoMapper = employerDetailsDtoMapper;
    }
    public String registerEmployer(EmployerDetailsDto employerDetailsDto) {
        // Check for existing email
//        if (employerRegistrationRepository.existsByEmail(employerDetailsDto.getEmail())) {
//            throw new EmployerRegistrationException("Email already registered");
//        }

        EmployerDetails employerData = employerDetailsDtoMapper.changeToEmployerDetails(employerDetailsDto);
        EmployerDetails savedEmployer = employerRegistrationRepository.save(employerData);

        return "Employer registered successfully. ID: " + savedEmployer.getId();
    }

    public EmployerDetailsDto getEmployerDetails(Long id) {

        if (employerRegistrationRepository.findById(id).isPresent()) {
            EmployerDetails employerDetails = employerRegistrationRepository.findById(id).get();
            return employerDetailsDtoMapper.mapToDto(employerDetails);
        } else {
            throw new RuntimeException("Employer not found with ID: " + id);
        }
    }

    public String updateEmployer(Long id,EmployerDetailsDto employerDetailsDto) {
        EmployerDetails existingEmployer = employerRegistrationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employer not found with ID: " + id));

        // Update the existing employer's details
        existingEmployer.setEmployerName(employerDetailsDto.getEmployerName());
        existingEmployer.setEmployerAddress(employerDetailsDto.getEmployerAddress());
        existingEmployer.setEmployerTelephone(employerDetailsDto.getEmployerTelephone());
        existingEmployer.setEmployerEmail(employerDetailsDto.getEmployerEmail());
        existingEmployer.setEmployerPostCode(employerDetailsDto.getEmployerPostCode());
        existingEmployer.setContactForename(employerDetailsDto.getContactForename());
        existingEmployer.setContactSurname(employerDetailsDto.getContactSurname());
        existingEmployer.setPdfPassword(employerDetailsDto.getPdfPassword());
        existingEmployer.setUserReference(employerDetailsDto.getUserReference());
        existingEmployer.setDatePAYESchemeStarted(employerDetailsDto.getDatePAYESchemeStarted());
        existingEmployer.setDatePAYESchemeCeased(employerDetailsDto.getDatePAYESchemeCeased());
        existingEmployer.setRtiBatchProcessing(employerDetailsDto.getRtiBatchProcessing());
        existingEmployer.setPreviousWorksNumberUnknown(employerDetailsDto.getPreviousWorksNumberUnknown());
        existingEmployer.setEnsureUniqueWorksNumber(employerDetailsDto.getEnsureUniqueWorksNumber());
        existingEmployer.setWarnBelowNationalMinimumWage(employerDetailsDto.getWarnBelowNationalMinimumWage());
        existingEmployer.setShowAgeOnHourlyTab(employerDetailsDto.getShowAgeOnHourlyTab());
        existingEmployer.setCompanyLogo(employerDetailsDto.getCompanyLogo());
        existingEmployer.setTaxOffice(employerDetailsDto.getTaxOffice());
        existingEmployer.setTerms(employerDetailsDto.getTerms());


        // Save the updated employer details
        employerRegistrationRepository.save(existingEmployer);

        return "Updated successfully";
    }

    public String deleteEmployer(Long id) {
        if (employerRegistrationRepository.existsById(id)) {
            employerRegistrationRepository.deleteById(id);
            return "Employer deleted successfully";
        } else {
            throw new RuntimeException("Employer not found with ID: " + id);
        }
    }
}
