package com.berkay.atm_simulation.config;

import com.berkay.atm_simulation.model.Account;
import com.berkay.atm_simulation.model.Role;
import com.berkay.atm_simulation.repository.AccountRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class DataSeeder implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataSeeder.class);

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    public DataSeeder(AccountRepository accountRepository, PasswordEncoder passwordEncoder) {
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if(accountRepository.findByAccountNumber("123456789").isEmpty()) {
            // Create a dummy account if it doesn't exist already.
            Account account = new Account();
            account.setAccountNumber("123456789");
            account.setOwnerName("Berkay");
            account.setRole(Role.USER);
            account.setBalance(BigDecimal.ZERO);
            account.setPinHash(passwordEncoder.encode("1234"));

            accountRepository.save(account);

            log.info("Created new account for: {}", account.getOwnerName());
        }
        else {
            log.info("Account already exists.");
        }

        if(accountRepository.findByAccountNumber("admin").isEmpty()) {
            // Create a dummy account if it doesn't exist already.
            Account account = new Account();
            account.setAccountNumber("admin");
            account.setOwnerName("Berkay");
            account.setRole(Role.ADMIN);
            account.setBalance(BigDecimal.ZERO);
            account.setPinHash(passwordEncoder.encode("admin"));

            accountRepository.save(account);

            log.info("Created new account for: {}", account.getOwnerName());
        }
        else {
            log.info("Account already exists.");
        }
    }
}