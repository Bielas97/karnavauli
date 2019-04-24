package com.karnavauli.app.controllers.rest_api;

import com.karnavauli.app.model.dto.KvTableDto;
import com.karnavauli.app.model.dto.TicketDto;
import com.karnavauli.app.model.entities.Ticket;
import com.karnavauli.app.model.jwt.Info;
import com.karnavauli.app.service.KvTableService;
import com.karnavauli.app.service.TicketService;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class RestKvTableController {
    private KvTableService kvTableService;
    private TicketService ticketService;
    private ModelMapper modelMapper;

    public RestKvTableController(KvTableService kvTableService, TicketService ticketService, ModelMapper modelMapper) {
        this.kvTableService = kvTableService;
        this.ticketService = ticketService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/user/all-tables")
    public List<KvTableDto> getAllTables() {
        return kvTableService.getAll();
    }

    @PutMapping("/admin/book-table/{id}")
    public Info bookKvTable(/*@RequestBody String owner,*/ @PathVariable Long id, @RequestBody KvTableDto kvTableDtoRequested) {
        Info info = new Info("");

        KvTableDto kvTableDto = kvTableService.getOneKvTable(id).orElseThrow(NullPointerException::new);

        List<String> owners = ticketService.getAll()
                .stream()
                .map(TicketDto::getFullName)
                .collect(Collectors.toList());

        if (owners.contains(kvTableDtoRequested.getOwner())) {
            kvTableDto.setOwner(kvTableDtoRequested.getOwner());
            info.setMessage("Changed owner!");
        } else {
            info.setMessage("Did not change owner!");
        }
        ticketService.getOneTicketByFullName(kvTableDto.getOwner()).ifPresent(ticketDto -> kvTableDto.setTicket(modelMapper.map(ticketDto, Ticket.class)));
        kvTableDto.setOccupiedPlaces(kvTableDto.getMaxPlaces());
        kvTableService.addOrUpdateKvTable(kvTableDto);

        info.setMessage(info.getMessage() + " Table [" + kvTableDto.getId() + "] booked!");
        return info;
    }

}
