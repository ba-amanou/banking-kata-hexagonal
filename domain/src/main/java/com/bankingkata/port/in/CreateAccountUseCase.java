package com.bankingkata.port.in;

import com.bankingkata.model.Account;
import com.bankingkata.model.Money;

public interface CreateAccountUseCase {
    Account createAccount(Money initialBalance);
}
