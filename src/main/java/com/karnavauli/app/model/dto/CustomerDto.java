package com.karnavauli.app.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.karnavauli.app.model.entities.User;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class CustomerDto {
    private Long id;
    private String name;
    private String surname;
    private Integer amountOfKVAppearance;
    private Boolean isIndex;
    @JsonIgnore
    private User user;
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @JsonIgnore
    private KvTableDto kvTable;
    private String kvTableName;
}
