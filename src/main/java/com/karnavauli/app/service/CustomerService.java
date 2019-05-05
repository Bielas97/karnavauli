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
import com.karnavauli.app.repository.CustomerRepository;
import com.karnavauli.app.repository.KvTableRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
public class CustomerService {
    private CustomerRepository customerRepository;
    private KvTableRepository kvTableRepository;
    private UserService userService;
    private ModelMapper modelMapper;
    private KvTableService kvTableService;

    public CustomerService(CustomerRepository customerRepository, KvTableRepository kvTableRepository, UserService userService, ModelMapper modelMapper, KvTableService kvTableService) {
        this.customerRepository = customerRepository;
        this.kvTableRepository = kvTableRepository;
        this.userService = userService;
        this.modelMapper = modelMapper;
        this.kvTableService = kvTableService;
    }

    public Customer addCustomer(CustomerDto customerDto) {
        log.info("Adding customer: '" + customerDto);
        try {
            KvTable table = kvTableRepository.findByName(customerDto.getKvTableName()).orElseThrow(NullPointerException::new);
            if(table.getSoldPlaces() >= table.getMaxPlaces() || table.getSoldPlaces() >= table.getOccupiedPlaces()){
                throw new MyException(ExceptionCode.SERVICE, "ADD CUSTOMER EXCEPTION: Already sold all places to this Table" );
            }
            Principal principal = SecurityContextHolder.getContext().getAuthentication();
            User userFromUsername = userService.getUserFromUsername(principal.getName());
            UserDto userDto = modelMapper.map(userFromUsername, UserDto.class);
            customerDto.setUser(userFromUsername);
            if(customerDto.getKvTable() == null && customerDto.getKvTableName() != null){
                kvTableService.getOneKvTableByName(customerDto.getKvTableName()).ifPresent(customerDto::setKvTable);
            }
            table.setSoldPlaces(table.getSoldPlaces() + 1);
            kvTableRepository.save(table);
            userService.decerementNumberOfTickets(userDto);
            return customerRepository.save(modelMapper.map(customerDto, Customer.class));
        } catch (Exception e) {
            throw new MyException(ExceptionCode.SERVICE, "ADD CUSTOMER EXCEPTION: " + e.getMessage());
        }
    }

    public Customer deleteCustomer(Long id) {
        try {
            CustomerDto customerDto = getOneCustomer(id).orElseThrow(NullPointerException::new);
            KvTable table = kvTableRepository.findById(customerDto.getKvTable().getId()).orElseThrow(NullPointerException::new);
            table.setSoldPlaces(table.getSoldPlaces() - 1);
            kvTableRepository.save(table);
            customerRepository.deleteById(id);
            Principal principal = SecurityContextHolder.getContext().getAuthentication();
            UserDto userDto = modelMapper.map(userService.getUserFromUsername(principal.getName()), UserDto.class);
            userService.incrementNumberOfTickets(userDto);
            log.info("Customer '" + customerDto + "' deleted!");
            return modelMapper.map(customerDto, Customer.class);
        } catch (Exception e) {
            throw new MyException(ExceptionCode.SERVICE, "DELETE CUSTOMER EXCEPTION: " + e.getMessage());
        }
    }

    public Optional<CustomerDto> getOneCustomer(Long id) {
        try {
            log.info("Getting one customer with id: " + id);
            return customerRepository.findById(id).map(c -> modelMapper.map(c, CustomerDto.class));
        } catch (Exception e) {
            throw new MyException(ExceptionCode.SERVICE, "GET ONE CUSTOMER EXCEPTION: " + e.getMessage());
        }
    }

    public Set<CustomerDto> getAll() {
        try {
            log.info("Getting all Customers");
            return customerRepository.findAll()
                    .stream()
                    .map(c -> modelMapper.map(c, CustomerDto.class))
                    .collect(Collectors.toSet());
        } catch (Exception e) {
            throw new MyException(ExceptionCode.SERVICE, "GETTING ALL CUSTOMERS EXCEPTION: " + e.getMessage());
        }
    }

