package com.karnavauli.app.controllers;

import com.karnavauli.app.model.dto.CustomerDto;
import com.karnavauli.app.model.dto.KvTableDto;
import com.karnavauli.app.model.dto.ManyCustomers;
import com.karnavauli.app.service.CustomerService;
import com.karnavauli.app.service.KvTableService;
import com.karnavauli.app.service.UserService;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Controller
public class CustomerController {
    private CustomerService customerService;
    private UserService userService;
    private KvTableService kvTableService;


    public CustomerController(CustomerService customerService, UserService userService, KvTableService kvTableService) {
        this.customerService = customerService;
        this.userService = userService;
        this.kvTableService = kvTableService;
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
        List<KvTableDto> free = kvTableService.getFreeTables();

        model.addAttribute("manyCustomers", new ManyCustomers(amountOfTickets));
        model.addAttribute("errors", new HashMap<>());

        model.addAttribute("tables", free);

        //model.addAttribute("numberOfFreeSeats", seatsUtils.getNumberOfFreeSeats());
        model.addAttribute("numberOfFreeSeats", kvTableService.getAllFreeSeats());
        model.addAttribute("amountOfTickets", amountOfTickets);
        model.addAttribute("enoughPlaces", true);
        //if (amountOfTickets > seatsUtils.updateTables().size()) {
        if (amountOfTickets > kvTableService.getAllFreeSeats()) {
            model.addAttribute("notEnoughPlaces", true);
        } else {
            model.addAttribute("notEnoughPlaces", false);
        }
        return "customerForm";
    }

    @PostMapping("/addCustomer")
    public String addCustomerPost(@ModelAttribute ManyCustomers manyCustomers, Principal principal){

        //System.out.println(kvTableService.getOneKvTable(manyCustomers.getKvTable().getId()));

        //System.out.println(manyCustomers.getCustomers());
        Long id = manyCustomers.getKvTableId();
        manyCustomers.setKvTable(kvTableService.getOneKvTable(id).get());
        kvTableService.incrementOccupiedPlaces(id, manyCustomers.getCustomers().size());


        //System.out.println(kvTableService.getOneKvTable(manyCustomers.getKvTable().getId()));

        //manyCustomers.setKvTable(kvTableService.getOneKvTable(manyCustomers.getKvTable().getId()).get());
        manyCustomers.setUserDto(userService.getUserDtoFromUsername(principal.getName()));
        customerService.addOrUpdateManyCustomers(manyCustomers);

        return "redirect:/showCustomers";
    }

   /* @PostMapping("/addCustomer")
    public String addCustomerPost(
            *//*@Valid*//* @ModelAttribute ManyCustomers manyCustomers,
                       // BindingResult bindingResult,
                       Model model,
                       Principal principal
    ) {
        System.out.println(manyCustomers);

        *//*if (bindingResult.hasErrors()) {
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

        seatsUtils.updateTables();*//*

        //customerService.setManyCustomers(manyCustomers);

        //manycustomers ma miec metode setUsers gdzie do kazdego custmersa daje usera ktory go dodal
        // customersService ma miec metode addOrUpdateManyCustomers ktora zapisuje manyCustomers

        *//*customerDto.setUser(userService.getUserDtoFromUsername(principal.getName()));
        customerService.addOrUpdateCustomer(customerDto);*//*

        //manyCustomers.getCustomers().forEach(customerDto -> seatsUtils.putTable(customerDto.getKvTable()));

        //seatsUtils.putTable();

        manyCustomers.setUserDto(userService.getUserDtoFromUsername(principal.getName()));
        customerService.addOrUpdateManyCustomers(manyCustomers);


        return "redirect:/showCustomers";
    }*/

    @GetMapping("/showCustomers")
    public String customers(Model model) {
        model.addAttribute("customers", customerService.getAll());
        return "customers";
    }


    @GetMapping("/customer/update/{id}")
    public String customerUpdate(Model model, @PathVariable Long id) {
        //seatsUtils.updateTables();

        //TODO isAnySeatFree - ZLE!!!!
       // boolean isAnySeatFree = seatsUtils.isAnySeatFree();
        model.addAttribute("customer", customerService.getOneCustomer(id).orElseThrow(NullPointerException::new));
        model.addAttribute("errors", new HashMap<>());

        //TODO wywalic availableSeats
       // model.addAttribute("seats", seatsUtils.getAvailableSeats());
        model.addAttribute("seats", kvTableService.getAllFreeSeats());
       // model.addAttribute("isAnySeatFree", isAnySeatFree);
        model.addAttribute("isAnySeatFree", true);

        return "updateCustomer";
    }

    @PostMapping("/customer/update")
    public String currencyUpdatePost(@ModelAttribute CustomerDto customerDto) {
        customerService.addOrUpdateCustomer(customerDto);
        //seatsUtils.updateTables();
        return "redirect:/showCustomers";
    }

    @GetMapping("/customer/remove/{id}")
    public String customerRemove(@PathVariable Long id) {
        Optional<CustomerDto> customerDto = customerService.getOneCustomer(id);
        customerDto.ifPresent(c -> kvTableService.decrementOccupiedPlaces(c.getKvTable().getId()));
        customerService.deleteCustomer(id);
        //seatsUtils.updateTables();
        return "redirect:/showCustomers";
    }
}
