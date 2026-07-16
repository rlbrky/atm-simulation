package com.berkay.atm_simulation.controller;

import com.berkay.atm_simulation.service.AccountService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
public class HomeController {

    private final AccountService accountService;

    public HomeController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/")
    public String home(Principal principal, Model model) {
        // principal is the logged user, spring injects it.
        model.addAttribute("balance", accountService.getBalance(principal.getName()));
        model.addAttribute("amountForm", new AmountForm());

        return "home";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/deposit")
    public String deposit(@Valid @ModelAttribute("amountForm") AmountForm form,
                          BindingResult result,
                          Principal principal,
                          RedirectAttributes ra){
        if(result.hasErrors()){
            ra.addFlashAttribute("error", "Please enter a valid positive amount.");
            return "redirect:/";
        }

        try {
            accountService.deposit(principal.getName(), form.getAmount());
            ra.addFlashAttribute("message", "Deposited successfully.");
        } catch (IllegalArgumentException e) {
            ra.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/";
    }

    @PostMapping("/withdraw")
    public String withdraw(@Valid @ModelAttribute("amountForm") AmountForm form,
                          BindingResult result,
                          Principal principal,
                          RedirectAttributes ra){
        if(result.hasErrors()){
            ra.addFlashAttribute("error", "Please enter a valid positive amount.");
            return "redirect:/";
        }

        try {
            accountService.withdraw(principal.getName(), form.getAmount());
            ra.addFlashAttribute("message", "Withdrew successfully.");
        } catch (IllegalArgumentException e) {
            ra.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/";
    }

    @GetMapping("/history")
    public String transactionHistory(Principal principal, Model model) {
        model.addAttribute("transactions",
                accountService.getTransactionHistory(principal.getName()));

        return "history";
    }
}
