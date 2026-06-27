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
    void should_throw_exception_when_subtract_result_is_negative() {
        Money money1 = Money.of("50.00");
        Money money2 = Money.of("100.00");

        assertThatThrownBy(() -> money1.subtract(money2))
            .isInstanceOf(InvalidAmountException.class)
            .hasMessage("Amount cannot be negative");
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

        assertThatThrownBy(() -> Money.of("50.1234"))
            .isInstanceOf(InvalidAmountException.class)
            .hasMessage("Amount cannot have more than 2 decimal places");
    }

    @Test
    void should_throw_exception_when_amount_has_trailing_zero_beyond_two_decimals() {
        assertThatThrownBy(() -> Money.of("10.100"))
            .isInstanceOf(InvalidAmountException.class)
            .hasMessage("Amount cannot have more than 2 decimal places");
    }     

    @Test
    void should_throw_exception_when_amount_is_null() {
        assertThatThrownBy(() -> new Money(null))
            .isInstanceOf(InvalidAmountException.class)
            .hasMessage("Amount cannot be null");
    }

    @Test
    void should_throw_exception_when_of_is_called_with_null() {
        assertThatThrownBy(() -> Money.of(null))
            .isInstanceOf(InvalidAmountException.class)
            .hasMessage("Amount cannot be null");
    }

    @Test
    void should_throw_exception_when_adding_null() {
        Money money = Money.of("100.00");
        assertThatThrownBy(() -> money.add(null))
            .isInstanceOf(InvalidAmountException.class)
            .hasMessage("Amount cannot be null");
    }

    @Test
    void should_throw_exception_when_subtracting_null() {
        Money money = Money.of("100.00");
        assertThatThrownBy(() -> money.subtract(null))
            .isInstanceOf(InvalidAmountException.class)
            .hasMessage("Amount cannot be null");
    }

    @Test
    void should_throw_exception_when_of_is_called_with_invalid_amount() {
        assertThatThrownBy(() -> Money.of("test"))
            .isInstanceOf(InvalidAmountException.class)
            .hasMessage("Invalid Amount");
    }

    @Test
    void should_return_true_when_amount_is_greater_than_other() {
        Money money1 = Money.of("100.00");
        Money money2 = Money.of("50.00");

        assertThat(money1.isGreaterThan(money2)).isTrue();
    }

    @Test
    void should_return_false_when_amount_is_not_greater_than_other() {
        Money money1 = Money.of("50.00");
        Money money2 = Money.of("100.00");

        assertThat(money1.isGreaterThan(money2)).isFalse();
    }
    
    @Test
    void should_return_false_when_amounts_are_equal() {
        Money money1 = Money.of("100.00");
        Money money2 = Money.of("100.00");

        assertThat(money1.isGreaterThan(money2)).isFalse();
    }

    @Test
    void should_return_true_when_amount_is_zero() {
        Money money = Money.of("0.00");

        assertThat(money.isZero()).isTrue();
    }

    @Test
    void should_return_false_when_amount_is_not_zero() {
        Money money = Money.of("0.01");

        assertThat(money.isZero()).isFalse();
    }
   
}

