package com.payroll.uk.payroll_processing.service;

import com.payroll.uk.payroll_processing.dto.employerdto.EmployerDetailsDTO;
import com.payroll.uk.payroll_processing.dto.mapper.EmployerDetailsDTOMapper;
import com.payroll.uk.payroll_processing.entity.employer.EmployerDetails;
import com.payroll.uk.payroll_processing.exception.DataValidationException;
import com.payroll.uk.payroll_processing.exception.ResourceNotFoundException;
import com.payroll.uk.payroll_processing.repository.EmployeeDetailsRepository;
import com.payroll.uk.payroll_processing.repository.EmployerDetailsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployerService {
    private static final Logger logging= LoggerFactory.getLogger(EmployerService.class);
    private final EmployerDetailsRepository employerDetailsRepository;
    private final EmployerDetailsDTOMapper employerDetailsDtoMapper;
    private final EmployeeDetailsRepository employeeDetailsRepository;

    public EmployerService(EmployerDetailsRepository employerDetailsRepository,
                           EmployerDetailsDTOMapper employerDetailsDtoMapper,
                           EmployeeDetailsRepository employeeDetailsRepository) {
        this.employerDetailsRepository = employerDetailsRepository;
        this.employerDetailsDtoMapper = employerDetailsDtoMapper;
        this.employeeDetailsRepository = employeeDetailsRepository;
    }
    public String registerEmployer(EmployerDetailsDTO employerDetailsDto) {

        if (employerDetailsDto==null){
            throw new ResourceNotFoundException("Employer details cannot be null");
        }
        EmployerDetails employerData = employerDetailsDtoMapper.changeToEmployerDetails(employerDetailsDto);
        if (employerData==null){
            throw new DataValidationException("Employer data cannot be null");
        }
        EmployerDetails savedEmployer = employerDetailsRepository.save(employerData);
        return "Employer registered successfully. ID: " + savedEmployer.getId();
    }

    public EmployerDetailsDTO getEmployerDetails(Long id) {
        if (id==null) {
            throw new DataValidationException("Employer ID cannot be null");
        }

        if (employerDetailsRepository.findById(id).isPresent()) {
            EmployerDetails employerDetails = employerDetailsRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Employer not found with ID: " + id));
            return employerDetailsDtoMapper.mapToEmployerDetailsDTO(employerDetails);
        } else {
            throw new DataValidationException("Employer not found with ID: " + id);
        }
    }

    public EmployerDetailsDTO updateEmployerDetailsById(Long id, EmployerDetailsDTO employerDetailsDto){
        if (employerDetailsDto.getEmployerId() == null || employerDetailsDto.getEmployerId().isEmpty()) {
            throw new DataValidationException("Employee ID or Id cannot be null or empty");
        }
        logging.info("update Employer Data: {}", employerDetailsDto);
        EmployerDetails existingEmployerDetails = employerDetailsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employer not found with ID: " + employerDetailsDto.getEmployerId()));

        logging.info("Existing Employer Details: {}", existingEmployerDetails);
        EmployerDetails updatedEmployerDetails = employerDetailsDtoMapper.changeToEmployerDetails(employerDetailsDto);
        updatedEmployerDetails.getBankDetails().setId(existingEmployerDetails.getBankDetails().getId());
        updatedEmployerDetails.setId(existingEmployerDetails.getId()); // Preserve the existing ID
//        updatedEmployerDetails.getTaxOffice().setPayrollGivingRef(existingEmployerDetails.getTaxOffice().getPayrollGivingRef());


        logging.info("Checking any fields updated in Employer Details like company name, tax year, pay period, region");
        if (isFieldsUpdated(existingEmployerDetails,updatedEmployerDetails)){
               employeeDetailsRepository.updateAllEmployeeDetails(updatedEmployerDetails.getCompanyDetails().getCompanyName(),
                       updatedEmployerDetails.getCompanyDetails().getCurrentPayPeriod(),
                       updatedEmployerDetails.getCompanyDetails().getCurrentTaxYear(),
                       updatedEmployerDetails.getCompanyDetails().getRegion());
        }
        logging.info("Updated Employer Details: {}", updatedEmployerDetails);
        EmployerDetails savedEmployerDetails = employerDetailsRepository.save(updatedEmployerDetails);
        return employerDetailsDtoMapper.mapToEmployerDetailsDTO(savedEmployerDetails);
    }

    public List<EmployerDetailsDTO> getAllEmployerDetails() {
        List<EmployerDetails> employerDetailsList = employerDetailsRepository.findAll();
        if ( employerDetailsList.isEmpty()) {
            throw new ResourceNotFoundException("No employer details found");
        }
        List<EmployerDetailsDTO> employerDetailsListedData = employerDetailsList.stream().map(employerDetailsDtoMapper::mapToEmployerDetailsDTO).toList();
        if(employerDetailsListedData.isEmpty()) {
            throw new DataValidationException("No employee details found");
        }
        return employerDetailsListedData;
    }

    public String deleteEmployer(Long id) {
        if (id==null) {
            throw new DataValidationException("Employer ID cannot be null");
        }
        if (employerDetailsRepository.existsById(id)) {
            employerDetailsRepository.deleteById(id);
            return "Employer deleted successfully";
        } else {
            throw new ResourceNotFoundException("Employer not found with ID: " + id);
        }
    }

    public boolean isFieldsUpdated(EmployerDetails existingEmployerDetails, EmployerDetails updatedEmployerDetails) {
       return !existingEmployerDetails.getCompanyDetails().getCompanyName().equals(updatedEmployerDetails.getCompanyDetails().getCompanyName()) ||
               !existingEmployerDetails.getCompanyDetails().getCurrentTaxYear().equals(updatedEmployerDetails.getCompanyDetails().getCurrentTaxYear()) ||
               !existingEmployerDetails.getCompanyDetails().getCurrentPayPeriod().equals(updatedEmployerDetails.getCompanyDetails().getCurrentPayPeriod()) ||
               !existingEmployerDetails.getCompanyDetails().getRegion().equals(updatedEmployerDetails.getCompanyDetails().getRegion());

    }
}
