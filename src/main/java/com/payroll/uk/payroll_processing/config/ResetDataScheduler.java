package com.payroll.uk.payroll_processing.config;

import com.payroll.uk.payroll_processing.entity.employer.EmployerDetails;
import com.payroll.uk.payroll_processing.repository.EmployerDetailsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

@Component
public class ResetDataScheduler {

    private static final Logger logger = LoggerFactory.getLogger(ResetDataScheduler.class);

    @Autowired
    private EmployerDetailsRepository employerDetailsRepository;

//    @Scheduled(cron = "0 */2 * * * *")
    /**
     * Updates the pay date for each employer to the same day next month.
     * Example: 2024-07-19 -> 2024-08-19
     */
    @Scheduled(cron = "0 0 0 1 * *") // Every month on the 1st at 00:00
    @Transactional
    public void updatePayDateAndResetFields() {
        List<EmployerDetails> employers = employerDetailsRepository.findAll();

        for (EmployerDetails employer : employers) {
            LocalDate currentPayDate = employer.getPayDate();
            if (currentPayDate == null) continue;

            // Add 1 month, but keep same day-of-month if possible
            int currentDay = currentPayDate.getDayOfMonth();
            YearMonth nextMonth = YearMonth.from(currentPayDate.plusMonths(1));
            int lastDayOfNextMonth = nextMonth.lengthOfMonth();

            // If original day is not valid in next month (e.g., 31st in Feb), adjust
            int nextDay = Math.min(currentDay, lastDayOfNextMonth);
            LocalDate nextPayDate = nextMonth.atDay(nextDay);

            employer.setPayDate(nextPayDate);
        }

        employerDetailsRepository.saveAll(employers);
        logger.info("successfully updated pay dates for all employers");
        logger.info("Updated pay date for all employers to same day next month");
    }

    /**
     * Resets monthly payroll contribution and tax fields to zero.
     */
    @Scheduled(cron = "0 1 0 1 * *") // Run 1 minute after pay date update
    @Transactional
    public void clearMonthlyPayrollFigures() {
        employerDetailsRepository.resetCurrentPayPeriodFieldsForAll(BigDecimal.ZERO);
        logger.info("successfully Reset current pay period figures to zero for all employers");
    }


}
