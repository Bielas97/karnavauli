package com.karnavauli.app.service;

import com.karnavauli.app.model.dto.KvTableDto;

import java.util.List;
import java.util.Optional;

public interface KvTableService {
    void addOrUpdateKvTable(KvTableDto kvTableDto);
    void deleteKvTable(Long id);
    Optional<KvTableDto> getOneKvTable(Long id);
    List<KvTableDto> getAll();

    //void initilizeKvTable();
    int getAllFreeSeats();
    boolean isAnySeatFree();
    List<KvTableDto> getFreeTables();
    void incrementOccupiedPlaces(Long id, int size);
    void decrementOccupiedPlaces(Long id);
}
