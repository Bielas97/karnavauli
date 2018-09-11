package com.karnavauli.app.model.dto;

import com.karnavauli.app.model.entities.User;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ManyCustomers {
    private List<CustomerDto> customers;

    public void setUserDto(User user) {
        for (int i = 0; i < customers.size(); i++) {
            customers.get(i).setUser(user);
        }
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
