package com.karnavauli.app.controllers.rest_api;

import com.karnavauli.app.model.dto.UserDto;
import com.karnavauli.app.model.jwt.Info;
import com.karnavauli.app.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class RestMainController {
    private UserService userService;

    public RestMainController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/admin")
    public Info admin() {
        return Info.builder().message("to jest admin").build();
    }

    @GetMapping("/user")
    public Info user() {
        return Info.builder().message("TO JEST USER").build();
    }

    /*@GetMapping("/admin/users")
    public List<UserDto> getAllUsers(){
        return userService.getAll();
    }*/
}
