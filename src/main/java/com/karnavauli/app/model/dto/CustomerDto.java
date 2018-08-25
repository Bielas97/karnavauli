package com.karnavauli.app.model.dto;

import com.karnavauli.app.model.entities.User;
import com.karnavauli.app.model.enums.Seat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
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
    private Seat seat;

    private int numberOfTickets;
}
