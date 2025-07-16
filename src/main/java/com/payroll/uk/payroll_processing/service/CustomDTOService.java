package com.payroll.uk.payroll_processing.service;

import com.payroll.uk.payroll_processing.dto.customdto.EmployeesSummaryInEmployerDTO;
import com.payroll.uk.payroll_processing.dto.customdto.EmployerDashBoardDetailsDTO;
import com.payroll.uk.payroll_processing.entity.employer.EmployerDetails;
import com.payroll.uk.payroll_processing.exception.EmployeeNotFoundException;
import com.payroll.uk.payroll_processing.repository.EmployeeDetailsRepository;
import com.payroll.uk.payroll_processing.repository.EmployerDetailsRepository;
import com.payroll.uk.payroll_processing.repository.PaySlipRepository;
import com.payroll.uk.payroll_processing.utils.TaxMonthUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CustomDTOService {
    private final PaySlipRepository paySlipRepository;
    private final EmployeeDetailsRepository employeeDetailsRepository;
    private final EmployerDetailsRepository employerDetailsRepository;

    public EmployerDashBoardDetailsDTO createDataForDashboard(String employerId) {

        try {
//            EmployerDetails employerField = employerDetailsRepository.findAll().stream().findFirst().get();
            Optional<EmployerDetails> employerData = employerDetailsRepository.findByEmployerId(employerId);
            EmployerDetails employerDetails = employerData.orElseThrow(() -> new EmployeeNotFoundException("Employer not found"));

            EmployerDashBoardDetailsDTO employerDashBoardDetailsDTO = new EmployerDashBoardDetailsDTO();
            employerDashBoardDetailsDTO.setTotalEmployees(employeeDetailsRepository.count());
            employerDashBoardDetailsDTO.setTotalPaySlips(paySlipRepository.count());
            employerDashBoardDetailsDTO.setTotalPaidGrossAmountYTD(employerDetails.getOtherEmployerDetails().getTotalPaidGrossAmountYTD());
            employerDashBoardDetailsDTO.setPayPeriod(employerDetails.getPayPeriod());
            employerDashBoardDetailsDTO.setCurrentYear(employerDetails.getTaxYear());
            employerDashBoardDetailsDTO.setCurrentPayMonth(TaxMonthUtils.getUkTaxMonth(LocalDate.now()));
            employerDashBoardDetailsDTO.setCurrentYearCompletedDays(TaxMonthUtils.getDaysCompletedInTaxYear(LocalDate.now()));
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

}
