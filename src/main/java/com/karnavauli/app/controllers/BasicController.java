package com.karnavauli.app.controllers;

import com.karnavauli.app.model.dto.KvTableDto;
import com.karnavauli.app.model.dto.TicketDto;
import com.karnavauli.app.service.CustomerService;
import com.karnavauli.app.service.KvTableService;
import com.karnavauli.app.service.TicketService;
import com.karnavauli.app.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;
import java.util.Comparator;
import java.util.List;

@Controller
public class BasicController {
    private UserService userService;
    private KvTableService kvTableService;
    private TicketService ticketService;

    public BasicController(UserService userService, KvTableService kvTableService, TicketService ticketService) {
        this.userService = userService;
        this.kvTableService = kvTableService;
        this.ticketService = ticketService;
    }

    @GetMapping("/")
    public String welcome(Model model, Principal principal) {
        String username = principal.getName();
        //liczba dostepnych biletow do sprzedania przez danego uzytkownika
        model.addAttribute("numberOfTickets", userService.getUserFromUsername(principal.getName()).getNumberOfTickets());
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
        return "users/loginForm";
    }

    @GetMapping("/accessDenied")
    public String accessDenied() {
        return "errors/accessDenied";
    }

    @GetMapping("/login/error")
    public String loginError(Model model) {
        final String loginErrorMessage = "Nieprawidłowe dane logowania";
        model.addAttribute("error", loginErrorMessage);
        return "users/loginForm";
    }

    @GetMapping("/tables")
    public String tables(Model model) {
        List<KvTableDto> tables = kvTableService.getAll();
        tables.sort(Comparator.comparing(KvTableDto::getName));
        model.addAttribute("tables", tables);
        return "kvtables/tables";
    }

    @GetMapping("/table/update/{id}")
    public String bookTable(Model model, @PathVariable Long id) {
        model.addAttribute("kvTable", kvTableService.getOneKvTable(id).orElseThrow(NullPointerException::new));
        List<TicketDto> ticketDtos = ticketService.getAll();
        TicketDto ticketRegular = TicketDto.builder().fullName("regular").shortName("regular").build();
        ticketDtos.add(ticketRegular);
        model.addAttribute("tickets", ticketDtos);
        return "kvtables/updateTable";
    }

    @PostMapping("/table/update")
    public String bookTablePost(@ModelAttribute KvTableDto kvTable) {
        if (!kvTable.getOwner().equals("regular")) {
            kvTable.setTicketDto(ticketService.getOneTicketByFullName(kvTable.getOwner()));
        }
        kvTable.setOccupiedPlaces(kvTable.getMaxPlaces());
        kvTableService.addOrUpdateKvTable(kvTable);
        System.out.println(kvTable.getTicketDto());

        return "redirect:/tables";
    }

}
