package com.femiproject.clinicmanagement;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class AppointmentManager {
    private List<Appointment> appointments;
    private final String appointmentFile = "appointments.json";
    private final ObjectMapper objectMapper;
    private final PatientManager patientManager;
    private final DoctorManager doctorManager;

    public AppointmentManager(PatientManager patientManager, DoctorManager doctorManager) {
        this.appointments = new ArrayList<>();
        this.patientManager = patientManager;
        this.doctorManager = doctorManager;
        this.objectMapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .enable(SerializationFeature.INDENT_OUTPUT);
        loadAppointments();
    }

    private void loadAppointments() {
        try {
            File file = new File(appointmentFile);
            if (file.exists() && file.length() > 0) {
                List<Appointment> loadedAppointments = objectMapper.readValue(file,
                        new TypeReference<List<Appointment>>() {
                        });
                appointments = loadedAppointments;
                System.out.println("Appointment loaded successfully from " + appointmentFile);
            } else {
                System.out.println("No existing appointment found. Starting with empty appointment list.");
            }
        } catch (IOException e) {
            System.err.println("Error loading appointment: " + e.getMessage());
            appointments = new ArrayList<>();
        }
    }

    private void saveAppointments() {
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(appointmentFile), appointments);
            System.out.println("Appointment saved successfully to " + appointmentFile);
        } catch (IOException e) {
            System.err.println("Error saving Appointment: " + e.getMessage());
        }
    }

    public boolean createAppointment(Patient patient, Doctor doctor, LocalDateTime desiredTime) {
        if (patient == null || doctor == null || desiredTime == null) {
            System.err.println("Patient, doctor, and appointment time cannot be null");
            return false;
        }

        if (desiredTime.isBefore(LocalDateTime.now())) {
            System.err.println("Cannot book appointment in the past");
            return false;
        }

        if (!doctor.isAvailableAt(desiredTime)) {
            System.err.println("Doctor is not available at the specified time");
            return false;
        }

        String appointmentId = UUID.randomUUID().toString();
        Appointment appointment = new Appointment(appointmentId, patient.getPatientId(), doctor.getDoctorId(),
                desiredTime);

        appointments.add(appointment);

        // Remove the slot from doctor's availability
        doctor.removeAvailableSlot(desiredTime);
        doctorManager.updateDoctor(doctor);

        // Add appointment to patient's list
        patient.getAppointments().add(appointment);
        patientManager.updatePatient(patient);

        saveAppointments();
        System.out.println("Appointment created successfully with ID: " + appointmentId);
        return true;
    }

    public List<Appointment> getAppointmentsByPatient(String patientId) {
        return appointments.stream()
                .filter(a -> a.getPatientId().equals(patientId))
                .toList();
    }

    public List<Appointment> getAppointmentsByDoctor(String doctorId) {
        return appointments.stream()
                .filter(a -> a.getDoctorId().equals(doctorId))
                .toList();
    }

    public List<Appointment> getAllAppointments() {
        return new ArrayList<>(appointments);
    }

    public boolean cancelAppointment(String appointmentId) {
        for (Appointment appointment : appointments) {
            if (appointment.getAppointmentId().equals(appointmentId)) {
                appointment.setStatus(Status.CANCELED);

                // Add the slot back to doctor's availability
                Doctor doctor = doctorManager.findDoctorById(appointment.getDoctorId());
                if (doctor != null) {
                    doctor.addAvailableSlot(appointment.getAppointmentTime());
                    doctorManager.updateDoctor(doctor);
                }

                saveAppointments();
                System.out.println("Appointment canceled successfully");
                return true;
            }
        }
        System.err.println("Appointment not found");
        return false;
    }
}
