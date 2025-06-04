//package unused;
//
//import com.payroll.uk.payroll_processing.entity.employee.OtherEmployeeDetails;
//
//import java.math.BigDecimal;
//
//public class OtherEmpDetails {
//    // Save OtherEmployeeDetails
//    OtherEmployeeDetails otherEmployeeDetails=new OtherEmployeeDetails();
//        System.out.println("OtherEmployeeDetails: ");
//    // Update OtherEmployeeDetails with personal allowance information
//    BigDecimal totalUsedPersonalAllowance = otherEmployeeDetailsRepository.findByTotalUsedPersonalAllowance();
//    BigDecimal remainingPersonalAllowance = otherEmployeeDetailsRepository.findByRemainingPersonalAllowance(paySlipCreateDto.getEmployeeId());
//        otherEmployeeDetails.setUsedPersonalAllowance(personalAllowance);
//        otherEmployeeDetails.setTotalUsedPersonalAllowance(totalUsedPersonalAllowance.add(personalAllowance));
//        otherEmployeeDetails.setRemainingPersonalAllowance(remainingPersonalAllowance.subtract(personalAllowance));
//
//        System.out.println("Used Personal Allowance: " + otherEmployeeDetails.getUsedPersonalAllowance());
//        System.out.println("totalUsedPersonalAllowance: "+otherEmployeeDetails.getTotalUsedPersonalAllowance());
//        System.out.println("Remaining Personal Allowance: " + otherEmployeeDetails.getRemainingPersonalAllowance());
//    // Update OtherEmployeeDetails with income tax information
//        otherEmployeeDetails.setIncomeTaxPaid(incomeTax);
//    BigDecimal totalIncomeTaxPaidInCompany = otherEmployeeDetailsRepository.findByTotalIncomeTaxPaidInCompany(paySlipCreateDto.getEmployeeId());
//
//        otherEmployeeDetails.setTotalIncomeTaxPaidInCompany(totalIncomeTaxPaidInCompany.add(incomeTax));
//        if("Yearly".equalsIgnoreCase(paySlipCreateDto.getPayPeriod())){
//        BigDecimal yearCount=otherEmployeeDetailsRepository.findByNumberOfYearsOfIncomeTaxPaid(paySlipCreateDto.getEmployeeId());
//        otherEmployeeDetails.setNumberOfYearsOfIncomeTaxPaid(yearCount.add(BigDecimal.ONE));
//    } else if ("Monthly".equalsIgnoreCase(paySlipCreateDto.getPayPeriod())) {
//        BigDecimal monthCount=otherEmployeeDetailsRepository.findByNumberOfMonthsOfIncomeTaxPaid(paySlipCreateDto.getEmployeeId());
//        otherEmployeeDetails.setNumberOfMonthsOfIncomeTaxPaid(monthCount.add(BigDecimal.ONE));
//    }
//        else if ("Weekly".equalsIgnoreCase(paySlipCreateDto.getPayPeriod())) {
//        BigDecimal weekCount=otherEmployeeDetailsRepository.findByNumberOfWeeksOfIncomeTaxPaid(paySlipCreateDto.getEmployeeId());
//        otherEmployeeDetails.setNumberOfWeeksOfIncomeTaxPaid(weekCount.add(BigDecimal.ONE));
//    }
//
//        System.out.println("Income Tax Paid: " + otherEmployeeDetails.getIncomeTaxPaid());
//        System.out.println("Total Income Tax Paid in Company: " + otherEmployeeDetails.getTotalIncomeTaxPaidInCompany());
//        System.out.println("Number of Years of Income Tax Paid: " + otherEmployeeDetails.getNumberOfYearsOfIncomeTaxPaid());
//        System.out.println("Number of Months of Income Tax Paid: " + otherEmployeeDetails.getNumberOfMonthsOfIncomeTaxPaid());
//        System.out.println("Number of Weeks of Income Tax Paid: " + otherEmployeeDetails.getNumberOfWeeksOfIncomeTaxPaid());
//
//    // Update OtherEmployeeDetails with National Insurance contributions
//        otherEmployeeDetails.setEmployeeNIContribution(NI);
//    BigDecimal totalEmployeeNIContributionInCompany=otherEmployeeDetailsRepository.findByTotalEmployeeNIContributionInCompany(paySlipCreateDto.getEmployeeId());
//        otherEmployeeDetails.setTotalEmployeeNIContributionInCompany(
//                totalEmployeeNIContributionInCompany.add(NI)
//            );
//
////        int numberOfNIPaidYearsInCompany=otherEmployeeDetailsRepository.findByNumberOfNIPaidYearsInCompany();
////        otherEmployeeDetails.setNumberOfNIPaidYearsInCompany(numberOfNIPaidYearsInCompany+1);
//
//        if("Yearly".equalsIgnoreCase(paySlipCreateDto.getPayPeriod())){
//        BigDecimal yearCount=otherEmployeeDetailsRepository.findByNumberOfYearsOfNIContributions(paySlipCreateDto.getEmployeeId());
//        otherEmployeeDetails.setNumberOfYearsOfNIContributions(yearCount.add(BigDecimal.ONE));
//    } else if ("Monthly".equalsIgnoreCase(paySlipCreateDto.getPayPeriod())) {
//        BigDecimal monthCount=otherEmployeeDetailsRepository.findByNumberOfMonthsOfNIContributions(paySlipCreateDto.getEmployeeId());
//        otherEmployeeDetails.setNumberOfMonthsOfNIContributions(monthCount.add(BigDecimal.ONE));
//    }
//        else if ("Weekly".equalsIgnoreCase(paySlipCreateDto.getPayPeriod())) {
//        BigDecimal weekCount=otherEmployeeDetailsRepository.findByNumberOfWeeksOfNIContributions(paySlipCreateDto.getEmployeeId());
//        otherEmployeeDetails.setNumberOfWeeksOfNIContributions(weekCount.add(BigDecimal.ONE));
//    }
//        System.out.println("Employee NI Contribution: " + otherEmployeeDetails.getEmployeeNIContribution());
//        System.out.println("Total Employee NI Contribution in Company: " + otherEmployeeDetails.getTotalEmployeeNIContributionInCompany());
//        System.out.println("Number of Years of NI Contributions: " + otherEmployeeDetails.getNumberOfYearsOfNIContributions());
//        System.out.println("Number of Months of NI Contributions: " + otherEmployeeDetails.getNumberOfMonthsOfNIContributions());
//        System.out.println("Number of Weeks of NI Contributions: " + otherEmployeeDetails.getNumberOfWeeksOfNIContributions());
//
//
//    OtherEmployeeDetails employeeDetails = otherEmployeeDetailsRepository.save(otherEmployeeDetails);
//        System.out.println("Successfully saved OtherEmployeeDetails: " + employeeDetails);
////        employeeDetails.setPaySlip(savedPaySlip); // if there's a relation
//        System.out.println("Other Employee Details:"+employeeDetails);
//        System.out.println("Service Data:"+savedPaySlip);
//}
