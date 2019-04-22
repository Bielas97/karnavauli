package com.karnavauli.app;

import com.karnavauli.app.model.dto.KvTableDto;
import com.karnavauli.app.model.entities.KvTable;
import com.karnavauli.app.repository.KvTableRepository;
import com.karnavauli.app.service.CustomerService;
import com.karnavauli.app.service.KvTableService;
import com.karnavauli.app.service.TicketService;
import org.modelmapper.ModelMapper;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class OnStartup {
    private KvTableService kvTableService;
    private CustomerService customerService;
    private TicketService ticketService;
    private ModelMapper modelMapper;
    private KvTableRepository kvTableRepository;

    public OnStartup(KvTableService kvTableService, CustomerService customerService, TicketService ticketService, ModelMapper modelMapper, KvTableRepository kvTableRepository) {
        this.kvTableService = kvTableService;
        this.customerService = customerService;
        this.ticketService = ticketService;
        this.modelMapper = modelMapper;
        this.kvTableRepository = kvTableRepository;
    }


    /**
     * metoda uruchamiana przy starcie aplikacji
     * inicjalizuje stoliki
     */
    @EventListener(ApplicationReadyEvent.class)
    public void initilizeKvTable() {
        KvTableDto kvTableDto = KvTableDto.builder().occupiedPlaces(0).soldPlaces(0).build();
        ticketService.getOneTicketByFullName("regular").ifPresent(ticketDto -> kvTableDto.setOwner(ticketDto.getFullName()));
        if (kvTableService.getAll().isEmpty() || kvTableService.getAll().size() < 91) {
            for (int i = 1; i < 92; i++) {
                System.out.println(i);
                if (i < 13) {
                    kvTableDto.setName("A0" + i);
                    kvTableDto.setMaxPlaces(10);
                    kvTableService.addOrUpdateKvTable(kvTableDto);
                } else if (i < 22) {
                    kvTableDto.setName("B0" + (i - 13));
                    kvTableDto.setMaxPlaces(10);
                    kvTableService.addOrUpdateKvTable(kvTableDto);
                } else if (i < 31) {
                    kvTableDto.setName("C0" + (i - 22));
                    kvTableDto.setMaxPlaces(10);
                    kvTableService.addOrUpdateKvTable(kvTableDto);
                } else if (i < 44) {
                    kvTableDto.setName("D0" + (i - 31));
                    kvTableDto.setMaxPlaces(10);
                    kvTableService.addOrUpdateKvTable(kvTableDto);
                } else if (i < 50) {
                    kvTableDto.setName("A1" + (i - 43));
                    kvTableDto.setMaxPlaces(8);
                    kvTableService.addOrUpdateKvTable(kvTableDto);
                } else if (i < 56) {
                    kvTableDto.setName("B1" + (i - 49));
                    kvTableDto.setMaxPlaces(8);
                    kvTableService.addOrUpdateKvTable(kvTableDto);
                } else if (i < 62) {
                    kvTableDto.setName("C1" + (i - 55));
                    kvTableDto.setMaxPlaces(8);
                    kvTableService.addOrUpdateKvTable(kvTableDto);
                } else if (i < 68) {
                    kvTableDto.setName("AD" + (i - 61));
                    kvTableDto.setMaxPlaces(8);
                    kvTableService.addOrUpdateKvTable(kvTableDto);
                } else if (i < 74) {
                    kvTableDto.setName("A2" + (i - 67));
                    kvTableDto.setMaxPlaces(8);
                    kvTableService.addOrUpdateKvTable(kvTableDto);
                } else if (i < 80) {
                    kvTableDto.setName("B2" + (i - 73));
                    kvTableDto.setMaxPlaces(8);
                    kvTableService.addOrUpdateKvTable(kvTableDto);
                } else if (i < 86) {
                    kvTableDto.setName("C2" + (i - 79));
                    kvTableDto.setMaxPlaces(8);
                    kvTableService.addOrUpdateKvTable(kvTableDto);
                } else {
                    kvTableDto.setName("D2" + (i - 85));
                    kvTableDto.setMaxPlaces(8);
                    kvTableService.addOrUpdateKvTable(kvTableDto);
                }
            }
        }
        kvTableService.setRegularTicketToAllTables();
    }
}
