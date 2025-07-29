package com.payroll.uk.payroll_processing.exception;

public class InvalidComputationException extends  RuntimeException{

    public InvalidComputationException(String message) {
        super(message);
    }

    public InvalidComputationException(String message, Throwable cause) {
        super(message, cause);
    }


}
