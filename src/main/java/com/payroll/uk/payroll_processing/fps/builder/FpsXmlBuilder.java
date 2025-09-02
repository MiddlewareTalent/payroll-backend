package com.payroll.uk.payroll_processing.fps.builder;

import com.payroll.uk.payroll_processing.fps.builder.employee.EmployeeMapper;
import com.payroll.uk.payroll_processing.fps.builder.employer.EmployerMapper;
import com.payroll.uk.payroll_processing.fps.builder.header.IRHeaderMapper;
import com.payroll.uk.payroll_processing.fps.dto.FpsEmployeeDTO;
import com.payroll.uk.payroll_processing.fps.dto.FpsSubmissionRequest;
import org.springframework.stereotype.Component;
import uk.gov.govtalk.taxation.paye.rti.fullpaymentsubmission._25_26._1.FullPaymentSubmission;
import uk.gov.govtalk.taxation.paye.rti.fullpaymentsubmission._25_26._1.IRenvelope;
import uk.gov.govtalk.taxation.paye.rti.fullpaymentsubmission._25_26._1.IRheader;

import java.util.ArrayList;
import java.util.List;

@Component
public class FpsXmlBuilder {

    private final EmployerMapper employerMapper;
    private final EmployeeMapper employeeMapper;
    private final IRHeaderMapper irHeaderMapper;
    private final FinalSubmissionMapper finalSubmissionMapper;

    public FpsXmlBuilder(EmployerMapper employerMapper,
                         EmployeeMapper employeeMapper,
                         IRHeaderMapper irHeaderMapper,
                         FinalSubmissionMapper finalSubmissionMapper) {
        this.employerMapper = employerMapper;
        this.employeeMapper = employeeMapper;
        this.irHeaderMapper = irHeaderMapper;
        this.finalSubmissionMapper = finalSubmissionMapper;
    }

    /**
     * Build FullPaymentSubmission from request DTO
     */
    public FullPaymentSubmission buildFpsSubmission(FpsSubmissionRequest request) {
        FullPaymentSubmission fps = new FullPaymentSubmission();

        // ✅ Employer details
        if (request.getEmpRefs() != null) {
            fps.setEmpRefs(employerMapper.map(request.getEmpRefs()));
        }

        // ✅ Tax year (e.g., "25-26")
        fps.setRelatedTaxYear(request.getRelatedTaxYear());

        // ✅ Employees mapping
        if (request.getEmployees() != null && !request.getEmployees().isEmpty()) {
            List<FullPaymentSubmission.Employee> employees = new ArrayList<>();
            for (FpsEmployeeDTO e : request.getEmployees()) {
                employees.add(
                        employeeMapper.map(e.getEmployeeDetails(), e.getEmployment())
                );
            }
            fps.getEmployee().addAll(employees);
        }

        // ✅ Final submission marker (if present)
        if (request.getFinalSubmission() != null) {
            fps.setFinalSubmission(
                    finalSubmissionMapper.mapToFinalSubmission(request.getFinalSubmission())
            );
        }

        return fps;
    }

    /**
     * Build IRenvelope containing FullPaymentSubmission + IRheader
     */
    public IRenvelope buildFpsEnvelope(FpsSubmissionRequest request) {
        IRenvelope envelope = new IRenvelope();

        // ✅ IRheader from request
        if (request.getIrHeader() != null) {
            IRheader irHeader = irHeaderMapper.mapToIRheader(request.getIrHeader());
            envelope.setIRheader(irHeader);
        }

        // ✅ FullPaymentSubmission payload
        FullPaymentSubmission fpsSubmission = buildFpsSubmission(request);
        envelope.setFullPaymentSubmission(fpsSubmission);

        return envelope;
    }
}
