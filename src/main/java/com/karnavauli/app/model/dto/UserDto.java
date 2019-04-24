package com.karnavauli.app.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.karnavauli.app.model.entities.Ticket;
import com.karnavauli.app.model.enums.Role;
import lombok.*;

import java.util.List;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class UserDto {
    private Long id;
    private String username;
    private String password;
    private Role role;
    private Integer numberOfTickets;
    @JsonIgnore
    private String confirmPassword;
    @JsonIgnore
    private Set<Ticket> tickets;
    private String ticketShortname;

    @Override
    public String toString() {
        return "UserDto{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", role=" + role +
                ", numberOfTickets=" + numberOfTickets +
                ", confirmPassword='" + confirmPassword + '\'' +
                ", tickets=" + tickets +
                '}';
    }
}
