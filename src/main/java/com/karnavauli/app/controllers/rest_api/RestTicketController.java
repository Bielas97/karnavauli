package com.karnavauli.app.controllers.rest_api;

import com.karnavauli.app.model.dto.TicketDto;
import com.karnavauli.app.model.dto.UserDto;
import com.karnavauli.app.model.entities.Ticket;
import com.karnavauli.app.model.jwt.Info;
import com.karnavauli.app.service.TicketService;
import com.karnavauli.app.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
public class RestTicketController {
    private TicketService ticketService;
    private UserService userService;

    public RestTicketController(TicketService ticketService, UserService userService) {
        this.ticketService = ticketService;
        this.userService = userService;
    }

    @GetMapping("/user/get-all-tickets")
    public List<TicketDto> getAllTickets(){
        return ticketService.getAll();
    }

    @PostMapping("/admin/add-ticket")
    public Info addTicket(@RequestBody TicketDto ticketDto){
        ticketService.addTicket(ticketDto);
        return Info.builder().message("Success! Ticket [" + ticketDto.getShortName() + "] added!").build();
    }

    @PutMapping("/admin/update-ticket/{id}")
    public Info updateTicket(@PathVariable Long id, @RequestBody TicketDto ticketDtoRequested, Principal principal){
        Info info = new Info("");
        TicketDto ticketDto = ticketService.getOneTicket(id).orElseThrow(NullPointerException::new);
        if(!ticketDtoRequested.getShortName().equals(ticketDto.getShortName())){
            info.setMessage("Ticket shortname [" + ticketDto.getShortName() + "] changed to [" + ticketDtoRequested.getShortName() + "]");
            ticketDto.setShortName(ticketDtoRequested.getShortName());
        }
        if(!ticketDtoRequested.getFullName().equals(ticketDto.getFullName())){
            if (info.getMessage().equals("")) {
                info.setMessage("Ticket fullname [" + ticketDto.getFullName() + "] changed to [" + ticketDtoRequested.getFullName() + "]");
            } else {
                info.setMessage(info.getMessage() + " Ticket fullname [" + ticketDto.getFullName() + "] changed to [" + ticketDtoRequested.getFullName() + "]");
            }
            ticketDto.setFullName(ticketDtoRequested.getFullName());
        }
        if(!ticketDtoRequested.getIsUni().equals(ticketDto.getIsUni())){
            if (info.getMessage().equals("")) {
                info.setMessage("Ticket isUni [" + ticketDto.getIsUni()+ "] changed to [" + ticketDtoRequested.getIsUni()+ "]");
            } else {
                info.setMessage(info.getMessage() + " Ticket isUni [" + ticketDto.getIsUni()+ "] changed to [" + ticketDtoRequested.getIsUni()+ "]");
            }
            ticketDto.setIsUni(ticketDtoRequested.getIsUni());
        }

        UserDto userDto = userService.getUserDtoFromUsername(principal.getName());
        ticketService.updateTicket(userDto, ticketDto);
        return info;
    }

    @DeleteMapping("/admin/delete-ticket/{id}")
    public Info deleteTicket(@PathVariable Long id, Principal principal){
        Ticket ticket = ticketService.deleteTicket(id, userService.getUserDtoFromUsername(principal.getName()));
        return Info.builder().message("Success! Ticket [" + ticket.getFullName() + "] deleted!").build();
    }
}
