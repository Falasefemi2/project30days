package com.femiproject.foodorderingservice;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Delivery {
    @JsonProperty("deliveryId")
    private String deliveryId;
    @JsonProperty("orderId")
    private String orderId;
    @JsonProperty("driverId")
    private String driverId;
    @JsonProperty("status")
    private DeliveryStatus status;

    @JsonCreator
    public Delivery(
            @JsonProperty("deliveryId") String deliveryId,
            @JsonProperty("orderId") String orderId,
            @JsonProperty("driverId") String driverId,
            @JsonProperty("status") DeliveryStatus status) {
        this.deliveryId = deliveryId;
        this.orderId = orderId;
        this.driverId = driverId;
        this.status = status;
    }

    // Constructor for creating new deliveries
    public Delivery(String orderId, String driverId) {
        this.deliveryId = generateDeliveryId();
        this.orderId = orderId;
        this.driverId = driverId;
        this.status = DeliveryStatus.ASSIGNED; // Default status when delivery is created
    }

    public String getDeliveryId() {
        return deliveryId;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getDriverId() {
        return driverId;
    }

    public DeliveryStatus getStatus() {
        return status;
    }

    public void setStatus(DeliveryStatus status) {
        this.status = status;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    private String generateDeliveryId() {
        return UUID.randomUUID().toString();
    }

    @Override
    public String toString() {
        return "Delivery [deliveryId=" + deliveryId + ", orderId=" + orderId + ", driverId=" + driverId
                + ", status=" + status + "]";
    }
}