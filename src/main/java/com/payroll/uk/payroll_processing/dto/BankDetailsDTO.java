package com.payroll.uk.payroll_processing.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class BankDetailsDTO {

//    @Schema(defaultValue = "0")
//    private Long id;
    private String  accountName;
    private String accountNumber;
    private String paymentReference;
    private String bankName;
    private String sortCode;
    private String bankAddress;
    private String bankPostCode;
    private String telephone;
    @Schema(defaultValue = "0")
    private int paymentLeadDays=0;
    @Schema(defaultValue = "false")
    private Boolean isRTIReturnsIncluded=false;
}
