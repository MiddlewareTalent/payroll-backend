package com.payroll.uk.payroll_processing.fps.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "fps_submission_log")
public class FpsSubmissionLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String employerRef;   // e.g. 635/A635

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String submissionXml; // XML sent to HMRC

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String responseXml;   // Response from HMRC

    private String correlationId;
    private String transactionId;
    private String status;        // Accepted / Rejected / Pending

    private LocalDateTime submittedAt;
}
