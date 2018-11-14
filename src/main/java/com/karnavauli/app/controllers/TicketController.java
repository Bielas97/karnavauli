package com.karnavauli.app.controllers;

import com.karnavauli.app.model.dto.TicketDto;
import com.karnavauli.app.model.dto.UserDto;
import com.karnavauli.app.model.enums.Role;
import com.karnavauli.app.service.TicketService;
import com.karnavauli.app.service.UserService;
import com.karnavauli.app.validators.TicketValidator;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

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
        return "tickets/showTickets";
    }

    @GetMapping("/addTicket")
    public String addTicket(Model model) {
        model.addAttribute("ticket", new TicketDto());
        model.addAttribute("sellers", userService.getAll());
        return "tickets/addTicket";
    }

    @PostMapping("/addTicket")
    public String addTicketPost(@ModelAttribute TicketDto ticketDto, BindingResult result, Principal principal) {
        //List<User> users = Collections.singletonList(userService.getUserFromUsername(principal.getName()));
        //ticketDto.setTicketDealers(users);
        //System.out.println(ticketService.getTablesForUser(userService.getUserFromUsername(principal.getName()).getId()));
        UserDto userDto = userService.getUserDtoFromUsername(principal.getName());
        userService.addUsersToTickets(userDto);
        ticketService.addTicketsToUsers(ticketDto);

        //System.out.println("----------------------" + ticketService.getTablesForUser(userService.getUserDtoFromUsername(principal.getName()).getId()));
        // ticketService.addOrUpdateTicket(ticketDto);
        //System.out.println(ticketDto);
        return "redirect:/tickets";
    }

    @GetMapping("/ticket/update/{id}")
    public String ticketUpdate(Model model, @PathVariable Long id) {
        model.addAttribute("role", Role.values());
        model.addAttribute("ticket", ticketService.getOneTicket(id).orElseThrow(NullPointerException::new));
        model.addAttribute("sellers", userService.getAll());
        TicketDto ticketDto = ticketService.getOneTicket(id).orElseThrow(NullPointerException::new);
        ticketService.removeTicketDealers(ticketDto);
        return "tickets/updateTicket";
    }

    @PostMapping("/ticket/update")
    public String ticketUpdatePost(@ModelAttribute TicketDto ticketDto, Principal principal) {
        UserDto userDto = userService.getUserDtoFromUsername(principal.getName());
        userService.addUsersToTickets(userDto);
        ticketService.addTicketsToUsers(ticketDto);
        //ticketService.addOrUpdateTicket(ticketDto);
        return "redirect:/tickets";
    }

    @GetMapping("/ticket/remove/{id}")
    public String ticketRemove(@PathVariable Long id) {
        ticketService.deleteTicket(id);
        return "redirect:/tickets";
    }
}
