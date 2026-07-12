package com.bankingkata.port.out;

import com.bankingkata.model.Account;

public interface SaveAccountPort {
    Account save(Account account);
}
