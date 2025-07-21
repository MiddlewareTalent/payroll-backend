package com.payroll.uk.payroll_processing.service;

import com.payroll.uk.payroll_processing.dto.employerdto.EmployerDetailsDTO;
import com.payroll.uk.payroll_processing.dto.mapper.EmployerDetailsDTOMapper;
import com.payroll.uk.payroll_processing.entity.employer.EmployerDetails;
import com.payroll.uk.payroll_processing.repository.EmployerDetailsRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployerService {
    private final EmployerDetailsRepository employerDetailsRepository;
    private final EmployerDetailsDTOMapper employerDetailsDtoMapper;

    public EmployerService(EmployerDetailsRepository employerDetailsRepository,
                           EmployerDetailsDTOMapper employerDetailsDtoMapper) {
        this.employerDetailsRepository = employerDetailsRepository;
        this.employerDetailsDtoMapper = employerDetailsDtoMapper;
    }
    public String registerEmployer(EmployerDetailsDTO employerDetailsDto) {
        // Check for existing email
//        if (employerRegistrationRepository.existsByEmail(employerDetailsDto.getEmail())) {
//            throw new EmployerRegistrationException("Email already registered");
//        }

        EmployerDetails employerData = employerDetailsDtoMapper.changeToEmployerDetails(employerDetailsDto);
        EmployerDetails savedEmployer = employerDetailsRepository.save(employerData);

        return "Employer registered successfully. ID: " + savedEmployer.getId();
    }

    public EmployerDetailsDTO getEmployerDetails(Long id) {

        if (employerDetailsRepository.findById(id).isPresent()) {
            EmployerDetails employerDetails = employerDetailsRepository.findById(id).get();
            return employerDetailsDtoMapper.mapToEmployerDetailsDTO(employerDetails);
        } else {
            throw new RuntimeException("Employer not found with ID: " + id);
        }
    }

    public EmployerDetailsDTO updateEmployerDetailsById(Long id, EmployerDetailsDTO employerDetailsDto){
        if (employerDetailsDto.getEmployerId() == null || employerDetailsDto.getEmployerId().isEmpty()) {
            throw new IllegalArgumentException("Employee ID or Id cannot be null or empty");
        }

        EmployerDetails existingEmployerDetails = employerDetailsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Employer not found with ID: " + employerDetailsDto.getEmployerId()));

        EmployerDetails updatedEmployerDetails = employerDetailsDtoMapper.changeToEmployerDetails(employerDetailsDto);
        updatedEmployerDetails.getBankDetails().setId(existingEmployerDetails.getBankDetails().getId());
        updatedEmployerDetails.setId(existingEmployerDetails.getId()); // Preserve the existing ID
        EmployerDetails savedEmployerDetails = employerDetailsRepository.save(updatedEmployerDetails);
        return employerDetailsDtoMapper.mapToEmployerDetailsDTO(savedEmployerDetails);
    }

    public List<EmployerDetailsDTO> getAllEmployeeDetails() {
        List<EmployerDetails> employerDetailsList = employerDetailsRepository.findAll();
        List<EmployerDetailsDTO> employerDetailsListedData = employerDetailsList.stream().map(employerDetailsDtoMapper::mapToEmployerDetailsDTO).toList();
        if(employerDetailsListedData.isEmpty()) {
            throw new IllegalArgumentException("No employee details found");
        }
        return employerDetailsListedData;
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
