package com.karnavauli.app.service;

import com.karnavauli.app.exceptions.ExceptionCode;
import com.karnavauli.app.exceptions.MyException;
import com.karnavauli.app.model.dto.KvTableDto;
import com.karnavauli.app.model.dto.TicketDto;
import com.karnavauli.app.model.dto.UserDto;
import com.karnavauli.app.model.entities.Ticket;
import com.karnavauli.app.model.entities.User;
import com.karnavauli.app.model.enums.Role;
import com.karnavauli.app.repository.TicketRepository;
import com.karnavauli.app.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
public class TicketService {
    private ModelMapper modelMapper;
    private TicketRepository ticketRepository;
    private UserRepository userRepository;


    public TicketService(ModelMapper modelMapper, TicketRepository ticketRepository, UserRepository userRepository) {
        this.modelMapper = modelMapper;
        this.ticketRepository = ticketRepository;
        this.userRepository = userRepository;
    }

    public Ticket addOrUpdateTicket(TicketDto ticketDto) {
        log.info("Adding or updating Ticket: '" + ticketDto + "'");
        try {
            return ticketRepository.save(modelMapper.map(ticketDto, Ticket.class));
        } catch (Exception e) {
            e.printStackTrace();
            throw new MyException(ExceptionCode.SERVICE, "ADDING OR UPDATING TICKET EXCEPTION: " + e.getMessage());
        }
    }

    public Ticket deleteTicket(Long id, UserDto userDto) {
        log.info("Deleting Ticket with id: '" + id + "'");
        try {
            Ticket ticketToBeReturned = ticketRepository.getOne(id);
            userDto.getTickets()
                    .removeIf(ticket -> ticket.getId().equals(id));
            userRepository.save(modelMapper.map(userDto, User.class));
            ticketRepository.deleteById(id);
            return ticketToBeReturned;
        } catch (Exception e) {
            e.printStackTrace();
            throw new MyException(ExceptionCode.SERVICE, "DELETING TICKET EXCEPTION: " + e.getMessage());
        }
    }

    public Optional<TicketDto> getOneTicket(Long id) {
        log.info("Getting one ticket with id " + id);
        try {
            return ticketRepository.findById(id).map(t -> modelMapper.map(t, TicketDto.class));
        } catch (Exception e) {
            throw new MyException(ExceptionCode.SERVICE, "GETTING ONE TICKET EXCEPTION: " + e.getMessage());
        }
    }

    public Optional<TicketDto> getOneTicketByFullName(String fullname) {
        log.info("Getting one ticket with fullname: " + fullname);
        try {
            return Optional.of(modelMapper.map(ticketRepository.findByFullName(fullname), TicketDto.class));
        } catch (Exception e) {
            throw new MyException(ExceptionCode.SERVICE, "GET TICKET BY FULL NAME EXCEPTION: " + e.getMessage());
        }
    }

    public List<TicketDto> getAll() {
        log.info("Getting all tickets");
        try {
            return ticketRepository.findAll()
                    .stream()
                    .map(c -> modelMapper.map(c, TicketDto.class))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new MyException(ExceptionCode.SERVICE, "GETTING ALL TICKETS EXCEPTION: " + e.getMessage());
        }
    }

    public Map<String, Integer> getFreeForUser(UserDto userDto) {
        List<KvTableDto> kvTablesForUser = getTablesForUser(userDto.getId());
        Map<String, Integer> freeTablesForUser = new HashMap<>();

        kvTablesForUser.forEach(kvTableDto -> freeTablesForUser.put(
                kvTableDto.getName(),
                kvTableDto.getMaxPlaces() - kvTableDto.getSoldPlaces()
        ));

        return freeTablesForUser;
    }

    public Map<String, Integer> getGroundFloorTables(Map<String, Integer> map) {
        return map.entrySet()
                .stream()
                .filter(m -> m.getKey().charAt(1) == '0')
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public Map<String, Integer> getFirstFloorTables(Map<String, Integer> map) {
        return map.entrySet()
                .stream()
                .filter(m -> m.getKey().charAt(1) == '1')
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public Map<String, Integer> getSecondFloorTables(Map<String, Integer> map) {
        return map.entrySet()
                .stream()
                .filter(m -> m.getKey().charAt(1) == '2')
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }


    public List<KvTableDto> getTablesForUser(Long userId) {
        try {
            return userRepository.findById(userId).orElseThrow(NullPointerException::new)
                    .getTickets()
                    .stream()
                    .flatMap(ticket -> ticket.getTables().stream())
                    .distinct()
                    .map(kvTable -> modelMapper.map(kvTable, KvTableDto.class))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            throw new MyException(ExceptionCode.SERVICE, "GET TABLES FOR USER EXCEPTION: " + e.getMessage());
        }
    }


    public Ticket addTicket(TicketDto ticketDto) {
        try {
            Set<User> admins = userRepository.findByRole(Role.CEO);
            Ticket ticket = modelMapper.map(ticketDto, Ticket.class);
            Ticket ticket1 = ticketRepository.save(ticket);
            admins.forEach(user1 -> user1.getTickets().add(ticket1));
            userRepository.saveAll(admins);
            log.info("Ticket: " + ticket1 + " added!");
            return ticket1;
        } catch (Exception e) {
            e.printStackTrace();
            throw new MyException(ExceptionCode.SERVICE, "ADD TICKET EXCEPTION: " + e.getMessage());
        }
    }

    public Ticket updateTicket(UserDto userDto, TicketDto ticketDto) {
        log.info("Updating ticket: " + ticketDto);
        try {
            if (userDto == null || ticketDto == null) {
                throw new NullPointerException("USERDTO OR TICKETDTO IS NULL");
            }
            return ticketRepository.save(modelMapper.map(ticketDto, Ticket.class));
        } catch (Exception e) {
            e.printStackTrace();
            throw new MyException(ExceptionCode.SERVICE, "UPDATE TICKET EXCEPTION: " + e.getMessage());
        }
    }
}
