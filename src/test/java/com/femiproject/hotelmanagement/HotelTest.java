package com.femiproject.hotelmanagement;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class HotelTest {
    private Hotel hotel;
    private List<Guest> guests;

    @BeforeEach
    void setUp() {
        hotel = new Hotel();
        guests = new ArrayList<>();
        guests.add(new Guest("John Doe", "1234567890", "Male"));
    }

    @Test
    void testBookRoom() {
        // Test booking a single room
        hotel.bookRoom(RoomType.SINGLE, RoomCategory.LUXURY, guests);

        // Test booking a double room with one guest
        hotel.bookRoom(RoomType.DOUBLE, RoomCategory.LUXURY, guests);

        // Test booking a double room with two guests
        List<Guest> doubleGuests = new ArrayList<>(guests);
        doubleGuests.add(new Guest("Jane Doe", "0987654321", "Female"));
        hotel.bookRoom(RoomType.DOUBLE, RoomCategory.DELUXEE, doubleGuests);

        // Verify rooms were created
        assertNotNull(hotel.findRoomByNumber(RoomType.SINGLE, RoomCategory.LUXURY, 1001));
        assertNotNull(hotel.findRoomByNumber(RoomType.DOUBLE, RoomCategory.LUXURY, 1002));
        assertNotNull(hotel.findRoomByNumber(RoomType.DOUBLE, RoomCategory.DELUXEE, 1003));
    }

    @Test
    void testFindRoomByNumber() {
        // Book a room first
        hotel.bookRoom(RoomType.SINGLE, RoomCategory.LUXURY, guests);

        // Test finding existing room
        Room room = hotel.findRoomByNumber(RoomType.SINGLE, RoomCategory.LUXURY, 1001);
        assertNotNull(room);
        assertEquals(1001, room.getRoomNumber());

        // Test finding non-existent room
        Room nonExistentRoom = hotel.findRoomByNumber(RoomType.SINGLE, RoomCategory.LUXURY, 9999);
        assertNull(nonExistentRoom);
    }

    @Test
    void testAvailability() {
        // Book a room first
        hotel.bookRoom(RoomType.SINGLE, RoomCategory.LUXURY, guests);

        // Test availability for booked room type/category
        hotel.availability(RoomType.SINGLE, RoomCategory.LUXURY);

        // Test availability for empty room type/category
        hotel.availability(RoomType.DOUBLE, RoomCategory.DELUXEE);
    }

    @Test
    void testDeallocate() {
        // Book a room first
        hotel.bookRoom(RoomType.SINGLE, RoomCategory.LUXURY, guests);

        // Test deallocating existing room
        hotel.deallocate(RoomType.SINGLE, RoomCategory.LUXURY, 1001);

        // Verify room is removed
        assertNull(hotel.findRoomByNumber(RoomType.SINGLE, RoomCategory.LUXURY, 1001));

        // Test deallocating non-existent room
        hotel.deallocate(RoomType.SINGLE, RoomCategory.LUXURY, 9999);
    }

    @Test
    void testOrder() {
        // Book a room first
        hotel.bookRoom(RoomType.SINGLE, RoomCategory.LUXURY, guests);

        // Test ordering food for single room
        hotel.order(RoomType.SINGLE, RoomCategory.LUXURY, 1001, FoodType.BURGER, 1);

        // Book a double room with two guests
        List<Guest> doubleGuests = new ArrayList<>(guests);
        doubleGuests.add(new Guest("Jane Doe", "0987654321", "Female"));
        hotel.bookRoom(RoomType.DOUBLE, RoomCategory.LUXURY, doubleGuests);

        // Test ordering food for double room
        hotel.order(RoomType.DOUBLE, RoomCategory.LUXURY, 1002, FoodType.PIZZA, 2);
    }

    @Test
    void testBill() {
        // Book a room and add food orders
        hotel.bookRoom(RoomType.SINGLE, RoomCategory.LUXURY, guests);
        hotel.order(RoomType.SINGLE, RoomCategory.LUXURY, 1001, FoodType.BURGER, 1);

        // Test generating bill
        hotel.bill(RoomType.SINGLE, RoomCategory.LUXURY, 1001);

        // Test generating bill for non-existent room
        hotel.bill(RoomType.SINGLE, RoomCategory.LUXURY, 9999);
    }

    @Test
    void testFeatures() {
        // Test features for all room types and categories
        hotel.features(RoomType.SINGLE, RoomCategory.LUXURY);
        hotel.features(RoomType.SINGLE, RoomCategory.DELUXEE);
        hotel.features(RoomType.DOUBLE, RoomCategory.LUXURY);
        hotel.features(RoomType.DOUBLE, RoomCategory.DELUXEE);
    }

    @Test
    void testShowAllRooms() {
        // Book rooms of different types
        hotel.bookRoom(RoomType.SINGLE, RoomCategory.LUXURY, guests);
        hotel.bookRoom(RoomType.DOUBLE, RoomCategory.DELUXEE, guests);

        // Test showing all rooms
        hotel.showAllRooms();
    }

    @Test
    void testCustomerDetails() {
        // Book a room first
        hotel.bookRoom(RoomType.SINGLE, RoomCategory.LUXURY, guests);

        // Test adding customer details to existing room
        hotel.customerDetails(RoomType.SINGLE, RoomCategory.LUXURY, 1001);

        // Test adding customer details to non-existent room
        hotel.customerDetails(RoomType.SINGLE, RoomCategory.LUXURY, 9999);
    }
}
