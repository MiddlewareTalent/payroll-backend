package com.payroll.uk.payroll_processing.service.payslip;

import com.payroll.uk.payroll_processing.dto.mapper.PaySlipCreateDTOMapper;
import com.payroll.uk.payroll_processing.entity.PaySlip;
import com.payroll.uk.payroll_processing.entity.employee.EmployeeDetails;
import com.payroll.uk.payroll_processing.entity.employee.OtherEmployeeDetails;
import com.payroll.uk.payroll_processing.entity.employer.EmployerDetails;
import com.payroll.uk.payroll_processing.entity.employer.OtherEmployerDetails;
import com.payroll.uk.payroll_processing.exception.EmployeeNotFoundException;
import com.payroll.uk.payroll_processing.repository.EmployeeDetailsRepository;
import com.payroll.uk.payroll_processing.repository.EmployerDetailsRepository;
import com.payroll.uk.payroll_processing.repository.PaySlipRepository;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Service
public class UpdatingDetails {
    private static final Logger logger = LoggerFactory.getLogger(UpdatingDetails.class);
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
        OtherEmployeeDetails updateOtherEmployeeDetails = new OtherEmployeeDetails();
        updateOtherEmployeeDetails=otherEmployeeDetails;
        logger.error("otherEmployeeDetails: {}", otherEmployeeDetails);

