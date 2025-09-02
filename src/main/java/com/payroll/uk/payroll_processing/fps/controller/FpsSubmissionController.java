package com.payroll.uk.payroll_processing.fps.controller;

import com.payroll.uk.payroll_processing.fps.dto.FpsSubmissionRequest;
import com.payroll.uk.payroll_processing.fps.entity.FpsSubmissionLog;
import com.payroll.uk.payroll_processing.fps.repository.FpsSubmissionLogRepository;
import com.payroll.uk.payroll_processing.fps.response.GovTalkResponseDTO;
import com.payroll.uk.payroll_processing.fps.service.FpsSubmissionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/fps")
public class FpsSubmissionController {

    private final FpsSubmissionService fpsSubmissionService;
    private final FpsSubmissionLogRepository logRepository;

    public FpsSubmissionController(FpsSubmissionService fpsSubmissionService,
                                   FpsSubmissionLogRepository logRepository) {
        this.fpsSubmissionService = fpsSubmissionService;
        this.logRepository = logRepository;
    }

    // 🔹 1. Submit FPS
    @PostMapping("/submit")
    public ResponseEntity<GovTalkResponseDTO> submitFps(@RequestBody FpsSubmissionRequest request) {
        try {
            GovTalkResponseDTO response = fpsSubmissionService.submitFps(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    // 🔹 2. Get all submission logs
    @GetMapping("/logs")
    public ResponseEntity<List<FpsSubmissionLog>> getAllLogs() {
        return ResponseEntity.ok(logRepository.findAll());
    }

    // 🔹 3. Get submission log by ID
    @GetMapping("/logs/{id}")
    public ResponseEntity<FpsSubmissionLog> getLogById(@PathVariable Long id) {
        return logRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
