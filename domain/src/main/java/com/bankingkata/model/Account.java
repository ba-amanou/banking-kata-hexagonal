package com.bankingkata.model;

import java.util.UUID;

import lombok.Getter;

@Getter
public class Account {
    private String id;
    private Money balance;

    private Account(String id, Money balance) {
        this.id = id;
        this.balance = balance;
    }

    public static Account create(Money initialBalance) {
        return new Account(UUID.randomUUID().toString(), initialBalance);
    }

    public static Account reconstitue(String id, Money balance) {
        return new Account(id, balance);
    }

    public void deposit(Money amount) {
        this.balance = this.balance.add(amount);
    }

    public void withdraw(Money amount) {
        this.balance = this.balance.subtract(amount);
    }
}
