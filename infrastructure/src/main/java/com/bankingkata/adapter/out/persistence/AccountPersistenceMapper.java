package com.bankingkata.adapter.out.persistence;

import org.springframework.stereotype.Component;

import com.bankingkata.model.Account;
import com.bankingkata.model.Money;

@Component
public class AccountPersistenceMapper {
    public AccountJpaEntity toJpaEntity(Account account) {
        return AccountJpaEntity.builder()
            .id(account.getId())
            .balance(account.getBalance().amount())
            .version(account.getVersion())
            .build();
    }

    public Account toDomain(AccountJpaEntity accountJpaEntity) {
        return Account.reconstitute(
            accountJpaEntity.getId(),
            new Money(accountJpaEntity.getBalance()),
            accountJpaEntity.getVersion()
        );
    }

}
