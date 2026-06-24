package com.bankingkata.e2e;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.client.RestTestClient;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;

import com.bankingkata.adapter.in.rest.request.AmountRequest;
import com.bankingkata.adapter.in.rest.request.CreateAccountRequest;
import com.bankingkata.adapter.in.rest.response.AccountResponse;
import com.bankingkata.model.TransactionType;

import org.testcontainers.junit.jupiter.Container;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureRestTestClient
@Testcontainers
public class AccountE2ETest {

    @Container
    static PostgreSQLContainer postgres = new PostgreSQLContainer("postgres:15");

    @Autowired
    private RestTestClient restTestClient;

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);

    }

    @Test
    void should_create_account_and_return_initial_balance() {
        CreateAccountRequest request = new CreateAccountRequest();
        request.setInitialBalance(new BigDecimal("100.00"));

        AccountResponse response = restTestClient.post()
            .uri("/accounts")
            .body(request)
            .exchange()
            .expectStatus().isCreated()
            .returnResult(AccountResponse.class)
            .getResponseBody();

        assertThat(response.getId()).isNotNull();
        assertThat(response.getBalance()).isEqualByComparingTo("100.00");

    }

    @Test
    void should_complete_full_account_lifecycle() {
        CreateAccountRequest createRequest = new CreateAccountRequest();
        createRequest.setInitialBalance(new BigDecimal("100.00"));

        AccountResponse createdAccount = restTestClient.post()
            .uri("/accounts")
            .body(createRequest)
            .exchange()
            .expectStatus().isCreated()
            .returnResult(AccountResponse.class)
            .getResponseBody();

        String accountId = createdAccount.getId();

        AmountRequest depositRequest = new AmountRequest();
        depositRequest.setAmount(new BigDecimal("50.00"));

        AccountResponse afterDeposit = restTestClient.post()
            .uri("/accounts/{id}/deposit", accountId)
            .body(depositRequest)
            .exchange()
            .expectStatus().isOk()
            .returnResult(AccountResponse.class)
            .getResponseBody();

        assertThat(afterDeposit.getBalance()).isEqualByComparingTo("150.00");

        AmountRequest withdrawRequest = new AmountRequest();
        withdrawRequest.setAmount(new BigDecimal("25.00"));

        AccountResponse afterWithdraw = restTestClient.post()
            .uri("/accounts/{id}/withdraw", accountId)
            .body(withdrawRequest)
            .exchange()
            .expectStatus().isOk()
            .returnResult(AccountResponse.class)
            .getResponseBody();

        assertThat(afterWithdraw.getBalance()).isEqualByComparingTo("125.00");


        AccountResponse currentBalance = restTestClient.get()
            .uri("/accounts/{id}/balance", accountId)
            .exchange()
            .expectStatus().isOk()
            .returnResult(AccountResponse.class)
            .getResponseBody();

        assertThat(currentBalance.getBalance()).isEqualByComparingTo("125.00");            
            
        restTestClient.get()
            .uri("/accounts/{id}/history", accountId)
            .exchange()
            .expectStatus().isOk()
            .expectBody()
            .jsonPath("$.length()").isEqualTo(2)
            .jsonPath("$[0].type").isEqualTo(TransactionType.DEPOSIT.name())
            .jsonPath("$[1].type").isEqualTo(TransactionType.WITHDRAWAL.name());
    }

    @Test
    void should_return_404_when_account_not_found() {
        String IdNotFound = UUID.randomUUID().toString();
        AmountRequest depositRequest = new AmountRequest();
        depositRequest.setAmount(new BigDecimal("50.00"));

        restTestClient.post()
            .uri("/accounts/" + IdNotFound + "/deposit")
            .body(depositRequest)
            .exchange()
            .expectStatus().isNotFound();
    }

    @Test
    void should_return_400_when_insufficient_funds() {
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

        AmountRequest withdrawRequest = new AmountRequest();
        withdrawRequest.setAmount(new BigDecimal("200.00"));

        restTestClient.post()
            .uri("/accounts/{id}/withdraw", accountId)
            .body(withdrawRequest)
            .exchange()
            .expectStatus().isBadRequest();
        
    }

}
