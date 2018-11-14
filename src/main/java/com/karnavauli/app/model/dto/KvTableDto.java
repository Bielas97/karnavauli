package com.karnavauli.app.model.dto;

import lombok.*;

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
    private Integer soldPlaces;
    private List<CustomerDto> customer;
    private String owner;
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private TicketDto ticketDto;


}
