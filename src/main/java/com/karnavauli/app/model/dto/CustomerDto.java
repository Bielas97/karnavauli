package com.karnavauli.app.model.dto;

import com.karnavauli.app.model.entities.KvTable;
import com.karnavauli.app.model.entities.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerDto {
    private Long id;
    private String name;
    private String surname;
    private String mail;
    private String phoneNumber;
    private String indexNumber;
    //TODO: czy tu powinien byc user czy userDto?????
    private User user;
    private KvTableDto kvTable;

    private int numberOfTickets;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CustomerDto that = (CustomerDto) o;
        return numberOfTickets == that.numberOfTickets &&
                Objects.equals(id, that.id) &&
                Objects.equals(name, that.name) &&
                Objects.equals(surname, that.surname) &&
                Objects.equals(mail, that.mail) &&
                Objects.equals(phoneNumber, that.phoneNumber) &&
                Objects.equals(indexNumber, that.indexNumber) &&
                Objects.equals(user, that.user) &&
                Objects.equals(kvTable, that.kvTable);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, name, surname, mail, phoneNumber, indexNumber, user, kvTable, numberOfTickets);
    }

    @Override
    public String toString() {
        return "CustomerDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", mail='" + mail + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", indexNumber='" + indexNumber + '\'' +
                ", user=" + user +
                ", numberOfTickets=" + numberOfTickets +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getIndexNumber() {
        return indexNumber;
    }

    public void setIndexNumber(String indexNumber) {
        this.indexNumber = indexNumber;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public KvTableDto getKvTable() {
        return kvTable;
    }

    public void setKvTable(KvTableDto kvTable) {
        this.kvTable = kvTable;
    }

    public int getNumberOfTickets() {
        return numberOfTickets;
    }

    public void setNumberOfTickets(int numberOfTickets) {
        this.numberOfTickets = numberOfTickets;
    }
}
