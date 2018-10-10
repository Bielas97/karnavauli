package com.karnavauli.app.model.dto;

import com.karnavauli.app.model.entities.Customer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Objects;


@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KvTableDto {
    private Long id;
    private String name;
    private Integer maxPlaces;
    private Integer occupiedPlaces;
    private List<CustomerDto> customer;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getMaxPlaces() {
        return maxPlaces;
    }

    public void setMaxPlaces(Integer maxPlaces) {
        this.maxPlaces = maxPlaces;
    }

    public Integer getOccupiedPlaces() {
        return occupiedPlaces;
    }

    public void setOccupiedPlaces(Integer occupiedPlaces) {
        this.occupiedPlaces = occupiedPlaces;
    }

    public List<CustomerDto> getCustomer() {
        return customer;
    }

    public void setCustomer(List<CustomerDto> customer) {
        this.customer = customer;
    }

    @Override
    public String toString() {
        return "KvTableDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", maxPlaces=" + maxPlaces +
                ", occupiedPlaces=" + occupiedPlaces +
                ", customer=" + customer +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        KvTableDto that = (KvTableDto) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(name, that.name) &&
                Objects.equals(maxPlaces, that.maxPlaces) &&
                Objects.equals(occupiedPlaces, that.occupiedPlaces) &&
                Objects.equals(customer, that.customer);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, name, maxPlaces, occupiedPlaces, customer);
    }
}
