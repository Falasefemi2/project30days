package com.femiproject.carrental;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Rental {
    @JsonProperty
    private LocalDateTime startDate;
    @JsonProperty
    private LocalDateTime returnDate;
    @JsonProperty
    private Vehicle vehicle;
    @JsonProperty
    private Status status;
    @JsonProperty
    private double rentalCost;

    @JsonCreator
    public Rental(
            @JsonProperty LocalDateTime startDate,
            @JsonProperty LocalDateTime returnDate,
            @JsonProperty Vehicle vehicle,
            @JsonProperty Status status,
            @JsonProperty double rentalCost) {
        this.startDate = startDate;
        this.returnDate = returnDate;
        this.vehicle = vehicle;
        this.status = status;
        this.rentalCost = rentalCost;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDateTime returnDate) {
        this.returnDate = returnDate;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public double getRentalCost() {
        return rentalCost;
    }

    public void setRentalCost(double rentalCost) {
        this.rentalCost = rentalCost;
    }

    @Override
    public String toString() {
        return "Rental [startDate=" + startDate + ", returnDate=" + returnDate + ", vehicle=" + vehicle + ", status="
                + status + ", rentalCost=" + rentalCost + "]";
    }

    public boolean isActive() {
        return status == Status.ACTIVE;
    }

    public boolean isReturned() {
        return status == Status.RETURNED;
    }

}
