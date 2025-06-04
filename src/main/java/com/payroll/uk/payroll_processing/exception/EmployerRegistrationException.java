package com.payroll.uk.payroll_processing.exception;

public class EmployerRegistrationException extends RuntimeException {

    public EmployerRegistrationException(String message) {
        super(message);
    }

    public EmployerRegistrationException(String message, Throwable cause) {
        super(message, cause);
    }

    public EmployerRegistrationException(Throwable cause) {
        super(cause);
    }
}
