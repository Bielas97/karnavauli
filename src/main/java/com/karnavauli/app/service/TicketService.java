package com.karnavauli.app.service;

import com.karnavauli.app.exceptions.ExceptionCode;
import com.karnavauli.app.exceptions.MyException;
import com.karnavauli.app.model.dto.KvTableDto;
import com.karnavauli.app.model.dto.ManyCustomers;
import com.karnavauli.app.model.dto.TicketDto;
import com.karnavauli.app.model.dto.UserDto;
import com.karnavauli.app.model.entities.KvTable;
import com.karnavauli.app.model.entities.Ticket;
import com.karnavauli.app.model.entities.User;
import com.karnavauli.app.repository.TicketRepository;
import com.karnavauli.app.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

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

    public void addOrUpdateTicket(TicketDto ticketDto) {
        try {
            ticketRepository.save(modelMapper.map(ticketDto, Ticket.class));
        } catch (Exception e) {
            e.printStackTrace();
            throw new MyException(ExceptionCode.SERVICE, "ADDING OR UPDATING TICKET EXCEPTION: " + e.getMessage());
        }
    }

    public void deleteTicket(Long id, UserDto userDto) {
        try {
            //TODO zapytac - nie da sie moze jakiegos cascade dac na userze zeby tego nie robic??
            userDto.getTickets()
                    .removeIf(ticket -> ticket.getId().equals(id));
            userRepository.save(modelMapper.map(userDto, User.class));

            ticketRepository.deleteById(id);
        } catch (Exception e) {
            e.printStackTrace();
            throw new MyException(ExceptionCode.SERVICE, "DELETING TICKET EXCEPTION: " + e.getMessage());
        }
    }

    public Optional<TicketDto> getOneTicket(Long id) {
        try {
            return ticketRepository.findById(id).map(t -> modelMapper.map(t, TicketDto.class));
        } catch (Exception e) {
            throw new MyException(ExceptionCode.SERVICE, "GETTING ONE TICKET EXCEPTION: " + e.getMessage());
        }
    }

    public TicketDto getOneTicketByFullName(String fullname) {
        try {
            return modelMapper.map(ticketRepository.findByFullName(fullname), TicketDto.class);
        } catch (Exception e) {
            throw new MyException(ExceptionCode.SERVICE, "GET TICKET BY FULL NAME EXCEPTION: " + e.getMessage());
        }
    }

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
        try {
            return ticketRepository.findAll()
                    .stream()
                    .map(c -> modelMapper.map(c, TicketDto.class))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new MyException(ExceptionCode.SERVICE, "GETTING ALL TICKETS EXCEPTION: " + e.getMessage());
        }
    }

    public void addTicketsToUsers(TicketDto ticketDto) {
        try {
            if (ticketDto == null) {
                throw new NullPointerException("TICKET DTO IS NULL");
            }


            //wersja nowa:
           /* List<User> userList = ticketDto.getTicketDealers()
                    .stream()
                    .filter(u -> u.getId() != null)
                    .map(u ->{
                        User user = userRepository.findById(u.getId()).orElseThrow(NullPointerException::new);
                        Ticket ticket = modelMapper.map(ticketDto, Ticket.class);

                        user.getTickets().add(ticket);
                        return user;
                    })
                    .collect(Collectors.toList());*/
            //.forEach(user -> userRepository.save(user));


            //wersja pierwotna
            /*ticketDto.setTicketDealers(ticketDto.getTicketDealers()
                    .stream()
                    .filter(user -> user.getId() != null)
                    .collect(Collectors.toList())
            );

            Ticket ticket = ticketRepository.save(modelMapper.map(ticketDto, Ticket.class));
            List<User> users = ticketDto
                    .getTicketDealers()
                    .stream()
                    .map(u -> {
                        User user = userRepository.findById(u.getId()).orElseThrow(NullPointerException::new);
                        user.getTickets().add(ticket);
                        //user.getTickets().addAll(Collections.singletonList(ticket));
                        return user;
                    })
                    .collect(Collectors.toList());
            userRepository.saveAll(users);*/
        } catch (Exception e) {
            e.printStackTrace();
            throw new MyException(ExceptionCode.SERVICE, "ADD TICKET TO USER EXCEPTION: " + e.getMessage());
        }
    }


    public Map<String, Integer> getFreeForUser(UserDto userDto) {
        List<KvTableDto> kvTablesForUser = getTablesForUser(userDto.getId());
        Map<String, Integer> freeTablesForUser = new HashMap<>();

        kvTablesForUser.forEach(kvTableDto -> {
            freeTablesForUser.put(kvTableDto.getName(), kvTableDto.getMaxPlaces() - kvTableDto.getSoldPlaces());
        });

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

    /*public void removeTicketDealers(TicketDto ticketDto) {
        try {
            ticketDto.getTicketDealers().clear();
        } catch (Exception e) {
            throw new MyException(ExceptionCode.SERVICE, "REMOVE TICKET DEALERS EXCEPTION: " + e.getMessage());
        }
    }*/

    /*public void removeDuplicatesFromTicketDealers(TicketDto ticketDto) {
        try {
            Set<String> usersNames = ticketDto.getTicketDealers()
                    .stream()
                    .map(User::getUsername)
                    .collect(Collectors.toSet());
            ticketDto.getTicketDealers().removeIf(user -> usersNames.contains(user.getUsername()));
        } catch (Exception e) {
            throw new MyException(ExceptionCode.SERVICE, "REMOVE DUPLICATES FROM TICKET DEALERS EXCEPTION: " + e.getMessage());
        }
    }*/

    public void addTicket(UserDto userDto, TicketDto ticketDto) {
        try {
            if (userDto == null || ticketDto == null) {
                throw new NullPointerException("USERDTO or TICKETDTO IS NULL");
            }

            userDto.getTickets().add(modelMapper.map(ticketDto, Ticket.class));
            User user = modelMapper.map(userDto, User.class);
            userRepository.save(user);


            /*ticketDto.getTicketDealers()
                    .stream()
                    .filter(u -> u.getId() != null)
                    .map(u ->{
                        User user = userRepository.findById(u.getId()).orElseThrow(NullPointerException::new);
                        Ticket ticket = modelMapper.map(ticketDto, Ticket.class);

                        System.out.println(ticket.getTicketDealers());

                        user.getTickets().add(ticket);
                        return user;
                    })
                    .collect(Collectors.toList())
                    .forEach(user -> userRepository.save(user));*/
        } catch (Exception e) {
            e.printStackTrace();
            throw new MyException(ExceptionCode.SERVICE, "ADD TICKET EXCEPTION: " + e.getMessage());
        }
    }

    public void updateTicket(UserDto userDto, TicketDto ticketDto) {
        try {
            if (userDto == null || ticketDto == null) {
                throw new NullPointerException("USERDTO OR TICKETDTO IS NULL");
            }
            ticketRepository.save(modelMapper.map(ticketDto, Ticket.class));

        } catch (Exception e) {
            e.printStackTrace();
            throw new MyException(ExceptionCode.SERVICE, "UPDATE TICKET EXCEPTION: " + e.getMessage());
        }
    }

   /* private TicketDto setRolesWithDelimiter(TicketDto ticket) {
        String rolesWithDelimiter = ticket.getTicketDealers().stream()
                .map(Enum::name)
                .collect(Collectors.joining(","));
        ticket.setRoles(rolesWithDelimiter);
        return ticket;*/
}
