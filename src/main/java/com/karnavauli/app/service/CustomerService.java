package com.karnavauli.app.service;

import com.karnavauli.app.exceptions.ExceptionCode;
import com.karnavauli.app.exceptions.MyException;
import com.karnavauli.app.model.dto.CustomerDto;
import com.karnavauli.app.model.dto.KvTableDto;
import com.karnavauli.app.model.dto.ManyCustomers;
import com.karnavauli.app.model.dto.UserDto;
import com.karnavauli.app.model.entities.Customer;
import com.karnavauli.app.model.entities.KvTable;
import com.karnavauli.app.model.entities.User;
import com.karnavauli.app.model.enums.Role;
import com.karnavauli.app.repository.CustomerRepository;
import com.karnavauli.app.repository.KvTableRepository;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class CustomerService {
    private CustomerRepository customerRepository;
    private KvTableRepository kvTableRepository;
    private UserService userService;
    private ModelMapper modelMapper;

    private Map<String, Long> amountOfOccupiedPlaces = new HashMap<>();

    public Map<String, Long> getAmountOfOccupiedPlaces() {
        return amountOfOccupiedPlaces;
    }

    public CustomerService(CustomerRepository customerRepository, KvTableRepository kvTableRepository, UserService userService, ModelMapper modelMapper) {
        this.customerRepository = customerRepository;
        this.kvTableRepository = kvTableRepository;
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    public void addCustomer(CustomerDto customerDto) {
        try {
            KvTable table = kvTableRepository.findById(customerDto.getKvTable().getId()).orElseThrow(NullPointerException::new);
            table.setSoldPlaces(table.getSoldPlaces() + 1);
            kvTableRepository.save(table);
            customerRepository.save(modelMapper.map(customerDto, Customer.class));
        } catch (Exception e) {
            throw new MyException(ExceptionCode.SERVICE, "ADD CUSTOMER EXCEPTION: " + e.getMessage());
        }
    }

    public void deleteCustomer(Long id) {
        try {
            CustomerDto customerDto = getOneCustomer(id).orElseThrow(NullPointerException::new);
            KvTable table = kvTableRepository.findById(customerDto.getKvTable().getId()).orElseThrow(NullPointerException::new);
            table.setSoldPlaces(table.getSoldPlaces() - 1);
            kvTableRepository.save(table);
            customerRepository.deleteById(id);
            Principal principal = SecurityContextHolder.getContext().getAuthentication();
            UserDto userDto = modelMapper.map(userService.getUserFromUsername(principal.getName()), UserDto.class);
            userService.incrementNumberOfTickets(userDto);
        } catch (Exception e) {
            throw new MyException(ExceptionCode.SERVICE, "DELETE CUSTOMER EXCEPTION: " + e.getMessage());
        }
    }

    public Optional<CustomerDto> getOneCustomer(Long id) {
        try {
            return customerRepository.findById(id).map(c -> modelMapper.map(c, CustomerDto.class));
        } catch (Exception e) {
            throw new MyException(ExceptionCode.SERVICE, "GET ONE CUSTOMER EXCEPTION: " + e.getMessage());
        }
    }

    public List<CustomerDto> getAll() {
        try {
            return customerRepository.findAll()
                    .stream()
                    .map(c -> modelMapper.map(c, CustomerDto.class))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new MyException(ExceptionCode.SERVICE, "GETTING ALL CUSTOMERS EXCEPTION: " + e.getMessage());
        }
    }

    public void addManyCustomers(ManyCustomers manyCustomers) {
        KvTableDto kvTableDto = modelMapper.map(kvTableRepository.getOne(manyCustomers.getKvTableId()), KvTableDto.class);
        if (kvTableDto.getSoldPlaces().equals(kvTableDto.getMaxPlaces()) || kvTableDto.getSoldPlaces() > kvTableDto.getMaxPlaces()) {
            throw new MyException(ExceptionCode.MAX_PLACES, "ALL PLACES ARE SOLD EXCEPTION");
        } else {
            try {
                for (int i = 0; i < manyCustomers.getCustomers().size(); i++) {
                    KvTable table = kvTableRepository.findById(manyCustomers.getCustomers().get(i).getKvTable().getId()).orElseThrow(NullPointerException::new);
                    table.setSoldPlaces(table.getSoldPlaces() + 1);
                    kvTableRepository.save(table);
                    customerRepository.save(modelMapper.map(manyCustomers.getCustomers().get(i), Customer.class));
                }
                Principal principal = SecurityContextHolder.getContext().getAuthentication();
                User userFromUsername = userService.getUserFromUsername(principal.getName());
                UserDto userDto = modelMapper.map(userFromUsername, UserDto.class);
                userService.decerementNumberOfTickets(userDto, manyCustomers.getCustomers().size());
            } catch (Exception e) {
                throw new MyException(ExceptionCode.SERVICE, "ADDING MANY CUSTOMERS EXCEPTION: " + e.getMessage());
            }
        }

    }

    public void updateManyCustomer(ManyCustomers manyCustomers) {
        try {
            for (int i = 0; i < manyCustomers.getCustomers().size(); i++) {
                Optional<CustomerDto> customerFromDb = getOneCustomer(manyCustomers.getCustomers().get(i).getId());
                Optional<KvTable> tableFromDb = kvTableRepository.findById(customerFromDb.get().getKvTable().getId());

                if (!manyCustomers.getKvTableId().equals(tableFromDb.get().getId())) {
                    tableFromDb.get().setSoldPlaces(tableFromDb.get().getSoldPlaces() - 1);
                    KvTableDto incrementedTable = manyCustomers.getCustomers().get(i).getKvTable();
                    incrementedTable.setSoldPlaces(incrementedTable.getSoldPlaces() + 1);
                    kvTableRepository.save(modelMapper.map(incrementedTable, KvTable.class));
                }

                kvTableRepository.save(tableFromDb.get());
                customerRepository.save(modelMapper.map(manyCustomers.getCustomers().get(i), Customer.class));
            }
        } catch (Exception e) {
            throw new MyException(ExceptionCode.SERVICE, "UPDATING MANY CUSTOMERS EXCEPTION: " + e.getMessage());
        }
    }

    //nie wiem czy zadziala.
    //NIE KORZYSTAC BEZ SPRAWDZENIA FUNKCJONALNOSCI
    public void updateCustomer(CustomerDto customerDto) {
        try {
            Optional<CustomerDto> customerFromDb = getOneCustomer(customerDto.getId());
            Optional<KvTable> tableFromDb = kvTableRepository.findById(customerFromDb.get().getKvTable().getId());

            if (!customerDto.getKvTable().getId().equals(tableFromDb.get().getId())) {
                tableFromDb.get().setSoldPlaces(tableFromDb.get().getSoldPlaces() - 1);
                KvTableDto incrementedTable = customerDto.getKvTable();
                incrementedTable.setSoldPlaces(incrementedTable.getSoldPlaces() + 1);
                kvTableRepository.save(modelMapper.map(incrementedTable, KvTable.class));
            }

            kvTableRepository.save(tableFromDb.get());
            customerRepository.save(modelMapper.map(customerDto, Customer.class));
        } catch (Exception e) {
            throw new MyException(ExceptionCode.SERVICE, "UPDATING CUSTOMER EXCEPTION: " + e.getMessage());
        }
    }

    public void initialFillAmountOfOccupiedPlaces() {
        amountOfOccupiedPlaces.clear();
        if (!customerRepository.findAll().isEmpty()) {
            List<CustomerDto> allCustomers = getAll();
            amountOfOccupiedPlaces = allCustomers.stream().collect(
                    Collectors.groupingBy(cusDto -> cusDto.getKvTable().getName(), Collectors.counting()));
        }
    }

    public void fillAmountOfOccupiedPlaces(KvTableDto kvTableDto, int amount) {
        if (amountOfOccupiedPlaces.isEmpty() || amountOfOccupiedPlaces.get(kvTableDto.getName()) == null) {
            amountOfOccupiedPlaces.put(kvTableDto.getName(), (long) amount);
        } else {
            amountOfOccupiedPlaces.put(kvTableDto.getName(), amountOfOccupiedPlaces.get(kvTableDto.getName()) + amount);
        }
    }

    public void decrementAmountOfOccupiedPlaces(KvTableDto kvTableDto) {
        Long decremention = amountOfOccupiedPlaces.get(kvTableDto.getName());
        amountOfOccupiedPlaces.replace(kvTableDto.getName(), decremention, decremention - 1);
    }

    public void decrementAmountOfOccupiedPlaces(KvTableDto kvTableDto, int amountOfDecremention) {
        Long decremention = amountOfOccupiedPlaces.get(kvTableDto.getName());
        amountOfOccupiedPlaces.replace(kvTableDto.getName(), decremention, decremention - amountOfDecremention);
    }

    public boolean OccupiedPlacesAreGreaterThanMax(KvTableDto kvTableDto) {
        if (amountOfOccupiedPlaces.get(kvTableDto.getName()) != null) {
            return amountOfOccupiedPlaces.get(kvTableDto.getName()) > kvTableDto.getMaxPlaces();
        }
        return false;
    }



    /**
     * price for people without student card: 190 zł
     * price for people with student card for all places on the ground floor: 150 zł
     * price for people with student card for all places on the other floors: 140 zł
     * @param manyCustomers
     * @return
     */
    public int countPriceToBePaid(ManyCustomers manyCustomers) {
        int price = 0;
        for (CustomerDto customerDto : manyCustomers.getCustomers()) {
            if (!customerDto.getIsIndex()) {
                price += 190;
            } else {
                if (customerDto.getKvTable().getName().charAt(1) != '0') {
                    price += 140;
                } else {
                    price += 150;
                }
            }
        }
        return price;
    }



}
