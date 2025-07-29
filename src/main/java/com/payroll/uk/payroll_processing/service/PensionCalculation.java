package com.payroll.uk.payroll_processing.service;

import com.payroll.uk.payroll_processing.dto.PensionCalculationPaySlipDTO;
import com.payroll.uk.payroll_processing.dto.mapper.PensionCalculationPaySlipDTOMapper;
import com.payroll.uk.payroll_processing.entity.PaySlip;
import com.payroll.uk.payroll_processing.entity.PensionCalculationPaySlip;
import com.payroll.uk.payroll_processing.entity.TaxThreshold;
import com.payroll.uk.payroll_processing.entity.employee.EmployeeDetails;
import com.payroll.uk.payroll_processing.exception.DataValidationException;
import com.payroll.uk.payroll_processing.exception.InvalidComputationException;
import com.payroll.uk.payroll_processing.repository.EmployeeDetailsRepository;
import com.payroll.uk.payroll_processing.repository.PensionDataRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

@Service
@AllArgsConstructor
public class PensionCalculation {
    private static final Logger logger = LoggerFactory.getLogger(PensionCalculation.class);

    private final TaxThresholdService taxThresholdService;
    private final EmployeeDetailsRepository employeeDetailsRepository;
    @Autowired
    private PensionCalculationPaySlipDTOMapper pensionCalculationPaySlipDTOMapper;
    @Autowired
    private PensionDataRepository pensionDataRepository;

    /**
     * Calculates the pension contribution based on income, eligibility, tax year, and pay period.
     *
     * @param income     Employee's pay for the given period
     * @param isEligible Whether the employee is eligible for pension contributions
     * @param taxYear    Tax year in format like "2025-26"
     * @param payPeriod  Pay frequency: "weekly", "monthly", "annually", etc.
     * @return Pension contribution amount for the given pay period
     */
    public BigDecimal calculateEmployeePensionContribution(BigDecimal income, boolean isEligible, String taxYear, String payPeriod) {

        // If the employee is not eligible, no pension contribution is required
        if (!isEligible) return BigDecimal.ZERO;

        // Validate input arguments
        if (income == null || taxYear == null || payPeriod == null) {
            throw new DataValidationException("Income, tax year, and pay period cannot be null");
        }

        try {
            // Fetch pension threshold bands (lower and upper limits) from tax configuration service
            BigDecimal[][] pensionSlabs = taxThresholdService.getPensionThresholds(
                    taxYear,
                    TaxThreshold.TaxRegion.ALL_REGIONS,
                    TaxThreshold.BandNameType.PENSION_CONTRIBUTION,
                    TaxThreshold.BandName.EMPLOYEE_PENSION_CONTRIBUTION
            );

            // Fetch pension deduction rate (e.g., 5%) for employees
            BigDecimal[] pensionRate = taxThresholdService.getPensionContributionRate(
                    taxYear,
                    TaxThreshold.TaxRegion.ALL_REGIONS,
                    TaxThreshold.BandNameType.PENSION_CONTRIBUTION,
                    TaxThreshold.BandName.EMPLOYEE_PENSION_CONTRIBUTION
            );

            // Extract the pensionable income limits and rate
            BigDecimal lowerLimit = pensionSlabs[0][0];
            BigDecimal upperLimit = pensionSlabs[0][1];
            BigDecimal deductionRate = pensionRate[0];

            // Convert pay-period-based income (weekly/monthly) to annual income
            BigDecimal grossIncomePerYear = calculateGrossSalary(income, payPeriod);

            // If annual income is below or equal to the lower threshold, no contribution
            if (grossIncomePerYear.compareTo(lowerLimit) <= 0) {
                return BigDecimal.ZERO;
            }
            // Calculate pensionable income
            BigDecimal pensionableIncome = calculatePensionableIncome(grossIncomePerYear, upperLimit, lowerLimit);

            // Calculate contribution: pensionable income × deduction rate
            BigDecimal contribution = pensionableIncome.multiply(deductionRate);

            // Convert the annual contribution to the appropriate amount based on pay period
            return calculateIncomeTaxBasedOnPayPeriod(contribution, payPeriod);
        }
        catch (Exception e) {
            logger.error("Error calculating employee pension contribution: {}", e.getMessage());
            throw new InvalidComputationException("Failed to calculate employee pension contribution "+ e.getMessage(),e);
        }
    }


