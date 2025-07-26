package com.payroll.uk.payroll_processing.service;

import com.payroll.uk.payroll_processing.dto.customdto.EmployeesSummaryInEmployerDTO;
import com.payroll.uk.payroll_processing.dto.customdto.EmployerDashBoardDetailsDTO;
import com.payroll.uk.payroll_processing.entity.PayPeriod;
import com.payroll.uk.payroll_processing.entity.employer.EmployerDetails;
import com.payroll.uk.payroll_processing.exception.EmployeeNotFoundException;
import com.payroll.uk.payroll_processing.repository.EmployeeDetailsRepository;
import com.payroll.uk.payroll_processing.repository.EmployerDetailsRepository;
import com.payroll.uk.payroll_processing.repository.PaySlipRepository;
import com.payroll.uk.payroll_processing.utils.TaxPeriodUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor
public class CustomDTOService {
    private final PaySlipRepository paySlipRepository;
    private final EmployeeDetailsRepository employeeDetailsRepository;
    private final EmployerDetailsRepository employerDetailsRepository;


    public EmployerDashBoardDetailsDTO createDataForDashboard() {

        try {
//            EmployerDetails employerField = employerDetailsRepository.findAll().stream().findFirst().get();
            EmployerDetails employerDetails = employerDetailsRepository.findAll().getFirst();
            if (employerDetails == null) {
                throw new EmployeeNotFoundException("Employer not found with ID: " );
            }

            EmployerDashBoardDetailsDTO employerDashBoardDetailsDTO = new EmployerDashBoardDetailsDTO();
            employerDashBoardDetailsDTO.setTotalEmployees(employeeDetailsRepository.count());
            employerDashBoardDetailsDTO.setTotalPaySlips(paySlipRepository.count());
            employerDashBoardDetailsDTO.setTotalPaidGrossAmountYTD(employerDetails.getOtherEmployerDetails().getTotalPaidGrossAmountYTD());
            employerDashBoardDetailsDTO.setPayPeriod(employerDetails.getCompanyDetails().getCurrentPayPeriod());
            employerDashBoardDetailsDTO.setCurrentYear(employerDetails.getCompanyDetails().getCurrentTaxYear());
            if (employerDetails.getCompanyDetails().getCurrentPayPeriod()== PayPeriod.MONTHLY) {
                employerDashBoardDetailsDTO.setCurrentPayPeriodNumber(TaxPeriodUtils.getUkTaxMonth(LocalDate.now()));
            }
            else if (employerDetails.getCompanyDetails().getCurrentPayPeriod()== PayPeriod.WEEKLY){
                employerDashBoardDetailsDTO.setCurrentPayPeriodNumber(TaxPeriodUtils.getUkTaxWeek(LocalDate.now()));
            }
            employerDashBoardDetailsDTO.setCurrentYearCompletedDays(TaxPeriodUtils.getDaysCompletedInTaxYear(LocalDate.now()));
            employerDashBoardDetailsDTO.setTotalIncomeTax(employerDetails.getOtherEmployerDetails().getTotalPAYEYTD());
            employerDashBoardDetailsDTO.setTotalEmployeeNI(employerDetails.getOtherEmployerDetails().getTotalEmployeesNIYTD());
            employerDashBoardDetailsDTO.setTotalEmployerNI(employerDetails.getOtherEmployerDetails().getTotalEmployersNIYTD());

            return employerDashBoardDetailsDTO;
        }
        catch (Exception e){
            throw new EmployeeNotFoundException("Error retrieving employer dashboard details: " + e.getMessage());
        }


    }

    public List<EmployeesSummaryInEmployerDTO> getAllData(){
        List<EmployeesSummaryInEmployerDTO> data = paySlipRepository.findByAllData();
        if (data.isEmpty()){
            throw new IllegalArgumentException("No data found");
        }
        return data;
    }

   /* public List<EmployeeDetailsDTO> getAllEmployeeDetailsForPaySlip() {
        List<EmployeeDetails> employeeDetails = employeeDetailsRepository.findCurrentPayPeriodEmployeesForPaySlipGeneration();
        if (employeeDetails.isEmpty()) {
            throw new EmployeeNotFoundException("No employee details found");
        }
        return employeeDetails.stream().map(employeeDetailsDTOMapper::mapToEmployeeDetailsDTO).collect(Collectors.toList());
    }*/



}
