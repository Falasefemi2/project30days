package com.femiproject.parceldeliverysystem;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Driver {
    @JsonProperty
    private String driverId;
    @JsonProperty
    private String name;
    @JsonProperty
    private String phone;
    @JsonProperty
    private List<String> assignedParcelIds;

    @JsonCreator
    public Driver(
            @JsonProperty String driverId,
            @JsonProperty String name,
            @JsonProperty String phone,
            @JsonProperty List<String> assignedParcelIds) {
        this.driverId = driverId;
        this.name = name;
        this.phone = phone;
        this.assignedParcelIds = assignedParcelIds;
    }

    public Driver(String name, String phone) {
        this.driverId = UUID.randomUUID().toString();
        this.name = name;
        this.phone = phone;
        this.assignedParcelIds = new ArrayList<>();
    }

    public String getDriverId() {
        return driverId;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public List<String> getAssignedParcelIds() {
        return assignedParcelIds;
    }

    @Override
    public String toString() {
        return "Driver [driverId=" + driverId + ", name=" + name + ", phone=" + phone + ", assignedParcelIds="
                + assignedParcelIds + "]";
    }
}
