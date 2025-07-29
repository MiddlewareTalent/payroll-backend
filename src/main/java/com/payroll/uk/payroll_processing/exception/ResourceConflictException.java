package com.payroll.uk.payroll_processing.exception;

public class ResourceConflictException extends  RuntimeException{
    public ResourceConflictException(String message) {

        super(message);
        System.out.println("ResourceConflictException: " + message);
    }

    public ResourceConflictException(String message, Throwable cause) {
        super(message, cause);
    }
}
