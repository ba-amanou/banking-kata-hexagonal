package com.bankingkata.model;

import java.math.BigDecimal;
import java.math.RoundingMode;

import com.bankingkata.exception.InvalidAmountException;

public record Money(BigDecimal amount) {
    public Money {
        if(amount.compareTo(BigDecimal.ZERO) < 0){
            throw new InvalidAmountException("Amount cannot be negative");
        }
        try {
            amount = amount.setScale(2, RoundingMode.UNNECESSARY);    
        } catch (ArithmeticException e) {
            throw new InvalidAmountException("Amount cannot have more than 2 decimal places");
        }
        
    }

    public static Money of(String amount) {
        try {
            return new Money(new BigDecimal(amount));
        } catch (NumberFormatException e) {
            throw new InvalidAmountException("Invalid Amount");
        }
    }
    
    public Money add(Money other){
        return new Money(this.amount.add(other.amount));
    }

    public Money subtract(Money other){
        if(other.amount.compareTo(this.amount) > 0){
            throw new InvalidAmountException("Insufficient funds");
        } 
        return new Money(this.amount.subtract(other.amount));
    }    
}
