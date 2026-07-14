package com.bankingkata.adapter.out.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

import com.bankingkata.model.Account;
import com.bankingkata.model.Money;

@Import({AccountPersistenceAdapter.class, AccountPersistenceMapper.class})
public class AccountPersistenceAdapterTest extends AbstractPersistenceTest {
    
    @Autowired
    private AccountPersistenceAdapter adapter;

    @Test
    void should_save_and_load_account() {
        Account account = Account.create(Money.of("100.00"));

        adapter.save(account);
        Optional<Account> loaded = adapter.load(account.getId());

        assertThat(loaded).isPresent();
        assertThat(loaded.get().getId()).isEqualTo(account.getId());
        assertThat(loaded.get().getBalance()).isEqualTo(Money.of("100.00"));
    }

    @Test 
    void should_return_empty_optional_when_account_not_found() {
        Optional<Account> loaded = adapter.load("id-not-found");

        assertThat(loaded).isEmpty();
    }

    @Test
    void should_update_account_balance_after_deposit() {
        Account account = Account.create(Money.of("100.00"));
        account = adapter.save(account);

        account.deposit(Money.of("50.00"));
        adapter.save(account);

        Optional<Account> loaded = adapter.load(account.getId());

        assertThat(loaded).isPresent();
        assertThat(loaded.get().getBalance()).isEqualTo(Money.of("150.00"));
    }

    @Test
    void should_return_persisted_account_with_up_date_version_after_save() {
        Account account = Account.create(Money.of("100.00"));
        Account saved = adapter.save(account);

        assertThat(saved.getVersion()).isEqualTo(0L);

        saved.deposit(Money.of("50.00"));
        Account savedAgain = adapter.save(saved);

        assertThat(savedAgain.getVersion()).isEqualTo(1L);
    }

}
