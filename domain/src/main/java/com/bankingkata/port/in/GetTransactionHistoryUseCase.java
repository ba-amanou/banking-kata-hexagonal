package com.bankingkata.port.in;

import java.util.List;

import com.bankingkata.model.Transaction;

public interface GetTransactionHistoryUseCase {
    List<Transaction> history(String accountId);
}
