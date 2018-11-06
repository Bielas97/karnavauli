package com.karnavauli.app.model.entities;

import com.karnavauli.app.model.dto.KvTableDto;
import com.karnavauli.app.model.enums.Role;
import lombok.*;
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
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<User> ticketDealers;
    @OneToMany(mappedBy = "ticket")
    private List<KvTable> tables;


}
