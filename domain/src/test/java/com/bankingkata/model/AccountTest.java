package com.bankingkata.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


import org.junit.jupiter.api.Test;

import com.bankingkata.exception.InvalidAmountException;

public class AccountTest {

    @Test
    void should_create_account_with_balance() {
        Account account1 = Account.create(Money.of("100.00"));
        assertThat(account1.getId()).isNotNull();
        assertThat(account1.getBalance()).isEqualTo(Money.of("100.00"));
    }

    @Test
    void should_be_able_to_deposit(){
        Account account1 = Account.create(Money.of("100.00"));
        account1.deposit(Money.of("100.00"));
        assertThat(account1.getBalance()).isEqualTo(Money.of("200.00"));
    }

    @Test
    void should_be_able_to_withdraw(){
        Account account1 = Account.create(Money.of("100.00"));
        account1.withdraw(Money.of("50.00"));
        assertThat(account1.getBalance()).isEqualTo(Money.of("50.00"));
    }   
    
    @Test
    void should_throw_exception_when_withdrawing_more_than_available() {
        Account account1 = Account.create(Money.of("100.00"));
        assertThatThrownBy(() -> account1.withdraw(Money.of("200.00")))
            .isInstanceOf(InvalidAmountException.class)
            .hasMessage("Insufficient funds");
    }
}
