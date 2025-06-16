package com.payroll.uk.payroll_processing.service.payslip;

import com.payroll.uk.payroll_processing.dto.PaySlipCreateDto;
import com.payroll.uk.payroll_processing.dto.mapper.PaySlipCreateDTOMapper;
import com.payroll.uk.payroll_processing.entity.PaySlip;
import com.payroll.uk.payroll_processing.entity.employee.EmployeeDetails;
import com.payroll.uk.payroll_processing.entity.employee.OtherEmployeeDetails;
import com.payroll.uk.payroll_processing.entity.employee.PostGraduateLoan;
import com.payroll.uk.payroll_processing.entity.employee.StudentLoan;
import com.payroll.uk.payroll_processing.entity.employer.EmployerDetails;
import com.payroll.uk.payroll_processing.entity.employer.OtherEmployerDetails;
import com.payroll.uk.payroll_processing.repository.EmployeeDetailsRepository;
import com.payroll.uk.payroll_processing.repository.EmployerDetailsRepository;
import com.payroll.uk.payroll_processing.repository.PaySlipRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Data
@AllArgsConstructor
@NoArgsConstructor
@Service
public class UpdatingDetails {
    @Autowired
    private PaySlipRepository paySlipRepository;
    @Autowired
    private EmployeeDetailsRepository employeeDetailsRepository;
    @Autowired
    private EmployerDetailsRepository employerDetailsRepository;

    @Autowired
    private PaySlipCreateDTOMapper paySlipCreateDTOMapper;

