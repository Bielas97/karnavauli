package com.karnavauli.app.service;

import com.karnavauli.app.model.dto.CustomerDto;
import com.karnavauli.app.model.enums.Seat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class SeatsService {
    private List<Seat> allSeats = Arrays.asList(Seat.values());

    private CustomerService customerService;

    @Autowired
    public SeatsService(CustomerService customerService) {
        this.customerService = customerService;
    }

    public SeatsService() {
        updateSeats();
    }

    public List<Seat> getAvailableSeats() {
        return updateSeats();
    }

    public List<Seat> updateSeats() {
        List<CustomerDto> customers = customerService.getAll();
        return allSeats.stream()
                .filter(seat -> customers.stream().noneMatch(c -> seat.name().equalsIgnoreCase(c.getSeat().name())))
                .collect(Collectors.toList());
    }

    /*public List<Seat> updateSeats2() {
        List<CustomerDto> customers = customerService.getAll();
        return customers.stream()
                .filter(c -> allSeats.stream().anyMatch(seat -> seat.name().equalsIgnoreCase(c.getSeat().name())))
                .map(CustomerDto::getSeat)
                .collect(Collectors.toList());
    }*/

   /* public List<Seat> updateSeatsByKuba() {
        List<CustomerDto> customers = customerService.getAll();
        return allSeats
                .stream()
                .filter(seat -> {
                    for (CustomerDto c : customers) {
                        if (c.getSeat().equals(seat)) {
                            return false;
                        }
                    }
                    return true;
                }).collect(Collectors.toList());
    }*/
}
