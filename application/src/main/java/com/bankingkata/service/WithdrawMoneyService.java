package com.bankingkata.service;

import com.bankingkata.exception.AccountNotFoundException;
import com.bankingkata.model.Account;
import com.bankingkata.model.Money;
import com.bankingkata.model.Transaction;
import com.bankingkata.port.in.WithdrawMoneyUseCase;
import com.bankingkata.port.out.LoadAccountPort;
import com.bankingkata.port.out.SaveAccountPort;
import com.bankingkata.port.out.SaveTransactionPort;
import com.bankingkata.port.out.UnitOfWork;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class WithdrawMoneyService implements WithdrawMoneyUseCase {
    
    private final SaveAccountPort saveAccountPort;
    private final LoadAccountPort loadAccountPort;
    private final SaveTransactionPort saveTransactionPort;
    private final UnitOfWork unitOfWork;

    @Override
    public Account withdraw(String accountId, Money amount) {
        return unitOfWork.execute(() -> {
            Account account = loadAccountPort.load(accountId)
                .orElseThrow(() -> new AccountNotFoundException(accountId));
            
            account.withdraw(amount);
            Transaction transaction = Transaction.withdrawal(accountId, amount);

            Account savedAccount = saveAccountPort.save(account);
            saveTransactionPort.save(transaction);

            return savedAccount;
        });
    }  
}
