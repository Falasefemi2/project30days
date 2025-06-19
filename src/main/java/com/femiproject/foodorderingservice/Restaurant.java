package com.femiproject.foodorderingservice;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Restaurant {
    @JsonProperty("restaurantId")
    private String restaurantId;

    @JsonProperty("name")
    private String name;

    @JsonProperty("address")
    private String address;

    @JsonProperty("menuItems")
    private List<MenuItem> menuItems;

    @JsonCreator
    public Restaurant(
            @JsonProperty("restaurantId") String restaurantId,
            @JsonProperty("name") String name,
            @JsonProperty("address") String address,
            @JsonProperty("menuItems") List<MenuItem> menuItems) {
        this.restaurantId = restaurantId;
        this.name = name;
        this.address = address;
        this.menuItems = menuItems != null ? menuItems : new ArrayList<>();
    }

    public Restaurant(String name, String address, List<MenuItem> menuItems) {
        this.restaurantId = generateRestaurantId();
        this.name = name;
        this.address = address;
        this.menuItems = menuItems;
    }

    public String getRestuarantId() {
        return restaurantId;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public List<MenuItem> getMenuItems() {
        return menuItems;
    }

    @Override
    public String toString() {
        return "Restaurant [restaurantId=" + restaurantId + ", name=" + name + ", address=" + address + ", menuItems="
                + menuItems + "]";
    }

    private String generateRestaurantId() {
        return UUID.randomUUID().toString();
    }

    public void addMenuItem(MenuItem item) {
        this.menuItems.add(item);
    }

    public void removeMenuItem(MenuItem item) {
        this.menuItems.remove(item);
    }
}
