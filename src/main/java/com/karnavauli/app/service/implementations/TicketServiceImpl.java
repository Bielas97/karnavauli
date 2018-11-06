package com.karnavauli.app.service.implementations;

import com.karnavauli.app.exceptions.ExceptionCode;
import com.karnavauli.app.exceptions.MyException;
import com.karnavauli.app.model.dto.TicketDto;
import com.karnavauli.app.model.entities.KvTable;
import com.karnavauli.app.model.entities.Ticket;
import com.karnavauli.app.model.entities.User;
import com.karnavauli.app.repository.TicketRepository;
import com.karnavauli.app.repository.UserRepository;
import com.karnavauli.app.service.TicketService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class TicketServiceImpl implements TicketService {
    private ModelMapper modelMapper;
    private TicketRepository ticketRepository;
    private UserRepository userRepository;

    public TicketServiceImpl(ModelMapper modelMapper, TicketRepository ticketRepository, UserRepository userRepository) {
        this.modelMapper = modelMapper;
        this.ticketRepository = ticketRepository;
        this.userRepository = userRepository;
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

    @Override
    public void addTicketsToUsers(TicketDto ticketDto) {

        try {

            if (ticketDto == null) {
                throw new NullPointerException("TICKET DTO IS NULL");
            }
            ticketDto.setTicketDealers(ticketDto.getTicketDealers().stream()
                    .filter(user -> user.getId() != null)
                    .collect(Collectors.toList()));
            Ticket ticket = ticketRepository.save(modelMapper.map(ticketDto, Ticket.class));
            List<User> users = ticketDto
                    .getTicketDealers()
                    .stream()
                    .map(u -> {
                        User user = userRepository.findById(u.getId()).orElseThrow(NullPointerException::new);
                        user.getTickets().addAll(Arrays.asList(ticket));
                        return user;
                    })
                    .collect(Collectors.toList());
            userRepository.saveAll(users);
        } catch (Exception e) {
            e.printStackTrace();
            throw new MyException(ExceptionCode.SERVICE, "ADD RICKET TO USER EXCEPTION: " + e.getMessage());
        }
    }

    public List<KvTable> getTablesForUser(Long userId) {
        try {
            System.out.println("======="+userRepository.findById(userId).get().getTickets());
            return userRepository.findById(userId).orElseThrow(NullPointerException::new)
                    .getTickets()
                    .stream()
                    .flatMap(ticket -> ticket.getTables().stream())
                    .distinct()
                    .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            throw new MyException(ExceptionCode.SERVICE, "...");
        }
    }

   /* private TicketDto setRolesWithDelimiter(TicketDto ticket) {
        String rolesWithDelimiter = ticket.getTicketDealers().stream()
                .map(Enum::name)
                .collect(Collectors.joining(","));
        ticket.setRoles(rolesWithDelimiter);
        return ticket;*/
}
