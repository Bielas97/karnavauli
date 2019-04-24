/*
package com.karnavauli.app.controllers;

import com.karnavauli.app.model.dto.CustomerDto;
import com.karnavauli.app.model.dto.KvTableDto;
import com.karnavauli.app.model.dto.ManyCustomers;
import com.karnavauli.app.model.dto.UserDto;
import com.karnavauli.app.model.entities.KvTable;
import com.karnavauli.app.repository.KvTableRepository;
import com.karnavauli.app.service.CustomerService;
import com.karnavauli.app.service.KvTableService;
import com.karnavauli.app.service.TicketService;
import com.karnavauli.app.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;
import java.util.*;

@Controller
@Slf4j
public class CustomerController {
    private CustomerService customerService;
    private UserService userService;
    private KvTableService kvTableService;
    private TicketService ticketService;

    private int priceToBePaid = 0;

    public CustomerController(CustomerService customerService, UserService userService, KvTableService kvTableService, TicketService ticketService) {
        this.customerService = customerService;
        this.userService = userService;
        this.kvTableService = kvTableService;
        this.ticketService = ticketService;

    }

    @GetMapping("/showCustomers")
    public String customers(Model model, Principal principal) {
        model.addAttribute("customers", customerService.getAll());
        //liczba dostepnych biletow do sprzedania przez danego uzytkownika
        model.addAttribute("numberOfTickets", userService.getUserFromUsername(principal.getName()).getNumberOfTickets());
        //nazwa zalogowanego usera
        model.addAttribute("user", principal.getName());
        //model.addAttribute("mapa", customerService.getAmountOfOccupiedPlaces());
        return "customers/customers";
    }

    @GetMapping("/addCustomer/{amountOfTickets}")
    public String addCustomerGet(Model model, @PathVariable int amountOfTickets, Principal principal) {
        UserDto userDto = userService.getUserDtoFromUsername(principal.getName());

        List<KvTableDto> freeForUser = kvTableService.getFreeTablesForUser(userDto, amountOfTickets);

        model.addAttribute("manyCustomers", new ManyCustomers(amountOfTickets));
        model.addAttribute("errors", new HashMap<>());
        model.addAttribute("freeTablesForUser", freeForUser);
        model.addAttribute("numberOfFreeSeats", kvTableService.getNumberOfAllFreeSeats());
        model.addAttribute("amountOfTickets", amountOfTickets);
        if (amountOfTickets > kvTableService.getNumberOfAllFreeSeats()) {
            model.addAttribute("notEnoughPlaces", true);
        } else {
            model.addAttribute("notEnoughPlaces", false);
        }
        model.addAttribute("numberOfTicketsForUser", userService.getUserFromUsername(principal.getName()).getNumberOfTickets());
        Map<String, Integer> getFreeTablesForUser = ticketService.getFreeForUser(userDto);
        model.addAttribute("freeTablesForUserGroundFloor", ticketService.getGroundFloorTables(getFreeTablesForUser));
        model.addAttribute("freeTablesForUserFirstFloor", ticketService.getFirstFloorTables(getFreeTablesForUser));
        model.addAttribute("freeTablesForUserSecondFloor", ticketService.getSecondFloorTables(getFreeTablesForUser));

        return "customers/customerForm";
    }

    @PostMapping("/addCustomer")
    public String addCustomerPost(@ModelAttribute ManyCustomers manyCustomers, BindingResult result, Principal principal) {
        customerService.addManyCustomers(userService.getUserFromUsername(principal.getName()), manyCustomers);
        priceToBePaid = customerService.countPriceToBePaid(manyCustomers);
        return "redirect:/price";
    }

    @GetMapping("/price")
    public String countPriceToBePaid(Model model) {
        model.addAttribute("priceToBePaid", priceToBePaid);
        int elo = 0;
        model.addAttribute(elo);

        return "calculator/price";
    }

    @GetMapping("/customer/update/{id}")
    public String customerUpdate(Model model, @PathVariable Long id, Principal principal) {
        ManyCustomers manyCustomers = new ManyCustomers(1);
        CustomerDto customerDto = customerService.getOneCustomer(id).orElseThrow(NullPointerException::new);
        List<CustomerDto> customerDtoList = new ArrayList<>();
        customerDtoList.add(customerDto);
        manyCustomers.setCustomers(customerDtoList);

        model.addAttribute("manyCustomers", manyCustomers);

        model.addAttribute("errors", new HashMap<>());

        UserDto userDto = userService.getUserDtoFromUsername(principal.getName());
        List<KvTableDto> freeTablesPlusCurrentTable = kvTableService.getFreeTablesPlusCurrentTable(userDto, manyCustomers);

        model.addAttribute("tables", freeTablesPlusCurrentTable);
        // model.addAttribute("isAnySeatFree", isAnySeatFree);
        model.addAttribute("isAnySeatFree", true);

        return "customers/updateCustomer";
    }


    @PostMapping("/customer/update")
    public String customerUpdatePost(@ModelAttribute ManyCustomers manyCustomers, Principal principal) {
        manyCustomers.setKvTable(kvTableService.getOneKvTable(manyCustomers.getKvTableId()).orElseThrow(NullPointerException::new));
        manyCustomers.setUserDto(userService.getUserFromUsername(principal.getName()));
        customerService.updateManyCustomer(manyCustomers);
        return "redirect:/showCustomers";
    }

    @GetMapping("/customer/remove/{id}")
    public String customerRemove(@PathVariable Long id) {
        customerService.deleteCustomer(id);
        return "redirect:/showCustomers";
    }
}
*/
