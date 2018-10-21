package com.karnavauli.app.service;

import com.karnavauli.app.model.dto.KvTableDto;
import com.karnavauli.app.model.dto.ManyCustomers;

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
    List<KvTableDto> getFreeTablesForAmountOfPeople(int amountOfPeople);
    void incrementOccupiedPlaces(Long id, int size);
    void decrementOccupiedPlaces(Long id);

    List<KvTableDto> getFreeTablesPlusCurrentTable(ManyCustomers manyCustomers);
}
