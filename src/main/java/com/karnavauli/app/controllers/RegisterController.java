package com.karnavauli.app.controllers;

import com.karnavauli.app.model.enums.Role;
import com.karnavauli.app.model.dto.UserDto;
import com.karnavauli.app.service.UserService;
import com.karnavauli.app.service.implementations.MyUserDetailsService;
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
    private MyUserDetailsService myUserDetailsService;
    private UserService userService;
    //private BCryptPasswordEncoder bCryptPasswordEncoder;

    public RegisterController(MyUserDetailsService myUserDetailsService, UserService userService/*, BCryptPasswordEncoder bCryptPasswordEncoder*/) {
        this.myUserDetailsService = myUserDetailsService;
        this.userService = userService;
        //this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @InitBinder
    private void initializeBinder(WebDataBinder webDataBinder) {
        webDataBinder.setValidator(new UserValidator());
    }

    @GetMapping("/users")
    public String showUsers(Model model){
        List<UserDto> users = userService.getAll();
        users.stream().forEach(e -> e.setPassword(null));
        model.addAttribute("users", users);
        return "showUsers";
    }

    @GetMapping("/register")
    public String registerUser(Model model){
        model.addAttribute("userDto", new UserDto());
        model.addAttribute("roles", Role.values());
        model.addAttribute("errors", new HashMap<>());
        return "register";
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
            myUserDetailsService.loadUserByUsername(userDto.getUsername());
            return "register";
        }

        // userDto.setPassword(bCryptPasswordEncoder.encode(userDto.getPassword()));
        userService.addOrUpdateUser(userDto);
        return "redirect:/";
    }
}
