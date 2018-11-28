package com.karnavauli.app.controllers;

import com.karnavauli.app.exceptions.ExceptionCode;
import com.karnavauli.app.exceptions.MyException;
import com.karnavauli.app.model.dto.CustomerDto;
import com.karnavauli.app.model.dto.KvTableDto;
import com.karnavauli.app.model.dto.ManyCustomers;
import com.karnavauli.app.model.entities.User;
import com.karnavauli.app.model.enums.Role;
import com.karnavauli.app.service.CustomerService;
import com.karnavauli.app.service.KvTableService;
import com.karnavauli.app.service.TicketService;
import com.karnavauli.app.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Controller
public class CustomerController {
    private CustomerService customerService;
    private UserService userService;
    private KvTableService kvTableService;
    private TicketService ticketService;

    private int priceToBePaid = 0;
    private ManyCustomers customersWithPrice = null;


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
        //List<KvTableDto> free = kvTableService.getFreeTablesForAmountOfPeople(amountOfTickets);
        List<KvTableDto> freeForUser;
        if (userService.getUserDtoFromUsername(principal.getName()).getRole().equals(Role.CEO)) {
            freeForUser = kvTableService.getFreeTablesForAmountOfPeople(amountOfTickets);
        } else {
            freeForUser = ticketService.getTablesForUser(userService.getUserDtoFromUsername(principal.getName()).getId());
            //jakis kodzik z filterkiem do amountOfTickets
        }
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

        model.addAttribute("tables", freeForUser);

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
        //TODO:
        return "customers/customerForm";
    }

    @PostMapping("/addCustomer")
    public String addCustomerPost(/*@Valid*/ @ModelAttribute ManyCustomers manyCustomers, BindingResult result, Principal principal) {
        User user = userService.getUserFromUsername(principal.getName());
        manyCustomers.setUserDto(user);
        Long id = manyCustomers.getKvTableId();
        kvTableService.getOneKvTable(id).ifPresent(kvTableDto -> {
            manyCustomers.setKvTable(kvTableDto);
            customerService.fillAmountOfOccupiedPlaces(kvTableDto, manyCustomers.getCustomers().size());
            if (customerService.OccupiedPlacesAreGreaterThanMax(kvTableDto)) {
                System.out.println("leci wyjatek");
                System.out.println(customerService.getAmountOfOccupiedPlaces());
                throw new MyException(ExceptionCode.MAX_PLACES, "NOT ANYMORE PLACES LEFT EXCEPTION");
            }
        });

        //manyCustomers.setKvTable(kvTableService.getOneKvTable(id).get());
        //kvTableService.incrementOccupiedPlaces(id, manyCustomers.getCustomers().size());

        customerService.addManyCustomers(manyCustomers);
        priceToBePaid = customerService.countPriceToBePaid(manyCustomers);
        System.out.println(priceToBePaid);
        customersWithPrice = manyCustomers;

        return "redirect:/price";
    }

    @GetMapping("/price")
    public String countPriceToBePaid(Model model) {
        System.out.println("----->" + priceToBePaid);
        model.addAttribute("priceToBePaid", priceToBePaid);
        int elo = 0;
        model.addAttribute(elo);

        return "calculator/price";
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
        return "customers/customers";
    }


    @GetMapping("/customer/update/{id}")
    public String customerUpdate(Model model, @PathVariable Long id) {
        ManyCustomers manyCustomers = new ManyCustomers(1);
        CustomerDto customerDto = customerService.getOneCustomer(id).orElseThrow(NullPointerException::new);
        List<CustomerDto> customerDtoList = new ArrayList<>();
        customerDtoList.add(customerDto);
        manyCustomers.setCustomers(customerDtoList);

        model.addAttribute("manyCustomers", manyCustomers);

        model.addAttribute("errors", new HashMap<>());

        List<KvTableDto> freeTablesPlusCurrenTable = kvTableService.getFreeTablesPlusCurrentTable(manyCustomers);

        model.addAttribute("tables", freeTablesPlusCurrenTable);
        // model.addAttribute("isAnySeatFree", isAnySeatFree);
        model.addAttribute("isAnySeatFree", true);

        return "customers/updateCustomer";
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
        KvTableDto kvTableDto = customerDto.get().getKvTable();
        customerService.decrementAmountOfOccupiedPlaces(kvTableDto);
        /*customerDto.ifPresent(c -> {
            kvTableService.getOneKvTable(c.getKvTable().getId()).ifPresent(kvTableDto -> {
                customerService.decrementAmountOfOccupiedPlaces(kvTableDto);
            });
        });*/
        customerService.deleteCustomer(id);
        //seatsUtils.updateTables();
        return "redirect:/showCustomers";
    }
}
