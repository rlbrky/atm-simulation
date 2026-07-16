package com.berkay.atm_simulation.service;

import com.berkay.atm_simulation.model.Account;
import com.berkay.atm_simulation.model.Transaction;
import com.berkay.atm_simulation.model.TransactionType;
import com.berkay.atm_simulation.repository.AccountRepository;
import com.berkay.atm_simulation.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    public AccountService(AccountRepository accountRepository, TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    public Account getAccountOrThrow(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber).orElseThrow(
                () -> new IllegalArgumentException("Account not found: " + accountNumber)
        );
    }

    public Optional<Account> findAccount(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber);
    }

    @Transactional(readOnly = true)
    public BigDecimal getBalance(String accountNumber){
        Account account = getAccountOrThrow(accountNumber);

        return account.getBalance();
    }

    @Transactional
    public void deposit(String accountNumber, BigDecimal amount){
        if(amount.compareTo(BigDecimal.ZERO) <= 0){
            throw new IllegalArgumentException("Amount must be greater than zero");
        }

        Account account = getAccountOrThrow(accountNumber);
        // Update account balance
        account.setBalance(account.getBalance().add(amount));

        // Record and save transaction itself
        Transaction deposit = new Transaction(TransactionType.DEPOSIT, amount, Instant.now(), account);
        transactionRepository.save(deposit);
        accountRepository.save(account);
    }

    @Transactional
    public void withdraw(String accountNumber, BigDecimal amount){
        if(amount.compareTo(BigDecimal.ZERO) <= 0){
            throw new IllegalArgumentException("Amount must be greater than zero");
        }

        Account account = getAccountOrThrow(accountNumber);

        if(account.getBalance().compareTo(amount) < 0){
            throw new IllegalArgumentException("Insufficient funds");
        }

        account.setBalance(account.getBalance().subtract(amount));

        Transaction withdrawal = new Transaction(TransactionType.WITHDRAWAL, amount, Instant.now(), account);
        transactionRepository.save(withdrawal);
        accountRepository.save(account);
    }

    @Transactional(readOnly = true)
    public List<Transaction> getTransactionHistory(String accountNumber){
        Account account = getAccountOrThrow(accountNumber);
        return transactionRepository.findByAccountOrderByTimestampDesc(account);
    }
}
