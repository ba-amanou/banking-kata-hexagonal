package com.bankingkata.service;

import java.util.List;

import com.bankingkata.model.Transaction;
import com.bankingkata.port.in.GetTransactionHistoryUseCase;
import com.bankingkata.port.out.LoadTransactionPort;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GetTransactionHistoryService implements GetTransactionHistoryUseCase {

    private final LoadTransactionPort loadTransactionPort;

    @Override
    public List<Transaction> history(String accountId) {
        return loadTransactionPort.loadByAccountId(accountId);
    }
    
}
