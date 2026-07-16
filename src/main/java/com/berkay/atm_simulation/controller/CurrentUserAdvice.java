package com.berkay.atm_simulation.controller;

import com.berkay.atm_simulation.model.Account;
import com.berkay.atm_simulation.service.AccountService;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.security.Principal;

@ControllerAdvice
public class CurrentUserAdvice {
    private final AccountService accountService;

    public CurrentUserAdvice(AccountService accountService) {
        this.accountService = accountService;
    }

    @ModelAttribute
    public void addCurrentUser(Principal principal, Model model) {
        if(principal == null) return;

        Account me = accountService.getAccountOrThrow(principal.getName());
        model.addAttribute("username", principal.getName());
        model.addAttribute("ownerName", me.getOwnerName());
    }
}
