package com.payroll.uk.payroll_processing.exception;

public class DuplicateNationalInsuranceException extends RuntimeException {
    public DuplicateNationalInsuranceException(String message) {
        super(message);
    }
}
