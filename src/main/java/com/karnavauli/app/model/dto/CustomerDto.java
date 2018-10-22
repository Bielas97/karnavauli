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
@Data
public class CustomerDto {
    private Long id;
    private String name;
    private String surname;
    private String faculty;
    private Boolean isIndex;
    //TODO: czy tu powinien byc user czy userDto?????
    private User user;
    private KvTableDto kvTable;

    //tylko do formularza updateujacego customersa
}
