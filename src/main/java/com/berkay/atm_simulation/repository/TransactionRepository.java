package com.berkay.atm_simulation.repository;

import com.berkay.atm_simulation.model.Account;
import com.berkay.atm_simulation.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByAccountOrderByTimestampDesc(Account account);
}
