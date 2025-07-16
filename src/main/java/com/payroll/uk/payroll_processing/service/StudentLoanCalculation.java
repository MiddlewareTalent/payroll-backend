package com.payroll.uk.payroll_processing.service;


import com.payroll.uk.payroll_processing.entity.TaxThreshold;
import com.payroll.uk.payroll_processing.entity.employee.PostGraduateLoan;
import com.payroll.uk.payroll_processing.entity.employee.StudentLoan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
//@Slf4j
public class StudentLoanCalculation {
    private static final Logger logger = LoggerFactory.getLogger(StudentLoanCalculation.class);
    @Autowired
    private TaxThresholdService taxThresholdService;


    public BigDecimal calculateStudentLoanDeduction(BigDecimal income, Boolean hasStudentLoan,  String taxYear, String payPeriod) {
        if(!hasStudentLoan){
            return BigDecimal.ZERO;
        }
        // Validate inputs
        if (income == null || taxYear == null || payPeriod == null) {
            throw new IllegalArgumentException("Income, tax year, and pay period cannot be null");
        }

        // Get student loan thresholds and rates
        BigDecimal[][] studentLoanSlabs = taxThresholdService.getStudentLoanThresholds(taxYear, TaxThreshold.TaxRegion.ALL_REGIONS, TaxThreshold.BandNameType.STUDENT_LOAN);
//        BigDecimal[] studentLoanRates = taxThresholdService.getStudentLoanRates(taxYear, TaxThreshold.TaxRegion.ALL_REGIONS, TaxThreshold.BandNameType.STUDENT_LOAN);
        BigDecimal studentLoan_Plan1=studentLoanSlabs[0][0]; //26065
        BigDecimal studentLoan_Plan2=studentLoanSlabs[1][0]; //28470
        BigDecimal studentLoan_Plan4=studentLoanSlabs[2][0];  //32745
        System.out.println("studentLoanSlabs[0][0]: "+studentLoanSlabs[0][0]);
        System.out.println("studentLoanSlabs[0][0]: "+studentLoanSlabs[1][0]);
        System.out.println("studentLoanSlabs[0][0]: "+studentLoanSlabs[2][0]);
        BigDecimal grossIncomePerYear=calculateGrossSalary(income,payPeriod);
        System.out.println("GrossIncomePerYear: "+grossIncomePerYear);
        if(grossIncomePerYear.compareTo(studentLoan_Plan1)>0 && grossIncomePerYear.compareTo(studentLoan_Plan2)<=0){
            System.out.println("studentLoan_Plan1 "+studentLoan_Plan1);

            BigDecimal[] rate =taxThresholdService.getStudentLoanBandByPlanTypeRate(taxYear,TaxThreshold.TaxRegion.ALL_REGIONS, TaxThreshold.BandNameType.STUDENT_LOAN, TaxThreshold.BandName.STUDENT_LOAN_PLAN_1);
            BigDecimal deductionRate=rate[0];
           BigDecimal studentLoanDeductionAmount= grossIncomePerYear.subtract(studentLoan_Plan1);
           BigDecimal amount= studentLoanDeductionAmount.multiply(deductionRate);
            return calculateIncomeTaxBasedOnPayPeriod(amount,payPeriod);

        }
        else if (grossIncomePerYear.compareTo(studentLoan_Plan2)>0 && grossIncomePerYear.compareTo(studentLoan_Plan4)<=0) {
            System.out.println("studentLoan_Plan2 "+studentLoan_Plan2);
            BigDecimal[] rate =taxThresholdService.getStudentLoanBandByPlanTypeRate(taxYear,TaxThreshold.TaxRegion.ALL_REGIONS, TaxThreshold.BandNameType.STUDENT_LOAN, TaxThreshold.BandName.STUDENT_LOAN_PLAN_2);
            BigDecimal deductionRate=rate[0];
            BigDecimal studentLoanDeductionAmount= grossIncomePerYear.subtract(studentLoan_Plan2);
            BigDecimal amount= studentLoanDeductionAmount.multiply(deductionRate);
            return calculateIncomeTaxBasedOnPayPeriod(amount,payPeriod);
        }
        else if (grossIncomePerYear.compareTo(studentLoan_Plan4)>0) {
            System.out.println("studentLoan_Plan4 "+studentLoan_Plan4);
            BigDecimal[] rate =taxThresholdService.getStudentLoanBandByPlanTypeRate(taxYear,TaxThreshold.TaxRegion.ALL_REGIONS, TaxThreshold.BandNameType.STUDENT_LOAN, TaxThreshold.BandName.STUDENT_LOAN_PLAN_4);
            BigDecimal deductionRate=rate[0];
            BigDecimal studentLoanDeductionAmount= grossIncomePerYear.subtract(studentLoan_Plan4);
            BigDecimal amount= studentLoanDeductionAmount.multiply(deductionRate);
            return calculateIncomeTaxBasedOnPayPeriod(amount,payPeriod);

        }
   return BigDecimal.ZERO;

    }
    public BigDecimal calculateStudentLoan(BigDecimal income, Boolean hasStudentLoan,StudentLoan.StudentLoanPlan studentLoanPlan,  String taxYear, String payPeriod){
        if(!hasStudentLoan){
            return BigDecimal.ZERO;
        }
        // Validate inputs
        if (income == null || taxYear == null || payPeriod == null) {
            throw new IllegalArgumentException("Income, tax year, and pay period cannot be null");
        }
        logger.info("income: {}, hasStudentLoan: {}, studentLoanPlan: {}, taxYear: {}, payPeriod: {}", income, hasStudentLoan, studentLoanPlan, taxYear, payPeriod);
        String studentLoanType=String.valueOf(studentLoanPlan);
        BigDecimal[][] studentThresholds=taxThresholdService.getStudentLoanThresholdsByPlanType(taxYear, TaxThreshold.TaxRegion.ALL_REGIONS, TaxThreshold.BandNameType.STUDENT_LOAN, TaxThreshold.BandName.valueOf(studentLoanType));
        BigDecimal studentLoan_PlanThreshold=studentThresholds[0][0];
        BigDecimal[] rate =taxThresholdService.getStudentLoanBandByPlanTypeRate(taxYear,TaxThreshold.TaxRegion.ALL_REGIONS, TaxThreshold.BandNameType.STUDENT_LOAN, TaxThreshold.BandName.STUDENT_LOAN_PLAN_2);
        BigDecimal deductionRate=rate[0];
        BigDecimal grossIncomePerYear=calculateGrossSalary(income,payPeriod);
        logger.info("GrossIncomePerYear:{}",grossIncomePerYear);
        if (grossIncomePerYear.compareTo(studentLoan_PlanThreshold)>0){
            BigDecimal studentLoanDeductionAmount= grossIncomePerYear.subtract(studentLoan_PlanThreshold);
            BigDecimal amount= studentLoanDeductionAmount.multiply(deductionRate);
            logger.info("Student Loan Deduction Amount: {}", amount);
            return calculateIncomeTaxBasedOnPayPeriod(amount,payPeriod);
        }
          return BigDecimal.ZERO;
    }
    public BigDecimal calculatePostGraduateLoan(BigDecimal income, Boolean hasPostGraduateLoan, PostGraduateLoan.PostgraduateLoanPlanType postgraduateLoanPlanType,String taxYear, String payPeriod){
        if(!hasPostGraduateLoan){
            return BigDecimal.ZERO;
        }
        if (income == null || taxYear == null || payPeriod == null) {
            throw new IllegalArgumentException("Income, tax year, and pay period cannot be null");
        }
        logger.info("income: {}, hasPostGraduateLoan: {}, postgraduateLoanPlanType: {}, taxYear: {}, payPeriod: {}", income, hasPostGraduateLoan, postgraduateLoanPlanType, taxYear, payPeriod);
        String postGraduateLoanType=String.valueOf(postgraduateLoanPlanType);
        List<TaxThreshold> postGraduateData = taxThresholdService.getPostGraduateLoan(taxYear, TaxThreshold.TaxRegion.ALL_REGIONS, TaxThreshold.BandNameType.POSTGRADUATE_LOAN, TaxThreshold.BandName.valueOf(postGraduateLoanType));

        BigDecimal postGraduateLoanPlan=postGraduateData.getFirst().getLowerBound();


        BigDecimal postGraduateLoanDeductionRate=postGraduateData.getFirst().getRate();
        BigDecimal grossIncomePerYear=calculateGrossSalary(income,payPeriod);
        logger.info("GrossIncomePerYear: {}",grossIncomePerYear);
        if (grossIncomePerYear.compareTo(postGraduateLoanPlan)>0){
            BigDecimal postGraduateDeductedAmount = grossIncomePerYear.subtract(postGraduateLoanPlan);
            BigDecimal amount = postGraduateDeductedAmount.multiply(postGraduateLoanDeductionRate);

            logger.info("Post Graduate Loan Deduction Amount: {}", amount);
            return calculateIncomeTaxBasedOnPayPeriod(amount,payPeriod);
        }

        return BigDecimal.ZERO;

    }
    private BigDecimal calculateGrossSalary(BigDecimal grossIncome,String payPeriod){
        return switch (payPeriod.toUpperCase()) {
            case "WEEKLY" -> grossIncome.multiply(BigDecimal.valueOf(52));
            case "MONTHLY" -> grossIncome.multiply(BigDecimal.valueOf(12));
            case "YEARLY" -> grossIncome;
            default -> throw new IllegalArgumentException("Invalid pay period. Must be WEEKLY, MONTHLY or YEARLY");
        };


    }

    private BigDecimal  calculateIncomeTaxBasedOnPayPeriod(BigDecimal incomeTax,String payPeriod){
        return switch (payPeriod.toUpperCase()) {
            case "WEEKLY" -> incomeTax.divide(BigDecimal.valueOf(52), 4, RoundingMode.HALF_UP);
            case "MONTHLY" -> incomeTax.divide(BigDecimal.valueOf(12), 4, RoundingMode.HALF_UP);
            case "YEARLY" -> incomeTax;
            default -> throw new IllegalArgumentException("Invalid pay period. Must be WEEKLY, MONTHLY or YEARLY");
        };
    }
}
