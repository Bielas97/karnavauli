package com.karnavauli.app.service;

import com.karnavauli.app.model.dto.UserDto;

import java.util.List;
import java.util.Optional;

public interface UserService {
    void addOrUpdateUser(UserDto userDto);
    void deleteUser(Long id);
    Optional<UserDto> getOneUser(Long id);
    List<UserDto> getAll();
}
