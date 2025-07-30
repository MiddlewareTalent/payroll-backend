package com.payroll.uk.payroll_processing.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    private ResponseEntity<Map<String, Object>> buildResponse(HttpStatus status, String message, Exception ex) {
        Map<String, Object> error = new HashMap<>();
        error.put("status", status.value());
        error.put("error", status.getReasonPhrase());
        error.put("message", message);
        error.put("timestamp", LocalDateTime.now().format(java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        return ResponseEntity.status(status).body(error);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleEmployeeNotFound(ResourceNotFoundException ex) {
        logger.warn("Employee not found", ex);
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage(), ex); //status 404 for not found
    }

    @ExceptionHandler(DataValidationException.class)
    public ResponseEntity<Map<String, Object>> handleDataValidation(DataValidationException ex) {
        logger.warn("Data validation failed", ex);
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), ex); //status 400 for bad request
    }

    @ExceptionHandler(InvalidComputationException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidComputation(InvalidComputationException ex) {
        logger.warn("Invalid computation", ex);
        return buildResponse(HttpStatus.UNPROCESSABLE_ENTITY, ex.getMessage(), ex); //status 422 for unprocessable entity
    }

    @ExceptionHandler(ResourceConflictException.class)
    public ResponseEntity<Map<String, Object>> handleResourceConflict(ResourceConflictException ex) {
        logger.warn("Resource conflict", ex);
        return buildResponse(HttpStatus.CONFLICT, ex.getMessage(), ex);  //status 409 for conflict
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneric(Exception ex) {
        logger.error("Unexpected server error", ex);
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error: " + ex.getMessage(), ex);  //status 500 for internal server error
    }
}
