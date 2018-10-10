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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;
    private String name;
    private Integer maxPlaces;
    private Integer occupiedPlaces;
    @OneToMany(mappedBy = "kvTable")
    private List<Customer> customer;
}
