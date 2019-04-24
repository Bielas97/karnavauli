package com.karnavauli.app.controllers.rest_api;

import com.karnavauli.app.model.dto.UserDto;
import com.karnavauli.app.model.jwt.Info;
import com.karnavauli.app.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class RestRegisterController {
    private UserService userService;

    public RestRegisterController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/admin/users")
    public List<UserDto> getAllUsers(){
        return userService.getAll();
    }

    @PostMapping("/admin/register")
    public Info registerUser(@RequestBody UserDto userDto){
        userService.addUser(userDto);
        return Info.builder().message("Succes! User [" + userDto.getUsername() + "] added!").build();
    }
}
