package com.karnavauli.app.model.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class KvTable {
    @GeneratedValue()
    @Id
    private Long id;
    private String name;
    private Integer maxPlaces;
    private Integer occupiedPlaces;
    private Integer soldPlaces;
    /*@OneToMany(mappedBy = "kvTable")
    private List<Customer> customer;*/
    private String owner;
    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JoinColumn(name = "ticket_id")
    private Ticket ticket;
}
