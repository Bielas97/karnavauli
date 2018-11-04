package com.karnavauli.app.service.implementations;

import com.karnavauli.app.model.dto.CustomerDto;
import com.karnavauli.app.model.dto.TicketDto;
import com.karnavauli.app.model.entities.Ticket;
import com.karnavauli.app.repository.TicketRepository;
import com.karnavauli.app.service.TicketService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TicketServiceImpl implements TicketService {
    private ModelMapper modelMapper;
    private TicketRepository ticketRepository;

    public TicketServiceImpl(ModelMapper modelMapper, TicketRepository ticketRepository) {
        this.modelMapper = modelMapper;
        this.ticketRepository = ticketRepository;
    }

    @Override
    public void addOrUpdateTicket(TicketDto ticketDto) {
        ticketRepository.save(modelMapper.map(ticketDto, Ticket.class));
    }

    @Override
    public void deleteTicket(Long id) {
        ticketRepository.deleteById(id);
    }

    @Override
    public Optional<TicketDto> getOneTicket(Long id) {
        return ticketRepository.findById(id).map(t -> modelMapper.map(t, TicketDto.class));
    }

    @Override
    public List<TicketDto> getAll() {
        /*List<TicketDto> tickets = ticketRepository.findAll()
                .stream()
                .map(t -> modelMapper.map(t, TicketDto.class))
                .collect(Collectors.toList());

          *//*all.forEach(el -> {
            List<Role> ticketDealers = el.getTicketDealers();
            String collect = ticketDealers.stream().map(Enum::name).collect(Collectors.joining(","));
            el.setRoles(collect);
        });*//*

        return tickets.stream()
                .map(this::setRolesWithDelimiter)
                .collect(Collectors.toList());*/
        return ticketRepository.findAll()
                .stream()
                .map(c -> modelMapper.map(c, TicketDto.class))
                .collect(Collectors.toList());
    }

   /* private TicketDto setRolesWithDelimiter(TicketDto ticket) {
        String rolesWithDelimiter = ticket.getTicketDealers().stream()
                .map(Enum::name)
                .collect(Collectors.joining(","));
        ticket.setRoles(rolesWithDelimiter);
        return ticket;*/
}
