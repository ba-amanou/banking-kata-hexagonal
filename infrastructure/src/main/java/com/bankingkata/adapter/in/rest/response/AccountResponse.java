package com.bankingkata.adapter.in.rest.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AccountResponse {
    private String id;
    private double balance;
    private List<TransactionResponse> transactions;
}
