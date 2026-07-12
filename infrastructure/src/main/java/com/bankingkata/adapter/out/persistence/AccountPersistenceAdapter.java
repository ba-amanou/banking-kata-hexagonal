package com.bankingkata.adapter.out.persistence;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.bankingkata.model.Account;
import com.bankingkata.port.out.LoadAccountPort;
import com.bankingkata.port.out.SaveAccountPort;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AccountPersistenceAdapter implements LoadAccountPort, SaveAccountPort {
    private final AccountJpaRepository accountJpaRepository;
    private final AccountPersistenceMapper mapper;

    @Override
    public Account save(Account account) {
        AccountJpaEntity accountEntity = mapper.toJpaEntity(account);
        // saveAndFlush not save : the returned entity's @Version must reflect
        // the actual post-update value before the enclosing transaction commits
        AccountJpaEntity saved = accountJpaRepository.saveAndFlush(accountEntity);
        return mapper.toDomain(saved);
    }
    
    @Override
    public Optional<Account> load(String accountId) {
       return accountJpaRepository.findById(accountId)
            .map(mapper::toDomain);
    }


}
