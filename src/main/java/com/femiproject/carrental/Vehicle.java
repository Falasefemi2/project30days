package com.femiproject.carrental;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Vehicle {
    @JsonProperty
    private String vehicleId;
    @JsonProperty
    private String name;
    @JsonProperty
    private int quantity;
    @JsonProperty
    private String color;
    @JsonProperty
    private double pricePerDay;

    @JsonCreator
    public Vehicle(
            @JsonProperty String vehicleId,
            @JsonProperty String name,
            @JsonProperty int quantity,
            @JsonProperty String color,
            @JsonProperty double pricePerDay) {
        this.vehicleId = vehicleId;
        this.name = name;
        this.quantity = quantity;
        this.color = color;
        this.pricePerDay = pricePerDay;
    }

    public Vehicle(String name, int quantity, String color, double pricePerDay) {
        this.vehicleId = UUID.randomUUID().toString();
        this.name = name;
        this.quantity = quantity;
        this.color = color;
        this.pricePerDay = pricePerDay;
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getColor() {
        return color;
    }

    public double getPricePerDay() {
        return pricePerDay;
    }

    public void setPricePerDay(double pricePerDay) {
        this.pricePerDay = pricePerDay;
    }

    @Override
    public String toString() {
        return "Vehicle [vehicleId=" + vehicleId + ", name=" + name + ", quantity=" + quantity + ", color=" + color
                + "]";
    }

    public boolean isAvailable() {
        return quantity > 0;
    }

    public void rentOut() {
        if (quantity <= 0)
            throw new IllegalStateException("Vehicle not available for rental");
        quantity--;
    }

    public void returnVehicle() {
        quantity++;
    }
}
