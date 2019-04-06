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
    @Column(name = "uczelnia")
    private Boolean isUni;

    //06-0402109 zamineiam to relacje na UNIDIRECTIONAL
    /*@ManyToMany(mappedBy = "tickets", fetch = FetchType.EAGER)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<User> ticketDealers;*/

    @OneToMany(mappedBy = "ticket")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<KvTable> tables;

}
