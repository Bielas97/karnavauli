package com.karnavauli.app.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.karnavauli.app.model.entities.Ticket;
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
    private String owner;
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @JsonIgnore
    private Ticket ticket;


}
