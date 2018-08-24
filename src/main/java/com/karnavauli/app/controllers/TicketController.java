package com.karnavauli.app.controllers;

import com.karnavauli.app.model.dto.CustomerDto;
import com.karnavauli.app.model.dto.TicketDto;
import com.karnavauli.app.model.enums.Role;
import com.karnavauli.app.service.TicketService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class TicketController {

    private TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @GetMapping("/tickets")
    public String showTickets(Model model) {
        model.addAttribute("tickets", ticketService.getAll());
        return "showTickets";
    }

    @GetMapping("/addTicket")
    public String addTicket(Model model) {
        model.addAttribute("ticket", new TicketDto());
        model.addAttribute("role", Role.values());
        return "addTicket";
    }

    @PostMapping("/addTicket")
    public String addTicketPost(@ModelAttribute TicketDto ticketDto) {
        ticketService.addOrUpdateTicket(ticketDto);
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
