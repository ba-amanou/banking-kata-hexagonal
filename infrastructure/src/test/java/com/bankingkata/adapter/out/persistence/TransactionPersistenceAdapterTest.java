package com.bankingkata.adapter.out.persistence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;

import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.context.annotation.Import;

import com.bankingkata.model.Money;
import com.bankingkata.model.Transaction;
import com.bankingkata.model.TransactionType;

@Import({TransactionPersistenceAdapter.class, TransactionPersistenceMapper.class})
public class TransactionPersistenceAdapterTest extends AbstractPersistenceTest {
    
    @Autowired
    private TransactionPersistenceAdapter adapter;
    @Autowired
    private TransactionJpaRepository repository;
    @Autowired
    private AccountJpaRepository accountJpaRepository;
    @Autowired
    private TestEntityManager entityManager;


    @Test
    void should_save_transaction_with_correct_data() {
        Transaction transaction = Transaction.deposit("account-1", Money.of("100.00"));

        adapter.save(transaction);

        TransactionJpaEntity saved = repository.findById(transaction.getId()).orElseThrow();
        assertThat(saved.getAccountId()).isEqualTo("account-1");
        assertThat(saved.getType()).isEqualTo(TransactionType.DEPOSIT);
        assertThat(saved.getAmount()).isEqualByComparingTo("100.00");
    }

    @Test 
    void should_throw_when_account_does_not_exist() {
        Transaction transaction = Transaction.deposit("account-1", Money.of("100.00"));

        assertThatThrownBy(() -> {
            adapter.save(transaction);
            entityManager.flush(); // to force the saving
        }).isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    void should_find_transactions_by_account_id() {
        AccountJpaEntity account1 = AccountJpaEntity.builder()
            .id("account-1")
            .balance(new BigDecimal("100.00"))
            .build();
        accountJpaRepository.save(account1);

        AccountJpaEntity account2 = AccountJpaEntity.builder()
            .id("account-2")
            .balance(new BigDecimal("100.00"))
            .build();
        accountJpaRepository.save(account2);        

        Transaction tx1 = Transaction.deposit("account-1", Money.of("100.00"));
        Transaction tx2 = Transaction.deposit("account-1", Money.of("50.00"));
        Transaction tx3 = Transaction.deposit("account-2", Money.of("100.00"));

        adapter.save(tx1);
        adapter.save(tx2);
        adapter.save(tx3);

        List<Transaction> transactions = adapter.loadByAccountId("account-1");

        assertThat(transactions).hasSize(2);
        assertThat(transactions).allMatch(tx -> tx.getAccountId().equals("account-1"));
    }

    @Test
    void should_return_empty_list_when_no_transactions() {
        List<Transaction> transactions = adapter.loadByAccountId("account-1");

        assertThat(transactions).isEmpty();
    }

}
