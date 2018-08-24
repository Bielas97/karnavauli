package com.karnavauli.app.controllers;

import com.karnavauli.app.service.SeatsService;
import com.karnavauli.app.model.dto.CustomerDto;
import com.karnavauli.app.model.enums.Seat;
import com.karnavauli.app.service.CustomerService;
import com.karnavauli.app.service.UserService;
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
    private SeatsService seatsService;

    public CustomerController(CustomerService customerService, UserService userService, SeatsService seatsService) {
        this.customerService = customerService;
        this.userService = userService;
        this.seatsService = seatsService;
    }

    @InitBinder
    private void initializeBinder(WebDataBinder webDataBinder) {
        webDataBinder.setValidator(new CustomerValidator());
    }

    @GetMapping("/addCustomer")
    public String addCustomerGet(Model model) {
        seatsService.updateSeats();

        boolean isAnySeatFree = true;
        if (seatsService.getAvailableSeats().isEmpty() || seatsService.getAvailableSeats().size() == 0) {
            isAnySeatFree = false;
        }
        System.out.println(isAnySeatFree);

        model.addAttribute("customerDto", new CustomerDto());
        model.addAttribute("errors", new HashMap<>());
        model.addAttribute("seats", seatsService.getAvailableSeats());
        model.addAttribute("isAnySeatFree", isAnySeatFree);
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

            seatsService.updateSeats();
            boolean isAnySeatFree = true;
            if (seatsService.getAvailableSeats().isEmpty() || seatsService.getAvailableSeats().size() == 0) {
                isAnySeatFree = false;
            }
            System.out.println(isAnySeatFree);

            model.addAttribute("errors", errors);
            model.addAttribute("customerDto", customerDto);
            model.addAttribute("seats", Seat.values());
            model.addAttribute("isAnySeatFree", isAnySeatFree);
            return "redirect:/";
        }

        seatsService.updateSeats();

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
    public String customerUpdate(Model model, @PathVariable Long id){
        seatsService.updateSeats();

        boolean isAnySeatFree = true;
        if (seatsService.getAvailableSeats().isEmpty() || seatsService.getAvailableSeats().size() == 0) {
            isAnySeatFree = false;
        }
        model.addAttribute("customer", customerService.getOneCustomer(id).orElseThrow(NullPointerException::new));
        model.addAttribute("errors", new HashMap<>());
        model.addAttribute("seats", seatsService.getAvailableSeats());
        model.addAttribute("isAnySeatFree", isAnySeatFree);

        return "updateCustomer";
    }

    @PostMapping("/customer/update")
    public String currencyUpdatePost(@ModelAttribute CustomerDto customerDto){
        customerService.addOrUpdateCustomer(customerDto);
        seatsService.updateSeats();
        return "redirect:/showCustomers";
    }

    @GetMapping("/customer/remove/{id}")
    public String customerRemove(@PathVariable Long id)
    {
        customerService.deleteCustomer(id);
        seatsService.updateSeats();
        return "redirect:/showCustomers";
    }
}
