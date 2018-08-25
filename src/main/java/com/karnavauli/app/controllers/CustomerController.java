package com.karnavauli.app.controllers;

import com.karnavauli.app.model.dto.CustomerDto;
import com.karnavauli.app.model.enums.Seat;
import com.karnavauli.app.service.CustomerService;
import com.karnavauli.app.service.UserService;
import com.karnavauli.app.utils.SeatsUtils;
import com.karnavauli.app.validators.CustomerValidator;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class CustomerController {
    private CustomerService customerService;
    private UserService userService;
    private SeatsUtils seatsUtils;

    public CustomerController(CustomerService customerService, UserService userService, SeatsUtils seatsUtils) {
        this.customerService = customerService;
        this.userService = userService;
        this.seatsUtils = seatsUtils;
    }

    @InitBinder
    private void initializeBinder(WebDataBinder webDataBinder) {
        webDataBinder.setValidator(new CustomerValidator());
    }

    @GetMapping("/customer")
    public String newCustomer(){
        return "newCustomer";
    }

    @GetMapping("/addCustomer/{amountOfTickets}")
    public String addCustomerGet(Model model, @PathVariable int amountOfTickets) {
        seatsUtils.updateSeats();

        System.out.println(amountOfTickets);

        model.addAttribute("customerDto", new CustomerDto());
        model.addAttribute("errors", new HashMap<>());
        model.addAttribute("seats", seatsUtils.getAvailableSeats());
        model.addAttribute("isAnySeatFree", seatsUtils.isAnySeatFree());
        model.addAttribute("amountOfTickets", amountOfTickets);
        return "customerForm";
    }

    @PostMapping("/addCustomer")
    public String addCustomerPost(
            @Valid @ModelAttribute CustomerDto customerDto,
            BindingResult bindingResult,
            Model model,
            Principal principal
    ) {
        if (bindingResult.hasErrors()) {
            //wyswietlanie errorow
            for (FieldError e : bindingResult.getFieldErrors()) {
                System.out.println(e);
            }
            Map<String, String> errors
                    = bindingResult
                    .getFieldErrors()
                    .stream()
                    .collect(Collectors.toMap(FieldError::getField, FieldError::getCode));

            seatsUtils.updateSeats();
            boolean isAnySeatFree = seatsUtils.isAnySeatFree();

            model.addAttribute("errors", errors);
            model.addAttribute("customerDto", customerDto);
            model.addAttribute("seats", Seat.values());
            model.addAttribute("isAnySeatFree", isAnySeatFree);
            return "redirect:/";
        }

        seatsUtils.updateSeats();

        customerDto.setUser(userService.getUserFromUsername(principal.getName()));
        customerService.addOrUpdateCustomer(customerDto);

        return "redirect:/showCustomers";
    }

    @GetMapping("/showCustomers")
    public String customers(Model model) {
        model.addAttribute("customers", customerService.getAll());

        return "customers";
    }

    @GetMapping("/customer/update/{id}")
    public String customerUpdate(Model model, @PathVariable Long id) {
        seatsUtils.updateSeats();

        boolean isAnySeatFree = seatsUtils.isAnySeatFree();
        model.addAttribute("customer", customerService.getOneCustomer(id).orElseThrow(NullPointerException::new));
        model.addAttribute("errors", new HashMap<>());
        model.addAttribute("seats", seatsUtils.getAvailableSeats());
        model.addAttribute("isAnySeatFree", isAnySeatFree);

        return "updateCustomer";
    }

    @PostMapping("/customer/update")
    public String currencyUpdatePost(@ModelAttribute CustomerDto customerDto) {
        customerService.addOrUpdateCustomer(customerDto);
        seatsUtils.updateSeats();
        return "redirect:/showCustomers";
    }

    @GetMapping("/customer/remove/{id}")
    public String customerRemove(@PathVariable Long id) {
        customerService.deleteCustomer(id);
        seatsUtils.updateSeats();
        return "redirect:/showCustomers";
    }
}