        try {

            BigDecimal totalUsedPersonalAllowance = otherEmployeeDetails.getTotalUsedPersonalAllowance();
            BigDecimal remainingPersonalAllowance = otherEmployeeDetails.getRemainingPersonalAllowance();
            if (!employeeDetails.isHasEmergencyCode()) {
//            BigDecimal remainingPersonalAllowanceInYear = employeeDetailsRepository.findByRemainingPersonalAllowanceInYear(employeeDetails.getEmployeeId());

                if (remainingPersonalAllowance.compareTo(BigDecimal.ZERO) <= 0) {
                    remainingPersonalAllowance = BigDecimal.ZERO; // Ensure it doesn't go negative
                } else {
//                    remainingPersonalAllowance = remainingPersonalAllowance.subtract(paySlip.getPersonalAllowance());
                    BigDecimal updatedRemaining = remainingPersonalAllowance.subtract(paySlip.getPersonalAllowance());
                    remainingPersonalAllowance = updatedRemaining.compareTo(BigDecimal.ZERO) < 0 ? BigDecimal.ZERO : updatedRemaining;

                }

                updateOtherEmployeeDetails.setUsedPersonalAllowance(paySlip.getPersonalAllowance());
                updateOtherEmployeeDetails.setTotalUsedPersonalAllowance(totalUsedPersonalAllowance.add(paySlip.getPersonalAllowance()));
                updateOtherEmployeeDetails.setRemainingPersonalAllowance(remainingPersonalAllowance);
                logger.error("otherEmployeeDetails.getRemainingPersonalAllowance() :{}", otherEmployeeDetails.getRemainingPersonalAllowance());
            } else  {

                if (remainingPersonalAllowance.compareTo(BigDecimal.ZERO) <= 0) {
                    remainingPersonalAllowance = BigDecimal.ZERO; // Ensure it doesn't go negative
                } else {

                    BigDecimal updatedRemaining = remainingPersonalAllowance.subtract(paySlip.getPersonalAllowance());
                    remainingPersonalAllowance = updatedRemaining.compareTo(BigDecimal.ZERO) < 0 ? BigDecimal.ZERO : updatedRemaining;

                }
                BigDecimal countPaid = otherEmployeeDetails.getNumberOfPayPeriodsEmergencyTaxCodeUsed();
                updateOtherEmployeeDetails.setNumberOfPayPeriodsEmergencyTaxCodeUsed(countPaid.add(BigDecimal.ONE));
                BigDecimal totalPaid = otherEmployeeDetails.getTotalAllowanceUsedDuringEmergencyCode();
                updateOtherEmployeeDetails.setTotalAllowanceUsedDuringEmergencyCode(totalPaid.add(paySlip.getPersonalAllowance()));
                updateOtherEmployeeDetails.setTotalUsedPersonalAllowance(totalUsedPersonalAllowance.add(paySlip.getPersonalAllowance()));
//            BigDecimal remainingPersonalAllowancesAmount = employeeDetailsRepository.findByRemainingPersonalAllowance(employeeDetails.getEmployeeId());
                updateOtherEmployeeDetails.setRemainingPersonalAllowance(remainingPersonalAllowance.subtract(paySlip.getPersonalAllowance()));
                logger.error("otherEmployeeDetails.getRemainingPersonalAllowance() :{}", otherEmployeeDetails.getRemainingPersonalAllowance());

            }
        }
        catch (Exception e) {
            logger.error("Error updating personal allowance in OtherEmployeeDetails: ", e);
            throw new RuntimeException("Failed to update personal allowance in OtherEmployeeDetails", e);
        }
        try {
            // Update OtherEmployeeDetails with total earnings amount YTD
            BigDecimal totalAmountEarned = otherEmployeeDetails.getTotalEarningsAmountYTD();
            if (totalAmountEarned == null) {
                totalAmountEarned = BigDecimal.ZERO;
            }
            updateOtherEmployeeDetails.setTotalEarningsAmountYTD(totalAmountEarned.add(paySlip.getGrossPayTotal()));
        }
        catch (Exception e) {
            logger.error("Error updating total earnings amount in OtherEmployeeDetails: ", e);
            throw new RuntimeException("Failed to update total earnings amount in OtherEmployeeDetails", e);
        }
        try {
            // Update OtherEmployeeDetails with income tax information
            updateOtherEmployeeDetails.setIncomeTaxPaid(paySlip.getIncomeTaxTotal());
            BigDecimal totalIncomeTaxPaidInCompany = otherEmployeeDetails.getTotalIncomeTaxPaidInCompany();

            updateOtherEmployeeDetails.setTotalIncomeTaxPaidInCompany(totalIncomeTaxPaidInCompany.add(paySlip.getIncomeTaxTotal()));
            if ("Yearly".equalsIgnoreCase(String.valueOf(employeeDetails.getPayPeriod()))) {
                BigDecimal yearCount = otherEmployeeDetails.getNumberOfYearsOfIncomeTaxPaid();
                updateOtherEmployeeDetails.setNumberOfYearsOfIncomeTaxPaid(yearCount.add(BigDecimal.ONE));
            } else if ("Monthly".equalsIgnoreCase(String.valueOf(employeeDetails.getPayPeriod()))) {
                BigDecimal monthCount = otherEmployeeDetails.getNumberOfMonthsOfIncomeTaxPaid();
                updateOtherEmployeeDetails.setNumberOfMonthsOfIncomeTaxPaid(monthCount.add(BigDecimal.ONE));
            } else if ("Weekly".equalsIgnoreCase(String.valueOf(employeeDetails.getPayPeriod()))) {
                BigDecimal weekCount = otherEmployeeDetails.getNumberOfWeeksOfIncomeTaxPaid();
                updateOtherEmployeeDetails.setNumberOfWeeksOfIncomeTaxPaid(weekCount.add(BigDecimal.ONE));
            }
        }
        catch (Exception e) {
            logger.error("Error updating income tax in OtherEmployeeDetails: ", e);
            throw new RuntimeException("Failed to update income tax in OtherEmployeeDetails", e);
        }
        try {
            // Update OtherEmployeeDetails with National Insurance contributions
            updateOtherEmployeeDetails.setEmployeeNIContribution(paySlip.getEmployeeNationalInsurance());
            BigDecimal totalEmployeeNIContributionInCompany = otherEmployeeDetails.getTotalEmployeeNIContributionInCompany();
            updateOtherEmployeeDetails.setTotalEmployeeNIContributionInCompany(
                    totalEmployeeNIContributionInCompany.add(paySlip.getEmployeeNationalInsurance())
            );


            if ("YEARLY".equalsIgnoreCase(String.valueOf(employeeDetails.getPayPeriod()))) {
                BigDecimal yearCount = otherEmployeeDetails.getNumberOfYearsOfNIContributions();
                updateOtherEmployeeDetails.setNumberOfYearsOfNIContributions(yearCount.add(BigDecimal.ONE));
            } else if ("MONTHLY".equalsIgnoreCase(String.valueOf(employeeDetails.getPayPeriod()))) {
                BigDecimal monthCount = otherEmployeeDetails.getNumberOfMonthsOfNIContributions();
                updateOtherEmployeeDetails.setNumberOfMonthsOfNIContributions(monthCount.add(BigDecimal.ONE));
            } else if ("WEEKLY".equalsIgnoreCase(String.valueOf(employeeDetails.getPayPeriod()))) {
                BigDecimal weekCount = otherEmployeeDetails.getNumberOfWeeksOfNIContributions();
                updateOtherEmployeeDetails.setNumberOfWeeksOfNIContributions(weekCount.add(BigDecimal.ONE));
            }
        }
        catch (Exception e) {
            logger.error("Error updating National Insurance contributions in OtherEmployeeDetails: ", e);
            throw new RuntimeException("Failed to update National Insurance contributions in OtherEmployeeDetails", e);
        }
        try{
            if (paySlip.isHasPensionEligible()) {
                updateOtherEmployeeDetails.setPensionContributeAmount(paySlip.getEmployeePensionContribution());
                BigDecimal totalPensionContribution = otherEmployeeDetails.getTotalAmountPensionContribution();
                totalPensionContribution = totalPensionContribution != null ? totalPensionContribution : BigDecimal.ZERO;
                updateOtherEmployeeDetails.setTotalAmountPensionContribution(totalPensionContribution.add(paySlip.getEmployeePensionContribution()));

                BigDecimal numberOfPayPeriodsPensionContribution = otherEmployeeDetails.getNumberOfPayPeriodsPensionContribution();
                numberOfPayPeriodsPensionContribution = numberOfPayPeriodsPensionContribution != null ? numberOfPayPeriodsPensionContribution : BigDecimal.ZERO;
                updateOtherEmployeeDetails.setNumberOfPayPeriodsPensionContribution(numberOfPayPeriodsPensionContribution.add(BigDecimal.ONE));
            }
        }
        catch (Exception e){
            logger.error("Error updating pension contribution in OtherEmployeeDetails: ", e);
            throw new RuntimeException("Failed to update pension contribution in OtherEmployeeDetails", e);
        }



