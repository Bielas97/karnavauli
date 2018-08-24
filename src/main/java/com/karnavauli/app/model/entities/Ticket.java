package com.karnavauli.app.model.entities;

import com.karnavauli.app.model.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity(name = "Bilety")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Ticket {
    @Id
    @GeneratedValue
    private Long id;
    @Column(name = "skrot")
    private String shortName;
    @Column(name = "pelna_nazwa")
    private String fullName;
    @Column(name = "cena")
    private Double price;
    @Column(name = "uczelnia")
    private Boolean isUni;
    @ElementCollection(targetClass = Role.class)
    @JoinTable(name = "tblRole", joinColumns = @JoinColumn(name = "ticketID"))
    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    private List<Role> ticketDealers;

}
