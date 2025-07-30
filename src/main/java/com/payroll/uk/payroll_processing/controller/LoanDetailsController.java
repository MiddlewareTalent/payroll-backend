package com.payroll.uk.payroll_processing.controller;

import com.payroll.uk.payroll_processing.dto.LoanCalculationPaySlipDTO;
import com.payroll.uk.payroll_processing.service.LoanPaySlipCalculation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/loan-details")
public class LoanDetailsController {
    @Autowired
    private LoanPaySlipCalculation loanPaySlipCalculation;


    @GetMapping("/all/loan-deducted/details")
    public ResponseEntity<?> getAllLoanDeductionDetails() {

            List<LoanCalculationPaySlipDTO> allData = loanPaySlipCalculation.getAllLoanCalculationPaySlip();
            return ResponseEntity.ok(allData);

    }
    @GetMapping("/loan-details/employeeId/{employeeId}")
    public ResponseEntity<?> getLoanDataByEmployeeId(@PathVariable String employeeId){

            List<LoanCalculationPaySlipDTO> employeeLoanData = loanPaySlipCalculation.getAllLoanDeductionsByEmployeeId(employeeId);
            return ResponseEntity.ok(employeeLoanData);

    }
    @GetMapping("/loan-details/ni/{nationalInsuranceNumber}")
    public ResponseEntity<?> getLoanDataByNationalInsuranceNumber(@PathVariable String nationalInsuranceNumber){

            List<LoanCalculationPaySlipDTO> employeeLoanData = loanPaySlipCalculation.getAllLoanDeductionsByNINumber(nationalInsuranceNumber);
            return ResponseEntity.ok(employeeLoanData);


    }
}
