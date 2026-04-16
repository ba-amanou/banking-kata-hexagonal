package com.bankingkata.adapter.out.persistence;

import org.springframework.stereotype.Component;

import com.bankingkata.model.Money;
import com.bankingkata.model.Transaction;
import com.bankingkata.model.TransactionType;

@Component
public class TransactionPersistenceMapper {
    
    public TransactionJpaEntity toEntity(Transaction transaction) {
        return TransactionJpaEntity.builder()
            .id(transaction.getId())
            .accountId(transaction.getAccountId())
            .type(transaction.getType().name())
            .amount(transaction.getAmount().amount())
            .date(transaction.getDate())
            .build();
    }

    public Transaction toDomain(TransactionJpaEntity transactionEntity) {
        return Transaction.builder()
            .id(transactionEntity.getId())
            .accountId(transactionEntity.getAccountId())
            .amount(new Money(transactionEntity.getAmount()))
            .type(TransactionType.valueOf(transactionEntity.getType()))
            .date(transactionEntity.getDate())
            .build();
    }
}
