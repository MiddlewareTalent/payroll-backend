package com.payroll.uk.payroll_processing.fps.dto.employment;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class EmploymentDTO {
    private String employmentOffPayrollWorker;
    private String employmentOccPenInd;
    private String employmentDirectorsNIC;
    private String employmentTaxWkOfApptOfDirector;
    private StarterDTO employmentStarter;
    private String employeeWorkplacePostcode;
    private String employmentPayrollId;
    private PayrollIdChangedDTO employmentPayrollIdChanged;
    private String paymentToANonIndividual;
    private String irregularPaymentIndicator;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate employmentLeavingDate;
    private FiguresToDateDTO employmentFiguresToDate;
    private PaymentDTO employmentPayment;
    private List<NILettersAndValuesDTO> employmentNILettersAndValues;







}
