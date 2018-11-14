package com.karnavauli.app.model.dto;

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
    private String faculty;
    private Boolean isIndex;
    //TODO: czy tu powinien byc user czy userDto????? -----USERDTO nie dziala przy zamianie
    private User user;
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private KvTableDto kvTable;

    //tylko do formularza updateujacego customersa
}
