package com.bankingkata.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import com.bankingkata.exception.InvalidAmountException;

public class TransactionTest {

    @Test
    void should_create_deposit_transaction() {
        Transaction transaction = Transaction.deposit("accountId",new Money(100.0));

        assertThat(transaction.getId()).isNotNull();
        assertThat(transaction.getAmount()).isEqualTo(new Money(100.0));
        assertThat(transaction.getType()).isEqualTo(TransactionType.DEPOSIT);
        assertThat(transaction.getDate()).isNotNull();
    }

    @Test
    void should_create_withdrawal_transaction() {
        Transaction transaction = Transaction.withdrawal("accountId",new Money(50.0));

        assertThat(transaction.getId()).isNotNull();
        assertThat(transaction.getAmount()).isEqualTo(new Money(50.0));
        assertThat(transaction.getType()).isEqualTo(TransactionType.WITHDRAWAL);
        assertThat(transaction.getDate()).isNotNull();
    }    

    @Test
    void should_throw_exception_when_amount_invalid() {
        LocalDateTime time = LocalDateTime.now();
            
        assertThatThrownBy(() -> {
            Transaction.builder()
            .id("1")
            .amount(new Money(-100.0))
            .date(time)
            .build();
        }).isInstanceOf(InvalidAmountException.class)
        .hasMessage("Amount cannot be negative");
    }
}
