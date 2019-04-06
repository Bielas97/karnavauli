package com.karnavauli.app.model.dto;

import com.karnavauli.app.model.entities.User;
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
    private Boolean isUni;
    //06-04-2019
    /*private List<User> ticketDealers;*/
    private List<KvTableDto> tablesDto;
}
