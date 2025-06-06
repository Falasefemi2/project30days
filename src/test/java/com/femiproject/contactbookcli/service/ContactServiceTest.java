package com.femiproject.contactbookcli.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.femiproject.contactbookcli.model.Contact;
import com.femiproject.contactbookcli.storage.Storage;

@ExtendWith(MockitoExtension.class)
public class ContactServiceTest {

    @Mock
    private Storage<Contact> storage;

    private ContactService contactService;
    private List<Contact> contacts;

    @BeforeEach
    void setUp() throws Exception {
        contacts = new ArrayList<>();
        when(storage.load()).thenReturn(contacts);
        contactService = new ContactService(storage);
    }

    @Test
    void testAddContact() throws Exception {
        Contact contact = new Contact("1", "John Doe", "1234567890", "john@example.com", "123 Main St");
        contactService.addContact(contact);
        verify(storage).save(anyList());
        assertEquals(1, contacts.size());
        assertEquals(contact, contacts.get(0));
    }

    @Test
    void testDeleteContact() throws Exception {
        Contact contact = new Contact("1", "John Doe", "1234567890", "john@example.com", "123 Main St");
        contacts.add(contact);

        boolean result = contactService.deleteContact("1");
        assertTrue(result);
        verify(storage).save(anyList());
        assertTrue(contacts.isEmpty());
    }

    @Test
    void testDeleteContactNotFound() throws Exception {
        boolean result = contactService.deleteContact("1");
        assertFalse(result);
        verify(storage, never()).save(anyList());
    }

    @Test
    void testFindContactById() {
        Contact contact = new Contact("1", "John Doe", "1234567890", "john@example.com", "123 Main St");
        contacts.add(contact);

        Contact found = contactService.findContactById("1");
        assertEquals(contact, found);
    }

    @Test
    void testFindContactByIdNotFound() {
        Contact found = contactService.findContactById("1");
        assertNull(found);
    }

    @Test
    void testListAllContacts() {
        Contact contact1 = new Contact("1", "John Doe", "1234567890", "john@example.com", "123 Main St");
        Contact contact2 = new Contact("2", "Jane Doe", "0987654321", "jane@example.com", "456 Oak St");
        contacts.add(contact1);
        contacts.add(contact2);

        List<Contact> allContacts = contactService.listAllContacts();
        assertEquals(2, allContacts.size());
        assertTrue(allContacts.contains(contact1));
        assertTrue(allContacts.contains(contact2));
    }

    @Test
    void testSearchContacts() {
        Contact contact1 = new Contact("1", "John Doe", "1234567890", "john@example.com", "123 Main St");
        Contact contact2 = new Contact("2", "Jane Doe", "0987654321", "jane@example.com", "456 Oak St");
        contacts.add(contact1);
        contacts.add(contact2);

        List<Contact> results = contactService.searchContacts("john");
        assertEquals(1, results.size());
        assertEquals(contact1, results.get(0));

        results = contactService.searchContacts("doe");
        assertEquals(2, results.size());
    }

    @Test
    void testUpdateContact() throws Exception {
        Contact contact = new Contact("1", "John Doe", "1234567890", "john@example.com", "123 Main St");
        contacts.add(contact);

        boolean result = contactService.updateContact("1", "John Smith", "9876543210", "john.smith@example.com",

                "789 New St");

        assertTrue(result);
        verify(storage).save(anyList());
        assertEquals("John Smith", contact.getName());
        assertEquals("9876543210", contact.getPhone());
        assertEquals("john.smith@example.com", contact.getEmail());
        assertEquals("789 New St", contact.getAddress());
    }

    @Test
    void testUpdateContactNotFound() throws Exception {
        boolean result = contactService.updateContact("1", "John Smith", "9876543210", "john.smith@example.com",
                "789 New St");

        assertFalse(result);
        verify(storage, never()).save(anyList());
    }
}
