package com.femiproject.carrental;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Vehicle(
        @JsonProperty("vehicleId") String vehicleId,
        @JsonProperty("name") String name,
        @JsonProperty("quantity") int quantity,
        @JsonProperty("color") String color,
        @JsonProperty("pricePerDay") double pricePerDay) {

    public Vehicle(String name, int quantity, String color, double pricePerDay) {
        this(UUID.randomUUID().toString(), name, quantity, color, pricePerDay);
    }

    public boolean isAvailable() {
        return quantity > 0;
    }

    public Vehicle rentOut() {
        if (quantity <= 0) {
            throw new IllegalStateException("Vehicle not available for rental");
        }
        return new Vehicle(vehicleId, name, quantity - 1, color, pricePerDay);
    }

    public Vehicle returnVehicle() {
        return new Vehicle(vehicleId, name, quantity + 1, color, pricePerDay);
    }
}
