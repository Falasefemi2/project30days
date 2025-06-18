package com.femiproject.hotelmanagement;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;

public class Guest {
    @JsonProperty("name")
    private final String name;

    @JsonProperty("contact")
    private final String contact;

    @JsonProperty("gender")
    private final String gender;

    @JsonProperty("foods")
    private final List<Food> foods;

    @JsonCreator
    public Guest(
            @JsonProperty("name") String name,
            @JsonProperty("contact") String contact,
            @JsonProperty("gender") String gender,
            @JsonProperty("foods") List<Food> foods) {
        this.name = name;
        this.contact = contact;
        this.gender = gender;
        this.foods = foods != null ? foods : new ArrayList<>();
    }

    public Guest(String name, String contact, String gender) {
        this.name = name;
        this.contact = contact;
        this.gender = gender;
        this.foods = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public String getContact() {
        return contact;
    }

    public String getGender() {
        return gender;
    }

    public List<Food> getFoods() {
        return foods;
    }

    public void addFood(Food food) {
        foods.add(food);
    }

    @Override
    public String toString() {
        return String.format("%s (%s) - %s", name, gender, contact);
    }
}