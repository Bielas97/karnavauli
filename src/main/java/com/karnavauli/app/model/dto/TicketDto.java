package com.karnavauli.app.model.dto;

import com.karnavauli.app.model.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class TicketDto {
    private Long id;
    private String shortName;
    private String fullName;
    private Double price;
    private Boolean isUni;
    private List<Role> ticketDealers;
    private String roles;
}