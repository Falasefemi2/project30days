package com.femiproject.clinicmanagement;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class PatientManager {

    private List<Patient> patients;
    private final String patient_file = "patients.json";
    private final ObjectMapper objectMapper;

    public PatientManager() {
        this.patients = new ArrayList<>();
        this.objectMapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .enable(SerializationFeature.INDENT_OUTPUT);
        loadPatients();
    }

    private void loadPatients() {
        try {
            File file = new File(patient_file);
            if (file.exists() && file.length() > 0) {
                List<Patient> loadedPatients = objectMapper.readValue(file, new TypeReference<List<Patient>>() {
                });
                patients = loadedPatients;
                System.out.println("Patient loaded successfully from " + patient_file);
            } else {
                System.out.println("No existing patient found. Starting with empty patient list.");
            }
        } catch (IOException e) {
            System.err.println("Error loading members: " + e.getMessage());
            patients = new ArrayList<>();
        }
    }

    private void savePatients() {
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(patient_file), patients);
            System.out.println("Patients saved successfully to " + patient_file);
        } catch (IOException e) {
            System.err.println("Error saving Patients: " + e.getMessage());
        }
    }

    public void addPatient(Patient patient) {
        if (patient != null) {
            patients.add(patient);
            savePatients();
            System.out.println("Patient " + patient.getName() + " added successfully");
        }
    }

    public Patient findPatientById(String patientId) {
        return patients.stream()
                .filter(p -> p.getPatientId().equals(patientId))
                .findFirst()
                .orElse(null);
    }

    public Patient findPatientByName(String name) {
        return patients.stream()
                .filter(p -> p.getName().equals(name))
                .findFirst()
                .orElse(null);
    }

    public List<Patient> listAllPatients() {
        return new ArrayList<>(patients);
    }

    public void updatePatient(Patient patient) {
        for (int i = 0; i < patients.size(); i++) {
            if (patients.get(i).getPatientId().equals(patient.getPatientId())) {
                patients.set(i, patient);
                savePatients();
                break;
            }
        }
    }
}
