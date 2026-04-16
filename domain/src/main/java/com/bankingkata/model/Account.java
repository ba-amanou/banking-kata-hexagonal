package com.bankingkata.model;

import java.util.UUID;

import lombok.Getter;

@Getter
public class Account {
    private String id;
    private Money balance;

    public Account(Money balance) {
        this.id = UUID.randomUUID().toString();
        this.balance = balance;
    }

    public Account(String id, Money balance) {
        this.id = id;
        this.balance = balance;
    }    

    public void deposit(Money amount) {
        this.balance = this.balance.add(amount);
    }

    public void withdraw(Money amount) {
        this.balance = this.balance.subtract(amount);
    }
}
