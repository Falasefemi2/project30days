package com.femiproject.foodorderingservice;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Driver {
    @JsonProperty("driverId")
    private String driverId;
    @JsonProperty("name")
    private String name;
    @JsonProperty("phoneNumber")
    private String phoneNumber;
    @JsonProperty("isAvailable")
    private boolean isAvailable;

    @JsonCreator
    public Driver(
            @JsonProperty("driverId") String driverId,
            @JsonProperty("name") String name,
            @JsonProperty("phoneNumber") String phoneNumber,
            @JsonProperty("isAvailable") boolean isAvailable) {
        this.driverId = driverId;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.isAvailable = isAvailable;
    }

    // Constructor for creating new drivers
    public Driver(String name, String phoneNumber) {
        this.driverId = generateDriverId();
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.isAvailable = true; // New drivers are available by default
    }

    public String getDriverId() {
        return driverId;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        this.isAvailable = available;
    }

    private String generateDriverId() {
        return UUID.randomUUID().toString();
    }

    @Override
    public String toString() {
        return "Driver [driverId=" + driverId + ", name=" + name + ", phoneNumber=" + phoneNumber
                + ", isAvailable=" + isAvailable + "]";
    }
}