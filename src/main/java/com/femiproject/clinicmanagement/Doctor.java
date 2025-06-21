package com.femiproject.clinicmanagement;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

public class Doctor {
    @JsonProperty
    private String doctorId;

    @JsonProperty
    private String name;

    @JsonProperty
    private String specialization;

    @JsonProperty
    @JsonSerialize(contentUsing = LocalDateTimeSerializer.class)
    @JsonDeserialize(contentUsing = LocalDateTimeDeserializer.class)
    private List<LocalDateTime> availableSlots;

    @JsonProperty
    private List<Appointment> appointments;

    @JsonCreator
    public Doctor(
            @JsonProperty("doctorId") String doctorId,
            @JsonProperty("name") String name,
            @JsonProperty("specialization") String specialization,
            @JsonProperty("availableSlots") List<LocalDateTime> availableSlots,
            @JsonProperty("appointments") List<Appointment> appointments) {
        this.doctorId = doctorId;
        this.name = name;
        this.specialization = specialization;
        this.availableSlots = availableSlots != null ? availableSlots : generateDefaultSlots();
        this.appointments = appointments != null ? appointments : new ArrayList<>();
    }

    public Doctor(String name, String specialization) {
        this.doctorId = UUID.randomUUID().toString();
        this.name = name;
        this.specialization = specialization;
        this.availableSlots = generateDefaultSlots();
        this.appointments = new ArrayList<>();
    }

    private List<LocalDateTime> generateDefaultSlots() {
        List<LocalDateTime> slots = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();

        // Generate slots for the next 7 days, 9 AM to 5 PM, every hour
        for (int day = 1; day <= 7; day++) {
            LocalDateTime date = now.plusDays(day).withHour(9).withMinute(0).withSecond(0).withNano(0);

            for (int hour = 9; hour < 17; hour++) {
                LocalDateTime slot = date.withHour(hour);
                slots.add(slot);
            }
        }

        return slots;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public String getName() {
        return name;
    }

    public List<LocalDateTime> getAvailableSlots() {
        return availableSlots;
    }

    public String getSpecialization() {
        return specialization;
    }

    public List<Appointment> getAppointments() {
        return appointments;
    }

    public void setAppointments(List<Appointment> appointments) {
        this.appointments = appointments;
    }

    public boolean isAvailableAt(LocalDateTime time) {
        return availableSlots.contains(time);
    }

    public void removeAvailableSlot(LocalDateTime time) {
        availableSlots.remove(time);
    }

    public void addAvailableSlot(LocalDateTime time) {
        if (!availableSlots.contains(time)) {
            availableSlots.add(time);
        }
    }

    @Override
    public String toString() {
        return "Doctor [doctorId=" + doctorId + ", name=" + name + ", specialization=" + specialization
                + ", appointments=" + appointments + "]";
    }
}
