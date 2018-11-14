/*
package com.karnavauli.app.repository;

import com.karnavauli.app.model.entities.User;
import com.karnavauli.app.model.enums.Role;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void findByUsername() {
        //given
        User user = User.builder().id(null).username("Michal").build();
        entityManager.persist(user);
        entityManager.flush();

        //when
        User byUsername = userRepository.findByUsername("Michal");

        //then
        assertEquals("Michal", byUsername.getUsername());
    }
}*/
