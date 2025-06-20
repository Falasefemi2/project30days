package com.femiproject.ecommerce;

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

    @JsonProperty("items")
    private List<CartItem> items;
    @JsonProperty("totalAmount")

    private double totalAmount;
    @JsonProperty("orderDate")

    private java.time.LocalDateTime orderDate;

    @JsonProperty("status")
    private OrderStatus status;

    @JsonCreator
    public Order(
            @JsonProperty("orderId") String orderId,
            @JsonProperty("userId") String userId,
            @JsonProperty("items") List<CartItem> items,
            @JsonProperty("totalAmount") double totalAmount,
            @JsonProperty("orderDate") java.time.LocalDateTime orderDate,
            @JsonProperty("status") OrderStatus status) {
        this.orderId = orderId;
        this.userId = userId;
        this.items = items;
        this.totalAmount = totalAmount;
        this.orderDate = orderDate;
        this.status = status;
    }

    public Order(String userId, List<CartItem> items) {
        this.orderId = UUID.randomUUID().toString();
        this.userId = userId;
        this.items = new ArrayList<>(items);
        this.totalAmount = items.stream()
                .mapToDouble(CartItem::getTotalPrice)
                .sum();
        this.orderDate = java.time.LocalDateTime.now();
        this.status = OrderStatus.PENDING;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getUserId() {
        return userId;
    }

    public List<CartItem> getItems() {
        return items;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public java.time.LocalDateTime getOrderDate() {
        return orderDate;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderId='" + orderId + '\'' +
                ", totalAmount=" + totalAmount +
                ", orderDate=" + orderDate +
                ", status=" + status +
                ", itemCount=" + items.size() +
                '}';
    }
}
