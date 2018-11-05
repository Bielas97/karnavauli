package com.karnavauli.app.controllers;

import com.karnavauli.app.model.dto.TicketDto;
import com.karnavauli.app.model.entities.User;
import com.karnavauli.app.model.enums.Role;
import com.karnavauli.app.service.TicketService;
import com.karnavauli.app.service.UserService;
import com.karnavauli.app.validators.TicketValidator;
import com.karnavauli.app.validators.UserValidator;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Controller
public class TicketController {
    private UserService userService;
    private TicketService ticketService;

    public TicketController(UserService userService, TicketService ticketService) {
        this.userService = userService;
        this.ticketService = ticketService;
    }

    @InitBinder
    private void initializeBinder(WebDataBinder webDataBinder) {
        webDataBinder.setValidator(new TicketValidator());
    }

    @GetMapping("/tickets")
    public String showTickets(Model model) {
        model.addAttribute("tickets", ticketService.getAll());
        return "showTickets";
    }

    @GetMapping("/addTicket")
    public String addTicket(Model model) {
        model.addAttribute("ticket", new TicketDto());
        model.addAttribute("sellers", userService.getAll());
        return "addTicket";
    }

    @PostMapping("/addTicket")
    public String addTicketPost(@ModelAttribute TicketDto ticketDto, BindingResult result, Principal principal) {
        System.out.println("*******************************************");
        //List<User> users = Collections.singletonList(userService.getUserDtoFromUsername(principal.getName()));
        //ticketDto.setTicketDealers(users);
        ticketService.addOrUpdateTicket(ticketDto);
        System.out.println(ticketDto);
        return "redirect:/tickets";
    }

    @GetMapping("/ticket/update/{id}")
    public String ticketUpdate(Model model, @PathVariable Long id) {
        model.addAttribute("role", Role.values());
        model.addAttribute("ticket", ticketService.getOneTicket(id).orElseThrow(NullPointerException::new));
        return "updateTicket";
    }

    @PostMapping("/ticket/update")
    public String currencyUpdatePost(@ModelAttribute TicketDto ticketDto) {
        ticketService.addOrUpdateTicket(ticketDto);
        return "redirect:/tickets";
    }

    @GetMapping("/ticket/remove/{id}")
    public String customerRemove(@PathVariable Long id) {
        ticketService.deleteTicket(id);
        return "redirect:/tickets";
    }
}
