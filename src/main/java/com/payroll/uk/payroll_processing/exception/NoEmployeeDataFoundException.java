package com.payroll.uk.payroll_processing.exception;

public class NoEmployeeDataFoundException extends RuntimeException {
    public NoEmployeeDataFoundException(String message) {
        super(message);
    }
}
