package com.bankingkata.adapter.in.rest.request;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class CreateAccountRequest {
    private BigDecimal initialBalance;
}
