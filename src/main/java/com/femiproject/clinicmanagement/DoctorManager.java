package com.femiproject.clinicmanagement;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class DoctorManager {

    private List<Doctor> doctors;
    private final String doctor_file = "doctors.json";
    private final ObjectMapper objectMapper;

    public DoctorManager() {
        this.doctors = new ArrayList<>();
        this.objectMapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .enable(SerializationFeature.INDENT_OUTPUT);
        loadDoctors();
    }

    private void loadDoctors() {
        try {
            File file = new File(doctor_file);
            if (file.exists() && file.length() > 0) {
                List<Doctor> loadedDoctors = objectMapper.readValue(file, new TypeReference<List<Doctor>>() {
                });
                doctors = loadedDoctors;
                System.out.println("Doctor loaded successfully from " + doctor_file);
            } else {
                System.out.println("No existing doctor found. Starting with empty doctor list.");
            }
        } catch (IOException e) {
            System.err.println("Error loading doctor: " + e.getMessage());
            doctors = new ArrayList<>();
        }
    }

    private void saveDoctors() {
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(doctor_file), doctors);
            System.out.println("Doctors saved successfully to " + doctor_file);
        } catch (IOException e) {
            System.err.println("Error saving doctors: " + e.getMessage());
        }
    }

    public void addDoctor(Doctor doctor) {
        if (doctor != null) {
            doctors.add(doctor);
            saveDoctors();
            System.out.println("Doctor " + doctor.getName() + " added successfully");
        }
    }

    public Doctor findDoctorById(String id) {
        return doctors.stream()
                .filter(d -> d.getDoctorId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public List<Doctor> listAllDoctor() {
        return new ArrayList<>(doctors);
    }

    public void updateDoctor(Doctor doctor) {
        for (int i = 0; i < doctors.size(); i++) {
            if (doctors.get(i).getDoctorId().equals(doctor.getDoctorId())) {
                doctors.set(i, doctor);
                saveDoctors();
                break;
            }
        }
    }
}
