package com.payroll.uk.payroll_processing.service;

import com.payroll.uk.payroll_processing.dto.LoanCalculationPaySlipDTO;
import com.payroll.uk.payroll_processing.dto.mapper.LoanCalculationPaySlipDTOMapper;
import com.payroll.uk.payroll_processing.entity.LoanCalculationPaySlip;
import com.payroll.uk.payroll_processing.entity.PaySlip;
import com.payroll.uk.payroll_processing.entity.employee.EmployeeDetails;
import com.payroll.uk.payroll_processing.entity.employee.PostGraduateLoan;
import com.payroll.uk.payroll_processing.entity.employee.StudentLoan;
import com.payroll.uk.payroll_processing.exception.DataValidationException;
import com.payroll.uk.payroll_processing.repository.EmployeeDetailsRepository;
import com.payroll.uk.payroll_processing.repository.LoanCalculationPaySlipRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
//@Slf4j
public class LoanPaySlipCalculation {
    private static final Logger logger = LoggerFactory.getLogger(LoanPaySlipCalculation.class);

    @Autowired
    private LoanCalculationPaySlipRepository loanCalculationPaySlipRepository;
    @Autowired
    private EmployeeDetailsRepository employeeDetailsRepository;
    @Autowired
    private StudentLoanCalculation studentLoanCalculation;
    @Autowired
    private LoanCalculationPaySlipDTOMapper loanCalculationPaySlipDTOMapper;


    @Transactional
    public LoanCalculationPaySlipDTO calculateLoanPaySlip(PaySlip paySlip){
        if(paySlip.getEmployeeId().isBlank()){
            throw new IllegalArgumentException("Employee ID cannot be empty");
        }
        Optional<EmployeeDetails> employeeData = employeeDetailsRepository.findByEmployeeId(paySlip.getEmployeeId());
        logger.info("Calculating loan pay slip for employee: {}", paySlip.getEmployeeId());
        EmployeeDetails employeeDetails = employeeData.orElseThrow(() -> new RuntimeException("Employee not found with ID: " + paySlip.getEmployeeId()));

        if (employeeDetails.getStudentLoan().getHasStudentLoan() && employeeDetails.getStudentLoan().getStudentLoanPlanType() == StudentLoan.StudentLoanPlan.NONE) {
            throw new IllegalArgumentException("Student loan plan type cannot be NONE when student loan is true.");
        }

        if (!employeeDetails.getStudentLoan().getHasStudentLoan() && employeeDetails.getStudentLoan().getStudentLoanPlanType() != StudentLoan.StudentLoanPlan.NONE) {
            throw new IllegalArgumentException("Student loan plan type must be NONE when student loan is false.");
        }
        if (employeeDetails.getPostGraduateLoan().getHasPostgraduateLoan() && employeeDetails.getPostGraduateLoan().getPostgraduateLoanPlanType() == PostGraduateLoan.PostgraduateLoanPlanType.NONE) {
            throw new IllegalArgumentException("Postgraduate loan plan type cannot be NONE when postgraduate loan is true.");
        }
        if (!employeeDetails.getPostGraduateLoan().getHasPostgraduateLoan() && employeeDetails.getPostGraduateLoan().getPostgraduateLoanPlanType() != PostGraduateLoan.PostgraduateLoanPlanType.NONE) {
            throw new IllegalArgumentException("Postgraduate loan plan type must be NONE when postgraduate loan is false.");
        }
        LoanCalculationPaySlip calculation = new LoanCalculationPaySlip();
        calculation.setEmployeeId(employeeDetails.getEmployeeId());
        calculation.setNationalInsuranceNumber(employeeDetails.getNationalInsuranceNumber());
        calculation.setStudentLoanPlanType(employeeDetails.getStudentLoan().getStudentLoanPlanType());
        calculation.setHasStudentLoan(employeeDetails.getStudentLoan().getHasStudentLoan());
       if(calculation.getHasStudentLoan() && calculation.getStudentLoanPlanType()!= StudentLoan.StudentLoanPlan.NONE){

           calculation.setTotalDeductionAmountInStudentLoan(paySlip.getStudentLoanDeductionAmount());
       }
       else {
           calculation.setTotalDeductionAmountInStudentLoan(BigDecimal.ZERO);
       }

        calculation.setPostgraduateLoanPlanType(employeeDetails.getPostGraduateLoan().getPostgraduateLoanPlanType());
        calculation.setHasPostgraduateLoan(employeeDetails.getPostGraduateLoan().getHasPostgraduateLoan());
        if(calculation.getHasPostgraduateLoan() && calculation.getPostgraduateLoanPlanType()!= PostGraduateLoan.PostgraduateLoanPlanType.NONE){
            calculation.setTotalDeductionAmountInPostgraduateLoan(paySlip.getPostgraduateDeductionAmount());
        }
        else{
            calculation.setTotalDeductionAmountInPostgraduateLoan(BigDecimal.ZERO);
        }
        calculation.setPaySlipReference(paySlip.getPaySlipReference());
        LoanCalculationPaySlip calculatedData = loanCalculationPaySlipRepository.save(calculation);
        logger.info("Successfully calculated and saved the loan pay slip {}",calculatedData);

        if(employeeDetails.getStudentLoan().getHasStudentLoan()){
            updatingStudentLoanInEmployeeDetails(calculatedData);
            logger.info("successfully updated the student loan details in employee Details");
        }
        if (employeeDetails.getPostGraduateLoan().getHasPostgraduateLoan()){
            updatingPostGraduateLoanInEmployeeDetails(calculatedData);
            logger.info("successfully updated the post graduate loan details in employee Details");
        }
        logger.info("Successfully updated the loan details");
        return loanCalculationPaySlipDTOMapper.mapToLoanCalculationPaySlipDTO(calculatedData);


    }

