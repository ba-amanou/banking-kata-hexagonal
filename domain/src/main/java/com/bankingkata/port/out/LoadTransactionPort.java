package com.bankingkata.port.out;

import java.util.List;

import com.bankingkata.model.Transaction;

public interface LoadTransactionPort {
    List<Transaction> loadByAccountId(String accountId);
}
