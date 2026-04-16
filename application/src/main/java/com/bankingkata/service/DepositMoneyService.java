package com.bankingkata.service;

import com.bankingkata.exception.AccountNotFoundException;
import com.bankingkata.model.Account;
import com.bankingkata.model.Money;
import com.bankingkata.model.Transaction;
import com.bankingkata.port.in.DepositMoneyUseCase;
import com.bankingkata.port.out.LoadAccountPort;
import com.bankingkata.port.out.SaveAccountPort;
import com.bankingkata.port.out.SaveTransactionPort;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DepositMoneyService implements DepositMoneyUseCase {
    
    private final SaveAccountPort saveAccountPort;
    private final LoadAccountPort loadAccountPort;
    private final SaveTransactionPort saveTransactionPort;

    @Override
    public Account deposit(String accountId, Money amount) {
        Account account = loadAccountPort.load(accountId);

        if(account == null) throw new AccountNotFoundException(accountId);

        account.deposit(amount);
        Transaction transaction = Transaction.deposit(accountId, amount);

        saveAccountPort.save(account);
        saveTransactionPort.save(transaction);

        return account;
    }

    
}
