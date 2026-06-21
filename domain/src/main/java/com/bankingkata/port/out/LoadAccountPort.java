package com.bankingkata.port.out;

import java.util.Optional;

import com.bankingkata.model.Account;

public interface LoadAccountPort {
    Optional<Account> load(String accountId);
}
