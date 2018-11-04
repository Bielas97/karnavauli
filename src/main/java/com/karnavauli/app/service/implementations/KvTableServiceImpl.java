package com.karnavauli.app.service.implementations;

import com.karnavauli.app.model.dto.CustomerDto;
import com.karnavauli.app.model.dto.KvTableDto;
import com.karnavauli.app.model.dto.ManyCustomers;
import com.karnavauli.app.model.dto.UserDto;
import com.karnavauli.app.model.entities.KvTable;
import com.karnavauli.app.repository.KvTableRepository;
import com.karnavauli.app.service.CustomerService;
import com.karnavauli.app.service.KvTableService;
import com.karnavauli.app.service.TicketService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class KvTableServiceImpl implements KvTableService {
    private KvTableRepository kvTableRepository;
    private ModelMapper modelMapper;
    private TicketService ticketService;

    private CustomerService customerService;

    public KvTableServiceImpl(KvTableRepository kvTableRepository, ModelMapper modelMapper, TicketService ticketService, CustomerService customerService) {
        this.kvTableRepository = kvTableRepository;
        this.modelMapper = modelMapper;
        this.ticketService = ticketService;
        this.customerService = customerService;
    }

    /*@Override
    @EventListener(ApplicationReadyEvent.class)
    public void initilizeKvTable() {
        for (int i = 1; i < 92; i++) {
            if ( getOneKvTable((long) i).get().getId() != i) {

                if (i < 13) {
                    addOrUpdateKvTable(KvTableDto.builder().id((long) i).name("A0" + i).maxPlaces(10).occupiedPlaces(0).build());
                } else if (i < 22) {
                    addOrUpdateKvTable(KvTableDto.builder().id((long) i).name("B0" + (i - 13)).maxPlaces(10).occupiedPlaces(0).build());
                } else if (i < 31) {
                    addOrUpdateKvTable(KvTableDto.builder().id((long) i).name("C0" + (i - 22)).maxPlaces(10).occupiedPlaces(0).build());
                } else if (i < 44) {
                    addOrUpdateKvTable(KvTableDto.builder().id((long) i).name("D0" + (i - 31)).maxPlaces(10).occupiedPlaces(0).build());
                } else if (i < 50) {
                    addOrUpdateKvTable(KvTableDto.builder().id((long) i).name("A1" + (i - 43)).maxPlaces(8).occupiedPlaces(0).build());
                } else if (i < 56) {
                    addOrUpdateKvTable(KvTableDto.builder().id((long) i).name("B1" + (i - 49)).maxPlaces(8).occupiedPlaces(0).build());
                } else if (i < 62) {
                    addOrUpdateKvTable(KvTableDto.builder().id((long) i).name("C1" + (i - 55)).maxPlaces(8).occupiedPlaces(0).build());
                } else if (i < 68) {
                    addOrUpdateKvTable(KvTableDto.builder().id((long) i).name("D1" + (i - 61)).maxPlaces(8).occupiedPlaces(0).build());
                } else if (i < 74) {
                    addOrUpdateKvTable(KvTableDto.builder().id((long) i).name("A2" + (i - 67)).maxPlaces(8).occupiedPlaces(0).build());
                } else if (i < 80) {
                    addOrUpdateKvTable(KvTableDto.builder().id((long) i).name("B2" + (i - 73)).maxPlaces(8).occupiedPlaces(0).build());
                } else if (i < 86) {
                    addOrUpdateKvTable(KvTableDto.builder().id((long) i).name("C2" + (i - 79)).maxPlaces(8).occupiedPlaces(0).build());
                } else {
                    addOrUpdateKvTable(KvTableDto.builder().id((long) i).name("D2" + (i - 85)).maxPlaces(8).occupiedPlaces(0).build());
                }
            }
        }
    }*/

    @Override
    public void addOrUpdateKvTable(KvTableDto kvTableDto) {
        kvTableRepository.save(modelMapper.map(kvTableDto, KvTable.class));
    }


    @Override
    public void deleteKvTable(Long id) {
        kvTableRepository.deleteById(id);
    }

    @Override
    public Optional<KvTableDto> getOneKvTable(Long id) {
        return kvTableRepository.findById(id).map(kvTable -> modelMapper.map(kvTable, KvTableDto.class));
    }

    @Override
    public List<KvTableDto> getAll() {
        return kvTableRepository.findAll()
                .stream()
                .map(kvTable -> modelMapper.map(kvTable, KvTableDto.class))
                .collect(Collectors.toList());
    }


    @Override
    public int getAllFreeSeats() {
        List<KvTableDto> allTables = getAll();
        int numberOfFreeSeats = 0;
        for (KvTableDto kvTable : allTables) {
            numberOfFreeSeats += (kvTable.getMaxPlaces() - kvTable.getOccupiedPlaces());
        }
        return numberOfFreeSeats;
    }

    @Override
    public boolean isAnySeatFree() {
        return getAllFreeSeats() > 0;
    }

    @Override
    public List<KvTableDto> getFreeTables() {
        return getAll()
                .stream()
                .filter(kvTableDto -> kvTableDto.getMaxPlaces() - kvTableDto.getOccupiedPlaces() != 0)
                .collect(Collectors.toList());
    }

    @Override
    public List<KvTableDto> getFreeTablesForAmountOfPeople(int amountOfPeople) {
        return getAll()
                .stream()
                .filter(kvTableDto -> kvTableDto.getMaxPlaces() - kvTableDto.getOccupiedPlaces() != 0 &&
                        kvTableDto.getMaxPlaces() - kvTableDto.getOccupiedPlaces() >= amountOfPeople)
                .collect(Collectors.toList());
    }


    @Override
    public void incrementOccupiedPlaces(Long id, int size) {
        Optional<KvTable> kvTable = kvTableRepository.findById(id);
        for (int i = 0; i < size; i++) {
            kvTable.ifPresent((kv) -> kv.setOccupiedPlaces(kvTable.get().getOccupiedPlaces() + 1));
        }

    }

    @Override
    public void decrementOccupiedPlaces(Long id) {
        Optional<KvTable> kvTable = kvTableRepository.findById(id);
        kvTable.ifPresent((kv) -> kv.setOccupiedPlaces(kvTable.get().getOccupiedPlaces() - 1));
    }


    @Override
    public void changeOwnerForTable(KvTableDto kvTableDto, String owners) {
        KvTableDto table = getOneKvTable(kvTableDto.getId()).orElseThrow(NullPointerException::new);
        table.setOwner(owners);
        addOrUpdateKvTable(table);

    }

    @Override
    public List<KvTableDto> getFreeTablesPlusCurrentTable(ManyCustomers manyCustomers) {
        List<KvTableDto> freeTablesPlusCurrenTable = getFreeTables();
        CustomerDto cus = manyCustomers.getCustomers().get(0);
        if (cus.getKvTable().getMaxPlaces() - cus.getKvTable().getOccupiedPlaces() == 0) {
            freeTablesPlusCurrenTable.add(0, manyCustomers.getCustomers().get(0).getKvTable());
        }
        return freeTablesPlusCurrenTable;
    }

    @Override
    public List<KvTableDto> getFreeTablesForUser(UserDto userDto) {
        List<KvTableDto> tables = new ArrayList<>();

        return tables;
    }
}
