package com.bankingkata.adapter.in.rest.request;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Data
public class CreateAccountRequest {
    @NotNull @PositiveOrZero private BigDecimal initialBalance;
}
