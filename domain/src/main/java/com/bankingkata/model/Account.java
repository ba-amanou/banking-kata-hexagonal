package com.bankingkata.model;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

@Getter
public class Account {
    private String id;
    private Money balance;
    private List<Transaction> transactions;

    

    public Account(String id, Money balance) {
        this.id = id;
        this.balance = balance;
        this.transactions = new ArrayList<>();
    }

    public void deposit(Money amount) {
        this.transactions.add(Transaction.deposit(amount));
        this.balance = this.balance.add(amount);
    }

    public void withdraw(Money amount) {
        this.transactions.add(Transaction.withdrawal(amount));
        this.balance = this.balance.subtract(amount);
    }
}
