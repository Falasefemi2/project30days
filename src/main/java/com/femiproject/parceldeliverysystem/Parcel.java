package com.femiproject.parceldeliverysystem;

import java.time.LocalDateTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Parcel {
    @JsonProperty
    private String parcelId;
    @JsonProperty
    private double weight;
    @JsonProperty
    private String origin;
    @JsonProperty
    private String destination;
    @JsonProperty
    private Status status;
    @JsonProperty
    private double shippingcost;
    @JsonProperty
    private LocalDateTime deliveryDate;
    @JsonProperty
    private String customerId;
    @JsonProperty
    private String driverId;

    @JsonCreator
    public Parcel(
            @JsonProperty String parcelId,
            @JsonProperty double weight,
            @JsonProperty String origin,
            @JsonProperty String destination,
            @JsonProperty Status status,
            @JsonProperty double shippingcost,
            @JsonProperty LocalDateTime deliveryDate,
            @JsonProperty String customerId,
            @JsonProperty String driverId) {
        this.parcelId = parcelId;
        this.weight = weight;
        this.origin = origin;
        this.destination = destination;
        this.status = status;
        this.shippingcost = shippingcost;
        this.deliveryDate = deliveryDate;
        this.customerId = customerId;
        this.driverId = driverId;
    }

    public Parcel(double weight, String origin, String destination, double shippingCost, String customerId) {
        this.parcelId = UUID.randomUUID().toString();
        this.weight = weight;
        this.origin = origin;
        this.destination = destination;
        this.status = Status.IN_TRANSIT;
        this.shippingcost = shippingCost;
        this.deliveryDate = null;
        this.customerId = customerId;
        this.driverId = null;
    }

    public String getParcelId() {
        return parcelId;
    }

    public double getWeight() {
        return weight;
    }

    public String getOrigin() {
        return origin;
    }

    public String getDestination() {
        return destination;
    }

    public Status getStatus() {
        return status;
    }

    public double getShippingcost() {
        return shippingcost;
    }

    public LocalDateTime getDeliveryDate() {
        return deliveryDate;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getDriverId() {
        return driverId;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setDeliveryDate(LocalDateTime deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    @Override
    public String toString() {
        return "Parcel [parcelId=" + parcelId + ", weight=" + weight + ", origin=" + origin + ", destination="
                + destination + ", status=" + status + ", shippingcost=" + shippingcost + ", deliveryDate="
                + deliveryDate + ", customerId=" + customerId + ", driverId=" + driverId + "]";
    }
}
