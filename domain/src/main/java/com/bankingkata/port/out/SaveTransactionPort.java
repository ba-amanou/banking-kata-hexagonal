package com.bankingkata.port.out;

import com.bankingkata.model.Transaction;

public interface SaveTransactionPort {
    void save(Transaction transaction);
}
