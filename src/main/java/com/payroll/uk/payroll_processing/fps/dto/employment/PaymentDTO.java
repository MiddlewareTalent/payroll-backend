package com.payroll.uk.payroll_processing.fps.dto.employment;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
public class PaymentDTO {
    private String payFrequency;
    private LocalDate pmtDate;
    private String lateReason;
    private String weekNo;
    private String monthNo;
    private int periodsCovered;
    private String aggregatedEarnings;
    private String pmtAfterLeaving;
    private String hoursWorked;


    private TaxCodeDTO taxCode;
    private BigDecimal taxablePay;
    private BigDecimal nonTaxOrNICPmt;
    private BigDecimal deductionsFromNetPay;
    private BigDecimal payAfterStatutoryDeductions;
    private BigDecimal benefitsTaxedViaPayroll;
    private BigDecimal class1ANICsYTD;
    private CarBenefits carBenefits;
    private BigDecimal employeePensionContributions;
    private BigDecimal itemsSubjectToClass1NIC;
    private BigDecimal employeePensionContributionsNotPaid;

    //studentLoan Recovered
   private StudentLoanRecoveredDTO studentLoanRecovered;
    private BigDecimal postgraduateLoanRecovered;
    private BigDecimal taxDeductedOrRefunded;
    private String onStrike;
    private String unpaidAbsence;
    private BigDecimal smpytd;
    private BigDecimal sppytd;
    private BigDecimal sapytd;
    private BigDecimal shPPYTD;
    private BigDecimal spbpytd;
    private BigDecimal sncpytd;
    private List<TrivialCommutationPaymentDTO> trivialCommutationPayments;

    private FlexibleDrawdownDTO flexibleDrawdown;





}
