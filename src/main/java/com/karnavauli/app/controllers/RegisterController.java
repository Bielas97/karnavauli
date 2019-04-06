package com.karnavauli.app.controllers;

import com.karnavauli.app.model.dto.UserDto;
import com.karnavauli.app.model.enums.Role;
import com.karnavauli.app.service.TicketService;
import com.karnavauli.app.service.UserService;
import com.karnavauli.app.validators.UserValidator;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class RegisterController {
    private UserService userService;
    private TicketService ticketService;

    public RegisterController(UserService userService, TicketService ticketService) {
        this.userService = userService;
        this.ticketService = ticketService;
    }

    @InitBinder
    private void initializeBinder(WebDataBinder webDataBinder) {
        webDataBinder.setValidator(new UserValidator());
    }

    @GetMapping("/users")
    public String showUsers(Model model){
        model.addAttribute("users", userService.getAll());
        return "users/showUsers";
    }

    @GetMapping("/register")
    public String registerUser(Model model){
        model.addAttribute("userDto", new UserDto());
        model.addAttribute("roles", Role.values());
        model.addAttribute("errors", new HashMap<>());
        model.addAttribute("formTickets", ticketService.getAll());
        return "users/register";
    }

    @PostMapping("/register")
    public String registerPost(
            @Valid @ModelAttribute UserDto userDto,
            BindingResult bindingResult,
            Model model
    ){
        if (bindingResult.hasErrors()) {
            Map<String, String> errors
                    = bindingResult
                    .getFieldErrors()
                    .stream()
                    .collect(Collectors.toMap(FieldError::getField, FieldError::getCode));
            model.addAttribute("errors", errors);
            model.addAttribute("userDto", userDto);
            model.addAttribute("roles", Role.values());
            model.addAttribute("formTickets", ticketService.getAll());
            return "users/register";
        }
        userService.addUser(userDto);
        return "redirect:/";
    }
}
