/*
package com.karnavauli.app.controllers;

import com.karnavauli.app.model.dto.KvTableDto;
import com.karnavauli.app.model.entities.Ticket;
import com.karnavauli.app.service.KvTableService;
import com.karnavauli.app.service.TicketService;
import com.karnavauli.app.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;

@Controller
public class BasicController {
    private UserService userService;
    private KvTableService kvTableService;
    private TicketService ticketService;
    private ModelMapper modelMapper;

    public BasicController(UserService userService, KvTableService kvTableService, TicketService ticketService, ModelMapper modelMapper) {
        this.userService = userService;
        this.kvTableService = kvTableService;
        this.ticketService = ticketService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/")
    public String welcome(Model model, Principal principal) {
        String username = principal.getName();
        model.addAttribute("numberOfTickets", userService.getUserFromUsername(principal.getName()).getNumberOfTickets());
        model.addAttribute("user", username);
        return "index";
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
        model.addAttribute("tables", kvTableService.getAll());
        return "kvtables/tables";
    }

    @GetMapping("/table/update/{id}")
    public String bookTable(Model model, @PathVariable Long id) {
        model.addAttribute("kvTable", kvTableService.getOneKvTable(id).orElseThrow(NullPointerException::new));
        model.addAttribute("tickets", ticketService.getAll());
        return "kvtables/updateTable";
    }

    @PostMapping("/table/update")
    public String bookTablePost(@ModelAttribute KvTableDto kvTable) {
        ticketService.getOneTicketByFullName(kvTable.getOwner()).ifPresent(ticketDto -> kvTable.setTicket(modelMapper.map(ticketDto, Ticket.class)));
        kvTable.setOccupiedPlaces(kvTable.getMaxPlaces());
        kvTableService.addOrUpdateKvTable(kvTable);
        return "redirect:/tables";
    }

}
*/
