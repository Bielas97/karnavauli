package com.karnavauli.app.controllers;

import com.karnavauli.app.model.dto.CustomerDto;
import com.karnavauli.app.model.dto.ManyCustomers;
import com.karnavauli.app.model.enums.KvTable;
import com.karnavauli.app.service.CustomerService;
import com.karnavauli.app.service.UserService;
import com.karnavauli.app.utils.SeatsUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

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

    //@InitBinder
    /*private void initializeBinder(WebDataBinder webDataBinder) {
        webDataBinder.setValidator(new CustomerValidator());
    }*/

    @GetMapping("/customer")
    public String newCustomer() {
        return "newCustomer";
    }

    @GetMapping("/addCustomer/{amountOfTickets}")
    public String addCustomerGet(Model model, @PathVariable int amountOfTickets) {
        //seatsUtils.updateTables();
        seatsUtils.putTable();
        for (Map.Entry entry : seatsUtils.getFreeSeatsInTables().entrySet()) {
            System.out.println(entry.getKey() + ", " + entry.getValue());
        }


        //tymczasowo
        int size = 1000;

        model.addAttribute("manyCustomers", new ManyCustomers(amountOfTickets));
        model.addAttribute("errors", new HashMap<>());
        model.addAttribute("seats", seatsUtils.getAvailableSeats());
        model.addAttribute("numberOfFreeSeats", size);
        //model.addAttribute("numberOfFreeSeats", seatsUtils.updateTables().size() + 1);
        model.addAttribute("isAnySeatFree", seatsUtils.isAnySeatFree());
        model.addAttribute("amountOfTickets", amountOfTickets);
        model.addAttribute("enoughPlaces", true);
        //if (amountOfTickets > seatsUtils.updateTables().size()) {
        if (amountOfTickets > size) {
            model.addAttribute("notEnoughPlaces", true);
        } else {
            model.addAttribute("notEnoughPlaces", false);
        }
        return "customerForm";
    }

    @PostMapping("/addCustomer")
    public String addCustomerPost(
            /*@Valid*/ @ModelAttribute ManyCustomers manyCustomers,
                       // BindingResult bindingResult,
                       Model model,
                       Principal principal
    ) {

        System.out.println(manyCustomers);

        /*if (bindingResult.hasErrors()) {
            //wyswietlanie errorow
            for (FieldError e : bindingResult.getFieldErrors()) {
                System.out.println(e);
            }
            Map<String, String> errors
                    = bindingResult
                    .getFieldErrors()
                    .stream()
                    .collect(Collectors.toMap(FieldError::getField, FieldError::getCode));

            seatsUtils.updateTables();
            boolean isAnySeatFree = seatsUtils.isAnySeatFree();

            model.addAttribute("errors", errors);
            model.addAttribute("customerDto", customerDto);
            model.addAttribute("seats", KvTable.values());
            model.addAttribute("isAnySeatFree", isAnySeatFree);
            return "redirect:/";
        }

        seatsUtils.updateTables();*/

        //customerService.setManyCustomers(manyCustomers);

        //manycustomers ma miec metode setUsers gdzie do kazdego custmersa daje usera ktory go dodal
        // customersService ma miec metode addOrUpdateManyCustomers ktora zapisuje manyCustomers

        /*customerDto.setUser(userService.getUserDtoFromUsername(principal.getName()));
        customerService.addOrUpdateCustomer(customerDto);*/

        //manyCustomers.getCustomers().forEach(customerDto -> seatsUtils.putTable(customerDto.getKvTable()));
        seatsUtils.putTable();

        manyCustomers.setUserDto(userService.getUserDtoFromUsername(principal.getName()));
        customerService.addOrUpdateManyCustomers(manyCustomers);


        return "redirect:/showCustomers";
    }

    @GetMapping("/showCustomers")
    public String customers(Model model) {
        model.addAttribute("customers", customerService.getAll());
        return "customers";
    }


    @GetMapping("/customer/update/{id}")
    public String customerUpdate(Model model, @PathVariable Long id) {
        //seatsUtils.updateTables();

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
        seatsUtils.updateTables();
        return "redirect:/showCustomers";
    }

    @GetMapping("/customer/remove/{id}")
    public String customerRemove(@PathVariable Long id) {
        customerService.deleteCustomer(id);
        seatsUtils.updateTables();
        return "redirect:/showCustomers";
    }
}
