package com.bankingkata.service;

import com.bankingkata.exception.AccountNotFoundException;
import com.bankingkata.model.Account;
import com.bankingkata.model.Money;
import com.bankingkata.port.in.WithdrawMoneyUseCase;
import com.bankingkata.port.out.LoadAccountPort;
import com.bankingkata.port.out.SaveAccountPort;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class WithdrawMoneyService implements WithdrawMoneyUseCase {
    
    private final SaveAccountPort saveAccountPort;
    private final LoadAccountPort loadAccountPort;

    @Override
    public Account withdraw(String accountId, Money amount) {
        Account account = loadAccountPort.load(accountId);
        
        if (account == null) throw new AccountNotFoundException(accountId);
        
        account.withdraw(amount);
        saveAccountPort.save(account);

        return account;
    }  
}
