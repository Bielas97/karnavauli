package com.karnavauli.app.repository;

import com.karnavauli.app.model.entities.Customer;
import com.karnavauli.app.model.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long>{
    Set<Customer> findByUser(User user);
}
