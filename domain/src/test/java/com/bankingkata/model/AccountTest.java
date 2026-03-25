package com.bankingkata.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

import com.bankingkata.exception.InvalidAmountException;

public class AccountTest {

    @Test
    void should_create_account_with_balance() {
        Account account1 = new Account(new Money(100.0));
        assertThat(account1.getId()).isNotNull();
        assertThat(account1.getBalance()).isEqualTo(new Money(100.0));
    }

    @Test
    void should_be_able_to_deposit(){
        Account account1 = new Account(new Money(100.0));
        account1.deposit(new Money(100.0));
        assertThat(account1.getBalance()).isEqualTo(new Money(200.0));
    }

    @Test
    void should_be_able_to_withdraw(){
        Account account1 = new Account(new Money(100.0));
        account1.withdraw(new Money(50.0));
        assertThat(account1.getBalance()).isEqualTo(new Money(50.0));
    }   
    
    @Test
    void should_throw_exception_when_withdrawing_more_than_available() {
        Account account1 = new Account(new Money(100.0));
        assertThatThrownBy(() -> account1.withdraw(new Money(200.0)))
            .isInstanceOf(InvalidAmountException.class)
            .hasMessage("Insufficient funds");
    }

    @Test
    void deposit_should_create_transaction() {
        Account account1 = new Account(new Money(100.0));
        account1.deposit(new Money(100.0));
        assertThat(account1.getTransactions().size()).isEqualTo(1);
        assertThat(account1.getTransactions().get(0).getAmount()).isEqualTo(new Money(100.0));
    }

    @Test 
    void withdraw_should_create_transaction() {
        Account account1 = new Account(new Money(100.0));
        account1.withdraw(new Money(50.0));
        assertThat(account1.getTransactions().size()).isEqualTo(1);
        assertThat(account1.getTransactions().get(0).getAmount()).isEqualTo(new Money(50.0)); 
    }

    @Test 
    void should_be_able_to_have_transaction_history(){
        Account account1 = new Account(new Money(100.0));
        account1.deposit(new Money(100.0));
        account1.withdraw(new Money(50.0));
        account1.deposit(new Money(100.0));
        assertThat(account1.getTransactions().size()).isEqualTo(3);
        assertThat(account1.getTransactions().get(0).getAmount()).isEqualTo(new Money(100.0));
        assertThat(account1.getTransactions().get(1).getAmount()).isEqualTo(new Money(50.0));
        assertThat(account1.getTransactions().get(2).getAmount()).isEqualTo(new Money(100.0));
    }
}
