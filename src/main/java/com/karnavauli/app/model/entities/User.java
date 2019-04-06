package com.karnavauli.app.model.entities;

import com.karnavauli.app.model.enums.Role;
import lombok.*;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class User {
    @Id
    @GeneratedValue
    private Long id;
    private String username;
    private String password;
    @Enumerated(EnumType.STRING)
    private Role role;
    private Integer numberOfTickets;
    @ManyToMany(cascade = {CascadeType.REFRESH  , CascadeType.DETACH, CascadeType.MERGE},  fetch = FetchType.EAGER)
    @JoinTable(
            name = "User_Ticket",
            joinColumns = { @JoinColumn(name = "user_id") },
            inverseJoinColumns = { @JoinColumn(name = "ticket_id") }
    )
    private List<Ticket> tickets;

    @Override
    public String toString() {
        return username;
    }
}