    public void updatingStudentLoanInEmployeeDetails(LoanCalculationPaySlip loanCalculationPaySlip){
        if (loanCalculationPaySlip==null){
            throw new IllegalArgumentException("Pay Slip data cannot empty");
        }

        if(loanCalculationPaySlip.getEmployeeId() == null){
            throw new IllegalArgumentException("Pay slip and Employee Id are miss match");
        }
        EmployeeDetails employeeDetails = employeeDetailsRepository.findByEmployeeId(loanCalculationPaySlip.getEmployeeId())
                .orElseThrow(() -> new RuntimeException("Employer not found with ID: " + loanCalculationPaySlip.getEmployeeId()));
        StudentLoan studentLoan = employeeDetails.getStudentLoan();
        StudentLoan updateStudentLoan = new StudentLoan();
        updateStudentLoan = studentLoan;
        try {
            updateStudentLoan.setHasStudentLoan(loanCalculationPaySlip.getHasStudentLoan());
            updateStudentLoan.setStudentLoanPlanType(loanCalculationPaySlip.getStudentLoanPlanType());
            updateStudentLoan.setDeductionAmountInStudentLoan(loanCalculationPaySlip.getTotalDeductionAmountInStudentLoan());

            BigDecimal totalNumberOfPaidStudentLoan = studentLoan.getNumberOfPayPeriodsOfStudentLoan();
            totalNumberOfPaidStudentLoan = totalNumberOfPaidStudentLoan != null ? totalNumberOfPaidStudentLoan : BigDecimal.ZERO;
            updateStudentLoan.setNumberOfPayPeriodsOfStudentLoan(totalNumberOfPaidStudentLoan.add(BigDecimal.ONE));
            BigDecimal totalDeductionAmount = studentLoan.getTotalDeductionAmountInStudentLoan();
            updateStudentLoan.setTotalDeductionAmountInStudentLoan(totalDeductionAmount.add(loanCalculationPaySlip.getTotalDeductionAmountInStudentLoan()));

            employeeDetails.setStudentLoan(updateStudentLoan);
            EmployeeDetails employeeData = employeeDetailsRepository.save(employeeDetails);
            System.out.println("Successfully Updated Student Loan Details in EmployerDetails: \n " + employeeData);
        }
        catch (Exception e) {
            logger.error("Error updating student loan details: {}", e.getMessage());
            throw new RuntimeException("Failed to update student loan details: " + e.getMessage());
        }

//        if("YEARLY".equalsIgnoreCase(String.valueOf(employeeDetails.getPayPeriod()))){
//            BigDecimal totalYearDeductionAmount=studentLoan.getYearlyDeductionAmountInStudentLoan();
//            updateStudentLoan.setYearlyDeductionAmountInStudentLoan(totalYearDeductionAmount.add(loanCalculationPaySlip.getTotalDeductionAmountInStudentLoan()));
//        }
//        else if ("MONTHLY".equalsIgnoreCase(String.valueOf(employeeDetails.getPayPeriod()))) {
//            BigDecimal totalMonthlyDeductionAmount=studentLoan.getMonthlyDeductionAmountInStudentLoan();
//            updateStudentLoan.setMonthlyDeductionAmountInStudentLoan(totalMonthlyDeductionAmount.add(loanCalculationPaySlip.getTotalDeductionAmountInStudentLoan()));
//        }
//        else if ("WEEKLY".equalsIgnoreCase(String.valueOf(employeeDetails.getPayPeriod()))) {
//            BigDecimal totalWeeklyDeductionAmount=studentLoan.getWeeklyDeductionAmountInStudentLoan();
//            updateStudentLoan.setWeeklyDeductionAmountInStudentLoan(totalWeeklyDeductionAmount.add(loanCalculationPaySlip.getTotalDeductionAmountInStudentLoan()));
//        }

    }
    public void updatingPostGraduateLoanInEmployeeDetails(LoanCalculationPaySlip loanCalculationPaySlip){
        if (loanCalculationPaySlip==null){
            throw new IllegalArgumentException("Pay Slip data cannot empty");
        }
        if(loanCalculationPaySlip.getEmployeeId() == null){
            throw new IllegalArgumentException("Pay slip and Employee Id are miss match");
        }
        EmployeeDetails employeeDetails = employeeDetailsRepository.findByEmployeeId(loanCalculationPaySlip.getEmployeeId())
                .orElseThrow(() -> new RuntimeException("Employer not found with ID: " + loanCalculationPaySlip.getEmployeeId()));
        PostGraduateLoan postGraduateLoan = employeeDetails.getPostGraduateLoan();
        PostGraduateLoan updatedPostGraduateLoan = new PostGraduateLoan();
        updatedPostGraduateLoan=postGraduateLoan;
        try {
            updatedPostGraduateLoan.setHasPostgraduateLoan(loanCalculationPaySlip.getHasPostgraduateLoan());
            updatedPostGraduateLoan.setPostgraduateLoanPlanType(loanCalculationPaySlip.getPostgraduateLoanPlanType());
            updatedPostGraduateLoan.setDeductionAmountInPostgraduateLoan(loanCalculationPaySlip.getTotalDeductionAmountInPostgraduateLoan());
            BigDecimal totalNumberOfPaidPostGraduateLoan = postGraduateLoan.getNumberOfPayPeriodsOfPostgraduateLoan();
            totalNumberOfPaidPostGraduateLoan = totalNumberOfPaidPostGraduateLoan != null ? totalNumberOfPaidPostGraduateLoan : BigDecimal.ZERO;
            updatedPostGraduateLoan.setNumberOfPayPeriodsOfPostgraduateLoan(totalNumberOfPaidPostGraduateLoan.add(BigDecimal.ONE));

            BigDecimal totalDeductionAmount = postGraduateLoan.getTotalDeductionAmountInPostgraduateLoan();
            updatedPostGraduateLoan.setTotalDeductionAmountInPostgraduateLoan(totalDeductionAmount.add(loanCalculationPaySlip.getTotalDeductionAmountInPostgraduateLoan()));

            employeeDetails.setPostGraduateLoan(updatedPostGraduateLoan);
            EmployeeDetails employeeData = employeeDetailsRepository.save(employeeDetails);
            System.out.println("Successfully Updated Post Graduate Loan Details in EmployerDetails: \n " + employeeData);
        }
        catch (Exception e) {
            logger.error("Error updating post graduate loan details: {}", e.getMessage());
            throw new RuntimeException("Failed to update post graduate loan details: " + e.getMessage());
        }

       /* if("YEARLY".equalsIgnoreCase(String.valueOf(employeeDetails.getPayPeriod()))){
            BigDecimal totalYearDeductionAmount=postGraduateLoan.getYearlyDeductionAmountInPostgraduateLoan();
            updatedPostGraduateLoan.setYearlyDeductionAmountInPostgraduateLoan(totalYearDeductionAmount.add(loanCalculationPaySlip.getTotalDeductionAmountInPostgraduateLoan()));
        }
        else if ("MONTHLY".equalsIgnoreCase(String.valueOf(employeeDetails.getPayPeriod()))) {
            BigDecimal totalMonthlyDeductionAmount=postGraduateLoan.getMonthlyDeductionAmountInPostgraduateLoan();
            updatedPostGraduateLoan.setMonthlyDeductionAmountInPostgraduateLoan(totalMonthlyDeductionAmount.add(loanCalculationPaySlip.getTotalDeductionAmountInPostgraduateLoan()));
        }
        else if ("WEEKLY".equalsIgnoreCase(String.valueOf(employeeDetails.getPayPeriod()))) {
            BigDecimal totalWeeklyDeductionAmount=postGraduateLoan.getWeeklyDeductionAmountInPostgraduateLoan();
            updatedPostGraduateLoan.setWeeklyDeductionAmountInPostgraduateLoan(totalWeeklyDeductionAmount.add(loanCalculationPaySlip.getTotalDeductionAmountInPostgraduateLoan()));
        }*/

    }

