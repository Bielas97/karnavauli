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
    private Integer amountOfKVAppearance;
    private Boolean isIndex;
    private User user;
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private KvTableDto kvTable;

}
