package com.berkay.atm_simulation.controller;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public class AmountForm {
    @NotNull @Positive
    private BigDecimal amount; // needs to reject zero, negatives and null values.

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
