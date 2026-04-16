package com.bankingkata.model;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class Transaction {
    private String id;
    private String accountId;
    private Money amount;
    private TransactionType type;
    private LocalDateTime date;

    public static Transaction deposit(String accountId, Money amount) {
        return Transaction.builder()
            .id(UUID.randomUUID().toString())
            .accountId(accountId)
            .amount(amount)
            .type(TransactionType.DEPOSIT)
            .date(LocalDateTime.now())
            .build();
    }

    public static Transaction withdrawal(String accountId, Money amount) {
        return Transaction.builder()
            .id(UUID.randomUUID().toString())
            .accountId(accountId)
            .amount(amount)
            .type(TransactionType.WITHDRAWAL)
            .date(LocalDateTime.now())
            .build();
    }    
}
