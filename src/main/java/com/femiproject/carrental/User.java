package com.femiproject.carrental;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

public record User(
        @JsonProperty("userId") String userId,
        @JsonProperty("name") String name,
        @JsonProperty("phone") String phone,
        @JsonProperty("rentals") List<Rental> rentals,
        @JsonProperty("vehicles") List<Vehicle> vehicles) {

    public User(String name, String phone) {
        this(UUID.randomUUID().toString(), name, phone, new ArrayList<>(), new ArrayList<>());
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
        System.out.println("Rental successful for: " + vehicle.name());
    }

    public void returnVehicle(Vehicle vehicle) {
        for (int i = 0; i < rentals.size(); i++) {
            Rental rental = rentals.get(i);
            if (rental.vehicle().vehicleId().equals(vehicle.vehicleId()) && rental.status() == Status.ACTIVE) {
                LocalDateTime now = LocalDateTime.now();
                long days = ChronoUnit.DAYS.between(rental.startDate(), now);
                if (days == 0)
                    days = 1;
                double cost = days * vehicle.pricePerDay();

                Rental updatedRental = rental.withReturnDate(now)
                        .withCost(cost)
                        .withStatus(Status.RETURNED);

                rentals.set(i, updatedRental);

                System.out.println("Vehicle returned successfully. Total cost: $" + cost + " for " + days + " day(s).");
                return;
            }
        }
        System.out.println("No active rental found for this vehicle.");
    }
}
