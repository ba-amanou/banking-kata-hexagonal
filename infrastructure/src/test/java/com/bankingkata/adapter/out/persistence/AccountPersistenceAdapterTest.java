package com.bankingkata.adapter.out.persistence;

import static org.assertj.core.api.Assertions.assertThat;

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
        Account account = new Account(new Money(100.0));

        adapter.save(account);
        Account loaded = adapter.load(account.getId());

        assertThat(loaded.getId()).isEqualTo(account.getId());
        assertThat(loaded.getBalance()).isEqualTo(new Money(100.0));
    }

    @Test 
    void should_return_null_when_account_not_found() {
        Account loaded = adapter.load("id-not-found");

        assertThat(loaded).isNull();
    }

    @Test
    void should_update_account_balance_after_deposit() {
        Account account = new Account(new Money(100.0));
        adapter.save(account);
        
        account.deposit(new Money(50.0));
        adapter.save(account);
        Account loaded = adapter.load(account.getId());

        assertThat(loaded.getBalance()).isEqualTo(new Money(150.0));
    }

}
