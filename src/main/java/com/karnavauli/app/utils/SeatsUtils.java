/*
package com.karnavauli.app.utils;

import com.karnavauli.app.model.dto.CustomerDto;
import com.karnavauli.app.model.enums.KvTable;
import com.karnavauli.app.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class SeatsUtils {
    private List<KvTable> allKvTables = Arrays.asList(KvTable.values());
    //mapa mowiaca ile osob wybralo dany stolik
    private final Map<KvTable, Integer> numberOfTableClicks = new HashMap<>();

    private CustomerService customerService;

    @Autowired
    public SeatsUtils(CustomerService customerService) {
        this.customerService = customerService;
    }

    public SeatsUtils() {
        updateTables();
    }

    public List<KvTable> getAvailableSeats() {
        return updateTables();
    }

    public Map<KvTable, Integer> getNumberOfTableClicks() {
        return numberOfTableClicks;
    }

    //organizuje mape numberOfClicks
    // przypisuje kazdemu stolikowi liczbe klikniec customersa

    //metoda ma ustawic w mapie: dla danego stolika ma byc liczba ktora okrelsa ilosc wystapien danego
    //stolika w bazie danych!!!
    //TODO: cos mi tu nie dziala
    public void putTable() {
        for (CustomerDto customerDto : customerService.getAll()) {
if (numberOfTableClicks.get(customerDto.getKvTable()) == null) {
                numberOfTableClicks.put(customerDto.getKvTable(), 0);
            } else {
                numberOfTableClicks.put(customerDto.getKvTable(), numberOfTableClicks.get(customerDto.getKvTable()) + 1);
            }

            Integer count = numberOfTableClicks.get(customerDto.getKvTable());
            if (count == null) {
                numberOfTableClicks.put(customerDto.getKvTable(), 1);
            }
            else {
                numberOfTableClicks.put(customerDto.getKvTable(), count + 1);
            }
        }
    }

*
     * zwraca liste stolikow ktore nie zostaly jescze wybrane(ktorych nie ma w bazie)
     *
     * @return


    //TODO ZLE!!!!! METODA DO WYWALENIA
    public List<KvTable> updateTables() {
        List<CustomerDto> customers = customerService.getAll();
        return allKvTables.stream()
                .filter(kvTable -> customers.stream().noneMatch(c -> kvTable.name().equalsIgnoreCase(c.getKvTable().name())))
                .collect(Collectors.toList());
    }

*
     * kazdemu stolikowi przypisuje dana (maksymalna) liczbe miejsc
     *
     * @return


    private Map<KvTable, Integer> initiliazeSeats() {
        Map<KvTable, Integer> seats = new HashMap<>();
        List<KvTable> allTables = Arrays.asList(KvTable.values());
        for (KvTable table : allTables) {
            //dawanie kazdemu stolikowi z parteru 10 miejsc
            if ("0".equals(String.valueOf(table.toString().charAt(1)))) {
                seats.put(table, 10);
            }
            //dawanie reszcie stolikow 8 miejsc
            else {
                seats.put(table, 8);
            }
        }
        return seats;
    }

    // mapa ma zwracac liczbe wolnych miejsc przy kazdym ze stolikow
    // np: A01, 5 - jest 5 wolnych miejsc przy stoliku A01
    public Map<KvTable, Integer> getFreeSeatsInTables() {
        List<KvTable> tables = Arrays.asList(KvTable.values());
        Map<KvTable, Integer> freeSeats = new HashMap<>();
        Map<KvTable, Integer> seats = initiliazeSeats(); // tutaj mam akutlana liczbe miejsc zamiast
        // domyslna liczbe miejsc dla kazdego stolika

        // freeseats.put(table, seats.get(table) - numberofclikcs.get(table))
        // czyli MAX miejsc w danym stoliku - seats.get(table)
        //zajete miejsca - numberOfClicks.get(table)
        for (KvTable table : tables) {
            //if(freeSeats.get(table) == null || freeSeats.get(table) == 0){
            if (!numberOfTableClicks.containsKey(table) || numberOfTableClicks.get(table) == 0) {
                freeSeats.put(table, seats.get(table));
            } else {
                System.out.println("*****"+table+"-> " + numberOfTableClicks.get(table));
                freeSeats.put(table, seats.get(table) - numberOfTableClicks.get(table));
            }
        }

        return freeSeats;
    }

    public List<KvTable> getFreeTables(){
List<KvTable> freeTables = new ArrayList<>();

        for (Map.Entry entry : getFreeSeatsInTables().entrySet()) {
            if((int) entry.getValue() != 0){
                freeTables.add((KvTable) entry.getKey());
            }

System.out.println(entry.getKey() + ", " + entry.getValue());
        }

        Map<KvTable, Integer> freeSeats = getFreeSeatsInTables();

        return freeSeats.entrySet().stream()
                .filter((kvTableMap) -> kvTableMap.getValue() != 0 && kvTableMap.getValue() >0)
                .map(Map.Entry::getKey)
                .sorted()
                .collect(Collectors.toList());
        }


    public boolean isAnySeatFree() {
        boolean isAnySeatFree = true;
        if (getAvailableSeats().isEmpty() || getAvailableSeats().size() == 0) {
            isAnySeatFree = false;
        }
        return isAnySeatFree;
    }

    public int getNumberOfFreeSeats(){
        int numberOfFreeSeats = 0;
        for (Map.Entry entry : getFreeSeatsInTables().entrySet()) {
            numberOfFreeSeats += (int) entry.getValue();
        }
        return numberOfFreeSeats;
    }



public List<KvTable> updateSeats2() {
        List<CustomerDto> customers = customerService.getAll();
        return customers.stream()
                .filter(c -> allKvTables.stream().anyMatch(kvTable -> kvTable.name().equalsIgnoreCase(c.getKvTable().name())))
                .map(CustomerDto::getKvTable)
                .collect(Collectors.toList());
    }


 public List<KvTable> updateSeatsByKuba() {
        List<CustomerDto> customers = customerService.getAll();
        return allKvTables
                .stream()
                .filter(kvTable -> {
                    for (CustomerDto c : customers) {
                        if (c.getKvTable().equals(kvTable)) {
                            return false;
                        }
                    }
                    return true;
                }).collect(Collectors.toList());
    }

}
*/
