package com.payroll.uk.payroll_processing.fps.builder.employee;

import com.payroll.uk.payroll_processing.fps.dto.employeeDetails.EmployeeDetailsDTO;
import com.payroll.uk.payroll_processing.fps.dto.employment.EmploymentDTO;
import org.springframework.stereotype.Component;
import uk.gov.govtalk.taxation.paye.rti.fullpaymentsubmission._25_26._1.FullPaymentSubmission;

import java.util.Objects;

@Component
public class EmployeeMapper {

    private final EmployeeDetailsMapper employeeDetailsMapper;
    private final EmploymentMapper employmentMapper;

    public EmployeeMapper(EmployeeDetailsMapper employeeDetailsMapper,
                          EmploymentMapper employmentMapper) {
        this.employeeDetailsMapper = employeeDetailsMapper;
        this.employmentMapper = employmentMapper;
    }

    public FullPaymentSubmission.Employee map(EmployeeDetailsDTO detailsDto,
                                              EmploymentDTO employmentDto) {
        Objects.requireNonNull(detailsDto, "EmployeeDetailsDTO is required");

        FullPaymentSubmission.Employee emp = new FullPaymentSubmission.Employee();

        // --- EmployeeDetails ---
        emp.setEmployeeDetails(employeeDetailsMapper.mapToEmployeeDetails(detailsDto));


        if (employmentDto != null) {
            FullPaymentSubmission.Employee.Employment mappedEmployment =
                    employmentMapper.mapToEmployment(employmentDto);

            emp.getEmployment().add(mappedEmployment);
        }

        return emp;
    }
}
