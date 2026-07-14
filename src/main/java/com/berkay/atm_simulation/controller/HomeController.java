package com.berkay.atm_simulation.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class HomeController {
    @GetMapping("/")
    public String home(Principal principal, Model model) {
        // principal is the logged user, spring injects it.
        model.addAttribute("username", principal.getName());

        return "home";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }
}
