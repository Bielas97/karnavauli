package com.karnavauli.app.service;

import com.karnavauli.app.model.dto.CustomerDto;
import com.karnavauli.app.model.entities.Customer;
import com.karnavauli.app.repository.CustomerRepository;
import com.karnavauli.app.service.implementations.CustomerServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyListOf;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CustomerServiceTest {

    @Mock
    CustomerRepository customerRepository;

    @Mock
    ModelMapper modelMapper;

    @InjectMocks
    CustomerServiceImpl customerService;

    @Test
    public void getAll() {
        List<Customer> customers = customerList();
        List<CustomerDto> customersDto = customerDtoList();
        when(customerRepository.findAll()).thenReturn(customers);
        when(modelMapper.map(customers.get(0), CustomerDto.class)).thenReturn(customersDto.get(0));
        when(modelMapper.map(customers.get(1), CustomerDto.class)).thenReturn(customersDto.get(1));
        List<CustomerDto> all = customerService.getAll();
        assertEquals(2, all.size());
        assertEquals("Test", all.get(0).getName());
        assertEquals("Test1", all.get(1).getName());
    }

    private List<Customer> customerList() {
        return Arrays.asList(
                Customer.builder().id(1L).name("Test").build(),
                Customer.builder().id(1L).name("Test1").build()
        );
    }

    private List<CustomerDto> customerDtoList() {
        return Arrays.asList(
                CustomerDto.builder().id(1L).name("Test").build(),
                CustomerDto.builder().id(1L).name("Test1").build()
        );
    }
}