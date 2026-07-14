package com.berkay.atm_simulation.security;

import com.berkay.atm_simulation.model.Account;
import com.berkay.atm_simulation.repository.AccountRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AccountUserDetailsService implements UserDetailsService {

    private final AccountRepository accountRepository;

    public AccountUserDetailsService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Account> account = accountRepository.findByAccountNumber(username);

        if(account.isEmpty()) {
            throw new UsernameNotFoundException(username);
        }

        return User.builder()
                .username(account.get().getAccountNumber())
                .password(account.get().getPinHash())
                .roles(account.get().getRole().name())
                .accountLocked(account.get().isLocked())
                .build();
    }
}
