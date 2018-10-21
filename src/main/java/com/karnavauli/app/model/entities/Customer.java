package com.karnavauli.app.model.entities;

import com.karnavauli.app.model.CustomerListener;
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
@EntityListeners(CustomerListener.class)
public class Customer {
    @Id
    @GeneratedValue
    private Long id;
    @Column(name = "imie")
    private String name;
    @Column(name = "nazwisko")
    private String surname;
    private String mail;
    @Column(name = "numer_telefonu")
    private String phoneNumber;
    @Column(name = "numer_indeksu")
    private String indexNumber;
    //many to one
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne
    @JoinColumn(name = "kvTable_id")
    private KvTable kvTable;
}
