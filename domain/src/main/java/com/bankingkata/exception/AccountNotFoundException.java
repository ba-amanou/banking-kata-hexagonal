package com.bankingkata.exception;

public class AccountNotFoundException extends DomainException {
    
    public AccountNotFoundException(String message) {
        super("Account not found with id: " + message);
    }
}
