package com.example.purchaseorderworkflow.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Handle PurchaseOrder not found exception
    @ExceptionHandler(PurchaseOrderNotFoundException.class)
    public ResponseEntity<String> handlePurchaseOrderNotFoundException(PurchaseOrderNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    // Handle Unauthorized access exception
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<String> handleUnauthorizedException(UnauthorizedException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
    }

    // Handle PurchaseOrder already exists exception
    @ExceptionHandler(PurchaseOrderAlreadyExistsException.class)
    public ResponseEntity<String> handlePurchaseOrderAlreadyExistsException(PurchaseOrderAlreadyExistsException ex) {
        return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(ex.getMessage());
    }

    // Handle any general exceptions (fallback)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred: " + ex.getMessage());
    }
}
