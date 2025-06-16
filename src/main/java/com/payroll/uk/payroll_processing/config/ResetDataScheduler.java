package com.payroll.uk.payroll_processing.config;

import com.payroll.uk.payroll_processing.repository.EmployerDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.YearMonth;

@Component
public class ResetDataScheduler {

    @Autowired
    private EmployerDetailsRepository employerDetailsRepository;

    @Scheduled(cron = "0 0 0 1 * *") // only on 1st of each month
    @Transactional
    public void updatePayDate() {
        LocalDate today = LocalDate.now();
        LocalDate endOfThisMonth = YearMonth.from(today).atEndOfMonth();

        LocalDate payDate = today.isAfter(endOfThisMonth)
                ? YearMonth.from(today).plusMonths(1).atEndOfMonth()
                : endOfThisMonth;

        employerDetailsRepository.updatePayDateForAll(payDate);
        System.out.println("Pay date updated to: " + payDate);
    }
}
