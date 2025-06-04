package com.payroll.uk.payroll_processing.exception;

public class CompanyRegistrationException extends RuntimeException {

    public CompanyRegistrationException(String companyName) {
        super(STR."Company registration not found with name: \{companyName}");
    }




}