        employeeDetails.setOtherEmployeeDetails(updateOtherEmployeeDetails);

        EmployeeDetails employeeData = employeeDetailsRepository.save(employeeDetails);
        System.out.println("Successfully Updated other Details in EmployeeDetails: \n  " + employeeData);

    }

    public void updatingOtherEmployerDetails(PaySlip paySlip){
        logger.info("updating the other employer details in payslip:");
        if (paySlip==null){
            throw new IllegalArgumentException("Pay Slip data cannot empty");
        }
        EmployeeDetails employeeDetails = employeeDetailsRepository.findByEmployeeId(paySlip.getEmployeeId())
                .orElseThrow(() -> new EmployeeNotFoundException("Employee not found with ID: " + paySlip.getEmployeeId()));

        EmployerDetails employerDetails = employerDetailsRepository.findAll().getFirst();
                if (employerDetails == null) {
                    throw new RuntimeException("Employer not found ");
                }

        OtherEmployerDetails otherEmployerDetails=employerDetails.getOtherEmployerDetails();
        OtherEmployerDetails updateOtherEmployerDetails = new OtherEmployerDetails();
        updateOtherEmployerDetails=otherEmployerDetails;
        logger.info("otherEmployerDetails: {}",otherEmployerDetails);
        try {
            logger.info("updating the total paid amount in OtherEmployerDetails");
            BigDecimal currentPaidAmount = otherEmployerDetails.getCurrentPayPeriodPaidGrossPay();
            currentPaidAmount = currentPaidAmount != null ? currentPaidAmount : BigDecimal.ZERO;
            updateOtherEmployerDetails.setCurrentPayPeriodPaidGrossPay(currentPaidAmount.add(paySlip.getGrossPayTotal()));

            BigDecimal totalPaidAMount = otherEmployerDetails.getTotalPaidGrossAmountYTD();
           totalPaidAMount=totalPaidAMount!=null?totalPaidAMount:BigDecimal.ZERO;
            updateOtherEmployerDetails.setTotalPaidGrossAmountYTD(totalPaidAMount.add(paySlip.getGrossPayTotal()));


        }
        catch (Exception e) {
            logger.error("Error updating total paid amount in OtherEmployerDetails: ", e);
            throw new RuntimeException("Failed to update total paid amount in OtherEmployerDetails", e);
        }

        try {
            logger.info("updating the total PAYE YTD in OtherEmployerDetails");

            BigDecimal currentPaidPAYEAmount= otherEmployerDetails.getCurrentPayPeriodPAYE();
            currentPaidPAYEAmount=currentPaidPAYEAmount!=null?currentPaidPAYEAmount:BigDecimal.ZERO;
            updateOtherEmployerDetails.setCurrentPayPeriodPAYE(currentPaidPAYEAmount.add(paySlip.getIncomeTaxTotal()));

            BigDecimal totalPAYE = otherEmployerDetails.getTotalPAYEYTD();
            updateOtherEmployerDetails.setTotalPAYEYTD(totalPAYE.add(paySlip.getIncomeTaxTotal()));
            logger.info("successfully updated the up to total PAYE ");
        }
        catch (Exception e) {
            logger.error("Error fetching totalPAYE", e);
        }
        try {
            BigDecimal currentPaidEmployeesNIAmount = otherEmployerDetails.getCurrentPayPeriodEmployeesNI();
            currentPaidEmployeesNIAmount = currentPaidEmployeesNIAmount != null ? currentPaidEmployeesNIAmount : BigDecimal.ZERO;
            updateOtherEmployerDetails.setCurrentPayPeriodEmployeesNI(currentPaidEmployeesNIAmount.add(paySlip.getEmployeeNationalInsurance()));

            BigDecimal totalEmployeeNIAmount = otherEmployerDetails.getTotalEmployeesNIYTD();
            updateOtherEmployerDetails.setTotalEmployeesNIYTD(totalEmployeeNIAmount.add(paySlip.getEmployeeNationalInsurance()));
            logger.info("successfully updated the total employee NI YTD ");
        }
        catch (Exception e) {
            logger.error("Error updating total employee NI YTD in OtherEmployerDetails: ", e);
            throw new RuntimeException("Failed to update total employee NI YTD in OtherEmployerDetails", e);
        }
        try {
            BigDecimal currentPaidEmployerNIAmount = otherEmployerDetails.getCurrentPayPeriodEmployersNI();
            currentPaidEmployerNIAmount = currentPaidEmployerNIAmount != null ? currentPaidEmployerNIAmount : BigDecimal.ZERO;
            updateOtherEmployerDetails.setCurrentPayPeriodEmployersNI(currentPaidEmployerNIAmount.add(paySlip.getEmployersNationalInsurance()));

            BigDecimal totalEmployersNI = otherEmployerDetails.getTotalEmployersNIYTD();
            updateOtherEmployerDetails.setTotalEmployersNIYTD(totalEmployersNI.add(paySlip.getEmployersNationalInsurance()));
        }
        catch (Exception e) {
            logger.error("Error updating total employers NI YTD in OtherEmployerDetails: ", e);
            throw new RuntimeException("Failed to update total employers NI YTD in OtherEmployerDetails", e);
        }

        try {
            BigDecimal currentPayPeriodEmployeesPension = otherEmployerDetails.getCurrentPayPeriodEmployeePensionContribution();
            currentPayPeriodEmployeesPension = currentPayPeriodEmployeesPension != null ? currentPayPeriodEmployeesPension : BigDecimal.ZERO;
            updateOtherEmployerDetails.setCurrentPayPeriodEmployeePensionContribution(currentPayPeriodEmployeesPension.add(paySlip.getEmployeePensionContribution()));

            BigDecimal totalEmployeesPensionContribution = otherEmployerDetails.getTotalEmployeePensionContribution();
            totalEmployeesPensionContribution = totalEmployeesPensionContribution != null ? totalEmployeesPensionContribution : BigDecimal.ZERO;
            updateOtherEmployerDetails.setTotalEmployeePensionContribution(totalEmployeesPensionContribution.add(paySlip.getEmployeePensionContribution()));

            BigDecimal currentPayPeriodEmployerPension = otherEmployerDetails.getCurrentPayPeriodEmployerPensionContribution();
            currentPayPeriodEmployerPension = currentPayPeriodEmployerPension != null ? currentPayPeriodEmployerPension : BigDecimal.ZERO;
            updateOtherEmployerDetails.setCurrentPayPeriodEmployerPensionContribution(currentPayPeriodEmployerPension.add(paySlip.getEmployerPensionContribution()));

            BigDecimal totalEmployerPensionContribution = otherEmployerDetails.getTotalEmployerPensionContribution();
            totalEmployerPensionContribution = totalEmployerPensionContribution != null ? totalEmployerPensionContribution : BigDecimal.ZERO;
            updateOtherEmployerDetails.setTotalEmployerPensionContribution(totalEmployerPensionContribution.add(paySlip.getEmployerPensionContribution()));
        }
        catch (Exception e) {
            logger.error("Error updating pension contributions in OtherEmployerDetails: ", e);
            throw new RuntimeException("Failed to update pension contributions in OtherEmployerDetails", e);
        }
        logger.info("successfully update the total employers NI YTD ");
        employerDetails.setOtherEmployerDetails(updateOtherEmployerDetails);
        EmployerDetails employerData =employerDetailsRepository.save(employerDetails);

        logger.info("Successfully Updated Other Employer Details in EmployerDetails: \n " + employerData);
    }

    public boolean isKTaxCode(String normalizedTaxCode){
        if (normalizedTaxCode == null || normalizedTaxCode.isEmpty()) {
            return false;
        }
        normalizedTaxCode = normalizedTaxCode.trim().toUpperCase();
     // Check if the tax code starts with 'K', 'SK', or 'CK'
        return normalizedTaxCode.matches("^K\\d+$") || normalizedTaxCode.matches("^SK\\d+$") || normalizedTaxCode.matches("^CK\\d+$"); // Valid K code format
    }

    public BigDecimal applyKCodeAdjustment(EmployeeDetails employeeDetails, String payPeriod) {

        BigDecimal totalKCodeAmount = employeeDetails.getKCodeTaxableAdjustmentAnnual();

        if (totalKCodeAmount == null || totalKCodeAmount.compareTo(BigDecimal.ZERO) <= 0) {
            totalKCodeAmount = BigDecimal.ZERO;
        }
        BigDecimal periodKCodeAmount = calculateIncomeTaxBasedOnPayPeriod(
                totalKCodeAmount, payPeriod
        );

        BigDecimal remainingKAmount = employeeDetails.getOtherEmployeeDetails().getRemainingKCodeAmount();
        if (remainingKAmount == null || remainingKAmount.compareTo(BigDecimal.ZERO) <= 0) {
            remainingKAmount = BigDecimal.ZERO;
        }
        else {

            BigDecimal newRemainingKAmount=remainingKAmount.subtract(periodKCodeAmount);
            remainingKAmount = newRemainingKAmount.compareTo(BigDecimal.ZERO) < 0 ? BigDecimal.ZERO : newRemainingKAmount;

        }
        employeeDetails.getOtherEmployeeDetails().setRemainingKCodeAmount(remainingKAmount);
        if (remainingKAmount.compareTo(BigDecimal.ZERO)<=0){
            return BigDecimal.ZERO;
        }
        if(periodKCodeAmount.compareTo(remainingKAmount)>0){
            return remainingKAmount;
        }


        return periodKCodeAmount;
    }

    public BigDecimal  calculateIncomeTaxBasedOnPayPeriod(BigDecimal incomeTax,String payPeriod){
        return switch (payPeriod.toUpperCase()) {
            case "WEEKLY" -> incomeTax.divide(BigDecimal.valueOf(52), 2, RoundingMode.HALF_UP);
            case "MONTHLY" -> incomeTax.divide(BigDecimal.valueOf(12), 2, RoundingMode.HALF_UP);
            case "QUARTERLY" -> incomeTax.divide(BigDecimal.valueOf(4), 2, RoundingMode.HALF_UP);
            case "YEARLY" -> incomeTax;
            default -> throw new IllegalArgumentException("Invalid pay period. Must be WEEKLY, MONTHLY or YEARLY");
        };
    }


}
