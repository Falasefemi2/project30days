package com.femiproject.hotelmanagement;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;

public class Room {
    @JsonProperty("roomNumber")
    private final int roomNumber;

    @JsonProperty("roomType")
    private final RoomType roomType;

    @JsonProperty("roomCategory")
    private final RoomCategory roomCategory;

    @JsonProperty("guests")
    private final List<Guest> guests;

    @JsonProperty("foods")
    private final List<Food> foods;

    @JsonCreator
    public Room(
            @JsonProperty("roomNumber") int roomNumber,
            @JsonProperty("roomType") RoomType roomType,
            @JsonProperty("roomCategory") RoomCategory roomCategory,
            @JsonProperty("guests") List<Guest> guests,
            @JsonProperty("foods") List<Food> foods) {
        this.roomNumber = roomNumber;
        this.roomType = roomType;
        this.roomCategory = roomCategory;
        this.guests = guests != null ? guests : new ArrayList<>();
        this.foods = foods != null ? foods : new ArrayList<>();
    }

    public Room(int roomNumber, RoomType roomType, RoomCategory roomCategory) {
        this.roomNumber = roomNumber;
        this.roomType = roomType;
        this.roomCategory = roomCategory;
        this.guests = new ArrayList<>();
        this.foods = new ArrayList<>();
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public RoomType getRoomType() {
        return roomType;
    }

    public RoomCategory getRoomCategory() {
        return roomCategory;
    }

    public List<Guest> getGuests() {
        return guests;
    }

    public List<Food> getFoods() {
        return foods;
    }

    public boolean isAvailable() {
        return guests.size() < (roomType == RoomType.SINGLE ? 1 : 2);
    }

    public void addGuest(Guest guest) {
        if (isAvailable()) {
            guests.add(guest);
        }
    }

    public void clearAllGuests() {
        guests.clear();
        foods.clear();
    }

    @Override
    public String toString() {
        return String.format("Room %d (%s %s) - Guests: %d/%d",
                roomNumber, roomType, roomCategory,
                guests.size(), (roomType == RoomType.SINGLE ? 1 : 2));
    }
}