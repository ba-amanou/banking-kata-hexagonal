package com.bankingkata.port.out;

import com.bankingkata.model.Account;

public interface LoadAccountPort {
    Account load(String accountId);
}
