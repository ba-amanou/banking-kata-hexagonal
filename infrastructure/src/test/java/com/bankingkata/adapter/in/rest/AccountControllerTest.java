package com.bankingkata.adapter.in.rest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.bankingkata.adapter.in.rest.request.AmountRequest;
import com.bankingkata.adapter.in.rest.request.CreateAccountRequest;
import com.bankingkata.adapter.in.rest.response.AccountResponse;
import com.bankingkata.adapter.in.rest.response.TransactionResponse;
import com.bankingkata.exception.AccountNotFoundException;
import com.bankingkata.exception.InvalidAmountException;
import com.bankingkata.model.Account;
import com.bankingkata.model.Money;
import com.bankingkata.model.Transaction;
import com.bankingkata.model.TransactionType;
import com.bankingkata.port.in.CreateAccountUseCase;
import com.bankingkata.port.in.DepositMoneyUseCase;
import com.bankingkata.port.in.GetAccountBalanceUseCase;
import com.bankingkata.port.in.GetTransactionHistoryUseCase;
import com.bankingkata.port.in.WithdrawMoneyUseCase;

import tools.jackson.databind.ObjectMapper;

@WebMvcTest(AccountController.class)
public class AccountControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CreateAccountUseCase createAccountUseCase;
    @MockitoBean
    private DepositMoneyUseCase depositMoneyUseCase;
    @MockitoBean
    private WithdrawMoneyUseCase withdrawMoneyUseCase;
    @MockitoBean
    private GetAccountBalanceUseCase getAccountBalanceUseCase;
    @MockitoBean
    private GetTransactionHistoryUseCase getTransactionHistoryUseCase;
    @MockitoBean
    private AccountMapper accountMapper;
    @MockitoBean
    private TransactionMapper transactionMapper;

    @Test
    void should_create_account_and_return_201() throws Exception {
        CreateAccountRequest request = new CreateAccountRequest();
        request.setInitialBalance(100.0);

        Account account = Account.create(new Money(100.0));
        AccountResponse response = new AccountResponse(account.getId(), 100.0);

        when(createAccountUseCase.createAccount(any())).thenReturn(account);
        when(accountMapper.toResponse(account)).thenReturn(response);

        mockMvc.perform(post("/accounts")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.balance").value(100.0));
    }

    @Test
    void should_deposit_and_return_account_with_200() throws Exception {
        AmountRequest request = new AmountRequest();
        request.setAmount(100.0);

        Account account = Account.create(new Money(100.0));
        AccountResponse response = new AccountResponse(account.getId(), account.getBalance().amount());

        when(depositMoneyUseCase.deposit(any(),any())).thenReturn(account);
        when(accountMapper.toResponse(account)).thenReturn(response);

        mockMvc.perform(post("/accounts/1/deposit")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.balance").value(100.0));

    }

    @Test
    void should_throw_exception_if_account_not_found_when_depositing() throws Exception {
        AmountRequest request = new AmountRequest();
        request.setAmount(100.0);

        when(depositMoneyUseCase.deposit(any(),any())).thenThrow(new AccountNotFoundException("1"));

        mockMvc.perform(post("/accounts/1/deposit")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.status").value(404))
        .andExpect(jsonPath("$.message").value("Account not found with id: 1"))
        .andExpect(jsonPath("$.timestamp").exists());
    }


    @Test
    void should_withdraw_and_return_account_with_200() throws Exception {
        AmountRequest request = new AmountRequest();
        request.setAmount(100.0);

        Account account = Account.create(new Money(100.0));
        AccountResponse response = new AccountResponse(account.getId(), account.getBalance().amount());

        when(withdrawMoneyUseCase.withdraw(any(),any())).thenReturn(account);
        when(accountMapper.toResponse(account)).thenReturn(response);

        mockMvc.perform(post("/accounts/1/withdraw")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.balance").value(100.0));
    }

    @Test
    void should_throw_exception_if_account_not_found_when_withdrawing() throws Exception {
        AmountRequest request = new AmountRequest();
        request.setAmount(100.0);

        when(withdrawMoneyUseCase.withdraw(any(),any())).thenThrow(new AccountNotFoundException("1"));

        mockMvc.perform(post("/accounts/1/withdraw")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.status").value(404))
        .andExpect(jsonPath("$.message").value("Account not found with id: 1"))
        .andExpect(jsonPath("$.timestamp").exists());
    }
    
    @Test
    void should_throw_exception_when_withdrawing_more_than_available() throws Exception {
        AmountRequest request = new AmountRequest();
        request.setAmount(100.0);

        when(withdrawMoneyUseCase.withdraw(any(),any())).thenThrow(new InvalidAmountException("Insufficient funds"));

        mockMvc.perform(post("/accounts/1/withdraw")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.status").value(400))
        .andExpect(jsonPath("$.message").value("Insufficient funds"))
        .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void should_return_balance() throws Exception {
        when(getAccountBalanceUseCase.getBalance(any())).thenReturn(new Money(100.0));

        mockMvc.perform(get("/accounts/1/balance")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.balance").value(100.0));
    }

    @Test
    void should_throw_exception_if_account_not_found_when_getting_balance() throws Exception {
        when(getAccountBalanceUseCase.getBalance(any())).thenThrow(new AccountNotFoundException("1"));

        mockMvc.perform(get("/accounts/1/balance")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.status").value(404))
        .andExpect(jsonPath("$.message").value("Account not found with id: 1"))
        .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void should_return_transaction_history() throws Exception{
        Account account1 = Account.create(new Money(100.0));
        List<Transaction> transactions = List.of(
            Transaction.deposit(account1.getId(), new Money(10.0)),
            Transaction.withdrawal(account1.getId(), new Money(20.0))
        );
        when(getTransactionHistoryUseCase.history(any())).thenReturn(transactions);
        when(transactionMapper.toResponse(transactions.get(0))).thenReturn(
            new TransactionResponse("id-1", account1.getId(), 10.0, TransactionType.DEPOSIT, LocalDateTime.now())
        );
        when(transactionMapper.toResponse(transactions.get(1))).thenReturn(
            new TransactionResponse("id-2", account1.getId(), 20.0, TransactionType.WITHDRAWAL, LocalDateTime.now())
        );
        
        mockMvc.perform(get("/accounts/1/history")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value(2))
        .andExpect(jsonPath("$[0].id").exists())
        .andExpect(jsonPath("$[0].accountId").value(account1.getId()))
        .andExpect(jsonPath("$[0].type").value(TransactionType.DEPOSIT.name()))
        .andExpect(jsonPath("$[0].amount").value(10.0))
        .andExpect(jsonPath("$[0].date").exists())
        .andExpect(jsonPath("$[0].id").exists())
        .andExpect(jsonPath("$[1].accountId").value(account1.getId()))
        .andExpect(jsonPath("$[1].type").value(TransactionType.WITHDRAWAL.name()))
        .andExpect(jsonPath("$[1].amount").value(20.0))
        .andExpect(jsonPath("$[1].date").exists());
    }
    
    
    
    
}
