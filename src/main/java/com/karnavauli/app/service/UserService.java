package com.karnavauli.app.service;

import com.karnavauli.app.exceptions.ExceptionCode;
import com.karnavauli.app.exceptions.MyException;
import com.karnavauli.app.model.dto.TicketDto;
import com.karnavauli.app.model.dto.UserDto;
import com.karnavauli.app.model.entities.Ticket;
import com.karnavauli.app.model.entities.User;
import com.karnavauli.app.model.enums.Role;
import com.karnavauli.app.repository.TicketRepository;
import com.karnavauli.app.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserService {
    private UserRepository userRepository;
    private ModelMapper modelMapper;
    private PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, ModelMapper modelMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public void updateUser(UserDto userDto) {
        try {
            userRepository.save(modelMapper.map(userDto, User.class));
        } catch (Exception e) {
            throw new MyException(ExceptionCode.SERVICE, "UPDATING USER EXCEPTION: " + e.getMessage());
        }
    }

    public void addUser(UserDto userDto) {
        try {
            userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
            userRepository.save(modelMapper.map(userDto, User.class));
        } catch (Exception e) {
            throw new MyException(ExceptionCode.SERVICE, "ADDING USER EXCEPTION: " + e.getMessage());
        }
    }

    public void deleteUser(Long id) {
        try {
            userRepository.deleteById(id);
        } catch (Exception e) {
            throw new MyException(ExceptionCode.SERVICE, "DELETING USER EXCEPTION: " + e.getMessage());
        }
    }

    public Optional<UserDto> getOneUser(Long id) {
        try {
            return userRepository.findById(id).map(u -> modelMapper.map(u, UserDto.class));
        } catch (Exception e) {
            throw new MyException(ExceptionCode.SERVICE, "GETTING ONE USER EXCEPTION: " + e.getMessage());
        }
    }

    public List<UserDto> getAll() {
        try {
            return userRepository.findAll()
                    .stream()
                    .map(u -> modelMapper.map(u, UserDto.class))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new MyException(ExceptionCode.SERVICE, "GETTING ALL USERS EXCEPTION: " + e.getMessage());
        }
    }

    public Long getUserIdFromUsername(String username) {
        try {
            return userRepository.findByUsername(username).getId();
        } catch (Exception e) {
            throw new MyException(ExceptionCode.SERVICE, "GETTING USER ID FROM USERNAME EXCEPTION: " + e.getMessage());
        }
    }

    public User getUserFromUsername(String username) {
        try {
            //return modelMapper.map(userRepository.findByUsername(username), UserDto.class);
            return userRepository.findByUsername(username);
        } catch (Exception e) {
            throw new MyException(ExceptionCode.SERVICE, "GETTING USER FROM USERNAME EXCEPTION: " + e.getMessage());
        }
    }

    public UserDto getUserDtoFromUsername(String username) {
        try {
            return modelMapper.map(userRepository.findByUsername(username), UserDto.class);
        } catch (Exception e) {
            throw new MyException(ExceptionCode.SERVICE, "GETTING USERDTO FROM USERNAME EXCEPTION: " + e.getMessage());
        }
    }

    public void changeRole(Long id, Role role) {
        try {
            User user = userRepository.findById(id).orElseThrow(() -> new NoSuchElementException("nie znaleziono uzytkownika"));
            user.setRole(role);
        } catch (Exception e) {
            throw new MyException(ExceptionCode.SERVICE, "CHANGING ROLE EXCEPTION: " + e.getMessage());
        }
    }

    public void incrementNumberOfTickets(UserDto userDto) {
        try {
            userDto.setNumberOfTickets(userDto.getNumberOfTickets() + 1);
            updateUser(userDto);
        } catch (Exception e) {
            throw new MyException(ExceptionCode.SERVICE, "INCREMENT NUMBER OF TICKETS EXCEPTION: " + e.getMessage());
        }
    }

    public void decerementNumberOfTickets(UserDto userDto, int size) {
        try {
            userDto.setNumberOfTickets(userDto.getNumberOfTickets() - size);
            updateUser(userDto);
        } catch (Exception e) {
            throw new MyException(ExceptionCode.SERVICE, "DECREMENT NUMBER OF TICKETS EXCEPTION: " + e.getMessage());
        }
    }


    public void decerementNumberOfTickets(UserDto userDto) {
        try {
            userDto.setNumberOfTickets(userDto.getNumberOfTickets() - 1);
            updateUser(userDto);
        } catch (Exception e) {
            throw new MyException(ExceptionCode.SERVICE, "DECREMENT NUMBER OF TICKETS EXCEPTION: " + e.getMessage());
        }
    }

    public boolean isUserTableEmpty() {
        return userRepository.count() == 0;
    }

    public UserDto getById(Long id) {
        try {
            return modelMapper.map(userRepository.findById(id).orElseThrow(NullPointerException::new), UserDto.class);
        } catch (Exception e) {
            throw new MyException(ExceptionCode.SERVICE, "GETTING USER BY ID EXCEPTION: " + e.getMessage());
        }
    }

   /* public void addUsersToTickets(UserDto userDto, TicketDto ticketDto) {
        try {
            if (userDto == null || ticketDto == null) {
                throw new NullPointerException("USERDTO or TICKETDTO IS NULL");
            }
            ticketDto.getTicketDealers()
                    .stream()
                    .filter(u -> u.getId() != null)
                    .map(u ->{
                        User user = userRepository.findById(u.getId()).orElseThrow(NullPointerException::new);
                        Ticket ticket = modelMapper.map(ticketDto, Ticket.class);

                        user.getTickets().add(ticket);
                        return user;
                    })
                    .collect(Collectors.toList())
                    .forEach(user -> userRepository.save(user));

          //zle
            User user = userRepository.save(modelMapper.map(userDto, User.class));
            List<Ticket> tickets = userDto
                    .getTickets()
                    .stream()
                    .map(t -> {
                        Ticket ticket = ticketRepository.findById(t.getId()).orElseThrow(NullPointerException::new);
                        ticket.getTicketDealers().addAll(Arrays.asList(user));
                        return ticket;
                    })
                    .distinct()
                    .collect(Collectors.toList());
            ticketRepository.saveAll(tickets);
        } catch (Exception e) {
            e.printStackTrace();
            throw new MyException(ExceptionCode.SERVICE, "ADD USERS TO TICKET EXCEPTION: " + e.getMessage());
        }
    }*/

    public void removeDuplciatedNames(List<User> users) {
        try {
            Set<String> userNames = users
                    .stream()
                    .map(User::getUsername)
                    .collect(Collectors.toSet());
            users.removeIf(us -> userNames.contains(us.getUsername()));
        } catch (Exception e) {
            throw new MyException(ExceptionCode.SERVICE, "REMOVE DUPLICATE NAMES EXCEPTION: " + e.getMessage());
        }
    }
}
