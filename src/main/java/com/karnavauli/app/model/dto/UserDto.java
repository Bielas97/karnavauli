package com.karnavauli.app.model.dto;

import com.karnavauli.app.model.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class UserDto {
    private String username;
    private String password;
    private Role role;
    private String confirmPassword;
}
