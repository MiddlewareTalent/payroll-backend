package com.payroll.uk.payroll_processing.dto.mapper;

import com.payroll.uk.payroll_processing.dto.LoanCalculationPaySlipDTO;
import com.payroll.uk.payroll_processing.entity.LoanCalculationPaySlip;
import org.springframework.stereotype.Component;

@Component
public class LoanCalculationPaySlipDTOMapper {

    public LoanCalculationPaySlip changeToLoanCalculationPaySlip(LoanCalculationPaySlipDTO loanCalculationPaySlipDTO) {
        if (loanCalculationPaySlipDTO == null) {
            return null;
        }
        LoanCalculationPaySlip loanCalculationPaySlip = new LoanCalculationPaySlip();
        loanCalculationPaySlip.setEmployeeId(loanCalculationPaySlipDTO.getEmployeeId());
        loanCalculationPaySlip.setNationalInsuranceNumber(loanCalculationPaySlipDTO.getNationalInsuranceNumber());
       loanCalculationPaySlip.setHasStudentLoan(loanCalculationPaySlipDTO.getHasStudentLoan());
       loanCalculationPaySlip.setStudentLoanPlanType(loanCalculationPaySlipDTO.getStudentLoanPlanType());
       loanCalculationPaySlip.setTotalDeductionAmountInStudentLoan(loanCalculationPaySlipDTO.getTotalDeductionAmountInStudentLoan());
        loanCalculationPaySlip.setHasPostgraduateLoan(loanCalculationPaySlipDTO.getHasPostgraduateLoan());
        loanCalculationPaySlip.setPostgraduateLoanPlanType(loanCalculationPaySlipDTO.getPostgraduateLoanPlanType());
        loanCalculationPaySlip.setTotalDeductionAmountInPostgraduateLoan(loanCalculationPaySlipDTO.getTotalDeductionAmountInPostgraduateLoan());
        loanCalculationPaySlip.setPaySlipReference(loanCalculationPaySlipDTO.getPaySlipReference());

        return loanCalculationPaySlip;


    }
    public LoanCalculationPaySlipDTO mapToLoanCalculationPaySlipDTO(LoanCalculationPaySlip loanCalculationPaySlip) {
        if (loanCalculationPaySlip == null) {
            return null;
        }
        LoanCalculationPaySlipDTO loanCalculationPaySlipDTO = new LoanCalculationPaySlipDTO();
        loanCalculationPaySlipDTO.setEmployeeId(loanCalculationPaySlip.getEmployeeId());
        loanCalculationPaySlipDTO.setNationalInsuranceNumber(loanCalculationPaySlip.getNationalInsuranceNumber());
        loanCalculationPaySlipDTO.setStudentLoanPlanType(loanCalculationPaySlip.getStudentLoanPlanType());
        loanCalculationPaySlipDTO.setHasStudentLoan(loanCalculationPaySlip.getHasStudentLoan());
        loanCalculationPaySlipDTO.setTotalDeductionAmountInStudentLoan(loanCalculationPaySlip.getTotalDeductionAmountInStudentLoan());
        loanCalculationPaySlipDTO.setHasPostgraduateLoan(loanCalculationPaySlip.getHasPostgraduateLoan());
        loanCalculationPaySlipDTO.setPostgraduateLoanPlanType(loanCalculationPaySlip.getPostgraduateLoanPlanType());
        loanCalculationPaySlipDTO.setTotalDeductionAmountInPostgraduateLoan(loanCalculationPaySlip.getTotalDeductionAmountInPostgraduateLoan());
        loanCalculationPaySlipDTO.setPaySlipReference(loanCalculationPaySlip.getPaySlipReference());
        return loanCalculationPaySlipDTO;
    }


}
