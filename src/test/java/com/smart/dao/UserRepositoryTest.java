package com.smart.dao;

import com.smart.entities.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;
    @Test
    void getUserByUserName() {
        User user =userRepository.getUserByUserName("sam@123");

        assertEquals(user.getName(),"Sam");
    }
}