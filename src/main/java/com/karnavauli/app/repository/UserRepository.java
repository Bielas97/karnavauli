package com.karnavauli.app.repository;

import com.karnavauli.app.model.enums.Role;
import com.karnavauli.app.model.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{
    User findByUsername(String username);
    Set<User> findByRole(Role role);
}
