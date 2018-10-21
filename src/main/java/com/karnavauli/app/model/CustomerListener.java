package com.karnavauli.app.model;

import com.karnavauli.app.model.entities.Customer;
import com.karnavauli.app.repository.KvTableRepository;
import org.springframework.stereotype.Component;

import javax.persistence.PrePersist;

@Component
public class CustomerListener {

    /**
     * @PrePersist Executed before the entity manager persist operation is actually executed or cascaded. This call is synchronous with the persist operation.
     * @PreRemove Executed before the entity manager remove operation is actually executed or cascaded. This call is synchronous with the remove operation.
     * @PostPersist Executed after the entity manager persist operation is actually executed or cascaded. This call is invoked after the database INSERT is executed.
     * @PostRemove Executed after the entity manager remove operation is actually executed or cascaded. This call is synchronous with the remove operation.
     * @PreUpdate Executed before the database UPDATE operation.
     * @PostUpdate Executed after the database UPDATE operation.
     * @PostLoad Executed after an entity has been loaded into the current persistence context or an entity has been refreshed.
     */
    @PrePersist
    public void incrementOccupiedPlaces(Customer customer) {
        customer.getKvTable().setOccupiedPlaces(customer.getKvTable().getOccupiedPlaces() + 1);
    }

    /*@PostRemove
    public void decrementOccupiedPlaces(Customer customer) {
        customer.getKvTable().setOccupiedPlaces(customer.getKvTable().getOccupiedPlaces() - 1);
    }*/
}
