package com.femiproject.clinicmanagement;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

public class Appointment {
    @JsonProperty
    private String appointmentId;

    @JsonProperty
    private String patientId;

    @JsonProperty
    private String doctorId;

    @JsonProperty
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime appointmentTime;

    @JsonProperty
    private Status status;

    @JsonCreator
    public Appointment(
            @JsonProperty("appointmentId") String appointmentId,
            @JsonProperty("patientId") String patientId,
            @JsonProperty("doctorId") String doctorId,
            @JsonProperty("appointmentTime") LocalDateTime appointmentTime,
            @JsonProperty("status") Status status) {
        this.appointmentId = appointmentId;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.appointmentTime = appointmentTime;
        this.status = status != null ? status : Status.SCHEDULED;
    }

    public Appointment(String appointmentId, String patientId, String doctorId, LocalDateTime appointmentTime) {
        this.appointmentId = appointmentId;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.appointmentTime = appointmentTime;
        this.status = Status.SCHEDULED;
    }

    public String getAppointmentId() {
        return appointmentId;
    }

    public String getPatientId() {
        return patientId;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public LocalDateTime getAppointmentTime() {
        return appointmentTime;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Appointment [appointmentId=" + appointmentId + ", patientId=" + patientId + ", doctorId=" + doctorId
                + ", appointmentTime=" + appointmentTime + ", status=" + status + "]";
    }
}
