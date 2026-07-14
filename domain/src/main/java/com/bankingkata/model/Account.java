package com.bankingkata.model;

import java.util.UUID;

import com.bankingkata.exception.InsufficientFundsException;
import com.bankingkata.exception.InvalidAmountException;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(of = "id")
public class Account {
    private String id;
    private Money balance;

    // Optimistic locking token, carried by the aggregate to protect
    // its consistency boundary regarless of the underlying persistence mechanism
    private Long version;

    private Account(String id, Money balance, Long version) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("Account id cannot be null or blank");
        }
        if(balance == null) {
            throw new InvalidAmountException("Balance cannot be null");
        }
        this.id = id;
        this.balance = balance;
        this.version = version;
    }

    public static Account create(Money initialBalance) {
        return new Account(UUID.randomUUID().toString(), initialBalance, null);
    }

    public static Account reconstitute(String id, Money balance, Long version) {
        return new Account(id, balance, version);
    }

    public void deposit(Money amount) {
        requireValidAmount(amount);
        this.balance = this.balance.add(amount);
    }

    public void withdraw(Money amount) {
        requireValidAmount(amount);
        if(amount.isGreaterThan(this.balance)) {
            throw new InsufficientFundsException("Insufficient funds");
        } 
        this.balance = this.balance.subtract(amount);
    }

    private static void requireValidAmount(Money amount) {
        if(amount == null) {
            throw new InvalidAmountException("Amount cannot be null");
        }
        if(amount.isZero()) {
            throw new InvalidAmountException("Amount must be greater than zero");
        }
    }
}
