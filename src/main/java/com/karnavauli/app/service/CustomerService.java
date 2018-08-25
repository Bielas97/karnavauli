package com.karnavauli.app.service;

import com.karnavauli.app.model.dto.CustomerDto;

import java.util.List;
import java.util.Optional;

public interface CustomerService {
    void addOrUpdateCustomer(CustomerDto customerDto);
    void deleteCustomer(Long id);
    Optional<CustomerDto> getOneCustomer(Long id);
    List<CustomerDto> getAll();
}
