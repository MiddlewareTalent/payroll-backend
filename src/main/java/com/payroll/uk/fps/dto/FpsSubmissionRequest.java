package com.payroll.uk.fps.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FpsSubmissionRequest {
    private String taxYear; // e.g. "2025-26"
    private String hmrcOfficeNumber; // 3 digits, e.g. "123"
    private String employerRef; // e.g. "123/A12345"
    private String accountsOfficeRef; // e.g. "123PA00123456"
    private String employerName;
    private String sender; // optional metadata: "Employer" / "Agent"
    private LocalDate submissionDate; // today's date
    private List<FpsEmployeeDTO> employees;
}

