package com.karnavauli.app.controllers;

import com.karnavauli.app.model.dto.*;
import com.karnavauli.app.service.CustomerService;
import com.karnavauli.app.service.KvTableService;
import com.karnavauli.app.service.TicketService;
import com.karnavauli.app.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.*;

@Controller
public class CustomerController {
    private CustomerService customerService;
    private UserService userService;
    private KvTableService kvTableService;
    private TicketService ticketService;


    public CustomerController(CustomerService customerService, UserService userService, KvTableService kvTableService, TicketService ticketService) {
        this.customerService = customerService;
        this.userService = userService;
        this.kvTableService = kvTableService;
        this.ticketService = ticketService;
    }

  /*  @InitBinder
    private void initializeBinder(WebDataBinder webDataBinder) {
        webDataBinder.setValidator(new CustomerValidator());
    }*/

    /*@GetMapping("/customer")
    public String newCustomer() {
        return "newCustomer";
    }*/

    @GetMapping("/addCustomer/{amountOfTickets}")
    public String addCustomerGet(Model model, @PathVariable int amountOfTickets, Principal principal) {
        List<KvTableDto> free = kvTableService.getFreeTablesForAmountOfPeople(amountOfTickets);
        /*free.stream().filter(kvTableDto -> {
            User user = userService.getUserFromUsername(principal.getName());
            for (int i = 0; i < user.getTickets().size(); i++) {
                if (kvTableDto.getOwner().equals(user.getTickets().get(i).getFullName())) {
                    return true;
                }
            }
            return false;
        });*/

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
        //liczba dostepnych biletow do sprzedania przez danego uzytkownika
        model.addAttribute("numberOfTickets", userService.getUserFromUsername(principal.getName()).getNumberOfTickets());

        return "customerForm";
    }

    @PostMapping("/addCustomer")
    public String addCustomerPost(/*@Valid*/ @ModelAttribute ManyCustomers manyCustomers, BindingResult result, Principal principal) {
        Long id = manyCustomers.getKvTableId();
        manyCustomers.setKvTable(kvTableService.getOneKvTable(id).get());
        //kvTableService.incrementOccupiedPlaces(id, manyCustomers.getCustomers().size());
        manyCustomers.setUserDto(userService.getUserFromUsername(principal.getName()));
        customerService.addManyCustomers(manyCustomers);

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

        *//*customerDto.setUser(userService.getUserFromUsername(principal.getName()));
        customerService.addOrUpdateCustomer(customerDto);*//*

        //manyCustomers.getCustomers().forEach(customerDto -> seatsUtils.putTable(customerDto.getKvTable()));

        //seatsUtils.putTable();

        manyCustomers.setUserDto(userService.getUserFromUsername(principal.getName()));
        customerService.addOrUpdateManyCustomers(manyCustomers);


        return "redirect:/showCustomers";
    }*/

    @GetMapping("/showCustomers")
    public String customers(Model model, Principal principal) {
        model.addAttribute("customers", customerService.getAll());
        //liczba dostepnych biletow do sprzedania przez danego uzytkownika
        model.addAttribute("numberOfTickets", userService.getUserFromUsername(principal.getName()).getNumberOfTickets());
        //nazwa zalogowanego usera
        model.addAttribute("user", principal.getName());
        return "customers";
    }


    @GetMapping("/customer/update/{id}")
    public String customerUpdate(Model model, @PathVariable Long id) {
        // boolean isAnySeatFree = seatsUtils.isAnySeatFree();
        ManyCustomers manyCustomers = new ManyCustomers(1);
        CustomerDto customerDto = customerService.getOneCustomer(id).orElseThrow(NullPointerException::new);
        List<CustomerDto> customerDtoList = new ArrayList<>();
        customerDtoList.add(customerDto);
        manyCustomers.setCustomers(customerDtoList);
        //manyCustomers.getCustomers().add(customerService.getOneCustomer(id).orElseThrow(NullPointerException::new));

        model.addAttribute("manyCustomers", manyCustomers);


        model.addAttribute("errors", new HashMap<>());

        //TODO wywalic availableSeats
        // model.addAttribute("seats", seatsUtils.getAvailableSeats());
        List<KvTableDto> freeTablesPlusCurrenTable = kvTableService.getFreeTablesPlusCurrentTable(manyCustomers);

        model.addAttribute("tables", freeTablesPlusCurrenTable);
        // model.addAttribute("isAnySeatFree", isAnySeatFree);
        model.addAttribute("isAnySeatFree", true);

        return "updateCustomer";
    }


    @PostMapping("/customer/update")
    public String customerUpdatePost(@ModelAttribute ManyCustomers manyCustomers, Principal principal) {

       /* Long id = customerDto.getKvTable().getId();
        if (Long.compare(id, kvTableService.getOneKvTable(id).get().getId()) == 0){

        }
            customerDto.setKvTable(kvTableService.getOneKvTable(id).get());*/
        Long id = manyCustomers.getKvTableId();
        manyCustomers.setKvTable(kvTableService.getOneKvTable(id).get());
        manyCustomers.setUserDto(userService.getUserFromUsername(principal.getName()));


        customerService.updateManyCustomer(manyCustomers);
        //seatsUtils.updateTables();
        return "redirect:/showCustomers";
    }

    @GetMapping("/customer/remove/{id}")
    public String customerRemove(@PathVariable Long id) {
        Optional<CustomerDto> customerDto = customerService.getOneCustomer(id);
        //customerDto.ifPresent(c -> kvTableService.decrementOccupiedPlaces(c.getKvTable().getId()));
        customerService.deleteCustomer(id);
        //seatsUtils.updateTables();
        return "redirect:/showCustomers";
    }
}
