package com.bankingkata.adapter.out.persistence;

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
    public void save(Account account) {
        AccountJpaEntity accountEntity = mapper.toJpaEntity(account);
        accountJpaRepository.save(accountEntity);
    }
    
    @Override
    public Account load(String accountId) {
       return accountJpaRepository.findById(accountId)
            .map(mapper::toDomain)
            .orElse(null);
    }


}
