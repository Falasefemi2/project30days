package com.femiproject.hotelmanagement;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Food {
    private final FoodType type;
    private final double price;

    @JsonCreator
    public Food(
            @JsonProperty("type") FoodType type,
            @JsonProperty("price") double price) {
        this.type = type;
        this.price = price;
    }

    public FoodType getType() {
        return type;
    }

    public double getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return String.format("%s - ₦%.2f", type, price);
    }
}
