package com.bankingkata.exception;

public class InsufficientFundsException extends DomainException {
    
    public InsufficientFundsException(String message) {
       super(message); 
    }
}
