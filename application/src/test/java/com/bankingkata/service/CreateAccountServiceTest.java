package com.bankingkata.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.bankingkata.model.Account;
import com.bankingkata.model.Money;
import com.bankingkata.port.out.SaveAccountPort;

@ExtendWith(MockitoExtension.class)
public class CreateAccountServiceTest {

    @Mock 
    SaveAccountPort saveAccountPort;
    @InjectMocks
    CreateAccountService createAccountService;

    @Test
    void should_create_account_with_initial_balance() {
        Money initialBalance = new Money(100.0);

        Account result = createAccountService.createAccount(initialBalance);

        assertThat(result.getId()).isNotNull();
        assertThat(result.getBalance()).isEqualTo(initialBalance);
        verify(saveAccountPort).save(result);
    }
}
