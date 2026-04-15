package com.bankingkata.adapter.in.rest.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountResponse {
    private String id;
    private double balance;
    private List<TransactionResponse> transactions;
}
