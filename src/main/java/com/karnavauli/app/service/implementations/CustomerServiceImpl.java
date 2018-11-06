package com.karnavauli.app.service.implementations;

import com.karnavauli.app.model.dto.CustomerDto;
import com.karnavauli.app.model.dto.KvTableDto;
import com.karnavauli.app.model.dto.ManyCustomers;
import com.karnavauli.app.model.dto.UserDto;
import com.karnavauli.app.model.entities.Customer;
import com.karnavauli.app.model.entities.KvTable;
import com.karnavauli.app.repository.CustomerRepository;
import com.karnavauli.app.repository.KvTableRepository;
import com.karnavauli.app.service.CustomerService;
import com.karnavauli.app.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class CustomerServiceImpl implements CustomerService {
    private CustomerRepository customerRepository;
    private KvTableRepository kvTableRepository;
    private UserService userService;
    private ModelMapper modelMapper;

    public CustomerServiceImpl(CustomerRepository customerRepository, KvTableRepository kvTableRepository, UserService userService, ModelMapper modelMapper) {
        this.customerRepository = customerRepository;
        this.kvTableRepository = kvTableRepository;
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @Override
    public void addCustomer(CustomerDto customerDto) {
        KvTable table = kvTableRepository.findById(customerDto.getKvTable().getId()).orElseThrow(NullPointerException::new);
        table.setOccupiedPlaces(table.getOccupiedPlaces() + 1);
        kvTableRepository.save(table);
        customerRepository.save(modelMapper.map(customerDto, Customer.class));
    }

    @Override
    public void deleteCustomer(Long id) {
        CustomerDto customerDto = getOneCustomer(id).orElseThrow(NullPointerException::new);
        KvTable table = kvTableRepository.findById(customerDto.getKvTable().getId()).orElseThrow(NullPointerException::new);
        table.setOccupiedPlaces(table.getOccupiedPlaces() - 1);
        kvTableRepository.save(table);
        customerRepository.deleteById(id);
        Principal principal = SecurityContextHolder.getContext().getAuthentication();
        UserDto userDto = modelMapper.map(userService.getUserFromUsername(principal.getName()), UserDto.class);
        userService.incrementNumberOfTickets(userDto);
    }

    @Override
    public Optional<CustomerDto> getOneCustomer(Long id) {
        return customerRepository.findById(id).map(c -> modelMapper.map(c, CustomerDto.class));
    }

    @Override
    public List<CustomerDto> getAll() {
        return customerRepository.findAll()
                .stream()
                .map(c -> modelMapper.map(c, CustomerDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public void addManyCustomers(ManyCustomers manyCustomers) {
        for (int i = 0; i < manyCustomers.getCustomers().size(); i++) {
            KvTable table = kvTableRepository.findById(manyCustomers.getCustomers().get(i).getKvTable().getId()).orElseThrow(NullPointerException::new);
            table.setOccupiedPlaces(table.getOccupiedPlaces() + 1);
            kvTableRepository.save(table);
            customerRepository.save(modelMapper.map(manyCustomers.getCustomers().get(i), Customer.class));
        }
        Principal principal = SecurityContextHolder.getContext().getAuthentication();
        UserDto userDto = modelMapper.map(userService.getUserFromUsername(principal.getName()), UserDto.class);
        userService.decerementNumberOfTickets(userDto, manyCustomers.getCustomers().size());



    }

    @Override
    public void updateManyCustomer(ManyCustomers manyCustomers) {
        for (int i = 0; i < manyCustomers.getCustomers().size(); i++) {
            Optional<CustomerDto> customerFromDb = getOneCustomer(manyCustomers.getCustomers().get(i).getId());
            Optional<KvTable> tableFromDb = kvTableRepository.findById(customerFromDb.get().getKvTable().getId());

            if (!manyCustomers.getKvTableId().equals(tableFromDb.get().getId())) {
                tableFromDb.get().setOccupiedPlaces(tableFromDb.get().getOccupiedPlaces() - 1);
                KvTableDto incrementedTable = manyCustomers.getCustomers().get(i).getKvTable();
                incrementedTable.setOccupiedPlaces(incrementedTable.getOccupiedPlaces() +1);
                kvTableRepository.save(modelMapper.map(incrementedTable, KvTable.class));
            }

            kvTableRepository.save(tableFromDb.get());
            customerRepository.save(modelMapper.map(manyCustomers.getCustomers().get(i), Customer.class));
        }
    }

    //nie wiem czy zadziala.
    //NIE KORZYSTAC BEZ SPRAWDZENIA FUNKCJONALNOSCI
    @Override
    public void updateCustomer(CustomerDto customerDto) {
        Optional<CustomerDto> customerFromDb = getOneCustomer(customerDto.getId());
        Optional<KvTable> tableFromDb = kvTableRepository.findById(customerFromDb.get().getKvTable().getId());

        if (!customerDto.getKvTable().getId().equals(tableFromDb.get().getId())) {
            tableFromDb.get().setOccupiedPlaces(tableFromDb.get().getOccupiedPlaces() - 1);
            KvTableDto incrementedTable = customerDto.getKvTable();
            incrementedTable.setOccupiedPlaces(incrementedTable.getOccupiedPlaces() +1);
            kvTableRepository.save(modelMapper.map(incrementedTable, KvTable.class));
        }

        kvTableRepository.save(tableFromDb.get());
        customerRepository.save(modelMapper.map(customerDto, Customer.class));
    }


}