    public void updatingOtherEmployeeDetails( PaySlip paySlip){
        if(paySlip.getEmployeeId() == null){
            throw new IllegalArgumentException("Pay slip and Employee Id are miss match");
        }
        EmployeeDetails employeeDetails = employeeDetailsRepository.findByEmployeeId(paySlip.getEmployeeId())
                .orElseThrow(() -> new RuntimeException("Employer not found with ID: " + paySlip.getEmployeeId()));
        OtherEmployeeDetails otherEmployeeDetails = employeeDetails.getOtherEmployeeDetails();

        if(!employeeDetails.getIsEmergencyCode()) {
            BigDecimal remainingPersonalAllowanceInYear = employeeDetailsRepository.findByRemainingPersonalAllowanceInYear(employeeDetails.getEmployeeId());
            BigDecimal totalUsedPersonalAllowance = employeeDetailsRepository.findByTotalUsedPersonalAllowance(employeeDetails.getEmployeeId());
            BigDecimal remainingPersonalAllowance = employeeDetailsRepository.findByRemainingPersonalAllowance(employeeDetails.getEmployeeId());
            if (remainingPersonalAllowance.compareTo(BigDecimal.ZERO) == 0) {
                remainingPersonalAllowance = remainingPersonalAllowanceInYear.subtract(paySlip.getPersonalAllowance());
            } else {
                remainingPersonalAllowance = remainingPersonalAllowance.subtract(paySlip.getPersonalAllowance());
            }

            otherEmployeeDetails.setUsedPersonalAllowance(paySlip.getPersonalAllowance());
            otherEmployeeDetails.setTotalUsedPersonalAllowance(totalUsedPersonalAllowance.add(paySlip.getPersonalAllowance()));
            otherEmployeeDetails.setRemainingPersonalAllowance(remainingPersonalAllowance.subtract(paySlip.getPersonalAllowance()));


        }
        // Update OtherEmployeeDetails with income tax information
        otherEmployeeDetails.setIncomeTaxPaid(paySlip.getIncomeTaxTotal());
        BigDecimal totalIncomeTaxPaidInCompany = employeeDetailsRepository.findByTotalIncomeTaxPaidInCompany(employeeDetails.getEmployeeId());

        otherEmployeeDetails.setTotalIncomeTaxPaidInCompany(totalIncomeTaxPaidInCompany.add(paySlip.getIncomeTaxTotal()));
        if("Yearly".equalsIgnoreCase(String.valueOf(employeeDetails.getPayPeriod()))){
            BigDecimal yearCount=employeeDetailsRepository.findByNumberOfYearsOfIncomeTaxPaid(employeeDetails.getEmployeeId());
            otherEmployeeDetails.setNumberOfYearsOfIncomeTaxPaid(yearCount.add(BigDecimal.ONE));
        } else if ("Monthly".equalsIgnoreCase(String.valueOf(employeeDetails.getPayPeriod()))) {
            BigDecimal monthCount=employeeDetailsRepository.findByNumberOfMonthsOfIncomeTaxPaid(employeeDetails.getEmployeeId());
            otherEmployeeDetails.setNumberOfMonthsOfIncomeTaxPaid(monthCount.add(BigDecimal.ONE));
        }
        else if ("Weekly".equalsIgnoreCase(String.valueOf(employeeDetails.getPayPeriod()))) {
            BigDecimal weekCount=employeeDetailsRepository.findByNumberOfWeeksOfIncomeTaxPaid(employeeDetails.getEmployeeId());
            otherEmployeeDetails.setNumberOfWeeksOfIncomeTaxPaid(weekCount.add(BigDecimal.ONE));
        }
        // Update OtherEmployeeDetails with National Insurance contributions
        otherEmployeeDetails.setEmployeeNIContribution(paySlip.getEmployeeNationalInsurance());
        BigDecimal totalEmployeeNIContributionInCompany=employeeDetailsRepository.findByTotalEmployeeNIContributionInCompany(employeeDetails.getEmployeeId());
        otherEmployeeDetails.setTotalEmployeeNIContributionInCompany(
                totalEmployeeNIContributionInCompany.add(paySlip.getEmployeeNationalInsurance())
        );

//        int numberOfNIPaidYearsInCompany=employeeDetailsRepository.findByNumberOfNIPaidYearsInCompany(employeeDetails.getEmployeeId());
//        otherEmployeeDetails.setNumberOfNIPaidYearsInCompany(numberOfNIPaidYearsInCompany+1);


        if("YEARLY".equalsIgnoreCase(String.valueOf(employeeDetails.getPayPeriod()))){
            BigDecimal yearCount=employeeDetailsRepository.findByNumberOfYearsOfNIContributions(employeeDetails.getEmployeeId());
            otherEmployeeDetails.setNumberOfYearsOfNIContributions(yearCount.add(BigDecimal.ONE));
        } else if ("MONTHLY".equalsIgnoreCase(String.valueOf(employeeDetails.getPayPeriod()))) {
            BigDecimal monthCount=employeeDetailsRepository.findByNumberOfMonthsOfNIContributions(employeeDetails.getEmployeeId());
            otherEmployeeDetails.setNumberOfMonthsOfNIContributions(monthCount.add(BigDecimal.ONE));
        }
        else if ("WEEKLY".equalsIgnoreCase(String.valueOf(employeeDetails.getPayPeriod()))) {
            BigDecimal weekCount=employeeDetailsRepository.findByNumberOfWeeksOfNIContributions(employeeDetails.getEmployeeId());
            otherEmployeeDetails.setNumberOfWeeksOfNIContributions(weekCount.add(BigDecimal.ONE));
        }

        employeeDetails.setOtherEmployeeDetails(otherEmployeeDetails);

        EmployeeDetails employeeData = employeeDetailsRepository.save(employeeDetails);
        System.out.println("Successfully Updated other Details in EmployeeDetails: \n  " + employeeData);

    }
    public void updatingStudentLoanInEmployeeDetails(PaySlip paySlip){
        if (paySlip==null){
            throw new IllegalArgumentException("Pay Slip data cannot empty");
        }

        if(paySlip.getEmployeeId() == null){
            throw new IllegalArgumentException("Pay slip and Employee Id are miss match");
        }
        EmployeeDetails employeeDetails = employeeDetailsRepository.findByEmployeeId(paySlip.getEmployeeId())
                .orElseThrow(() -> new RuntimeException("Employer not found with ID: " + paySlip.getEmployeeId()));
        StudentLoan studentLoan = employeeDetails.getStudentLoan();


        if("YEARLY".equalsIgnoreCase(String.valueOf(paySlip.getPayPeriod()))){
            BigDecimal totalYearDeductionAmount=employeeDetailsRepository.findByYearlyDeductionAmountInStudentLoan(employeeDetails.getEmployeeId());
            studentLoan.setYearlyDeductionAmountInStudentLoan(totalYearDeductionAmount.add(paySlip.getStudentLoanDeductionAmount()));
        }
        else if ("MONTHLY".equalsIgnoreCase(String.valueOf(paySlip.getPayPeriod()))) {
            BigDecimal totalMonthlyDeductionAmount=employeeDetailsRepository.findByMonthlyDeductionAmountInStudentLoan(employeeDetails.getEmployeeId());
            studentLoan.setMonthlyDeductionAmountInStudentLoan(totalMonthlyDeductionAmount.add(paySlip.getStudentLoanDeductionAmount()));
        }
        else if ("WEEKLY".equalsIgnoreCase(String.valueOf(paySlip.getPayPeriod()))) {
            BigDecimal totalWeeklyDeductionAmount=employeeDetailsRepository.findByWeeklyDeductionAmountInStudentLoan(employeeDetails.getEmployeeId());
            studentLoan.setWeeklyDeductionAmountInStudentLoan(totalWeeklyDeductionAmount.add(paySlip.getStudentLoanDeductionAmount()));
        }
        BigDecimal totalDeductionAmount=employeeDetailsRepository.findByTotalDeductionAmountInStudentLoan(employeeDetails.getEmployeeId());
        studentLoan.setTotalDeductionAmountInStudentLoan(totalDeductionAmount.add(paySlip.getStudentLoanDeductionAmount()));

        employeeDetails.setStudentLoan(studentLoan);
        EmployeeDetails employeeData = employeeDetailsRepository.save(employeeDetails);
        System.out.println("Successfully Updated Student Loan Details in EmployerDetails: \n " + employeeData);

    }
    public void updatingPostGraduateLoanInEmployeeDetails(PaySlip paySlip){
        if (paySlip==null){
            throw new IllegalArgumentException("Pay Slip data cannot empty");
        }
        if(paySlip.getEmployeeId() == null){
            throw new IllegalArgumentException("Pay slip and Employee Id are miss match");
        }
        EmployeeDetails employeeDetails = employeeDetailsRepository.findByEmployeeId(paySlip.getEmployeeId())
                .orElseThrow(() -> new RuntimeException("Employer not found with ID: " + paySlip.getEmployeeId()));
        PostGraduateLoan postGraduateLoan = employeeDetails.getPostGraduateLoan();

        if("YEARLY".equalsIgnoreCase(String.valueOf(paySlip.getPayPeriod()))){
            BigDecimal totalYearDeductionAmount=employeeDetailsRepository.findByYearlyDeductionAmountInPostgraduateLoan(employeeDetails.getEmployeeId());
            postGraduateLoan.setYearlyDeductionAmountInPostgraduateLoan(totalYearDeductionAmount.add(paySlip.getPostgraduateDeductionAmount()));
        }
        else if ("MONTHLY".equalsIgnoreCase(String.valueOf(paySlip.getPayPeriod()))) {
            BigDecimal totalMonthlyDeductionAmount=employeeDetailsRepository.findByMonthlyDeductionAmountInPostgraduateLoan(employeeDetails.getEmployeeId());
            postGraduateLoan.setMonthlyDeductionAmountInPostgraduateLoan(totalMonthlyDeductionAmount.add(paySlip.getPostgraduateDeductionAmount()));
        }
        else if ("WEEKLY".equalsIgnoreCase(String.valueOf(paySlip.getPayPeriod()))) {
            BigDecimal totalWeeklyDeductionAmount=employeeDetailsRepository.findByWeeklyDeductionAmountInPostgraduateLoan(employeeDetails.getEmployeeId());
            postGraduateLoan.setWeeklyDeductionAmountInPostgraduateLoan(totalWeeklyDeductionAmount.add(paySlip.getPostgraduateDeductionAmount()));
        }

        BigDecimal totalDeductionAmount=employeeDetailsRepository.findByTotalDeductionAmountInPostgraduateLoan(employeeDetails.getEmployeeId());
        postGraduateLoan.setTotalDeductionAmountInPostgraduateLoan(totalDeductionAmount.add(paySlip.getPostgraduateDeductionAmount()));

        employeeDetails.setPostGraduateLoan(postGraduateLoan);
        EmployeeDetails employeeData = employeeDetailsRepository.save(employeeDetails);
        System.out.println("Successfully Updated Post Graduate Loan Details in EmployerDetails: \n " + employeeData);



    }
    public void updatingOtherEmployerDetails(PaySlip paySlip){
        log.info("updating the other employer details in payslip:");
        if (paySlip==null){
            throw new IllegalArgumentException("Pay Slip data cannot empty");
        }
        EmployeeDetails employeeDetails = employeeDetailsRepository.findByEmployeeId(paySlip.getEmployeeId())
                .orElseThrow(() -> new RuntimeException("Employer not found with ID: " + paySlip.getEmployeeId()));

        EmployerDetails employerDetails = employerDetailsRepository.findByEmployerId(employeeDetails.getEmployerId())
                .orElseThrow(() -> new RuntimeException("Employer not found with ID: " + employeeDetails.getEmployerId()));

        OtherEmployerDetails otherEmployerDetails=employerDetails.getOtherEmployerDetails();

        if ("MONTHLY".equalsIgnoreCase(String.valueOf(paySlip.getPayPeriod()))){
            otherEmployerDetails.setMonthlyPAYE(paySlip.getIncomeTaxTotal());
        }
        else if ("WEEKLY".equalsIgnoreCase(String.valueOf(paySlip.getPayPeriod()))){
            otherEmployerDetails.setWeeklyPAYE(paySlip.getIncomeTaxTotal());
        }
        else if ("YEARLY".equalsIgnoreCase(String.valueOf(paySlip.getPayPeriod()))){
            otherEmployerDetails.setYearlyPAYE(paySlip.getIncomeTaxTotal());
        }


        BigDecimal totalPAYE = employerDetailsRepository.findByTotalPAYEYTD(employerDetails.getEmployerId());

        otherEmployerDetails.setTotalPAYEYTD(totalPAYE.add(paySlip.getIncomeTaxTotal()));
        log.info("successfully updated the up to total PAYE ");

        if ("MONTHLY".equalsIgnoreCase(String.valueOf(paySlip.getPayPeriod()))){

            otherEmployerDetails.setMonthlyEmployeesNI(paySlip.getEmployeeNationalInsurance());

        }
        else if ("WEEKLY".equalsIgnoreCase(String.valueOf(paySlip.getPayPeriod()))){
            otherEmployerDetails.setWeeklyEmployeesNI(paySlip.getEmployeeNationalInsurance());
        }
        else if ("YEARLY".equalsIgnoreCase(String.valueOf(paySlip.getPayPeriod()))){
            otherEmployerDetails.setYearlyEmployeesNI(paySlip.getEmployeeNationalInsurance());
        }
        log.info("database query before");


        BigDecimal totalEmployeeNIAmount =employerDetailsRepository.findByTotalEmployeesNIYTD(employerDetails.getEmployerId());
        System.out.println(totalEmployeeNIAmount);
        otherEmployerDetails.setTotalEmployeesNIYTD(totalEmployeeNIAmount.add(paySlip.getEmployeeNationalInsurance()));
        log.info("successfully updated the total employee NI YTD ");
        if ("MONTHLY".equalsIgnoreCase(String.valueOf(paySlip.getPayPeriod()))){
            otherEmployerDetails.setMonthlyEmployersNI(paySlip.getEmployersNationalInsurance());
        }
        else if ("WEEKLY".equalsIgnoreCase(String.valueOf(paySlip.getPayPeriod()))){
            otherEmployerDetails.setWeeklyEmployersNI(paySlip.getEmployersNationalInsurance());
        }
        else if ("YEARLY".equalsIgnoreCase(String.valueOf(paySlip.getPayPeriod()))){
            otherEmployerDetails.setYearlyEmployersNI(paySlip.getEmployersNationalInsurance());
        }
        BigDecimal totalEmployersNI =employerDetailsRepository.findByTotalEmployersNIYTD(employerDetails.getEmployerId());
        otherEmployerDetails.setTotalEmployersNIYTD(totalEmployersNI.add(paySlip.getEmployersNationalInsurance()));
        log.info("successfully update the total employers NI YTD ");
        employerDetails.setOtherEmployerDetails(otherEmployerDetails);
        EmployerDetails employerData =employerDetailsRepository.save(employerDetails);

        System.out.println("Successfully Updated Other Employer Details in EmployerDetails: \n " + employerData);
    }


}
