package com.bankingkata.service;

import com.bankingkata.exception.AccountNotFoundException;
import com.bankingkata.model.Account;
import com.bankingkata.model.Money;
import com.bankingkata.port.in.GetAccountBalanceUseCase;
import com.bankingkata.port.out.LoadAccountPort;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GetAccountBalanceService implements GetAccountBalanceUseCase {

    private final LoadAccountPort loadAccountPort;

    @Override
    public Money getBalance(String accountId) {
        Account account = loadAccountPort.load(accountId);

        if(account == null) throw new AccountNotFoundException(accountId);
        
        return account.getBalance();
    }
    
}
