package com.karnavauli.app.service;

import com.karnavauli.app.model.dto.CustomerDto;
import com.karnavauli.app.model.dto.ManyCustomers;
import com.karnavauli.app.model.entities.Customer;

import java.util.List;
import java.util.Optional;

public interface CustomerService {
    void addCustomer(CustomerDto customerDto);
    void deleteCustomer(Long id);
    Optional<CustomerDto> getOneCustomer(Long id);
    List<CustomerDto> getAll();

    void addManyCustomers(ManyCustomers manyCustomers);
    void updateManyCustomer(ManyCustomers manyCustomers);
    void updateCustomer(CustomerDto customerDto);
}
