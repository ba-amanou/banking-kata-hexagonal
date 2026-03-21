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
    private Money amount;
    private TransactionType type;
    private LocalDateTime date;

    public static Transaction deposit(Money amount) {
        return Transaction.builder()
            .id(UUID.randomUUID().toString())
            .amount(amount)
            .type(TransactionType.DEPOSIT)
            .date(LocalDateTime.now())
            .build();
    }

    public static Transaction withdrawal(Money amount) {
        return Transaction.builder()
            .id(UUID.randomUUID().toString())
            .amount(amount)
            .type(TransactionType.WITHDRAWAL)
            .date(LocalDateTime.now())
            .build();
    }    
}
