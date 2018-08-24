package com.karnavauli.app.repository;

import com.karnavauli.app.model.enums.Role;
import com.karnavauli.app.model.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{
    User findByUsername(String username);
}
