package com.karnavauli.app.service;

import com.karnavauli.app.exceptions.ExceptionCode;
import com.karnavauli.app.exceptions.MyException;
import com.karnavauli.app.model.dto.CustomerDto;
import com.karnavauli.app.model.dto.TicketDto;
import com.karnavauli.app.model.dto.UserDto;
import com.karnavauli.app.model.entities.Ticket;
import com.karnavauli.app.model.entities.User;
import com.karnavauli.app.model.enums.Role;
import com.karnavauli.app.repository.TicketRepository;
import com.karnavauli.app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final TicketRepository ticketRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    public List<UserDto> getUsersByRole(Role role) {
        return userRepository.findByRole(role)
                .stream()
                .map(user -> modelMapper.map(user, UserDto.class))
                .collect(Collectors.toList());
    }


    public User updateUser(UserDto userDto) {
        log.info("Updating user: " + userDto);
        try {
            return userRepository.save(modelMapper.map(userDto, User.class));
        } catch (Exception e) {
            throw new MyException(ExceptionCode.SERVICE, "UPDATING USER EXCEPTION: " + e.getMessage());
        }
    }

    public User addUser(UserDto userDto) {
        log.info("Adding user: " + userDto);
        try {
            User user = modelMapper.map(userDto, User.class);
            Set<Ticket> tickets = null;
            if( userDto.getTickets() != null ) {
                tickets = userDto
                        .getTickets()
                        .stream()
                        .map(ticket -> ticketRepository.findById(ticket.getId()).orElseThrow(() -> new MyException(ExceptionCode.SERVICE, "EX")))
                        .collect(Collectors.toSet());
            }
            else if((userDto.getTicketShortname() != null && !userDto.getTicketShortname().equals("")) || userDto.getTickets().isEmpty()){
                tickets = ticketRepository.findAll()
                        .stream()
                        .filter(ticket -> ticket.getShortName().equals(userDto.getTicketShortname()))
                        .collect(Collectors.toSet());
            }
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setTickets(tickets);

            return userRepository.save(user);
        } catch (Exception e) {
            throw new MyException(ExceptionCode.SERVICE, "ADDING USER EXCEPTION: " + e.getMessage());
        }
    }

    public User deleteUser(Long id) {
        log.info("Deleting user with id: " + id);
        try {
            userRepository.deleteById(id);
            return userRepository.getOne(id);
        } catch (Exception e) {
            throw new MyException(ExceptionCode.SERVICE, "DELETING USER EXCEPTION: " + e.getMessage());
        }
    }

    public Optional<UserDto> getOneUser(Long id) {
        log.info("Getting one user with id: " + id);
        try {
            return userRepository.findById(id).map(u -> modelMapper.map(u, UserDto.class));
        } catch (Exception e) {
            throw new MyException(ExceptionCode.SERVICE, "GETTING ONE USER EXCEPTION: " + e.getMessage());
        }
    }

    public List<UserDto> getAll() {
        log.info("Getting all users: ");
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
