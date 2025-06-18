package com.femiproject.hotelmanagement;

public enum FoodType {

    BURGER(50),
    FRIES(60),
    PIZZA(70),
    DRINK(30);

    private final int itemPrice;

    FoodType(int itemPrice) {
        this.itemPrice = itemPrice;
    }

    public int getItemPrice() {
        return itemPrice;
    }
}