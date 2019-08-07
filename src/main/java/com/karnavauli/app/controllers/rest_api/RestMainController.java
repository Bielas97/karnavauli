package com.karnavauli.app.controllers.rest_api;

import com.karnavauli.app.model.jwt.Info;
import com.karnavauli.app.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RestMainController {
    private final UserService userService;

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
