package com.bankingkata.adapter.in.rest.response;

import java.time.LocalDateTime;

import com.bankingkata.model.TransactionType;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TransactionResponse {
    private String id;
    private String accountId;
    private double amount;
    private TransactionType type;
    private LocalDateTime date;
}
