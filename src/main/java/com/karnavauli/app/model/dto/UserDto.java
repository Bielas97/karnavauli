package com.karnavauli.app.model.dto;

import com.karnavauli.app.model.entities.Ticket;
import com.karnavauli.app.model.enums.Role;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
/*@Getter
@Setter
@EqualsAndHashCode*/
@Data
public class UserDto {
    private Long id;
    private String username;
    private String password;
    private Role role;
    private Integer numberOfTickets;
    private String confirmPassword;
    private List<Ticket> tickets;

    /*@Override
    public String toString() {
        return "" + username + " " + password;
    }*/

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
