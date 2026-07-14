package com.bankingkata.exception;

public class ConcurrentAccountModificationException extends DomainException {

    public ConcurrentAccountModificationException() {
        super("Account was modified concurrently, please retry the operation");
    }
    
}
