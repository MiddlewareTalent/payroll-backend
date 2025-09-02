package com.payroll.uk.payroll_processing.fps.dto;


import com.payroll.uk.payroll_processing.fps.EmployerDetailsDataDTO;
import com.payroll.uk.payroll_processing.fps.dto.ir.IRHeaderDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FpsSubmissionRequest {

    private IRHeaderDTO irHeader;
    private EmployerDetailsDataDTO empRefs;
    private String relatedTaxYear;
    private List<FpsEmployeeDTO> employees;
    private FinalSubmissionDTO finalSubmission;

}

