package com.bankingkata.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.bankingkata.exception.AccountNotFoundException;
import com.bankingkata.model.Account;
import com.bankingkata.model.Money;
import com.bankingkata.port.out.LoadAccountPort;

@ExtendWith(MockitoExtension.class)
public class GetAccountBalanceServiceTest {
    
    @Mock
    private LoadAccountPort loadAccountPort;
    @InjectMocks
    private GetAccountBalanceService getAccountBalanceService;

    @Test
    void should_be_able_to_get_balance_account() {
        Money amount = new Money(100.0);
        Account account1 = new Account(amount);
        when(loadAccountPort.load("1")).thenReturn(account1);

        Money result = getAccountBalanceService.getBalance("1");

        assertThat(result).isEqualTo(amount);
    }

    @Test
    void should_throw_exception_when_account_not_found() {
        when(loadAccountPort.load("404")).thenReturn(null);

        assertThatThrownBy(() -> getAccountBalanceService.getBalance("404"))
            .isInstanceOf(AccountNotFoundException.class);
    }      

}
