package com.payroll.uk.payroll_processing.fps.response;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class GovTalkResponseDTO {

    private String correlationId;
    private String transactionId;
    private String status; // "Accepted", "Rejected", "Error"
    private List<String> errorMessages = new ArrayList<>();
}

