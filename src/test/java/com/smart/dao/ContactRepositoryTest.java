package com.smart.dao;

import com.smart.entities.Contact;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ContactRepositoryTest {
    @Autowired
    private ContactRepository contactRepository;
    @Test
    void findContactByUser() {
        List<Contact> contacts = contactRepository.findContactByUser(253);
        assertNotEquals(contacts.size(),0);
    }
}