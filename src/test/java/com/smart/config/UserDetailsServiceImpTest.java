package com.smart.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class UserDetailsServiceImpTest {
    @Autowired
    private UserDetailsServiceImp userDetailsServiceImp;
    @Test
    void loadUserByUsername() {
        UserDetails ud=userDetailsServiceImp.loadUserByUsername("sam@123");
        assertNotEquals(ud.getPassword(),"sam123");
        assertNotNull(ud.getPassword());
    }
}