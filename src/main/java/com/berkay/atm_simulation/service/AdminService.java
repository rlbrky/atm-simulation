package com.berkay.atm_simulation.service;

import com.berkay.atm_simulation.model.Account;
import com.berkay.atm_simulation.model.Role;
import com.berkay.atm_simulation.repository.AccountRepository;
import com.berkay.atm_simulation.repository.TransactionRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class AdminService {

    private final AccountRepository accountRepository;

    private final PasswordEncoder passwordEncoder;

    private final TransactionRepository transactionRepository;

    public AdminService(AccountRepository accountRepository, PasswordEncoder passwordEncoder, TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
        this.transactionRepository = transactionRepository;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional(readOnly = true)
    public List<Account> listAccounts(){
        return accountRepository.findAll();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public void unlockAccount(Long accountId){
        Account account = accountRepository.findById(accountId).orElseThrow();

        account.setLocked(false);
        account.setFailedAttempts(0);

        accountRepository.save(account);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public void createAccount(String accountNumber, String ownerName, String pin, Role role){
        if(accountRepository.findByAccountNumber(accountNumber).isPresent()){
            throw new IllegalArgumentException("Account number already exists");
        }

        Account account = new Account();
        account.setAccountNumber(accountNumber);
        account.setOwnerName(ownerName);
        account.setPinHash(passwordEncoder.encode(pin));
        account.setRole(role);
        account.setBalance(BigDecimal.ZERO);
        accountRepository.save(account);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public void deleteAccount(Long accountId){
        Account account = accountRepository.findById(accountId).orElseThrow();
        transactionRepository.deleteByAccount(account); // clear foreign keys first
        accountRepository.delete(account);
    }
}
