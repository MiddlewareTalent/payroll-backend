package com.payroll.uk.payroll_processing.fps.dto.ir;


import com.payroll.uk.payroll_processing.fps.dto.FpsSubmissionRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** Maps to <IRenvelope> containing <IRheader> and <FullPaymentSubmission> */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class IREnvelopeDTO {
    private IRHeaderDTO irHeader;
    private FpsSubmissionRequest fullPaymentSubmission;
}
