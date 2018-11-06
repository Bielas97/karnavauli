package com.karnavauli.app.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class KvTableDto {
    private Long id;
    private String name;
    private Integer maxPlaces;
    private Integer occupiedPlaces;
    private List<CustomerDto> customer;
    private String owner;
    private TicketDto ticketDto;


}
