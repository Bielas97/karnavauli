package com.karnavauli.app.model.dto;

import com.karnavauli.app.model.entities.User;
import com.karnavauli.app.service.KvTableService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ManyCustomers {
    private List<CustomerDto> customers;
    private Long kvTableId;


    public Long getKvTableId() {
        return kvTableId;
    }

    public void setKvTableId(Long kvTableId) {
        this.kvTableId = kvTableId;
    }

    public void setKvTable(KvTableDto kvTable) {
        customers.forEach(kvTableDto -> kvTableDto.setKvTable(kvTable));

    }

    public void setUserDto(User user) {
        customers.forEach(customerDto -> customerDto.setUser(user));
    }

    public ManyCustomers() {
    }

    public ManyCustomers(int size) {
        customers = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            customers.add(new CustomerDto());
        }
    }

    public List<CustomerDto> getCustomers() {
        return customers;
    }

    public void setCustomers(List<CustomerDto> customers) {
        this.customers = customers;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ManyCustomers that = (ManyCustomers) o;
        return Objects.equals(customers, that.customers);
    }

    @Override
    public int hashCode() {

        return Objects.hash(customers);
    }

    @Override
    public String toString() {
        return "ManyCustomers{" +
                "customers=" + customers +
                '}';
    }
}
