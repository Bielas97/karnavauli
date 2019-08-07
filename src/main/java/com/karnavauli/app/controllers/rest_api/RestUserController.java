package com.karnavauli.app.controllers.rest_api;

import com.karnavauli.app.model.dto.UserDto;
import com.karnavauli.app.model.entities.User;
import com.karnavauli.app.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RestUserController {
    private final UserService userService;

    @PostMapping("/registerRest")
    public User register(@RequestBody UserDto user){
        return userService.addUser(user);
    }
}
