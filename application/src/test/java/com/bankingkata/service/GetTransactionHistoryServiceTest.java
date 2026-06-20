package com.bankingkata.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.bankingkata.model.Account;
import com.bankingkata.model.Money;
import com.bankingkata.model.Transaction;
import com.bankingkata.port.out.LoadTransactionPort;

@ExtendWith(MockitoExtension.class)
public class GetTransactionHistoryServiceTest {
    
    @Mock
    private LoadTransactionPort loadTransactionPort;

    @InjectMocks
    private GetTransactionHistoryService getTransactionHistoryService;
    
    @Test
    void should_return_transaction_history() {
        Account account1 = Account.create(Money.of("100.00"));
        List<Transaction> transactions = List.of(
            Transaction.deposit(account1.getId(), Money.of("10.00")),
            Transaction.deposit(account1.getId(), Money.of("20.00"))
        );

        when(loadTransactionPort.loadByAccountId(account1.getId())).thenReturn(transactions);

        List<Transaction> result = getTransactionHistoryService.history(account1.getId());

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getAmount()).isEqualTo(Money.of("10.00"));
        assertThat(result.get(1).getAmount()).isEqualTo(Money.of("20.00"));
    }

    @Test
    void should_return_empty_list_when_account_not_exist() {
        when(loadTransactionPort.loadByAccountId("unknown")).thenReturn(Collections.emptyList());

        List<Transaction> result = getTransactionHistoryService.history("unknown");

        assertThat(result).isEmpty();
    }
}
