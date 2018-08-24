package com.karnavauli.app.service;

import com.karnavauli.app.model.dto.TicketDto;

import java.util.List;
import java.util.Optional;

public interface TicketService {
    void addOrUpdateTicket(TicketDto ticketDto);
    void deleteTicket(Long id);
    Optional<TicketDto> getOneTicket(Long id);
    List<TicketDto> getAll();
}
