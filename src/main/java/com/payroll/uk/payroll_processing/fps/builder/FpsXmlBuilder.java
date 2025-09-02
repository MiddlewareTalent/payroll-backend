package com.payroll.uk.payroll_processing.fps.builder;


import com.payroll.uk.payroll_processing.fps.builder.employee.EmployeeMapper;
import com.payroll.uk.payroll_processing.fps.builder.employer.EmployerMapper;
import com.payroll.uk.payroll_processing.fps.builder.header.IRHeaderMapper;
import com.payroll.uk.payroll_processing.fps.dto.FpsSubmissionRequest;
import org.springframework.stereotype.Component;
import uk.gov.govtalk.taxation.paye.rti.fullpaymentsubmission._25_26._1.FullPaymentSubmission;

import java.util.ArrayList;
import java.util.List;

@Component
public class FpsXmlBuilder {

    private final EmployerMapper employerMapper;
    private final EmployeeMapper employeeMapper;
    private final IRHeaderMapper irHeaderMapper;
    private final FinalSubmissionMapper finalSubmissionMapper ;


    public FpsXmlBuilder(EmployerMapper employerMapper,
                         EmployeeMapper employeeMapper,
                         IRHeaderMapper irHeaderMapper,
                         FinalSubmissionMapper  finalSubmissionMapper) {
        this.employerMapper = employerMapper;
        this.employeeMapper = employeeMapper;
        this.irHeaderMapper = irHeaderMapper;
        this.finalSubmissionMapper = finalSubmissionMapper;
    }

    public FullPaymentSubmission buildFpsSubmission(FpsSubmissionRequest request) {
        FullPaymentSubmission fps = new FullPaymentSubmission();

        // --- Employer ---
        fps.setEmpRefs(employerMapper.map(request.getEmpRefs()));

        // --- Related Tax Year ---
        fps.setRelatedTaxYear(request.getRelatedTaxYear());




        // --- Employees ---
        if (request.getEmployees() != null && !request.getEmployees().isEmpty()) {
            List<FullPaymentSubmission.Employee> employees = new ArrayList<>();
            request.getEmployees().forEach(e ->
                    employees.add(employeeMapper.map(e.getEmployeeDetails(),
                            e.getEmployment())));

            fps.getEmployee().addAll(employees);
        }


        // --- FinalSubmission flag ---
        if (request.getFinalSubmission() != null) {
            fps.setFinalSubmission(
                    finalSubmissionMapper.mapToFinalSubmission(request.getFinalSubmission())
            );
        }



        return fps;
    }
}

