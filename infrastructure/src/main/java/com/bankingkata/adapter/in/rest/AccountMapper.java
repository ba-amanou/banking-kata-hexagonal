package com.bankingkata.adapter.in.rest;

import org.springframework.stereotype.Component;

import com.bankingkata.adapter.in.rest.response.AccountResponse;
import com.bankingkata.model.Account;


@Component
public class AccountMapper {
    
    public AccountResponse toResponse(Account account) {
        return new AccountResponse(account.getId(), account.getBalance().amount());
    }
}
