package com.bankingkata.adapter.in.rest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.bankingkata.adapter.in.rest.exception.InvalidAccountIdException;
import com.bankingkata.adapter.in.rest.request.AmountRequest;
import com.bankingkata.adapter.in.rest.request.CreateAccountRequest;
import com.bankingkata.adapter.in.rest.response.AccountResponse;
import com.bankingkata.adapter.in.rest.response.BalanceResponse;
import com.bankingkata.adapter.in.rest.response.TransactionResponse;
import com.bankingkata.model.Account;
import com.bankingkata.model.Money;
import com.bankingkata.model.Transaction;
import com.bankingkata.port.in.CreateAccountUseCase;
import com.bankingkata.port.in.DepositMoneyUseCase;
import com.bankingkata.port.in.GetAccountBalanceUseCase;
import com.bankingkata.port.in.GetTransactionHistoryUseCase;
import com.bankingkata.port.in.WithdrawMoneyUseCase;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/accounts")
@AllArgsConstructor
public class AccountController {
    
    private final CreateAccountUseCase createAccountUseCase;
    private final DepositMoneyUseCase depositMoneyUseCase;
    private final WithdrawMoneyUseCase withdrawMoneyUseCase;
    private final GetAccountBalanceUseCase getAccountBalanceUseCase;
    private final GetTransactionHistoryUseCase getTransactionHistoryUseCase;
    private final AccountMapper accountMapper;
    private final TransactionMapper transactionMapper;

    @Operation(summary = "Create a new account")
    @ApiResponse(responseCode = "201", description = "Account created successfully")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AccountResponse createAccount(@Valid @RequestBody CreateAccountRequest request) {
        Money initialBalance = new Money(request.getInitialBalance());

        Account account = createAccountUseCase.createAccount(initialBalance);

        AccountResponse response = accountMapper.toResponse(account);
        return response;
    }

    @Operation(summary = "Deposit money to an account")
    @ApiResponse(responseCode = "200", description = "Deposit successful")
    @ApiResponse(responseCode = "404", description = "Account not found")
    @PostMapping("/{id}/deposit")
    public AccountResponse deposit(@PathVariable("id") String id, @Valid @RequestBody AmountRequest request) {
        validateId(id);
        Money amount = new Money(request.getAmount());

        Account account = depositMoneyUseCase.deposit(id, amount);

        AccountResponse response = accountMapper.toResponse(account);
        return response;
    }
    
    @Operation(summary = "Withdraw money from an account")
    @ApiResponse(responseCode = "200", description = "Withdrawal successful")
    @ApiResponse(responseCode = "404", description = "Account not found")
    @ApiResponse(responseCode = "400", description = "Insufficient funds")    
    @PostMapping("/{id}/withdraw")
    public AccountResponse withdraw(@PathVariable("id") String id, @Valid @RequestBody AmountRequest request) {
        validateId(id);
        Money amount = new Money(request.getAmount());

        Account account = withdrawMoneyUseCase.withdraw(id, amount);
        
        AccountResponse response = accountMapper.toResponse(account);
        return response;
    }

    @Operation(summary = "Get account balance")
    @ApiResponse(responseCode = "200", description = "Balance retrieved successfully")
    @ApiResponse(responseCode = "404", description = "Account not found")
    @GetMapping("/{id}/balance")
    public BalanceResponse balance(@PathVariable("id") String id) {
        validateId(id);
        Money balance = getAccountBalanceUseCase.getBalance(id);
        
        return new BalanceResponse(balance.amount());
    }

    @Operation(summary = "Get account transaction history")
    @ApiResponse(responseCode = "200", description = "Transaction history retrieved successfully")
    @ApiResponse(responseCode = "404", description = "Account not found")
    @GetMapping("/{id}/history")
    public List<TransactionResponse> history(@PathVariable("id") String id) {
        validateId(id);
        List<Transaction> transactions = getTransactionHistoryUseCase.history(id);
        
        return transactions.stream().map(transactionMapper::toResponse).toList();
    }

    private void validateId(String id) {
        try {
            UUID.fromString(id);
        } catch (IllegalArgumentException e) {
            throw new InvalidAccountIdException(id);
        }
    }

}
