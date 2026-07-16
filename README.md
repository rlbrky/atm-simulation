# ATM Simulation
> A secure, web-based ATM built to learn Spring Security, Hibernate/JPA, and Thymeleaf.

## Screenshots

### Login
<img width="432" height="278" alt="Login Screen" src="https://github.com/user-attachments/assets/7f9b550f-1b3c-4793-be29-4b5ca350cb24" />

### Dashboard
<img width="2420" height="379" alt="Account dashboard with balance and deposit/withdraw forms" src="https://github.com/user-attachments/assets/dbc1d336-f109-4776-92f9-19f9d1a1854f" />

### Transaction History
<img width="2421" height="625" alt="Transaction history table, newest first" src="https://github.com/user-attachments/assets/c971a32c-9682-4c4a-8cfd-f31fdeaacd7f" />

### Admin Panel
<img width="2413" height="738" alt="Admin panel listing accounts with unlock and delete actions" src="https://github.com/user-attachments/assets/5b0bdec9-d5c3-4fc3-9093-c329be2b3905" />

## Features
- PIN authentication with account lockout after 3 repeated failures
- Balance on home page, deposits, withdrawals
- Transaction history (newest first)
- Admin module: list, create, unlock, and delete accounts

## Security & correctness highlights
- PINs hashed with **BCrypt** (never stored in plaintext)
- Account **lockout** driven by Spring Security authentication events
- **CSRF** protection on every state-changing form
- **Role-based** authorization enforced at both URL and method level (`@PreAuthorize`)
- Balance changes are **`@Transactional`** and use **`BigDecimal`**
- Least-privilege DB user; DB password kept out of source via an environment variable

## Tech stack
Java 26 · Spring Boot 4.1 · Spring Security · Spring Data JPA / Hibernate · Thymeleaf · MySQL · Maven

## Getting started
### Prerequisites
Java 26, MySQL 8, (Maven — or use the included `./mvnw` wrapper)

### 1. Create the database and a scoped user
```sql
CREATE DATABASE atm_simulation;
CREATE USER 'atm_user'@'localhost' IDENTIFIED BY 'your-password';
GRANT ALL PRIVILEGES ON atm_simulation.* TO 'atm_user'@'localhost';
```
### 2. Provide the DB password via environment variable
Set `ATM_DB_PASSWORD=your-password` in your environment (or your IDE's run configuration).
### 3. Run
```bash
./mvnw spring-boot:run
```
Then open http://localhost:8080

## Architecture
- controller → service (@Transactional business logic) → repository (Spring Data) → MySQL, with Spring Security's filter chain in front and a UserDetailsService bridging accounts to authentication.
