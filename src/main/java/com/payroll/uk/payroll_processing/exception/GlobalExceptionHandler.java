//package com.payroll.uk.payroll_processing.exception;
//
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.MethodArgumentNotValidException;
//import org.springframework.web.bind.annotation.ControllerAdvice;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.validation.FieldError;
//
//import java.util.stream.Collectors;
//
//@ControllerAdvice
//public class GlobalExceptionHandler {
//
//    // Handle company registration conflicts (e.g., duplicate company)
//    @ExceptionHandler(CompanyRegistrationException.class)
//    public ResponseEntity<?> handleCompanyRegistrationException(CompanyRegistrationException ex) {
//        // Check if it's a "not found" case
//        if (ex.getMessage().contains("not found")) {
//            return ResponseEntity
//                    .status(HttpStatus.NOT_FOUND)
//                    .body(new ErrorResponse("COMPANY_NOT_FOUND", ex.getMessage()));
//        }
//
//        // Default to bad request for other company registration errors
//        return ResponseEntity
//                .status(HttpStatus.BAD_REQUEST)
//                .body(new ErrorResponse("COMPANY_REGISTRATION_ERROR", ex.getMessage()));
//    }
//
//    // Handle employer registration exceptions
//    @ExceptionHandler(EmployerRegistrationException.class)
//    public ResponseEntity<String> handleEmployerRegistrationException(EmployerRegistrationException ex) {
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
//    }
//
//    // Handle validation exceptions
//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex) {
//        String errorMessage = ex.getBindingResult()
//                .getFieldErrors()
//                .stream()
//                .map(FieldError::getDefaultMessage)
//                .collect(Collectors.joining(", "));
//
//        return ResponseEntity
//                .status(HttpStatus.BAD_REQUEST)
//                .body(new ErrorResponse("VALIDATION_ERROR", errorMessage));
//    }
//
//    // Global fallback
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<String> handleGenericException(Exception ex) {
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                .body("An unexpected error occurred");
//    }
//
//    public record ErrorResponse(String errorCode, String message) {}
//}