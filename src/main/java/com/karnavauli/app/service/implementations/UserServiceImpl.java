package com.karnavauli.app.service.implementations;

import com.karnavauli.app.exceptions.ExceptionCode;
import com.karnavauli.app.exceptions.MyException;
import com.karnavauli.app.model.entities.Ticket;
import com.karnavauli.app.model.enums.Role;
import com.karnavauli.app.model.entities.User;
import com.karnavauli.app.model.dto.UserDto;
import com.karnavauli.app.repository.TicketRepository;
import com.karnavauli.app.repository.UserRepository;
import com.karnavauli.app.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;
    private ModelMapper modelMapper;
    private PasswordEncoder passwordEncoder;
    private TicketRepository ticketRepository;

    public UserServiceImpl(UserRepository userRepository, ModelMapper modelMapper, PasswordEncoder passwordEncoder, TicketRepository ticketRepository) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
        this.ticketRepository = ticketRepository;
    }

    @Override
    public void addOrUpdateUser(UserDto userDto) {
        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
        userRepository.save(modelMapper.map(userDto, User.class));
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public Optional<UserDto> getOneUser(Long id) {
        return userRepository.findById(id).map(u -> modelMapper.map(u, UserDto.class));
    }

    @Override
    public List<UserDto> getAll() {
        return userRepository.findAll()
                .stream()
                .map(u -> modelMapper.map(u, UserDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public Long getUserIdFromUsername(String username) {
        return userRepository.findByUsername(username).getId();
    }

    @Override
    public User getUserFromUsername(String username) {
        //return modelMapper.map(userRepository.findByUsername(username), UserDto.class);
        return userRepository.findByUsername(username);
    }

    @Override
    public UserDto getUserDtoFromUsername(String username) {
        return modelMapper.map(userRepository.findByUsername(username), UserDto.class);
    }

    @Override
    public void changeRole(Long id, Role role) {
        User user = userRepository.findById(id).orElseThrow(() -> new NoSuchElementException("nie znaleziono uzytkownika"));
        user.setRole(role);
    }

    @Override
    public void incrementNumberOfTickets(UserDto userDto) {
        userDto.setNumberOfTickets(userDto.getNumberOfTickets() + 1);
        addOrUpdateUser(userDto);
    }

    @Override
    public void decerementNumberOfTickets(UserDto userDto, int size) {
        userDto.setNumberOfTickets(userDto.getNumberOfTickets() - size);
        addOrUpdateUser(userDto);
    }


    @Override
    public void decerementNumberOfTickets(UserDto userDto) {
        userDto.setNumberOfTickets(userDto.getNumberOfTickets() - 1);
        addOrUpdateUser(userDto);
    }

    @Override
    public boolean isUserTableEmpty() {
        return userRepository.count() == 0;
    }

    @Override
    public UserDto getById(Long id) {
        return modelMapper.map(userRepository.findById(id).orElseThrow(NullPointerException::new), UserDto.class);
    }

    @Override
    public void addUsersToTickets(UserDto userDto) {
        try {
            if (userDto == null) {
                throw new NullPointerException("USERDTO IS NULL");
            }
            User user = userRepository.save(modelMapper.map(userDto, User.class));
            List<Ticket> tickets = userDto
                    .getTickets()
                    .stream()
                    .map(t -> {
                        Ticket ticket = ticketRepository.findById(t.getId()).orElseThrow(NullPointerException::new);
                        ticket.getTicketDealers().addAll(Arrays.asList(user));
                        return ticket;
                    })
                    .collect(Collectors.toList());
            ticketRepository.saveAll(tickets);
        } catch (Exception e) {
            e.printStackTrace();
            throw new MyException(ExceptionCode.SERVICE, "ADD USERS TO TICKET EXCEPTION: " + e.getMessage());
        }

    }
}
