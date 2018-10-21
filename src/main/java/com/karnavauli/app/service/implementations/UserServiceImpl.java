package com.karnavauli.app.service.implementations;

import com.karnavauli.app.model.enums.Role;
import com.karnavauli.app.model.entities.User;
import com.karnavauli.app.model.dto.UserDto;
import com.karnavauli.app.repository.UserRepository;
import com.karnavauli.app.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;
    private ModelMapper modelMapper;

    public UserServiceImpl(UserRepository userRepository, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public void addOrUpdateUser(UserDto userDto) {
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
    public User getUserDtoFromUsername(String username) {
        //return modelMapper.map(userRepository.findByUsername(username), UserDto.class);
        return userRepository.findByUsername(username);
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
}
