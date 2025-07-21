package com.payroll.uk.payroll_processing.dto.employerdto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.payroll.uk.payroll_processing.dto.BankDetailsDTO;
import com.payroll.uk.payroll_processing.entity.BankDetails;
import com.payroll.uk.payroll_processing.entity.PayPeriod;
import com.payroll.uk.payroll_processing.entity.TaxThreshold;
import com.payroll.uk.payroll_processing.entity.employer.CompanyDetails;
import com.payroll.uk.payroll_processing.entity.employer.OtherEmployerDetails;
import com.payroll.uk.payroll_processing.entity.employer.TaxOffice;
import com.payroll.uk.payroll_processing.entity.employer.Terms;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployerDetailsDTO {

    private Long id;
    private CompanyDetailsDTO companyDetailsDTO;

    private String employerId;
    private String employerName;
    private String employerAddress;
    private String employerPostCode;
    private String employerTelephone;
    private String employerEmail;

    private TaxOfficeDTO taxOfficeDTO;
    private BankDetailsDTO bankDetailsDTO;
    private TermsDTO termsDTO;
    private OtherEmployerDetailsDTO otherEmployerDetailsDTO;

}
