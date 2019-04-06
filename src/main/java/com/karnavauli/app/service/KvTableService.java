package com.karnavauli.app.service;

import com.karnavauli.app.exceptions.ExceptionCode;
import com.karnavauli.app.exceptions.MyException;
import com.karnavauli.app.model.dto.CustomerDto;
import com.karnavauli.app.model.dto.KvTableDto;
import com.karnavauli.app.model.dto.ManyCustomers;
import com.karnavauli.app.model.dto.UserDto;
import com.karnavauli.app.model.entities.KvTable;
import com.karnavauli.app.repository.KvTableRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class KvTableService {
    private KvTableRepository kvTableRepository;
    private ModelMapper modelMapper;
    private TicketService ticketService;

    private CustomerService customerService;

    public KvTableService(KvTableRepository kvTableRepository, ModelMapper modelMapper, TicketService ticketService, CustomerService customerService) {
        this.kvTableRepository = kvTableRepository;
        this.modelMapper = modelMapper;
        this.ticketService = ticketService;
        this.customerService = customerService;
    }


    public void addOrUpdateKvTable(KvTableDto kvTableDto) {
        try {
            kvTableRepository.save(modelMapper.map(kvTableDto, KvTable.class));
        } catch (Exception e) {
            throw new MyException(ExceptionCode.SERVICE, "ADDING OR UPDATING EXCEPTION: " + e.getMessage());
        }
    }


    public void deleteKvTable(Long id) {
        try {
            kvTableRepository.deleteById(id);
        } catch (Exception e) {
            throw new MyException(ExceptionCode.SERVICE, "DELTEING KVTABLE EXCEPTION: " + e.getMessage());
        }
    }

    public Optional<KvTableDto> getOneKvTable(Long id) {
        try {
            return kvTableRepository.findById(id).map(kvTable -> modelMapper.map(kvTable, KvTableDto.class));
        } catch (Exception e) {
            throw new MyException(ExceptionCode.SERVICE, "GETTIN ONE KV TABLE EXCEPTION: " + e.getMessage());
        }
    }

    public List<KvTableDto> getAll() {
        try {
            return kvTableRepository.findAll()
                    .stream()
                    .map(kvTable -> modelMapper.map(kvTable, KvTableDto.class))
                    .sorted(Comparator.comparing(KvTableDto::getName))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new MyException(ExceptionCode.SERVICE, "GETTING ALL KVTABLES EXCEPTION: " + e.getMessage());
        }
    }


    public int getAllFreeSeats() {
        try {
            List<KvTableDto> allTables = getAll();
            int numberOfFreeSeats = 0;
            for (KvTableDto kvTable : allTables) {
                numberOfFreeSeats += (kvTable.getMaxPlaces() - kvTable.getOccupiedPlaces());
            }
            return numberOfFreeSeats;
        } catch (Exception e) {
            throw new MyException(ExceptionCode.SERVICE, "GETTING ALL FREE SEATS EXCEPTION: " + e.getMessage());
        }
    }

    public boolean isAnySeatFree() {
        return getAllFreeSeats() > 0;
    }

    public List<KvTableDto> getFreeTables() {
        try {
            return getAll()
                    .stream()
                    .filter(kvTableDto -> kvTableDto.getMaxPlaces() - kvTableDto.getSoldPlaces() != 0)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new MyException(ExceptionCode.SERVICE, "GETTING FREE TABLES EXCEPTION: " + e.getMessage());
        }
    }

    public List<KvTableDto> getFreeTablesForAmountOfPeople(int amountOfPeople) {
        try {
            return getAll()
                    .stream()
                    .filter(kvTableDto -> kvTableDto.getMaxPlaces() - kvTableDto.getSoldPlaces() != 0 &&
                            kvTableDto.getMaxPlaces() - kvTableDto.getSoldPlaces() >= amountOfPeople)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new MyException(ExceptionCode.SERVICE, "GETTING FREE TABLES FOR AMOUNT OF PEOPLE ECEPTION: " + e.getMessage());
        }
    }


    public void incrementOccupiedPlaces(Long id, int size) {
        try {
            Optional<KvTable> kvTable = kvTableRepository.findById(id);
            for (int i = 0; i < size; i++) {
                kvTable.ifPresent((kv) -> kv.setOccupiedPlaces(kvTable.get().getOccupiedPlaces() + 1));
            }
        } catch (Exception e) {
            throw new MyException(ExceptionCode.SERVICE, "INCREMENT OCCUPIED PLACES EXCEPTION: " + e.getMessage());
        }

    }

    public void decrementOccupiedPlaces(Long id) {
        try {
            Optional<KvTable> kvTable = kvTableRepository.findById(id);
            kvTable.ifPresent((kv) -> kv.setOccupiedPlaces(kvTable.get().getOccupiedPlaces() - 1));
        } catch (Exception e) {
            throw new MyException(ExceptionCode.SERVICE, "DECREMENT OCCUPIED PLACES ESCPETION: " + e.getMessage());
        }
    }


    public void changeOwnerForTable(KvTableDto kvTableDto, String owners) {
        try {
            KvTableDto table = getOneKvTable(kvTableDto.getId()).orElseThrow(NullPointerException::new);
            table.setOwner(owners);
            addOrUpdateKvTable(table);
        } catch (Exception e) {
            throw new MyException(ExceptionCode.SERVICE, "CHANGE OWNER FOR TABLE EXCEPTION: " + e.getMessage());
        }

    }

    public List<KvTableDto> getFreeTablesPlusCurrentTable(ManyCustomers manyCustomers) {
        try {

            List<KvTableDto> freeTablesPlusCurrenTable = getFreeTables();
            CustomerDto cus = manyCustomers.getCustomers().get(0);
            if (cus.getKvTable().getMaxPlaces() - cus.getKvTable().getSoldPlaces() == 0) {
                freeTablesPlusCurrenTable.add(0, manyCustomers.getCustomers().get(0).getKvTable());
            }
            return freeTablesPlusCurrenTable;
        } catch (Exception e) {
            throw new MyException(ExceptionCode.SERVICE, "GET FREE TABLES PLUS CURRENT TABLE EXCEPTION: " + e.getMessage());
        }
    }
}
