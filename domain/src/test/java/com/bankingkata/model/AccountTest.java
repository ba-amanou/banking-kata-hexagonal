package com.bankingkata.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

import com.bankingkata.exception.InsufficientFundsException;
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
            .isInstanceOf(InsufficientFundsException.class)
            .hasMessage("Insufficient funds");
    }

    @Test
    void should_throw_exception_when_depositing_zero() {
        Account account1 = Account.create(Money.of("100.00"));
        assertThatThrownBy(() -> account1.deposit(Money.of("0.00")))
            .isInstanceOf(InvalidAmountException.class)
            .hasMessage("Amount must be greater than zero");
    }

    @Test
    void should_throw_exception_when_withdrawing_zero() {
        Account account1 = Account.create(Money.of("100.00"));
        assertThatThrownBy(() -> account1.withdraw(Money.of("0.00")))
            .isInstanceOf(InvalidAmountException.class)
            .hasMessage("Amount must be greater than zero");
    }

    @Test
    void should_throw_exception_when_depositing_null() {
        Account account1 = Account.create(Money.of("100.00"));
        assertThatThrownBy(() -> account1.deposit(null))
            .isInstanceOf(InvalidAmountException.class)
            .hasMessage("Amount cannot be null");
    }

    @Test
    void should_throw_exception_when_withdrawing_null() {
        Account account1 = Account.create(Money.of("100.00"));
        assertThatThrownBy(() -> account1.withdraw(null))
            .isInstanceOf(InvalidAmountException.class)
            .hasMessage("Amount cannot be null");
    }

    @Test
    void should_throw_exception_when_creating_account_with_null_balance() {
        assertThatThrownBy(() -> Account.create(null))
            .isInstanceOf(InvalidAmountException.class)
            .hasMessage("Balance cannot be null");
    }

    @Test
    void should_throw_exception_when_reconstituting_with_null_balance() {
        assertThatThrownBy(() -> Account.reconstitute("id-1", null, null))
            .isInstanceOf(InvalidAmountException.class)
            .hasMessage("Balance cannot be null");
    }

    @Test
    void should_throw_exception_when_reconstituting_with_null_id() {
        assertThatThrownBy(() -> Account.reconstitute(null, Money.of("100.00"), 0L))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Account id cannot be null or blank");
    }

    @Test
    void should_throw_exception_when_reconstituting_with_blank_id() {
        assertThatThrownBy(() -> Account.reconstitute("   ", Money.of("100.00"), 0L))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Account id cannot be null or blank");
    }

    @Test
    void should_be_equal_when_same_id_even_with_different_balance() {
        Account account1 = Account.reconstitute("same-id", Money.of("100.00"), 0L);
        Account account2 = Account.reconstitute("same-id", Money.of("999.00"), 1L);
        assertThat(account1).isEqualTo(account2);
    }

    @Test
    void should_not_be_equal_when_different_id() {
        Account account1 = Account.reconstitute("id-1", Money.of("100.00"), 0L);
        Account account2 = Account.reconstitute("id-2", Money.of("100.00"), 0L);
        assertThat(account1).isNotEqualTo(account2);
    }

    @Test
    void should_have_null_version_when_creating_a_new_account() {
        Account account = Account.create(Money.of("100.00"));
        assertThat(account.getVersion()).isNull();
    }
 
    @Test
    void should_carry_the_given_version_when_reconstituting() {
        Account account = Account.reconstitute("id-1", Money.of("100.00"), 4L);
        assertThat(account.getVersion()).isEqualTo(4L);
    }

}
