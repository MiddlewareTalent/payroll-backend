package com.payroll.uk.payroll_processing.service.payslip;

import com.payroll.uk.payroll_processing.dto.PaySlipCreateDto;
import com.payroll.uk.payroll_processing.dto.mapper.PaySlipCreateDTOMapper;
import com.payroll.uk.payroll_processing.entity.PaySlip;
import com.payroll.uk.payroll_processing.exception.DataValidationException;
import com.payroll.uk.payroll_processing.repository.PaySlipRepository;
import com.payroll.uk.payroll_processing.service.ni.NationalInsuranceCalculation;
import com.payroll.uk.payroll_processing.service.PersonalAllowanceCalculation;
import com.payroll.uk.payroll_processing.service.incometax.TaxCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class PaySlipService {


    @Autowired
    private PaySlipRepository paySlipRepository;
    @Autowired
    private PaySlipCreateDTOMapper paySlipCreateDtoMapper;
    @Autowired
    private PaySlipGeneration paySlipGeneration;


    public PaySlipCreateDto createPaySlip(String employeeId){
        if (employeeId== null || employeeId.isEmpty()) {
            throw new DataValidationException("Employee ID cannot be null or empty");
        }
        return paySlipGeneration.fillPaySlip(employeeId);
    }


    public List<PaySlipCreateDto> getAllEmployeeId(String employeeId){
        if(employeeId==null){
            throw new DataValidationException("Employee Cannot be null or empty");
        }
        if(!paySlipRepository.existsByEmployeeId(employeeId)){
            throw new DataValidationException("Employee Id is not found");
        }

        List<PaySlip> listOfPayslips = paySlipRepository.findByEmployeeId(employeeId);
        List<PaySlipCreateDto> listOfPayslipsData = listOfPayslips.stream().map(paySlipCreateDtoMapper::mapToDto).toList();

        if (listOfPayslipsData.isEmpty()){
            throw new DataValidationException("Payslip data is not found");
        }
        return listOfPayslipsData;
    }

    public PaySlipCreateDto getPaySlipByReferences(String paySlipReference){
        if(paySlipReference ==null){
            throw new DataValidationException("paySlipReference Number cannot be Null");
        }
        if(!paySlipRepository.existsByPaySlipReference(paySlipReference)){
            throw  new DataValidationException("paySlipReference number is not found");
        }
        PaySlip paySlipData = paySlipRepository.findByPaySlipReference(paySlipReference);
        if(paySlipData==null){
            throw new DataValidationException("payslip data is null");
        }
        return paySlipCreateDtoMapper.mapToDto(paySlipData);
    }

    public List<PaySlipCreateDto> getAllPaySlips() {
        List<PaySlip> paySlips = paySlipRepository.findAll();
        if (paySlips.isEmpty()) {
            throw new DataValidationException("No payslips found");
        }
        return paySlips.stream().map(paySlipCreateDtoMapper::mapToDto).toList();
    }

    public List<PaySlipCreateDto> getAllPaySlipsByPeriod(String periodEnd) {
        if (periodEnd == null || periodEnd.isEmpty()) {
            throw new DataValidationException("Period end date cannot be null or empty");
        }
        List<PaySlip> paySlips = paySlipRepository.findAllPaySlipsByPeriod(periodEnd);
        if (paySlips.isEmpty()) {
            throw new DataValidationException("No payslips found for the specified period");
        }
        return paySlips.stream().map(paySlipCreateDtoMapper::mapToDto).toList();
    }
    public PaySlipCreateDto getPaySlipByEmployeeIdAndPeriodEnd(String employeeId, String periodEnd) {
        if (employeeId == null || employeeId.isEmpty()) {
            throw new DataValidationException("Employee ID cannot be null or empty");
        }
        if (periodEnd == null || periodEnd.isEmpty()) {
            throw new DataValidationException("Period end date cannot be null or empty");
        }
        List<PaySlip> paySlips = paySlipRepository.findPaySlipsByEmployeeIdAndPeriodEnd(employeeId, periodEnd);
        if (paySlips.isEmpty()) {
            throw new DataValidationException("No payslips found for the specified employee and period");
        }
        return paySlipCreateDtoMapper.mapToDto(paySlips.getFirst());
    }

}
