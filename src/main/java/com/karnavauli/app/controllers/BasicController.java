package com.karnavauli.app.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class BasicController {

    @GetMapping("/")
    public String welcome(Model model, Principal principal) {
        String username = principal.getName();
        model.addAttribute("user", username);
        return "index";
    }

    @GetMapping("/user")
    public String testUser() {
        return "testUser";
    }

    @GetMapping("/admin")
    public String testAdmin() {
        return "testAdmin";
    }

    @GetMapping("/uczelnie")
    public String testUczelnie() {
        return "testUczelnie";
    }

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("error", "");
        return "loginForm";
    }

    @GetMapping("/accessDenied")
    public String accessDenied() {
        return "accessDenied";
    }

    @GetMapping("/login/error")
    public String loginError(Model model)
    {
        final String loginErrorMessage = "Nieprawidłowe dane logowania";
        model.addAttribute("error", loginErrorMessage);
        return "loginForm";
    }

}
