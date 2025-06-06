package com.payroll.uk.payroll_processing.service;

import com.payroll.uk.payroll_processing.dto.employerdto.EmployerDetailsDto;
import com.payroll.uk.payroll_processing.dto.mapper.EmployerDetailsDtoMapper;
import com.payroll.uk.payroll_processing.entity.employer.EmployerDetails;
import com.payroll.uk.payroll_processing.repository.EmployerDetailsRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployerService {
    private final EmployerDetailsRepository employerDetailsRepository;
    private final EmployerDetailsDtoMapper employerDetailsDtoMapper;

    public EmployerService(EmployerDetailsRepository employerDetailsRepository,
                           EmployerDetailsDtoMapper employerDetailsDtoMapper) {
        this.employerDetailsRepository = employerDetailsRepository;
        this.employerDetailsDtoMapper = employerDetailsDtoMapper;
    }
    public String registerEmployer(EmployerDetailsDto employerDetailsDto) {
        // Check for existing email
//        if (employerRegistrationRepository.existsByEmail(employerDetailsDto.getEmail())) {
//            throw new EmployerRegistrationException("Email already registered");
//        }

        EmployerDetails employerData = employerDetailsDtoMapper.changeToEmployerDetails(employerDetailsDto);
        EmployerDetails savedEmployer = employerDetailsRepository.save(employerData);

        return "Employer registered successfully. ID: " + savedEmployer.getId();
    }

    public EmployerDetailsDto getEmployerDetails(Long id) {

        if (employerDetailsRepository.findById(id).isPresent()) {
            EmployerDetails employerDetails = employerDetailsRepository.findById(id).get();
            return employerDetailsDtoMapper.mapToDto(employerDetails);
        } else {
            throw new RuntimeException("Employer not found with ID: " + id);
        }
    }

    public String updateEmployer(Long id,EmployerDetailsDto employerDetailsDto) {
        EmployerDetails existingEmployer = employerDetailsRepository.findById(id)
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
        existingEmployer.setTaxOffice(employerDetailsDtoMapper.changeToTaxOffice(employerDetailsDto.getTaxOfficeDto()));
        existingEmployer.setTerms(employerDetailsDtoMapper.changeToTerms(employerDetailsDto.getTermsDto()));
//        existingEmployer.setBankDetails(employerDetailsDtoMapper.mapToBankDetails(employerDetailsDto.getBankDetailsDTO()));
        existingEmployer.setRegion(employerDetailsDto.getRegion());
        existingEmployer.setTaxYear(employerDetailsDto.getTaxYear());
        existingEmployer.setPayPeriod(employerDetailsDto.getPayPeriod());
        existingEmployer.setCompanyName(employerDetailsDto.getCompanyName());


        // Save the updated employer details
        employerDetailsRepository.save(existingEmployer);

        return "Updated successfully";
    }

    public EmployerDetailsDto updateEmployerDetailsById(Long id, EmployerDetailsDto employerDetailsDto){
        if (employerDetailsDto.getEmployerId() == null || employerDetailsDto.getEmployerId().isEmpty()) {
            throw new IllegalArgumentException("Employee ID or Id cannot be null or empty");
        }

        EmployerDetails existingEmployeeDetails = employerDetailsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Employee not found with ID: " + employerDetailsDto.getEmployerId()));

        EmployerDetails updatedEmployeeDetails = employerDetailsDtoMapper.changeToEmployerDetails(employerDetailsDto);
        updatedEmployeeDetails.getBankDetails().setId(existingEmployeeDetails.getBankDetails().getId());
        updatedEmployeeDetails.setId(existingEmployeeDetails.getId()); // Preserve the existing ID
        EmployerDetails savedEmployeeDetails = employerDetailsRepository.save(updatedEmployeeDetails);
        return employerDetailsDtoMapper.mapToDto(savedEmployeeDetails);
    }

    public List<EmployerDetailsDto> getAllEmployeeDetails() {
        List<EmployerDetails> employeeDetailsList = employerDetailsRepository.findAll();
        List<EmployerDetailsDto> employeeDetailsListedData = employeeDetailsList.stream().map(employerDetailsDtoMapper::mapToDto).toList();
        if(employeeDetailsListedData.isEmpty()) {
            throw new IllegalArgumentException("No employee details found");
        }
        return employeeDetailsListedData;
    }

    public String deleteEmployer(Long id) {
        if (employerDetailsRepository.existsById(id)) {
            employerDetailsRepository.deleteById(id);
            return "Employer deleted successfully";
        } else {
            throw new RuntimeException("Employer not found with ID: " + id);
        }
    }
}
