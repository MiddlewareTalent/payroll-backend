package com.payroll.uk.payroll_processing.exception;

public class InvalidTaxCodeException extends RuntimeException  {
    public InvalidTaxCodeException(String message) {
        super(message);
    }
}
