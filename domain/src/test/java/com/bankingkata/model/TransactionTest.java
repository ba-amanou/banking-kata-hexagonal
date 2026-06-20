package com.bankingkata.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import com.bankingkata.exception.InvalidAmountException;

public class TransactionTest {

    @Test
    void should_create_deposit_transaction() {
        Transaction transaction = Transaction.deposit("accountId",Money.of("100.00"));

        assertThat(transaction.getId()).isNotNull();
        assertThat(transaction.getAccountId()).isEqualTo("accountId");
        assertThat(transaction.getAmount()).isEqualTo(Money.of("100.00"));
        assertThat(transaction.getType()).isEqualTo(TransactionType.DEPOSIT);
        assertThat(transaction.getDate()).isNotNull();
    }

    @Test
    void should_create_withdrawal_transaction() {
        Transaction transaction = Transaction.withdrawal("accountId",Money.of("50.00"));

        assertThat(transaction.getId()).isNotNull();
        assertThat(transaction.getAccountId()).isEqualTo("accountId");
        assertThat(transaction.getAmount()).isEqualTo(Money.of("50.00"));
        assertThat(transaction.getType()).isEqualTo(TransactionType.WITHDRAWAL);
        assertThat(transaction.getDate()).isNotNull();
    }    

    @Test
    void should_not_be_able_to_build_transaction_with_invalid_money() {
        LocalDateTime time = LocalDateTime.now();
            
        assertThatThrownBy(() -> {
            Transaction.builder()
            .id("1")
            .amount(Money.of("-100.00"))
            .date(time)
            .build();
        }).isInstanceOf(InvalidAmountException.class)
        .hasMessage("Amount cannot be negative");
    }
}
