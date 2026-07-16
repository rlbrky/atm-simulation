package com.berkay.atm_simulation;

import com.berkay.atm_simulation.model.Account;
import com.berkay.atm_simulation.model.Transaction;
import com.berkay.atm_simulation.repository.AccountRepository;
import com.berkay.atm_simulation.repository.TransactionRepository;
import com.berkay.atm_simulation.service.AccountService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {
    @Mock
    AccountRepository accountRepository;
    @Mock
    TransactionRepository transactionRepository;
    @InjectMocks
    AccountService accountService;

    @Test
    void deposit_increasesBalanceAndRecordsTransactions() {
        // given
        Account account = new Account();
        account.setAccountNumber("123");
        account.setBalance(new BigDecimal("100.00"));
        when(accountRepository.findByAccountNumber("123")).thenReturn(Optional.of(account));

        // when
        accountService.deposit("123", new BigDecimal("50.00"));

        // then
        assertThat(account.getBalance()).isEqualByComparingTo("150.00");
        verify(transactionRepository).save(any(Transaction.class));
    }

    @Test
    void deposit_rejectsZeroAmount() {
        assertThatThrownBy(() -> accountService.deposit("123", BigDecimal.ZERO))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void deposit_rejectsNegativeAmount() {
        assertThatThrownBy(() -> accountService.deposit("123", new BigDecimal("-50.00")))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void withdraw_decreasesBalance() {
        // given
        Account account = new Account();
        account.setAccountNumber("123");
        account.setBalance(new BigDecimal("100.00"));
        when(accountRepository.findByAccountNumber("123")).thenReturn(Optional.of(account));

        // when
        accountService.withdraw("123", new BigDecimal("50.00"));

        // then
        assertThat(account.getBalance()).isEqualByComparingTo("50.00");
        verify(transactionRepository).save(any(Transaction.class));
    }

    @Test
    void withdraw_rejectsInsufficientFunds() {
        // given
        Account account = new Account();
        account.setAccountNumber("123");
        account.setBalance(new BigDecimal("40.00"));
        when(accountRepository.findByAccountNumber("123")).thenReturn(Optional.of(account));

        // when

        // then
        assertThatThrownBy(() -> accountService.withdraw("123", new BigDecimal("50.00")))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void withdraw_allowsExactBalance() {
        // given
        Account account = new Account();
        account.setAccountNumber("123");
        account.setBalance(new BigDecimal("100.00"));
        when(accountRepository.findByAccountNumber("123")).thenReturn(Optional.of(account));

        // when
        accountService.withdraw("123", new BigDecimal("100.00"));

        // then
        assertThat(account.getBalance()).isEqualByComparingTo(BigDecimal.ZERO);
        verify(transactionRepository).save(any(Transaction.class));
    }
}
