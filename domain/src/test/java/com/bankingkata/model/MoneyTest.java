package com.bankingkata.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

import com.bankingkata.exception.InvalidAmountException;

public class MoneyTest {
    @Test
    void should_create_money_with_valid_amount() {
        Money money = new Money(100.0);
        assertThat(money.amount()).isEqualTo(100.0);
    }

    @Test
    void should_throw_exception_when_amount_is_negative() {
        assertThatThrownBy(() -> new Money(-10.0))
            .isInstanceOf(InvalidAmountException.class)
            .hasMessage("Amount cannot be negative");
    }

    @Test
    void should_add_two_money_amounts() {
        Money money1 = new Money(100.0);
        Money money2 = new Money(50.0);

        Money result = money1.add(money2);
        assertThat(result.amount()).isEqualTo(150.0);
    }

    @Test
    void should_subtract_two_money_amounts() {
        Money money1 = new Money(100.0);
        Money money2 = new Money(50.0);

        Money result = money1.subtract(money2);
        assertThat(result.amount()).isEqualTo(50.0);
    }  
    
    @Test
    void should_throw_exception_when_subtracting_more_than_available() {
        Money money1 = new Money(50.0);
        Money money2 = new Money(100.0);

        assertThatThrownBy(() -> money1.subtract(money2))
            .isInstanceOf(InvalidAmountException.class)
            .hasMessage("Insufficient funds");
    }
}

