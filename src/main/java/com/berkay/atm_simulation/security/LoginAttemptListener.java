package com.berkay.atm_simulation.security;

import com.berkay.atm_simulation.model.Account;
import com.berkay.atm_simulation.repository.AccountRepository;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
public class LoginAttemptListener {

    private final AccountRepository accountRepository;
    private static final int MAX_FAILED_ATTEMPTS = 3;

    public LoginAttemptListener(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @EventListener
    @Transactional
    public void onFailure(AuthenticationFailureBadCredentialsEvent event) {
        Optional<Account> account = accountRepository.findByAccountNumber(event.getAuthentication().getName());
        if (account.isEmpty()) {
            //TODO: write a message that account doesn't exists.
        }
        else {
            int failedAttempts = account.get().getFailedAttempts();
            failedAttempts++;

            account.get().setFailedAttempts(failedAttempts);

            //TODO: write a message showing attempt count and failed message.
            if(failedAttempts >= MAX_FAILED_ATTEMPTS) {
                account.get().setLocked(true);
            }

            accountRepository.save(account.get());
        }
    }

    @EventListener
    @Transactional
    public void onSuccess(AuthenticationSuccessEvent event) {
        Optional<Account> account = accountRepository.findByAccountNumber(event.getAuthentication().getName());
        account.get().setFailedAttempts(0);

        accountRepository.save(account.get());
    }
}
