package com.example.purchaseorderworkflow.exception;

public class PurchaseOrderAlreadyExistsException extends RuntimeException {
    public PurchaseOrderAlreadyExistsException(String message) {
        super(message);
    }
}
