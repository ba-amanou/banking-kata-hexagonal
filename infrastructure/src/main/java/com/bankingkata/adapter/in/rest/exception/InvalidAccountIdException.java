package com.bankingkata.adapter.in.rest.exception;

public class InvalidAccountIdException extends RuntimeException {
    
    public InvalidAccountIdException(String message) {
        super("Invalid account id: " + message);
    }
}
