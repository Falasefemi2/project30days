package com.femiproject.foodorderingservice;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class User {

    @JsonProperty("userId")
    private String userId;

    @JsonProperty("name")
    private String name;

    @JsonProperty("email")
    private String email;

    @JsonProperty("phoneNumber")
    private String phoneNumber;

    @JsonProperty("address")
    private String address;

    @JsonProperty("orderHistory")
    private List<String> orderHistory;

    @JsonProperty("paymentMethod")
    private PaymentMethod paymentMethod;

    @JsonCreator
    public User(
            @JsonProperty("userId") String userId,
            @JsonProperty("name") String name,
            @JsonProperty("email") String email,
            @JsonProperty("phoneNumber") String phoneNumber,
            @JsonProperty("address") String address,
            @JsonProperty("orderHistory") List<String> orderHistory,
            @JsonProperty("paymentMethod") PaymentMethod paymentMethod) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.orderHistory = orderHistory != null ? orderHistory : new ArrayList<>();
        this.paymentMethod = paymentMethod;
    }

    public User(String name, String email, String phoneNumber, String address, PaymentMethod paymentMethod) {
        this.userId = generateUserId();
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.orderHistory = new ArrayList<>();
        this.paymentMethod = paymentMethod;
    }

    public String getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public List<String> getOrderHistory() {
        return orderHistory;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    @Override
    public String toString() {
        return "User [userId=" + userId + ", name=" + name + ", email=" + email + ", phoneNumber=" + phoneNumber
                + ", address=" + address + ", orderHistory=" + orderHistory + ", paymentMethod=" + paymentMethod + "]";
    }

    private String generateUserId() {
        return UUID.randomUUID().toString();
    }

    public void addOrderToHistory(String orderId) {
        this.orderHistory.add(orderId);
    }
}
