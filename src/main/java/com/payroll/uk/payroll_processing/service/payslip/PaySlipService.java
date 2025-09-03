package com.payroll.uk.payroll_processing.service.payslip;

import com.payroll.uk.payroll_processing.dto.PaySlipCreateDto;
import com.payroll.uk.payroll_processing.dto.mapper.PaySlipCreateDTOMapper;
import com.payroll.uk.payroll_processing.entity.PaySlip;
import com.payroll.uk.payroll_processing.exception.DataValidationException;
import com.payroll.uk.payroll_processing.repository.PaySlipRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Service
public class PaySlipService {
    private static final Logger logging= LoggerFactory.getLogger(PaySlipService.class);

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
        logging.info("pay slip creation started for employeeId: {}", employeeId);

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
//            throw new DataValidationException("No payslips found for the specified period");
            return Collections.emptyList();
        }
        return paySlips.stream().map(paySlipCreateDtoMapper::mapToDto).toList();
    }
    public List<PaySlipCreateDto> getPaySlipByEmployeeIdAndPeriodEnd(String employeeId, String periodEnd) {
        if (employeeId == null || employeeId.isEmpty()) {
            throw new DataValidationException("Employee ID cannot be null or empty");
        }
        if (periodEnd == null || periodEnd.isEmpty()) {
            throw new DataValidationException("Period end date cannot be null or empty");
        }
        List<PaySlip> paySlips = paySlipRepository.findPaySlipsByEmployeeIdAndPeriodEnd(employeeId, periodEnd);
        if (paySlips.isEmpty()) {
//            throw new DataValidationException("No payslips found for the specified employee and period");
            return Collections.emptyList();
        }
        return paySlips.stream().map(paySlipCreateDtoMapper::mapToDto).toList();
    }


    public Page<PaySlipCreateDto> getAllPaySlipsByPeriodData(String periodEnd, int page, int size, String sortBy, String sortDirection) {
        if (periodEnd == null || periodEnd.isEmpty()) {
            throw new DataValidationException("Period end date cannot be null or empty");
        }
        if (page < 0) {
            throw new DataValidationException("Page number must be zero or greater");
        }
        if (size <= 0) {
            throw new DataValidationException("Page size must be greater than zero");
        }
        if (sortBy == null || sortBy.isEmpty()) {
            throw new DataValidationException("Sort field cannot be null or empty");
        }
        if (!sortDirection.equalsIgnoreCase("asc") && !sortDirection.equalsIgnoreCase("desc")) {
            throw new DataValidationException("Sort direction must be 'asc' or 'desc'");
        }

        // Set up sorting direction
        Sort.Direction direction = sortDirection.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

        // Fetch paginated data
        Page<PaySlip> paySlipsPage = paySlipRepository.findByPeriodEnd(periodEnd, pageable);

        if (paySlipsPage.isEmpty()) {
            throw new DataValidationException("No payslips found for the specified period");
        }

        // Convert to DTO page
        return paySlipsPage.map(paySlipCreateDtoMapper::mapToDto);
    }


}
