package com.karnavauli.app.service;

import com.karnavauli.app.model.entities.User;
import com.karnavauli.app.model.enums.Role;
import com.karnavauli.app.model.dto.UserDto;

import java.util.List;
import java.util.Optional;

public interface UserService {
    void addOrUpdateUser(UserDto userDto);
    void deleteUser(Long id);
    Optional<UserDto> getOneUser(Long id);
    List<UserDto> getAll();

    Long getUserIdFromUsername(String username);
    User getUserFromUsername(String username);
    void changeRole(Long id, Role role);
}
