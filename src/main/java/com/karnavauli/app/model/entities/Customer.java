package com.karnavauli.app.model.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity(name = "klienci")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Customer {
    @Id
    @GeneratedValue
    private Long id;
    @Column(name = "imie")
    private String name;
    @Column(name = "nazwisko")
    private String surname;
    @Column(name = "wydział")
    private String faculty;
    @Column(name = "czy_jest_index")
    private Boolean isIndex;
    //many to one
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne
    @JoinColumn(name = "kvTable_id")
    private KvTable kvTable;
}
