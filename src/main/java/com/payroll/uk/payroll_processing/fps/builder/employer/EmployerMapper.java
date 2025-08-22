package com.payroll.uk.payroll_processing.fps.builder.employer;


import com.payroll.uk.payroll_processing.fps.EmployerDetailsDataDTO;
import org.springframework.stereotype.Component;
import uk.gov.govtalk.taxation.paye.rti.fullpaymentsubmission._25_26._1.FullPaymentSubmission;

import java.util.Objects;

@Component
public class EmployerMapper {

    public FullPaymentSubmission.EmpRefs map(EmployerDetailsDataDTO dto) {
        Objects.requireNonNull(dto, "EmployerDetailsDataDTO is required");

        FullPaymentSubmission.EmpRefs empRefs = new FullPaymentSubmission.EmpRefs();

        // Required
        empRefs.setOfficeNo(dto.getEmpRefsOfficeNo());
        empRefs.setPayeRef(dto.getEmpRefsPayeRef());

        // Optional
        if (dto.getEmpRefsAORef() != null) {
            empRefs.setAORef(dto.getEmpRefsAORef());
        }
        if (dto.getEmpRefsSAUTR() != null) {
            empRefs.setSAUTR(dto.getEmpRefsSAUTR());
        }
        if (dto.getEmpRefsCOTAXREF() != null) {
            empRefs.setCOTAXRef(dto.getEmpRefsCOTAXREF());
        }

        return empRefs;
    }
}
