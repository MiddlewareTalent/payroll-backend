package com.payroll.uk.payroll_processing.exception;

public class InvalidPayPeriodException extends RuntimeException {
    public InvalidPayPeriodException(String message) {
        super(message);
    }
}
