package com.karnavauli.app.model.entities;

import lombok.*;

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
    @OneToMany(mappedBy = "ticket")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<KvTable> tables;

}
