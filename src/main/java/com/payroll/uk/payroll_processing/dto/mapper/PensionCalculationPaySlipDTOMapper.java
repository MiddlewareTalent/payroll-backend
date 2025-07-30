package com.payroll.uk.payroll_processing.dto.mapper;

import com.payroll.uk.payroll_processing.dto.PensionCalculationPaySlipDTO;
import com.payroll.uk.payroll_processing.entity.PensionCalculationPaySlip;
import com.payroll.uk.payroll_processing.exception.ResourceNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class PensionCalculationPaySlipDTOMapper {

    public PensionCalculationPaySlipDTO mapToPensionCalculationPaySlipDTO(PensionCalculationPaySlip pensionCalculationPaySlip) {
        if (pensionCalculationPaySlip==null){
            throw new ResourceNotFoundException("No Data Found or cannot be null");
        }
        PensionCalculationPaySlipDTO pensionCalculationPaySlipDTO = new PensionCalculationPaySlipDTO();
        pensionCalculationPaySlipDTO.setPaySlipReference(pensionCalculationPaySlip.getPaySlipReference());
        pensionCalculationPaySlipDTO.setEmployeeId(pensionCalculationPaySlip.getEmployeeId());
        pensionCalculationPaySlipDTO.setNationalInsuranceNumber(pensionCalculationPaySlip.getNationalInsuranceNumber());
        pensionCalculationPaySlipDTO.setHasPensionEligible(pensionCalculationPaySlip.isHasPensionEligible());
        pensionCalculationPaySlipDTO.setIncome(pensionCalculationPaySlip.getIncome());
        pensionCalculationPaySlipDTO.setPensionContributionDeductionAmount(pensionCalculationPaySlip.getPensionContributionDeductionAmount());
        pensionCalculationPaySlipDTO.setEmployerPensionContributionAmount(pensionCalculationPaySlip.getEmployerPensionContributionAmount());
        pensionCalculationPaySlipDTO.setId(pensionCalculationPaySlip.getId());
        return pensionCalculationPaySlipDTO;

    }

}
