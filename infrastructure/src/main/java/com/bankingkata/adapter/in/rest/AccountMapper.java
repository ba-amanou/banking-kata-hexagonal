package com.bankingkata.adapter.in.rest;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.bankingkata.adapter.in.rest.response.AccountResponse;
import com.bankingkata.adapter.in.rest.response.TransactionResponse;
import com.bankingkata.model.Account;
import com.bankingkata.model.Transaction;


@Component
public class AccountMapper {
    
    public AccountResponse toResponse(Account account) {
        List<TransactionResponse> transactions = account.getTransactions()
            .stream().map(this::toTransactionResponse)
            .collect(Collectors.toList());

        return new AccountResponse(account.getId(), account.getBalance().amount(), transactions);
    }

    private TransactionResponse toTransactionResponse(Transaction transaction){
        return new TransactionResponse(
            transaction.getId(),
            transaction.getAmount().amount(),
            transaction.getType(),
            transaction.getDate()
        );
    }
}
