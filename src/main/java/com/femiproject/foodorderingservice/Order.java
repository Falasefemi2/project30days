package com.femiproject.foodorderingservice;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Order {

    @JsonProperty("orderId")
    private String orderId;

    @JsonProperty("userId")
    private String userId;

    @JsonProperty("restaurantId")
    private String restaurantId;

    @JsonProperty("menuItems")
    private List<MenuItem> menuItems;

    @JsonProperty("totalPrice")
    private double totalPrice;

    @JsonProperty("status")
    private Status status;

    @JsonProperty("orderTime")
    private LocalDateTime orderTime;

    @JsonCreator
    public Order(
            @JsonProperty("orderId") String orderId,
            @JsonProperty("userId") String userId,
            @JsonProperty("restaurantId") String restaurantId,
            @JsonProperty("menuItems") List<MenuItem> menuItems,
            @JsonProperty("totalPrice") double totalPrice,
            @JsonProperty("status") Status status,
            @JsonProperty("orderTime") LocalDateTime orderTime) {
        this.orderId = orderId;
        this.userId = userId;
        this.restaurantId = restaurantId;
        this.menuItems = menuItems != null ? menuItems : new ArrayList<>();
        this.totalPrice = totalPrice;
        this.status = status;
        this.orderTime = orderTime;
    }

    public Order(String orderId, String userId, String restaurantId, Status status) {
        this.orderId = generateOrderId();
        this.orderId = orderId;
        this.userId = userId;
        this.restaurantId = restaurantId;
        this.status = status;
        this.menuItems = new ArrayList<>();
        this.orderTime = LocalDateTime.now();
        this.totalPrice = 0.0;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getUserId() {
        return userId;
    }

    public String getRestaurantId() {
        return restaurantId;
    }

    public List<MenuItem> getMenuItems() {
        return menuItems;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Status getStatus() {
        return status;
    }

    public LocalDateTime getOrderTime() {
        return orderTime;
    }

    @Override
    public String toString() {
        return "Order [orderId=" + orderId + ", userId=" + userId + ", restaurantId=" + restaurantId + ", menuItems="
                + menuItems + ", totalPrice=" + totalPrice + ", status=" + status + ", orderTime=" + orderTime + "]";
    }

    private String generateOrderId() {
        return UUID.randomUUID().toString();
    }

    public void addMenuItem(MenuItem item) {
        this.menuItems.add(item);
        calculateTotalPrice();
    }

    public void removeMenuItem(MenuItem item) {
        this.menuItems.remove(item);
        calculateTotalPrice();
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    private void calculateTotalPrice() {
        this.totalPrice = menuItems.stream()
                .mapToDouble(MenuItem::getPrice)
                .sum();
    }
}
