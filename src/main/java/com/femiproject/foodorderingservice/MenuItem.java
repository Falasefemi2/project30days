package com.femiproject.foodorderingservice;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class MenuItem {
    @JsonProperty("menuId")
    private String menuId;
    @JsonProperty("name")
    private String name;
    @JsonProperty("price")
    private double price;
    @JsonProperty("description")
    private String description;

    @JsonCreator
    public MenuItem(
            @JsonProperty("menuId") String menuId,
            @JsonProperty("name") String name,
            @JsonProperty("price") double price,
            @JsonProperty("description") String description) {
        this.menuId = menuId;
        this.name = name;
        this.price = price;
        this.description = description;
    }

    // Constructor for creating new menu items
    public MenuItem(String name, double price, String description) {
        this.menuId = generateMenuId();
        this.name = name;
        this.price = price;
        this.description = description;
    }

    public String getMenuId() {
        return menuId;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    private String generateMenuId() {
        return UUID.randomUUID().toString();
    }

    @Override
    public String toString() {
        return "MenuItem [menuId=" + menuId + ", name=" + name + ", price=" + price
                + ", description=" + description + "]";
    }
}