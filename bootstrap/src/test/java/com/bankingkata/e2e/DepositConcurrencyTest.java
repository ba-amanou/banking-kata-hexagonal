package com.bankingkata.e2e;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doAnswer;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

import com.bankingkata.adapter.in.rest.response.BalanceResponse;
import com.bankingkata.exception.ConcurrentAccountModificationException;
import com.bankingkata.model.Account;
import com.bankingkata.model.Money;
import com.bankingkata.port.in.DepositMoneyUseCase;
import com.bankingkata.port.out.LoadAccountPort;

public class DepositConcurrencyTest extends AbstractPostgresE2ETest {
    
    @Autowired
    private DepositMoneyUseCase depositMoneyUseCase;

    @MockitoSpyBean
    private LoadAccountPort loadAccountPort;

    private AccountApiFixtures fixtures;

    @BeforeEach
    void setUp() {
        fixtures = new AccountApiFixtures(restTestClient);
    }

    @Test
    void two_concurrent_deposits_must_not_silently_lose_one_of_them() throws Exception {
        String accountId = fixtures.createAccountBalance("0.00");

        CyclicBarrier bothHaveRead = new CyclicBarrier(2);
        AtomicInteger loadCallCount = new AtomicInteger();

        doAnswer(invocation -> {
            Object result = invocation.callRealMethod();
            // let the third call (get balance) through without waiting
            if(loadCallCount.incrementAndGet() <= 2) {
                bothHaveRead.await(5, TimeUnit.SECONDS);
            }
            return result;
        }).when(loadAccountPort).load(accountId);

        ExecutorService pool = Executors.newFixedThreadPool(2);
        Callable<Account> deposit = () -> depositMoneyUseCase.deposit(accountId, Money.of("10.00"));

        List<Future<Account>> futures = List.of(
            pool.submit(deposit), pool.submit(deposit)
        );

        int succeeded = 0;
        int conflicted = 0;
        for(Future<Account> future : futures) {
            try {
                future.get(10, TimeUnit.SECONDS);
                succeeded++;
            } catch (ExecutionException e) {
                assertThat(e.getCause()).isInstanceOf(ConcurrentAccountModificationException.class);
                conflicted++;
            }
        }
        pool.shutdown();

        assertThat(succeeded).isEqualTo(1);
        assertThat(conflicted).isEqualTo(1);

        BalanceResponse balance = restTestClient.get()
            .uri("/accounts/{id}/balance", accountId)
            .exchange()
            .expectStatus().isOk()
            .returnResult(BalanceResponse.class)
            .getResponseBody();

        // Sanity check only: a lost update never sums writes, so this alone 
        // wouldn't catch it. Succeeded/conflicted above are what proves the fix
        assertThat(balance.getBalance()).isEqualByComparingTo("10.00");

    }
    
}
