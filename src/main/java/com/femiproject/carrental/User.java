package com.femiproject.carrental;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class User {
    @JsonProperty
    private String userId;
    @JsonProperty
    private String name;
    @JsonProperty
    private String phone;
    @JsonProperty
    private List<Rental> rentals;
    @JsonProperty
    private List<Vehicle> vehicles;

    @JsonCreator
    public User(
            @JsonProperty String userId,
            @JsonProperty String name,
            @JsonProperty String phone,
            @JsonProperty List<Rental> rentals,
            @JsonProperty List<Vehicle> vehicles) {
        this.userId = userId;
        this.name = name;
        this.phone = phone;
        this.rentals = rentals != null ? rentals : new ArrayList<>();
        this.vehicles = vehicles != null ? vehicles : new ArrayList<>();
    }

    public User(String name, String phone) {
        this.userId = UUID.randomUUID().toString();
        this.name = name;
        this.phone = phone;
        this.rentals = new ArrayList<>();
        this.vehicles = new ArrayList<>();
    }

    public String getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public List<Rental> getRentals() {
        return rentals;
    }

    public List<Vehicle> getVehicles() {
        return vehicles;
    }

    public void rentVehicle(Vehicle vehicle) {
        if (!vehicle.isAvailable()) {
            System.out.println("Vehicle not available.");
            return;
        }

        LocalDateTime now = LocalDateTime.now();
        Rental rental = new Rental(
                now,
                null,
                vehicle,
                Status.ACTIVE,
                0);

        rentals.add(rental);
        vehicle.rentOut();
        System.out.println("Rental successful for: " + vehicle.getName());
    }

    public void returnVehicle(Vehicle vehicle) {
        Rental activeRental = rentals.stream()
                .filter(r -> r.getVehicle().equals(vehicle) && r.getStatus().equals(Status.ACTIVE)).findFirst()
                .orElse(null);

        if (activeRental == null) {
            System.out.println("No active rental found for this vehicle.");
            return;
        } else {
            LocalDateTime now = LocalDateTime.now();
            long days = ChronoUnit.DAYS.between(activeRental.getStartDate(), now);
            double cost = days * vehicle.getPricePerDay();
            activeRental.setReturnDate(now);
            activeRental.setRentalCost(cost);
            activeRental.setStatus(Status.RETURNED);

            vehicle.returnVehicle();
            System.out.println("Vehicle returned successfully.");
        }

    }

    @Override
    public String toString() {
        return "User [userId=" + userId + ", name=" + name + ", phone=" + phone + ", rentals=" + rentals + "]";
    }

}
