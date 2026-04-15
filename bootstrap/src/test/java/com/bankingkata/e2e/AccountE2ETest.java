package com.bankingkata.e2e;

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
        request.setInitialBalance(100.0);

        restTestClient.post()
            .uri("/accounts")
            .body(request)
            .exchange()
            .expectStatus().isCreated()
            .expectBody()
            .jsonPath("$.balance").isEqualTo(100.0)
            .jsonPath("$.id").exists();

    }

    @Test
    void should_complete_full_account_lifecycle() {
        CreateAccountRequest createRequest = new CreateAccountRequest();
        createRequest.setInitialBalance(100.0);

        String accountId = restTestClient.post()
            .uri("/accounts")
            .body(createRequest)
            .exchange()
            .expectStatus().isCreated()
            .returnResult(AccountResponse.class)
            .getResponseBody()
            .getId();

        AmountRequest depositRequest = new AmountRequest();
        depositRequest.setAmount(50.0);

        restTestClient.post()
            .uri("/accounts/{id}/deposit", accountId)
            .body(depositRequest)
            .exchange()
            .expectStatus().isOk()
            .expectBody()
            .jsonPath("$.balance").isEqualTo(150.0);

        AmountRequest withdrawRequest = new AmountRequest();
        withdrawRequest.setAmount(25.0);

        restTestClient.post()
            .uri("/accounts/{id}/withdraw", accountId)
            .body(withdrawRequest)
            .exchange()
            .expectStatus().isOk()
            .expectBody()
            .jsonPath("$.balance").isEqualTo(125.0);

        restTestClient.get()
            .uri("/accounts/{id}/balance", accountId)
            .exchange()
            .expectStatus().isOk()
            .expectBody()
            .jsonPath("$.balance").isEqualTo(125.0);             
    }

    @Test
    void should_return_404_when_account_not_found() {
        AmountRequest depositRequest = new AmountRequest();
        depositRequest.setAmount(50.0);

        restTestClient.post()
            .uri("/accounts/unknownid/deposit")
            .body(depositRequest)
            .exchange()
            .expectStatus().isNotFound();
    }

    @Test
    void should_return_400_when_insufficient_funds() {
        CreateAccountRequest createRequest = new CreateAccountRequest();
        createRequest.setInitialBalance(100.0);

        String accountId = restTestClient.post()
            .uri("/accounts")
            .body(createRequest)
            .exchange()
            .expectStatus().isCreated()
            .returnResult(AccountResponse.class)
            .getResponseBody()
            .getId();

        AmountRequest withdrawRequest = new AmountRequest();
        withdrawRequest.setAmount(200.0);

        restTestClient.post()
            .uri("/accounts/{id}/withdraw", accountId)
            .body(withdrawRequest)
            .exchange()
            .expectStatus().isBadRequest();
        
    }

}
