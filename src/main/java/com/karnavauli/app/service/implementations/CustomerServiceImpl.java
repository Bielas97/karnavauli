package com.karnavauli.app.service.implementations;

import com.karnavauli.app.model.entities.Customer;
import com.karnavauli.app.model.dto.CustomerDto;
import com.karnavauli.app.model.enums.Seat;
import com.karnavauli.app.repository.CustomerRepository;
import com.karnavauli.app.service.CustomerService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CustomerServiceImpl implements CustomerService {
    private CustomerRepository customerRepository;
    private ModelMapper modelMapper;

    private List<Seat> allSeats = Arrays.asList(Seat.values());
    private List<Seat> availableSeats;


    public CustomerServiceImpl(CustomerRepository customerRepository, ModelMapper modelMapper) {
        this.customerRepository = customerRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public void addOrUpdateCustomer(CustomerDto customerDto) {
        customerRepository.save(modelMapper.map(customerDto, Customer.class));
    }

    @Override
    public void deleteCustomer(Long id) {
        customerRepository.deleteById(id);
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

}
