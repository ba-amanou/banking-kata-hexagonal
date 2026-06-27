package com.bankingkata.model;

import java.math.BigDecimal;
import java.math.RoundingMode;

import com.bankingkata.exception.InvalidAmountException;

public record Money(BigDecimal amount) {
    public Money {
        if(amount == null) {
            throw new InvalidAmountException("Amount cannot be null");
        }
        if(amount.compareTo(BigDecimal.ZERO) < 0){
            throw new InvalidAmountException("Amount cannot be negative");
        }
        if(amount.scale() > 2) {
            throw new InvalidAmountException("Amount cannot have more than 2 decimal places");
        }
        amount = amount.setScale(2, RoundingMode.UNNECESSARY);  
    }

    public static Money of(String amount) {
        if(amount == null) {
            throw new InvalidAmountException("Amount cannot be null");
        }
        try {
            return new Money(new BigDecimal(amount));
        } catch (NumberFormatException e) {
            throw new InvalidAmountException("Invalid Amount");
        }
    }
    
    public Money add(Money other){
        if(other == null) {
            throw new InvalidAmountException("Amount cannot be null");
        }        
        return new Money(this.amount.add(other.amount));
    }

    public Money subtract(Money other){
        if(other == null) {
            throw new InvalidAmountException("Amount cannot be null");
        }        
        if(other.amount.compareTo(this.amount) > 0){
            throw new InvalidAmountException("Insufficient funds");
        } 
        return new Money(this.amount.subtract(other.amount));
    }    
}
