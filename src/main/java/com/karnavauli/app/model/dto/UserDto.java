package com.karnavauli.app.model.dto;

import com.karnavauli.app.model.entities.Ticket;
import com.karnavauli.app.model.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class UserDto {
    private Long id;
    private String username;
    private String password;
    private Role role;
    private Integer numberOfTickets;
    private String confirmPassword;
    private List<Ticket> tickets;
}
