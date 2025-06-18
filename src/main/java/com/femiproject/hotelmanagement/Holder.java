package com.femiproject.hotelmanagement;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;

public class Holder {
    @JsonProperty("singleLuxuryRooms")
    private List<Room> singleLuxuryRooms;

    @JsonProperty("singleDeluxeRooms")
    private List<Room> singleDeluxeRooms;

    @JsonProperty("doubleLuxuryRooms")
    private List<Room> doubleLuxuryRooms;

    @JsonProperty("doubleDeluxeRooms")
    private List<Room> doubleDeluxeRooms;

    @JsonCreator
    public Holder(
            @JsonProperty("singleLuxuryRooms") List<Room> singleLuxuryRooms,
            @JsonProperty("singleDeluxeRooms") List<Room> singleDeluxeRooms,
            @JsonProperty("doubleLuxuryRooms") List<Room> doubleLuxuryRooms,
            @JsonProperty("doubleDeluxeRooms") List<Room> doubleDeluxeRooms) {
        this.singleLuxuryRooms = singleLuxuryRooms != null ? singleLuxuryRooms : new ArrayList<>();
        this.singleDeluxeRooms = singleDeluxeRooms != null ? singleDeluxeRooms : new ArrayList<>();
        this.doubleLuxuryRooms = doubleLuxuryRooms != null ? doubleLuxuryRooms : new ArrayList<>();
        this.doubleDeluxeRooms = doubleDeluxeRooms != null ? doubleDeluxeRooms : new ArrayList<>();
    }

    public Holder() {
        this.singleLuxuryRooms = new ArrayList<>();
        this.singleDeluxeRooms = new ArrayList<>();
        this.doubleLuxuryRooms = new ArrayList<>();
        this.doubleDeluxeRooms = new ArrayList<>();
    }

    public List<Room> getRooms(RoomType type, RoomCategory category) {
        return switch (type) {
            case SINGLE -> switch (category) {
                case LUXURY -> singleLuxuryRooms;
                case DELUXEE -> singleDeluxeRooms;
            };
            case DOUBLE -> switch (category) {
                case LUXURY -> doubleLuxuryRooms;
                case DELUXEE -> doubleDeluxeRooms;
            };
        };
    }

    public List<Room> getSingleLuxuryRooms() {
        return singleLuxuryRooms;
    }

    public List<Room> getSingleDeluxeRooms() {
        return singleDeluxeRooms;
    }

    public List<Room> getDoubleLuxuryRooms() {
        return doubleLuxuryRooms;
    }

    public List<Room> getDoubleDeluxeRooms() {
        return doubleDeluxeRooms;
    }

    public void setSingleLuxuryRooms(List<Room> singleLuxuryRooms) {
        this.singleLuxuryRooms = singleLuxuryRooms != null ? singleLuxuryRooms : new ArrayList<>();
    }

    public void setSingleDeluxeRooms(List<Room> singleDeluxeRooms) {
        this.singleDeluxeRooms = singleDeluxeRooms != null ? singleDeluxeRooms : new ArrayList<>();
    }

    public void setDoubleLuxuryRooms(List<Room> doubleLuxuryRooms) {
        this.doubleLuxuryRooms = doubleLuxuryRooms != null ? doubleLuxuryRooms : new ArrayList<>();
    }

    public void setDoubleDeluxeRooms(List<Room> doubleDeluxeRooms) {
        this.doubleDeluxeRooms = doubleDeluxeRooms != null ? doubleDeluxeRooms : new ArrayList<>();
    }
}