    public ManyCustomers addManyCustomers(User user, ManyCustomers manyCustomers) {
        manyCustomers.setUserDto(user);
        KvTableDto kvTableDto = kvTableService.getOneKvTable(manyCustomers.getKvTableId()).orElseThrow(NullPointerException::new);
        if (kvTableDto.getSoldPlaces().equals(kvTableDto.getMaxPlaces()) || kvTableDto.getSoldPlaces() >= kvTableDto.getMaxPlaces()) {
            throw new MyException(ExceptionCode.MAX_PLACES, "ALL PLACES ARE SOLD EXCEPTION");
        } else {
            try {
                Long id = manyCustomers.getKvTableId();
                KvTable kvTable = kvTableRepository.findById(id).orElseThrow(NullPointerException::new);
                int newSoldPlaces = kvTable.getSoldPlaces() + manyCustomers.getCustomers().size();
                kvTable.setSoldPlaces(newSoldPlaces);

                manyCustomers.setKvTable(modelMapper.map(kvTable, KvTableDto.class));
                for (CustomerDto customerDto : manyCustomers.getCustomers()) {
                    customerRepository.save(modelMapper.map(customerDto, Customer.class));
                }
                Principal principal = SecurityContextHolder.getContext().getAuthentication();
                User userFromUsername = userService.getUserFromUsername(principal.getName());
                UserDto userDto = modelMapper.map(userFromUsername, UserDto.class);
                userService.decerementNumberOfTickets(userDto, manyCustomers.getCustomers().size());
                manyCustomers.getCustomers().forEach(customerDto -> log.info("Customer '" + customerDto + "' added!"));
                return manyCustomers;
            } catch (Exception e) {
                throw new MyException(ExceptionCode.SERVICE, "ADDING MANY CUSTOMERS EXCEPTION: " + e.getMessage());
            }
        }

    }

    public ManyCustomers updateManyCustomer(ManyCustomers manyCustomers) {
        try {
            for (int i = 0; i < manyCustomers.getCustomers().size(); i++) {
                //customer ktory jest wlasnie zapisywany
                CustomerDto customerFromDb = getOneCustomer(manyCustomers.getCustomers().get(i).getId()).orElseThrow(NullPointerException::new);

                //kvTable custmera ktory jest wlasnie zapisywany
                KvTableDto tableFromDb = kvTableService.getOneKvTable(customerFromDb.getKvTable().getId()).orElseThrow(NullPointerException::new);

                //porownanie manyCustomers kvTable (czylo tych co chce zapisac) z aktualnym kvTable Przypisanym do danego customersa
                if (!manyCustomers.getKvTableId().equals(tableFromDb.getId())) {
                    tableFromDb.setSoldPlaces(tableFromDb.getSoldPlaces() - 1);
                    KvTableDto incrementedTableDto = kvTableService.getOneKvTable(manyCustomers.getKvTableId()).orElseThrow(NegativeArraySizeException::new);
                    incrementedTableDto.setSoldPlaces(incrementedTableDto.getSoldPlaces() + 1);
                    incrementedTableDto.setTicket(tableFromDb.getTicket());
                    customerFromDb.setKvTable(incrementedTableDto);
                    customerRepository.save(modelMapper.map(customerFromDb, Customer.class));
                    kvTableService.addOrUpdateKvTable(tableFromDb);
                    kvTableService.addOrUpdateKvTable(incrementedTableDto);
                }
                kvTableService.addOrUpdateKvTable(tableFromDb);
                customerRepository.save(modelMapper.map(customerFromDb, Customer.class));
                manyCustomers.getCustomers().forEach(customerDto -> log.info("Customer '" + customerDto + "' updated!"));
            }
        } catch (Exception e) {
            throw new MyException(ExceptionCode.SERVICE, "UPDATING MANY CUSTOMERS EXCEPTION: " + e.getMessage());
        }
        return manyCustomers;
    }

    //nie wiem czy zadziala.
    //NIE KORZYSTAC BEZ SPRAWDZENIA FUNKCJONALNOSCI
    /*public void updateCustomer(CustomerDto customerDto) {
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
    }*/

    /**
     * price for people without student card: 190 zł
     * price for people with student card for all places on the ground floor: 150 zł
     * price for people with student card for all places on the other floors: 140 zł
     *
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
        log.info("Price for " + manyCustomers.getCustomers().size() + " counted: " + price);
        return price;
    }

    public Set<CustomerDto> getCustomersSoldByUser(UserDto userDto){
        User user = modelMapper.map(userDto, User.class);
         return customerRepository.findByUser(user)
                .stream()
                .map(customer-> modelMapper.map(customer, CustomerDto.class))
                .collect(Collectors.toSet());
    }

}
