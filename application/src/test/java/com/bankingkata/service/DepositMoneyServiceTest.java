package com.bankingkata.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.bankingkata.exception.AccountNotFoundException;
import com.bankingkata.model.Account;
import com.bankingkata.model.Money;
import com.bankingkata.model.TransactionType;
import com.bankingkata.port.out.LoadAccountPort;
import com.bankingkata.port.out.SaveAccountPort;
import com.bankingkata.port.out.SaveTransactionPort;

@ExtendWith(MockitoExtension.class)
public class DepositMoneyServiceTest {

    @Mock
    private SaveAccountPort saveAccountPort;
    @Mock
    private LoadAccountPort loadAccountPort;
    @Mock
    private SaveTransactionPort saveTransactionPort;
    @InjectMocks
    private DepositMoneyService depositMoneyService;

    @Test
    void should_be_able_to_deposit_money() {
        Money amount = new Money(100.0);
        Account account1 = new Account(amount);
        when(loadAccountPort.load("1")).thenReturn(account1);

        depositMoneyService.deposit("1", amount);

        assertThat(account1.getBalance()).isEqualTo(new Money(200.0));
        verify(saveAccountPort).save(account1);
    }

    @Test
    void should_save_transaction_with_correct_data_when_deposit() {
        Money amount = new Money(100.0);
        Account account1 = new Account(amount);
        when(loadAccountPort.load("1")).thenReturn(account1);

        depositMoneyService.deposit("1", amount);

        verify(saveTransactionPort).save(argThat(tx ->
            tx.getAccountId().equals("1") &&
            tx.getType() == TransactionType.DEPOSIT &&
            tx.getAmount().equals(amount)
        ));
    }

    @Test
    void should_throw_exception_when_account_not_found() {
        when(loadAccountPort.load("404")).thenReturn(null);

        assertThatThrownBy(() -> depositMoneyService.deposit("404", new Money(100.0)))
            .isInstanceOf(AccountNotFoundException.class);
    }

    @Test
    void should_not_save_anything_when_account_not_found() {
        when(loadAccountPort.load("404")).thenReturn(null);

        assertThatThrownBy(() -> depositMoneyService.deposit("404", new Money(100.0)))
            .isInstanceOf(AccountNotFoundException.class);
            
        verifyNoInteractions(saveAccountPort, saveTransactionPort);
    }
}
