package com.bankingkata.adapter.in.rest;

import org.springframework.stereotype.Component;

import com.bankingkata.adapter.in.rest.response.TransactionResponse;
import com.bankingkata.model.Transaction;

@Component
public class TransactionMapper {
    
    public TransactionResponse toResponse(Transaction transaction) {
        return new TransactionResponse(transaction.getId(), 
            transaction.getAccountId(), 
            transaction.getAmount().amount(), 
            transaction.getType(), 
            transaction.getDate());
    }
}
