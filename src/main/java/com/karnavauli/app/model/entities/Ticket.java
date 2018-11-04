package com.karnavauli.app.model.entities;

import com.karnavauli.app.model.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;

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
    @ManyToMany(mappedBy = "tickets", fetch = FetchType.EAGER)
    private List<User> ticketDealers;


}
