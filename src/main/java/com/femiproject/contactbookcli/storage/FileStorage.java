package com.femiproject.contactbookcli.storage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import com.femiproject.contactbookcli.model.Contact;

public class FileStorage implements Storage<Contact> {

    private final ObjectMapper objectMapper;
    private final String contactFile = "contact.json";

    public FileStorage() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    @Override
    public void save(List<Contact> contacts) throws Exception {
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(contactFile), contacts);
        } catch (IOException e) {
            System.out.println("Error saving task: " + e.getMessage());
        }
    }

    @Override
    public List<Contact> load() throws Exception {
        File file = new File(contactFile);
        if (!file.exists() || file.length() == 0) {
            return new ArrayList<>();
        }
        return objectMapper.readValue(file, new TypeReference<List<Contact>>() {
        });
    }

}
