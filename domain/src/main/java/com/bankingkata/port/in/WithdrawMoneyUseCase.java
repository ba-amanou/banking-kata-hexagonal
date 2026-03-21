package com.bankingkata.port.in;

import com.bankingkata.model.Account;
import com.bankingkata.model.Money;

public interface WithdrawMoneyUseCase {
    Account withdraw(String accountId, Money amount);
}
