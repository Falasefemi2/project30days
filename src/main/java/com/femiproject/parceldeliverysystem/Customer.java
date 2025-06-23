package com.femiproject.parceldeliverysystem;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Customer {
    @JsonProperty
    private String customerId;
    @JsonProperty
    private String name;
    @JsonProperty
    private String phone;
    @JsonProperty
    private List<String> parcels;

    @JsonCreator
    public Customer(
            @JsonProperty String customerId,
            @JsonProperty String name,
            @JsonProperty String phone,
            @JsonProperty List<String> parcels) {
        this.customerId = customerId;
        this.name = name;
        this.phone = phone;
        this.parcels = parcels != null ? parcels : new ArrayList<>();
    }

    public Customer(String name, String phone) {
        this.customerId = UUID.randomUUID().toString();
        this.name = name;
        this.phone = phone;
        this.parcels = new ArrayList<>();
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public List<String> getParcels() {
        return parcels;
    }

    @Override
    public String toString() {
        return "Customer [customerId=" + customerId + ", name=" + name + ", phone=" + phone + ", parcels=" + parcels
                + "]";
    }
}
