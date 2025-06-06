package com.femiproject.contactbookcli.cli;

import java.util.List;
import java.util.Scanner;
import java.util.UUID;

import com.femiproject.contactbookcli.model.Contact;
import com.femiproject.contactbookcli.service.ContactService;
import com.femiproject.contactbookcli.storage.CsvFileStorage;
import com.femiproject.contactbookcli.storage.FileStorage;
import com.femiproject.contactbookcli.storage.Storage;

public class ContactBookCLI {

    private Storage<Contact> storage;
    private ContactService service;
    private Scanner scanner;

    public ContactBookCLI() throws Exception {
        this.storage = new CsvFileStorage(); // can also be stored in json format with the fileStorage
        this.service = new ContactService(storage);
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        System.out.println("Welcome to Contact Book CLI!");
        String command;

        while (true) {
            System.out.print("\nEnter command (add, update, delete, search, list, exit): ");
            command = scanner.nextLine().trim().toLowerCase();

            switch (command) {
                case "add" -> addContact();
                case "update" -> updateContact();
                case "delete" -> deleteContact();
                case "search" -> searchContacts();
                case "list" -> listContacts();
                case "exit" -> {
                    System.out.println("Goodbye!");
                    return;
                }
                default -> {
                    System.out.println("Unknown command. Try again.");
                }
            }
        }
    }

    public void addContact() {
        System.out.print("Enter name: ");
        String name = scanner.nextLine();

        System.out.print("Enter phone: ");
        String phone = scanner.nextLine();

        System.out.print("Enter email: ");
        String email = scanner.nextLine();

        System.out.print("Enter address: ");
        String address = scanner.nextLine();

        String id = UUID.randomUUID().toString();

        Contact contact = new Contact(id, name, phone, email, address);

        try {
            service.addContact(contact);
            System.out.println("Contact added with ID: " + id);
        } catch (Exception e) {
            System.err.println("Error adding contact: " + e.getMessage());
        }
    }

    private void updateContact() {
        System.out.print("Enter contact ID to update: ");
        String id = scanner.nextLine();

        System.out.print("Enter new name (leave blank to keep unchanged): ");
        String name = scanner.nextLine();

        System.out.print("Enter new phone (leave blank to keep unchanged): ");
        String phone = scanner.nextLine();

        System.out.print("Enter new email (leave blank to keep unchanged): ");
        String email = scanner.nextLine();

        System.out.print("Enter new address (leave blank to keep unchanged): ");
        String address = scanner.nextLine();

        try {
            boolean updated = service.updateContact(id,
                    name.isBlank() ? null : name,
                    phone.isBlank() ? null : phone,
                    email.isBlank() ? null : email,
                    address.isBlank() ? null : address);

            if (updated) {
                System.out.println("Contact updated successfully.");
            } else {
                System.out.println("Contact not found.");
            }
        } catch (Exception e) {
            System.err.println("Error updating contact: " + e.getMessage());
        }
    }

    private void deleteContact() {
        System.out.print("Enter contact ID to delete: ");
        String id = scanner.nextLine();

        try {
            boolean deleted = service.deleteContact(id);
            if (deleted) {
                System.out.println("Contact deleted.");
            } else {
                System.out.println("Contact not found.");
            }
        } catch (Exception e) {
            System.err.println("Error deleting contact: " + e.getMessage());
        }
    }

    private void searchContacts() {
        System.out.print("Enter search query: ");
        String query = scanner.nextLine();

        List<Contact> results = service.searchContacts(query);
        if (results.isEmpty()) {
            System.out.println("No contacts found.");
        } else {
            results.forEach(this::printContact);
        }
    }

    private void listContacts() {
        List<Contact> contacts = service.listAllContacts();
        if (contacts.isEmpty()) {
            System.out.println("No contacts found.");
        } else {
            contacts.forEach(this::printContact);
        }
    }

    private void printContact(Contact contact) {
        System.out.println("--------------------------------");
        System.out.println("ID: " + contact.getId());
        System.out.println("Name: " + contact.getName());
        System.out.println("Phone: " + contact.getPhone());
        System.out.println("Email: " + contact.getEmail());
        System.out.println("Address: " + contact.getAddress());
    }

}
