package com.bankingkata.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

import com.bankingkata.exception.InvalidAmountException;

public class MoneyTest {
    @Test
    void should_create_money_with_valid_amount() {
        Money money = Money.of("100.00");
        assertThat(money.amount()).isEqualByComparingTo("100.00");
    }

    @Test
    void should_throw_exception_when_amount_is_negative() {
        assertThatThrownBy(() -> Money.of("-10.00"))
            .isInstanceOf(InvalidAmountException.class)
            .hasMessage("Amount cannot be negative");
    }

    @Test
    void should_add_two_money_amounts() {
        Money money1 = Money.of("100.00");
        Money money2 = Money.of("50.00");

        Money result = money1.add(money2);
        assertThat(result.amount()).isEqualByComparingTo("150.00");
    }

    @Test
    void should_subtract_two_money_amounts() {
        Money money1 = Money.of("100.00");
        Money money2 = Money.of("50.00");

        Money result = money1.subtract(money2);
        assertThat(result.amount()).isEqualByComparingTo("50.00");
    }  
    
    @Test
    void should_throw_exception_when_subtracting_more_than_available() {
        Money money1 = Money.of("50.00");
        Money money2 = Money.of("100.00");

        assertThatThrownBy(() -> money1.subtract(money2))
            .isInstanceOf(InvalidAmountException.class)
            .hasMessage("Insufficient funds");
    }

    @Test
    void should_treat_equal_values_as_equal_regardless_of_trailing_zero_scale() {
        Money money1 = Money.of("100");
        Money money2 = Money.of("100.0");
        Money money3 = Money.of("100.00");

        assertThat(money1).isEqualTo(money2).isEqualTo(money3);
    }

    @Test
    void should_treat_different_values_as_not_equal() {
        Money money1 = Money.of("100.00");
        Money money2 = Money.of("100.01");
        assertThat(money1).isNotEqualTo(money2);
    }

    @Test
    void should_accept_zero_amount() {
        Money money = Money.of("0.00");
        assertThat(money.amount()).isEqualByComparingTo(BigDecimal.ZERO);
    }

    @Test
    void should_not_throw_when_subtracting_exact_available_amount() {
        Money money1 = Money.of("100.00");
        Money result = money1.subtract(Money.of("100.00"));
        assertThat(result.amount()).isEqualByComparingTo(BigDecimal.ZERO);
    }

    @Test
    void should_throw_exception_when_too_many_decimal_places() {

        assertThatThrownBy(() -> Money.of("50.123"))
            .isInstanceOf(InvalidAmountException.class)
            .hasMessage("Amount cannot have more than 2 decimal places");
    }
}

