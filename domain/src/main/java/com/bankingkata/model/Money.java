package com.bankingkata.model;

import com.bankingkata.exception.InvalidAmountException;

public record Money(double amount) {
    public Money {
        if(amount < 0){
            throw new InvalidAmountException("Amount cannot be negative");
        }
    }
    
    public Money add(Money other){
        return new Money(this.amount + other.amount);
    }

    public Money subtract(Money other){
        if(other.amount > this.amount){
            throw new InvalidAmountException("Insufficient funds");
        } 
        return new Money(this.amount - other.amount);
    }    
}
