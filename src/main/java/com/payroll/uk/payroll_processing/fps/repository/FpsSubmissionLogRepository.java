package com.payroll.uk.payroll_processing.fps.repository;

import com.payroll.uk.payroll_processing.fps.entity.FpsSubmissionLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FpsSubmissionLogRepository extends JpaRepository<FpsSubmissionLog, Long> {
}
