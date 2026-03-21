package com.bankingkata.port.in;

import com.bankingkata.model.Account;
import com.bankingkata.model.Money;

public interface DepositMoneyUseCase {
    Account deposit(String accountId, Money amount);
}
