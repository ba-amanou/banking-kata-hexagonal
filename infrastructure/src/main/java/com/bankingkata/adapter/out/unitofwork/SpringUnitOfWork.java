package com.bankingkata.adapter.out.unitofwork;

import java.util.function.Supplier;

import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;

import com.bankingkata.exception.ConcurrentAccountModificationException;
import com.bankingkata.port.out.UnitOfWork;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SpringUnitOfWork implements UnitOfWork {
    
    private final TransactionTemplate transactionTemplate;

    @Override
    public <T> T execute(Supplier<T> action) {
        try {
            return transactionTemplate.execute(status -> action.get());   
        } catch (OptimisticLockingFailureException e) {
            throw new ConcurrentAccountModificationException();
        }
    }
 
}
