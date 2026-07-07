package com.bankingkata.adapter.out.unitofwork;

import java.util.function.Supplier;

import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;

import com.bankingkata.port.out.UnitOfWork;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SpringUnitOfWork implements UnitOfWork {
    
    private final TransactionTemplate transactionTemplate;

    @Override
    public <T> T execute(Supplier<T> action) {
       return transactionTemplate.execute(status -> action.get());
    }
 
}
