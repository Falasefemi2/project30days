package com.femiproject.clinicmanagement;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Patient {
    @JsonProperty
    private String patientId;

    @JsonProperty
    private String name;

    @JsonProperty
    private String email;

    @JsonProperty
    private String phone;

    @JsonProperty
    private List<Appointment> appointments;

    @JsonCreator
    public Patient(
            @JsonProperty String patientId,
            @JsonProperty String name,
            @JsonProperty String email,
            @JsonProperty String phone,
            @JsonProperty List<Appointment> appointments) {
        this.patientId = patientId;
        this.name = name;
        this.phone = phone;
        this.appointments = appointments != null ? appointments : new ArrayList<>();
    }

    public Patient(String name, String email, String phone) {
        this.patientId = UUID.randomUUID().toString();
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.appointments = new ArrayList<>();
    }

    public String getPatientId() {
        return patientId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public List<Appointment> getAppointments() {
        return appointments;
    }

    public void setAppointments(List<Appointment> appointments) {
        this.appointments = appointments;
    }

    @Override
    public String toString() {
        return "Patient [patientId=" + patientId + ", name=" + name + ", email=" + email + ", phone=" + phone
                + ", appointments=" + appointments + "]";
    }
}