    public BigDecimal calculateEmployerPensionContribution(BigDecimal income, boolean isEligible, String taxYear, String payPeriod) {

        // If the employee is not eligible, employer also contributes nothing
        if (!isEligible) return BigDecimal.ZERO;

        // Validate input arguments
        if (income == null || taxYear == null || payPeriod == null) {
            throw new DataValidationException("Income, tax year, and pay period cannot be null");
        }

        try {
            // Fetch pension threshold bands (lower and upper limits) for employer
            BigDecimal[][] pensionSlabs = taxThresholdService.getPensionThresholds(
                    taxYear,
                    TaxThreshold.TaxRegion.ALL_REGIONS,
                    TaxThreshold.BandNameType.PENSION_CONTRIBUTION,
                    TaxThreshold.BandName.EMPLOYER_PENSION_CONTRIBUTION
            );

            BigDecimal[] pensionRate = taxThresholdService.getPensionContributionRate(
                    taxYear,
                    TaxThreshold.TaxRegion.ALL_REGIONS,
                    TaxThreshold.BandNameType.PENSION_CONTRIBUTION,
                    TaxThreshold.BandName.EMPLOYER_PENSION_CONTRIBUTION
            );

            // Extract lower and upper thresholds and the rate
            BigDecimal lowerLimit = pensionSlabs[0][0];
            BigDecimal upperLimit = pensionSlabs[0][1];
            BigDecimal deductionRate = pensionRate[0];

            // Convert income to annual income for threshold comparison
            BigDecimal grossIncomePerYear = calculateGrossSalary(income, payPeriod);

            // If income is less than or equal to lower threshold, no contribution
            if (grossIncomePerYear.compareTo(lowerLimit) <= 0) {
                return BigDecimal.ZERO;
            }

            // Calculate pensionable income
            BigDecimal pensionableIncome = calculatePensionableIncome(grossIncomePerYear, upperLimit, lowerLimit);

            // Calculate contribution: pensionableIncome × employer rate
            BigDecimal contribution = pensionableIncome.multiply(deductionRate);

            // Convert back to pay-period amount (e.g., monthly or weekly)
            return calculateIncomeTaxBasedOnPayPeriod(contribution, payPeriod);
        }
        catch (Exception e) {
            logger.error("Error calculating employer pension contribution: {}", e.getMessage());
            throw new InvalidComputationException("Failed to calculate employer pension contribution "+ e.getMessage(),e);
        }
    }

    private static BigDecimal calculatePensionableIncome(BigDecimal grossIncomePerYear, BigDecimal upperLimit, BigDecimal lowerLimit) {
        BigDecimal pensionableIncome;

        if (grossIncomePerYear.compareTo(upperLimit) <= 0) {
            pensionableIncome = grossIncomePerYear.subtract(lowerLimit);
        } else {

            pensionableIncome = upperLimit.subtract(lowerLimit);
        }
        return pensionableIncome;
    }


    private BigDecimal  calculateIncomeTaxBasedOnPayPeriod(BigDecimal incomeTax,String payPeriod){
        return switch (payPeriod.toUpperCase()) {
            case "WEEKLY" -> incomeTax.divide(BigDecimal.valueOf(52), 2, RoundingMode.HALF_UP);
            case "MONTHLY" -> incomeTax.divide(BigDecimal.valueOf(12), 2, RoundingMode.HALF_UP);
            case "QUARTERLY" -> incomeTax.divide(BigDecimal.valueOf(4), 2, RoundingMode.HALF_UP);
            case "YEARLY" -> incomeTax;
            default -> throw new DataValidationException("Invalid pay period. Must be "+payPeriod);
        };
    }


    private BigDecimal calculateGrossSalary(BigDecimal grossIncome,String payPeriod){
        BigDecimal annualGross;
        switch (payPeriod.toUpperCase()) {
            case "WEEKLY" -> annualGross=grossIncome.multiply(BigDecimal.valueOf(52));
            case "MONTHLY" -> annualGross=grossIncome.multiply(BigDecimal.valueOf(12));
            case "QUARTERLY" -> annualGross=grossIncome.multiply(BigDecimal.valueOf(4));
            case "YEARLY" -> annualGross=grossIncome;
            default -> throw new DataValidationException("Invalid pay period. Must be WEEKLY, MONTHLY or YEARLY");
        };
        return annualGross.setScale(2, RoundingMode.HALF_UP);


    }


    public PensionCalculationPaySlipDTO calculatePensionContributionPaySlip(PaySlip paySlip) {
        if (paySlip==null){
            throw new DataValidationException("PaySlip cannot be null ");
        }
        // Fetch employee details from the repository
        Optional<EmployeeDetails> employeeData = employeeDetailsRepository.findByEmployeeId(paySlip.getEmployeeId());
        logger.info("Calculating loan pay slip for employee: {}", paySlip.getEmployeeId());
        EmployeeDetails employeeDetails = employeeData.orElseThrow(() -> new RuntimeException("Employee not found with ID: " + paySlip.getEmployeeId()));

        PensionCalculationPaySlip pensionCalculationPaySlip = getPensionCalculationPaySlip(paySlip, employeeDetails);
        PensionCalculationPaySlip savedPensionData = pensionDataRepository.save(pensionCalculationPaySlip);
        return pensionCalculationPaySlipDTOMapper.mapToPensionCalculationPaySlipDTO(savedPensionData);
    }

    private static PensionCalculationPaySlip getPensionCalculationPaySlip(PaySlip paySlip, EmployeeDetails employeeDetails) {
        PensionCalculationPaySlip pensionCalculationPaySlip = new PensionCalculationPaySlip();
        pensionCalculationPaySlip.setPaySlipReference(paySlip.getPaySlipReference());
        pensionCalculationPaySlip.setEmployeeId(paySlip.getEmployeeId());
        pensionCalculationPaySlip.setHasPensionEligible(employeeDetails.isHasPensionEligible());
        pensionCalculationPaySlip.setNationalInsuranceNumber(employeeDetails.getNationalInsuranceNumber());
        pensionCalculationPaySlip.setIncome(paySlip.getGrossPayTotal());
        pensionCalculationPaySlip.setPensionContributionDeductionAmount(paySlip.getEmployeePensionContribution());
        pensionCalculationPaySlip.setEmployerPensionContributionAmount(paySlip.getEmployerPensionContribution());
        return pensionCalculationPaySlip;
    }



}
