package com.bankingkata.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.bankingkata.port.in.CreateAccountUseCase;
import com.bankingkata.port.in.DepositMoneyUseCase;
import com.bankingkata.port.in.GetAccountBalanceUseCase;
import com.bankingkata.port.in.WithdrawMoneyUseCase;
import com.bankingkata.port.out.LoadAccountPort;
import com.bankingkata.port.out.SaveAccountPort;
import com.bankingkata.service.CreateAccountService;
import com.bankingkata.service.DepositMoneyService;
import com.bankingkata.service.GetAccountBalanceService;
import com.bankingkata.service.WithdrawMoneyService;

@Configuration
public class UseCaseConfig {
    
    @Bean
    public CreateAccountUseCase createAccountUseCase(SaveAccountPort saveAccountPort) {
        return new CreateAccountService(saveAccountPort);
    }

    @Bean
    public DepositMoneyUseCase depositMoneyUseCase(LoadAccountPort loadAccountPort, SaveAccountPort saveAccountPort) {
        return new DepositMoneyService(saveAccountPort, loadAccountPort);
    }

    @Bean
    public WithdrawMoneyUseCase withdrawMoneyUseCase(LoadAccountPort loadAccountPort, SaveAccountPort saveAccountPort) {
        return new WithdrawMoneyService(saveAccountPort, loadAccountPort);
    }

    @Bean
    public GetAccountBalanceUseCase getAccountBalanceUseCase(LoadAccountPort loadAccountPort) {
        return new GetAccountBalanceService(loadAccountPort);
    }    
    
}
