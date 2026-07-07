package com.bankingkata.e2e;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.client.RestTestClient;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;

import com.bankingkata.adapter.in.rest.request.CreateAccountRequest;
import com.bankingkata.adapter.in.rest.response.AccountResponse;
import com.bankingkata.adapter.in.rest.response.BalanceResponse;
import com.bankingkata.adapter.out.persistence.TransactionJpaRepository;
import com.bankingkata.model.Money;
import com.bankingkata.port.in.DepositMoneyUseCase;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureRestTestClient
@Testcontainers
public class DepositAtomicityTest {
    
    @Container
    static PostgreSQLContainer postgres = new PostgreSQLContainer("postgres:15");

    @Autowired
    private RestTestClient restTestClient;

    @Autowired
    private DepositMoneyUseCase depositMoneyUseCase;

    @MockitoBean
    private TransactionJpaRepository transactionJpaRepository;

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Test
    void should_rollback_account_balance_when_transaction_history_save_fails() {
        CreateAccountRequest createRequest = new CreateAccountRequest();
        createRequest.setInitialBalance(new BigDecimal("100.00"));

        String accountId = restTestClient.post()
            .uri("/accounts")
            .body(createRequest)
            .exchange()
            .expectStatus().isCreated()
            .returnResult(AccountResponse.class)
            .getResponseBody()
            .getId();

        when(transactionJpaRepository.save(any()))
            .thenThrow(new RuntimeException("simulated history persistence failure"));

        assertThatThrownBy(() ->
            depositMoneyUseCase.deposit(accountId, Money.of("50.00"))
        ).isInstanceOf(RuntimeException.class);

        BalanceResponse balanceAfterFailedDeposit = restTestClient.get()
            .uri("/accounts/{id}/balance", accountId)
            .exchange()
            .expectStatus().isOk()
            .returnResult(BalanceResponse.class)
            .getResponseBody();

        assertThat(balanceAfterFailedDeposit.getBalance())
            .as("account balance must not be credited if the transaction history was not persisted")
            .isEqualByComparingTo("100.00");
    }
}