    public List<LoanCalculationPaySlipDTO> getAllLoanCalculationPaySlip(){
        List<LoanCalculationPaySlip> loanCalculationPaySlipList = loanCalculationPaySlipRepository.findAll();
        if(loanCalculationPaySlipList.isEmpty()){
           throw new DataValidationException("No loan deduction pay slips found");
        }
        return loanCalculationPaySlipList.stream().map(loanCalculationPaySlipDTOMapper::mapToLoanCalculationPaySlipDTO).toList();


    }
    public List<LoanCalculationPaySlipDTO> getAllLoanDeductionsByEmployeeId(String employeeId){
        List<LoanCalculationPaySlip> loanCalculationPaySlipList = loanCalculationPaySlipRepository.findAllByEmployeeId(employeeId);
        if(loanCalculationPaySlipList.isEmpty()){
            throw new DataValidationException("No loan deduction pay slips found for employee with ID: " + employeeId);
        }
        return loanCalculationPaySlipList.stream().map(loanCalculationPaySlipDTOMapper::mapToLoanCalculationPaySlipDTO).toList();
    }

    public List<LoanCalculationPaySlipDTO> getAllLoanDeductionsByNINumber(String nationalInsuranceNumber){
        List<LoanCalculationPaySlip> loanCalculationPaySlipList = loanCalculationPaySlipRepository.findAllByNationalInsuranceNumber(nationalInsuranceNumber);
        if(loanCalculationPaySlipList.isEmpty()){
            throw new DataValidationException("No loan deduction pay slips found for national insurance number: " + nationalInsuranceNumber);
        }
        return loanCalculationPaySlipList.stream().map(loanCalculationPaySlipDTOMapper::mapToLoanCalculationPaySlipDTO).toList();
    }




}


