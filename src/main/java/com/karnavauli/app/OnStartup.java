package com.karnavauli.app;

import com.karnavauli.app.model.dto.KvTableDto;
import com.karnavauli.app.repository.KvTableRepository;
import com.karnavauli.app.service.KvTableService;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class OnStartup {
    private KvTableService kvTableService;
    private KvTableRepository kvTableRepository;

    public OnStartup(KvTableService kvTableService, KvTableRepository kvTableRepository) {
        this.kvTableService = kvTableService;
        this.kvTableRepository = kvTableRepository;
    }

    /**
     * metoda uruchamiana przy starcie aplikacji
     * inicjalizuje stoliki
     */
    @EventListener(ApplicationReadyEvent.class)
    public void initilizeKvTable() {

        if (kvTableRepository.findAll().isEmpty() || kvTableRepository.findAll().size() < 91) {
            for (int i = 1; i < 92; i++) {
                if (i < 13) {
                    kvTableService.addOrUpdateKvTable(KvTableDto.builder().id((long) i).name("A0" + i).maxPlaces(10).occupiedPlaces(0).soldPlaces(0).owner("regular").build());
                } else if (i < 22) {
                    kvTableService.addOrUpdateKvTable(KvTableDto.builder().id((long) i).name("B0" + (i - 13)).maxPlaces(10).occupiedPlaces(0).soldPlaces(0).owner("regular").build());
                } else if (i < 31) {
                    kvTableService.addOrUpdateKvTable(KvTableDto.builder().id((long) i).name("C0" + (i - 22)).maxPlaces(10).occupiedPlaces(0).soldPlaces(0).owner("regular").build());
                } else if (i < 44) {
                    kvTableService.addOrUpdateKvTable(KvTableDto.builder().id((long) i).name("D0" + (i - 31)).maxPlaces(10).occupiedPlaces(0).soldPlaces(0).owner("regular").build());
                } else if (i < 50) {
                    kvTableService.addOrUpdateKvTable(KvTableDto.builder().id((long) i).name("A1" + (i - 43)).maxPlaces(8).occupiedPlaces(0).soldPlaces(0).owner("regular").build());
                } else if (i < 56) {
                    kvTableService.addOrUpdateKvTable(KvTableDto.builder().id((long) i).name("B1" + (i - 49)).maxPlaces(8).occupiedPlaces(0).soldPlaces(0).owner("regular").build());
                } else if (i < 62) {
                    kvTableService.addOrUpdateKvTable(KvTableDto.builder().id((long) i).name("C1" + (i - 55)).maxPlaces(8).occupiedPlaces(0).soldPlaces(0).owner("regular").build());
                } else if (i < 68) {
                    kvTableService.addOrUpdateKvTable(KvTableDto.builder().id((long) i).name("D1" + (i - 61)).maxPlaces(8).occupiedPlaces(0).soldPlaces(0).owner("regular").build());
                } else if (i < 74) {
                    kvTableService.addOrUpdateKvTable(KvTableDto.builder().id((long) i).name("A2" + (i - 67)).maxPlaces(8).occupiedPlaces(0).soldPlaces(0).owner("regular").build());
                } else if (i < 80) {
                    kvTableService.addOrUpdateKvTable(KvTableDto.builder().id((long) i).name("B2" + (i - 73)).maxPlaces(8).occupiedPlaces(0).soldPlaces(0).owner("regular").build());
                } else if (i < 86) {
                    kvTableService.addOrUpdateKvTable(KvTableDto.builder().id((long) i).name("C2" + (i - 79)).maxPlaces(8).occupiedPlaces(0).soldPlaces(0).owner("regular").build());
                } else {
                    kvTableService.addOrUpdateKvTable(KvTableDto.builder().id((long) i).name("D2" + (i - 85)).maxPlaces(8).occupiedPlaces(0).soldPlaces(0).owner("regular").build());
                }
            }
        }

    }
}
