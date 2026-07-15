package com.berkay.atm_simulation.controller;

import com.berkay.atm_simulation.model.Role;
import com.berkay.atm_simulation.service.AdminService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/admin/accounts")
    public String accounts(Model model, Principal principal) {
        model.addAttribute("username", principal.getName());
        model.addAttribute("accounts", adminService.listAccounts());
        model.addAttribute("createAccountForm", new CreateAccountForm());
        model.addAttribute("roles", Role.values());


        return "admin/accounts";
    }

    @PostMapping("/admin/accounts/{id}/unlock")
    public String unlockAccount(@PathVariable Long id, RedirectAttributes ra) {
        adminService.unlockAccount(id);
        ra.addFlashAttribute("message", "Account unlocked");

        return "redirect:/admin/accounts";
    }

    @PostMapping("/admin/accounts")
    public String createAccount(@Valid @ModelAttribute("createAccountForm") CreateAccountForm createAccountForm,
                                BindingResult result,
                                RedirectAttributes ra) {
        if(result.hasErrors()) {
            ra.addFlashAttribute("error", "Please enter valid data for your account");
            return "redirect:/admin/accounts";
        }

        try {
            adminService.createAccount(createAccountForm.getAccountNumber(),
                    createAccountForm.getOwnerName(),
                    createAccountForm.getPin(),
                    createAccountForm.getRole()
            );
            ra.addFlashAttribute("message", "Account created successfully");
        } catch (IllegalArgumentException e) {
            ra.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/admin/accounts";
    }
}
