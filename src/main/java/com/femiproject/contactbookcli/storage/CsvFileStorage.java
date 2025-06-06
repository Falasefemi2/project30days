package com.femiproject.contactbookcli.storage;

import com.femiproject.contactbookcli.model.Contact;
import com.opencsv.*;
import com.opencsv.exceptions.CsvException;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class CsvFileStorage implements Storage<Contact> {

    private final String csvFile = "contacts.csv";
    private List<Contact> contacts;

    public CsvFileStorage() throws IOException, CsvException {
        contacts = new ArrayList<>();
        load();
    }

    @Override
    public void save(List<Contact> contacts) throws IOException {
        try (Writer writer = Files.newBufferedWriter(Paths.get(csvFile));
                CSVWriter csvWriter = new CSVWriter(writer)) {

            // Write header
            String[] header = { "id", "name", "phone", "email", "address" };
            csvWriter.writeNext(header);

            for (Contact c : contacts) {
                String[] line = { c.getId(), c.getName(), c.getPhone(), c.getEmail(), c.getAddress() };
                csvWriter.writeNext(line);
            }
        }
    }

    @Override
    public List<Contact> load() throws IOException, CsvException {
        File file = new File(csvFile);
        if (!file.exists() || file.length() == 0) {
            // No file yet, return empty list
            return contacts;
        }

        try (Reader reader = Files.newBufferedReader(Paths.get(csvFile));
                CSVReader csvReader = new CSVReader(reader)) {

            List<String[]> records = csvReader.readAll();

            contacts.clear();
            boolean firstLine = true;
            for (String[] record : records) {
                if (firstLine) {
                    firstLine = false; // skip header
                    continue;
                }
                if (record.length >= 5) {
                    Contact c = new Contact(record[0], record[1], record[2], record[3], record[4]);
                    contacts.add(c);
                }
            }
        }

        return contacts;
    }
}
