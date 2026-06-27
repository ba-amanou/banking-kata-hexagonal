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

    @Test
    void should_be_equal_when_same_id() {
        Transaction t1 = Transaction.builder().id("tx-1").accountId("acc-1")
            .amount(Money.of("100.00")).type(TransactionType.DEPOSIT).date(LocalDateTime.now()).build();
        Transaction t2 = Transaction.builder().id("tx-1").accountId("acc-2")
            .amount(Money.of("50.00")).type(TransactionType.WITHDRAWAL).date(LocalDateTime.now()).build();

        assertThat(t1).isEqualTo(t2);
    }

    @Test
    void should_not_be_equal_when_different_id_even_with_same_fields() {
        LocalDateTime time = LocalDateTime.now();
        Transaction t1 = Transaction.builder().id("tx-1").accountId("acc-1")
            .amount(Money.of("100.00")).type(TransactionType.DEPOSIT).date(time).build();
        Transaction t2 = Transaction.builder().id("tx-2").accountId("acc-1")
            .amount(Money.of("100.00")).type(TransactionType.DEPOSIT).date(time).build();

        assertThat(t1).isNotEqualTo(t2);
    }    
}
