package com.bankingkata.port.in;

import com.bankingkata.model.Money;

public interface GetAccountBalanceUseCase {
    Money getBalance(String accountId);
}
