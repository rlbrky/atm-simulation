package com.berkay.atm_simulation;

import com.berkay.atm_simulation.model.Account;
import com.berkay.atm_simulation.model.Role;
import com.berkay.atm_simulation.repository.AccountRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;

@SpringBootApplication
public class AtmSimulationApplication {

	public static void main(String[] args) {
		SpringApplication.run(AtmSimulationApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(AccountRepository accountRepository, PasswordEncoder passwordEncoder) {
		return args -> {
			if(accountRepository.findByAccountNumber("123456789").isEmpty()) {
				// Create a dummy account if it doesn't exist already.
				Account account = new Account();
				account.setAccountNumber("123456789");
				account.setOwnerName("Berkay");
				account.setRole(Role.USER);
				account.setBalance(BigDecimal.ZERO);
				account.setPinHash(passwordEncoder.encode("1234"));

				accountRepository.save(account);

				System.out.println("Created new account for: " + account.getOwnerName());
			}
			else {
				System.out.println("Account already exists.");
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

				System.out.println("Created new account for: " + account.getOwnerName());
			}
			else {
				System.out.println("Account already exists.");
			}

		};
	}
}
