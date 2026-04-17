package com.bankingkata.service;

import com.bankingkata.model.Account;
import com.bankingkata.model.Money;
import com.bankingkata.port.in.CreateAccountUseCase;
import com.bankingkata.port.out.SaveAccountPort;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CreateAccountService implements CreateAccountUseCase {

    private final SaveAccountPort saveAccountPort;

    @Override
    public Account createAccount(Money initialBalance) {
        Account newAccount = Account.create(initialBalance);

        saveAccountPort.save(newAccount);
        
        return newAccount;
    }
    
}
