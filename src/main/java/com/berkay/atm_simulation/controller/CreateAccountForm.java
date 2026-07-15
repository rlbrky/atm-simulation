package com.berkay.atm_simulation.controller;

import com.berkay.atm_simulation.model.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class CreateAccountForm {
    @NotBlank private String accountNumber;

    @NotBlank
    private String ownerName;
    @NotBlank
    @Pattern(regexp = "\\d{4}", message = "PIN must be exactly 4 digits")
    private String pin;
    @NotNull
    private Role role;

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
