package com.bankingkata.adapter.out.persistence;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.bankingkata.model.Account;
import com.bankingkata.model.Money;
import com.bankingkata.model.Transaction;
import com.bankingkata.model.TransactionType;

@Component
public class AccountPersistenceMapper {
    public AccountJpaEntity toJpaEntity(Account account) {
        List<TransactionJpaEntity> transactionsJpaEntity = account.getTransactions()
            .stream()
            .map(this::toTransactionJpaEntity)
            .collect(Collectors.toList());

        return AccountJpaEntity.builder()
            .id(account.getId())
            .balance(account.getBalance().amount())
            .transactions(transactionsJpaEntity)
            .build();
    }

    private TransactionJpaEntity toTransactionJpaEntity(Transaction transaction) {
        return TransactionJpaEntity.builder()
            .id(transaction.getId())
            .amount(transaction.getAmount().amount())
            .type(transaction.getType().name())
            .date(transaction.getDate())
            .build();
            
    }

    public Account toDomain(AccountJpaEntity accountJpaEntity) {
        List<Transaction> transactions = accountJpaEntity.getTransactions()
            .stream()
            .map(this::toTransactionDomain)
            .collect(Collectors.toList());

        return new Account(accountJpaEntity.getId(), new Money(accountJpaEntity.getBalance()), transactions);
    }

    private Transaction toTransactionDomain(TransactionJpaEntity transactionJpaEntity) {
        return Transaction.builder()
            .id(transactionJpaEntity.getId())
            .amount(new Money(transactionJpaEntity.getAmount()))
            .type(TransactionType.valueOf(transactionJpaEntity.getType()))
            .date(transactionJpaEntity.getDate())
            .build();
            
    }
}
