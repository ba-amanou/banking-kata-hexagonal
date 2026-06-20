package com.bankingkata.adapter.in.rest.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.bankingkata.model.TransactionType;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TransactionResponse {
    private String id;
    private String accountId;
    private BigDecimal amount;
    private TransactionType type;
    private LocalDateTime date;
}
