package com.payroll.uk.payroll_processing.exception;

public class DataValidationException extends RuntimeException {
    public DataValidationException(String message) {
        super(message);
    }

    public DataValidationException(String message, Throwable cause) {
        super(message, cause);
    }

    public DataValidationException(String message, String employeeId) {
    }
}