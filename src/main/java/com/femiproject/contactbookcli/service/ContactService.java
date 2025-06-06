package com.femiproject.contactbookcli.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.femiproject.contactbookcli.model.Contact;
import com.femiproject.contactbookcli.storage.Storage;

public class ContactService {

    private List<Contact> contacts;
    private Storage<Contact> storage;

    public ContactService(Storage<Contact> storage) throws Exception {
        this.storage = storage;
        this.contacts = storage.load();
        if (this.contacts == null)
            this.contacts = new ArrayList<>();
    }

    public void addContact(Contact contact) throws Exception {
        contacts.add(contact);
        storage.save(contacts);
    }

    public Contact findContactById(String id) {
        return contacts.stream()
                .filter(s -> s.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public boolean deleteContact(String id) throws Exception {
        Contact contact = findContactById(id);

        if (contact != null) {
            contacts.remove(contact);
            storage.save(contacts);
            return true;
        }
        return false;
    }

    public boolean updateContact(String id, String name, String phone, String email, String address) throws Exception {
        Contact contact = findContactById(id);
        if (contact == null) {
            return false;
        }

        if (name != null) {
            contact.setName(name);
        }
        if (phone != null) {
            contact.setPhone(phone);
        }
        if (email != null) {
            contact.setEmail(email);
        }
        if (address != null) {
            contact.setAddress(address);
        }
        storage.save(contacts);
        return true;
    }

    public List<Contact> searchContacts(String query) {
        if (query == null || query.trim().isEmpty()) {
            return new ArrayList<>();
        }

        String searchQuery = query.toLowerCase().trim();
        return contacts.stream()
                .filter(contact -> {
                    return (contact.getId() != null && contact.getId().toLowerCase().contains(searchQuery)) ||
                            (contact.getName() != null && contact.getName().toLowerCase().contains(searchQuery)) ||
                            (contact.getPhone() != null && contact.getPhone().toLowerCase().contains(searchQuery)) ||
                            (contact.getEmail() != null && contact.getEmail().toLowerCase().contains(searchQuery)) ||
                            (contact.getAddress() != null && contact.getAddress().toLowerCase().contains(searchQuery));
                })
                .collect(Collectors.toList());

    }

    public List<Contact> listAllContacts() {
        return new ArrayList<>(contacts);
    }

}