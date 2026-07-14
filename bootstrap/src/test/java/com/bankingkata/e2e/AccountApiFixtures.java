package com.bankingkata.e2e;

import java.math.BigDecimal;

import org.springframework.test.web.servlet.client.RestTestClient;

import com.bankingkata.adapter.in.rest.request.CreateAccountRequest;
import com.bankingkata.adapter.in.rest.response.AccountResponse;

public class AccountApiFixtures {
    private final RestTestClient client;

    public AccountApiFixtures(RestTestClient client) {
        this.client = client;
    }

    String createAccountBalance(String initialBalance) {
        CreateAccountRequest request = new CreateAccountRequest();
        request.setInitialBalance(new BigDecimal(initialBalance));

        return client.post()
            .uri("/accounts")
            .body(request)
            .exchange()
            .expectStatus().isCreated()
            .returnResult(AccountResponse.class)
            .getResponseBody()
            .getId();
    }
    
}
