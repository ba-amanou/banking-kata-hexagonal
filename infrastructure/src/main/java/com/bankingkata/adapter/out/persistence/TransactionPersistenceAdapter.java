package com.bankingkata.adapter.out.persistence;

import java.util.List;

import org.springframework.stereotype.Component;

import com.bankingkata.model.Transaction;
import com.bankingkata.port.out.LoadTransactionPort;
import com.bankingkata.port.out.SaveTransactionPort;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TransactionPersistenceAdapter implements SaveTransactionPort, LoadTransactionPort {
    private final TransactionJpaRepository transactionJpaRepository;
    private final TransactionPersistenceMapper mapper;

    @Override
    public void save(Transaction transaction) {
        TransactionJpaEntity transactionEntity = mapper.toEntity(transaction);
        transactionJpaRepository.save(transactionEntity);
    }    
    
    @Override
    public List<Transaction> loadByAccountId(String accountId) {
        return transactionJpaRepository.findByAccountId(accountId)
            .stream()
            .map(mapper::toDomain)
            .toList();
    }



    
}
