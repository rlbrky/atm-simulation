package com.berkay.atm_simulation.repository;

import com.berkay.atm_simulation.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}
