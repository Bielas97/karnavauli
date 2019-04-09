package com.karnavauli.app.model.entities;

import com.karnavauli.app.model.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
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

    @ManyToMany(cascade = CascadeType.PERSIST,  fetch = FetchType.EAGER)
    @JoinTable(
            name = "User_Ticket",
            joinColumns = { @JoinColumn(name = "user_id") },
            inverseJoinColumns = { @JoinColumn(name = "ticket_id") }
    )
    private Set<Ticket> tickets;

    @Override
    public String toString() {
        return username;
    }
}
