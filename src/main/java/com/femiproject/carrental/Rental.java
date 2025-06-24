package com.femiproject.carrental;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Rental(
        @JsonProperty("startDate") LocalDateTime startDate,
        @JsonProperty("returnDate") LocalDateTime returnDate,
        @JsonProperty("vehicle") Vehicle vehicle,
        @JsonProperty("status") Status status,
        @JsonProperty("rentCost") double rentalCost) {

    public boolean isActive() {
        return status == Status.ACTIVE;
    }

    public boolean isReturned() {
        return status == Status.RETURNED;
    }

    public Rental markReturned() {
        return new Rental(startDate, returnDate, vehicle, Status.RETURNED, rentalCost);
    }

    public Rental updateCost(double newCost) {
        return new Rental(startDate, returnDate, vehicle, status, newCost);
    }

    public Rental withStatus(Status newStatus) {
        return new Rental(startDate, returnDate, vehicle, newStatus, rentalCost);
    }

    public Rental withCost(double newCost) {
        return new Rental(startDate, returnDate, vehicle, status, newCost);
    }

    public Rental withReturnDate(LocalDateTime newReturnDate) {
        return new Rental(startDate, newReturnDate, vehicle, status, rentalCost);
    }

}
