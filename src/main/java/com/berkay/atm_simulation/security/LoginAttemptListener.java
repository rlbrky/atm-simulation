package com.berkay.atm_simulation.security;

import com.berkay.atm_simulation.model.Account;
import com.berkay.atm_simulation.repository.AccountRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger log = LoggerFactory.getLogger(LoginAttemptListener.class);

    public LoginAttemptListener(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @EventListener
    @Transactional
    public void onFailure(AuthenticationFailureBadCredentialsEvent event) {

        String username = event.getAuthentication().getName();
        Optional<Account> account = accountRepository.findByAccountNumber(username);

        if (account.isEmpty()) {
            log.warn("Failed login attempt for unknown account: {}", username);
        }
        else {
            int failedAttempts = account.get().getFailedAttempts();
            failedAttempts++;

            account.get().setFailedAttempts(failedAttempts);
            log.warn("Failed login attempt #{} for account {}", failedAttempts, username);

            if(failedAttempts >= MAX_FAILED_ATTEMPTS) {
                account.get().setLocked(true);
                log.warn("Account locked for {}", username);
            }

            accountRepository.save(account.get());
        }
    }

    @EventListener
    @Transactional
    public void onSuccess(AuthenticationSuccessEvent event) {
        accountRepository.findByAccountNumber(event.getAuthentication().getName()).ifPresent(account -> {
            account.setFailedAttempts(0);
            accountRepository.save(account);
        });
    }
}
