package com.femiproject.hotelmanagement;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

public class Hotel {
    private static final ObjectMapper objectMapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
    private static final String DATA_FILE = "hotel_data.json";
    private static Holder holder = new Holder();
    private static Scanner scanner = new Scanner(System.in);
    private static final AtomicInteger roomNumberGenerator = new AtomicInteger(1001);

    static {
        loadData();
    }

    private static void loadData() {
        try {
            File file = new File(DATA_FILE);
            if (file.exists()) {
                holder = objectMapper.readValue(file, Holder.class);
                System.out.println("Data loaded successfully from " + DATA_FILE);
            } else {
                System.out.println("No existing data file found. Starting fresh.");
            }
        } catch (IOException e) {
            System.out.println("Error loading data: " + e.getMessage());
        }
    }

    private static void saveData() {
        try {
            objectMapper.writeValue(new File(DATA_FILE), holder);
            System.out.println("Data saved successfully to " + DATA_FILE);
        } catch (IOException e) {
            System.out.println("Error saving data: " + e.getMessage());
        }
    }

    private int generateRoomNumber() {
        return roomNumberGenerator.getAndIncrement();
    }

    public Room findRoomByNumber(RoomType type, RoomCategory category, int roomNumber) {
        return holder.getRooms(type, category).stream()
                .filter(r -> r.getRoomNumber() == roomNumber)
                .findFirst()
                .orElse(null);
    }

    private Guest inputGuestDetails() {
        System.out.println("\nEnter customer name: ");
        String name = scanner.nextLine();

        System.out.println("Enter customer contact: ");
        String contact = scanner.nextLine();

        System.out.println("Enter customer gender: ");
        String gender = scanner.nextLine();

        return new Guest(name, contact, gender);
    }

    public void customerDetails(RoomType roomType, RoomCategory roomCategory, int roomNumber) {
        Room room = findRoomByNumber(roomType, roomCategory, roomNumber);

        if (room == null) {
            System.out.println("Room not found.");
            return;
        }

        if (!room.isAvailable()) {
            System.out.println("Room is already full.");
            return;
        }

        Guest firstGuest = inputGuestDetails();
        room.addGuest(firstGuest);
        System.out.println("First guest added to " + roomType + " " + roomCategory + " Room #" + roomNumber);

        if (roomType == RoomType.DOUBLE && room.isAvailable()) {
            System.out.print("Would you like to add a second guest? (y/n): ");
            String response = scanner.nextLine().trim().toLowerCase();
            if (response.equals("y") || response.equals("yes")) {
                Guest secondGuest = inputGuestDetails();
                room.addGuest(secondGuest);
                System.out.println("Second guest added to Room #" + roomNumber);
            }
        }

        saveData();
    }

    private int getRoomCapacity(RoomType roomType) {
        return roomType == RoomType.SINGLE ? 1 : 2;
    }

    public void bookRoom(RoomType roomType, RoomCategory roomCategory, List<Guest> guests) {
        List<Room> rooms = holder.getRooms(roomType, roomCategory);
        int newRoomNumber = generateRoomNumber();

        Room room = new Room(newRoomNumber, roomType, roomCategory);

        int maxCapacity = getRoomCapacity(roomType);
        if (guests.size() > maxCapacity) {
            System.out.println("Error: Cannot add " + guests.size() + " guests to a " + roomType
                    + " room (max capacity: " + maxCapacity + ")");
            return;
        }

        for (Guest guest : guests) {
            room.addGuest(guest);
        }

        rooms.add(room);
        saveData();
        System.out.println("Room " + newRoomNumber + " successfully booked for " + guests.size() + " guest(s).");
        System.out.println("Room details: " + room);
    }

    public void features(RoomType roomType, RoomCategory roomCategory) {
        switch (roomType) {
            case SINGLE -> {
                switch (roomCategory) {
                    case LUXURY -> System.out.println("""
                            Type: SINGLE
                            Category: LUXURY
                            Beds: 1
                            AC: Yes
                            Free breakfast: Yes
                            Price: ₦3000
                            """);
                    case DELUXEE -> System.out.println("""
                            Type: SINGLE
                            Category: DELUXE
                            Beds: 1
                            AC: No
                            Free breakfast: No
                            Price: ₦2000
                            """);
                }
            }

            case DOUBLE -> {
                switch (roomCategory) {
                    case LUXURY -> System.out.println("""
                            Type: DOUBLE
                            Category: LUXURY
                            Beds: 2
                            AC: Yes
                            Free breakfast: Yes
                            Price: ₦4000
                            """);
                    case DELUXEE -> System.out.println("""
                            Type: DOUBLE
                            Category: DELUXE
                            Beds: 2
                            AC: No
                            Free breakfast: No
                            Price: ₦3000
                            """);
                }
            }
        }
    }

    public void availability(RoomType roomType, RoomCategory roomCategory) {
        List<Room> rooms = holder.getRooms(roomType, roomCategory);
        boolean found = false;

        System.out.println("\n=== Available " + roomType + " " + roomCategory + " Rooms ===");
        for (Room room : rooms) {
            if (room.isAvailable()) {
                int availableSpots = (roomType == RoomType.SINGLE ? 1 : 2) - room.getGuests().size();
                System.out.println("Room #" + room.getRoomNumber() + " - Available spots: " + availableSpots);
                found = true;
            }
        }

        if (!found) {
            System.out.println("No available rooms in " + roomType + " " + roomCategory);
        }
    }

    private int getRoomPrice(RoomType roomType, RoomCategory roomCategory) {
        return switch (roomType) {
            case SINGLE -> switch (roomCategory) {
                case LUXURY -> 3000;
                case DELUXEE -> 2000;
            };
            case DOUBLE -> switch (roomCategory) {
                case LUXURY -> 4000;
                case DELUXEE -> 3000;
            };
        };
    }

    public void bill(RoomType roomType, RoomCategory roomCategory, int roomNumber) {
        Room room = findRoomByNumber(roomType, roomCategory, roomNumber);

        if (room == null) {
            System.out.println("Room not found.");
            return;
        }

        List<Guest> guests = room.getGuests();

        if (guests.isEmpty()) {
            System.out.println("Room is empty. No bill to generate.");
            return;
        }

        double totalAmount = 0;
        int roomPrice = getRoomPrice(roomType, roomCategory);

        System.out.println("\n***************");
        System.out.println("     BILL");
        System.out.println("***************");
        System.out.println("Room #" + roomNumber + " (" + roomType + " " + roomCategory + ")");
        System.out.println();

        for (int i = 0; i < guests.size(); i++) {
            Guest guest = guests.get(i);
            System.out.println("Guest " + (i + 1) + ": " + guest.getName());

            double foodTotal = 0;
            for (Food food : guest.getFoods()) {
                System.out.println("  " + food);
                foodTotal += food.getPrice();
            }

            double guestTotal = roomPrice + foodTotal;
            System.out.println("Room charge: ₦" + roomPrice);
            System.out.println("Food charge: ₦" + foodTotal);
            System.out.println("Guest subtotal: ₦" + guestTotal);
            System.out.println("-----------------------");

            totalAmount += guestTotal;
        }

        System.out.println("TOTAL AMOUNT: ₦" + totalAmount);
        System.out.println("***************");
    }

    public void deallocate(RoomType roomType, RoomCategory roomCategory, int roomNumber) {
        List<Room> rooms = holder.getRooms(roomType, roomCategory);
        Room room = findRoomByNumber(roomType, roomCategory, roomNumber);

        if (room == null) {
            System.out.println("Room not found.");
            return;
        }

        if (room.getGuests().isEmpty()) {
            System.out.println("Room is already empty.");
            return;
        }

        room.clearAllGuests();
        rooms.remove(room);
        saveData();
        System.out.println("Room " + roomNumber + " deallocated successfully.");
    }

    public void order(RoomType roomType, RoomCategory roomCategory, int roomNumber, FoodType foodType, int quantity) {
        Room room = findRoomByNumber(roomType, roomCategory, roomNumber);

        if (room == null) {
            System.out.println("Room not found.");
            return;
        }

        if (room.getGuests().isEmpty()) {
            System.out.println("No guests in this room.");
            return;
        }

        Food food = new Food(foodType, foodType.getItemPrice() * quantity);

        // For Single room
        if (roomType == RoomType.SINGLE) {
            room.getGuests().get(0).addFood(food);
            System.out.println("Food order placed for guest: " + room.getGuests().get(0).getName());
        } else {
            // For Double room
            if (room.getGuests().size() == 1) {
                room.getGuests().get(0).addFood(food);
                System.out.println("Food order placed for guest: " + room.getGuests().get(0).getName());
            } else {
                System.out.println("Double room has multiple guests. Choose which guest to order for:");
                for (int i = 0; i < room.getGuests().size(); i++) {
                    System.out.println((i + 1) + ". " + room.getGuests().get(i).getName());
                }
                System.out.print("Enter choice (1-" + room.getGuests().size() + "): ");
                int choice = scanner.nextInt();
                scanner.nextLine();

                if (choice >= 1 && choice <= room.getGuests().size()) {
                    int index = choice - 1;
                    room.getGuests().get(index).addFood(food);
                    System.out.println("Food order placed for guest: " + room.getGuests().get(index).getName());
                } else {
                    System.out.println("Invalid guest selection.");
                    return;
                }
            }
        }

        saveData();
    }

    public void showAllRooms() {
        System.out.println("\n=== ALL ROOMS ===");
        boolean hasRooms = false;

        for (RoomType type : RoomType.values()) {
            for (RoomCategory category : RoomCategory.values()) {
                List<Room> rooms = holder.getRooms(type, category);
                if (!rooms.isEmpty()) {
                    System.out.println("\n" + type + " " + category + " Rooms:");
                    for (Room room : rooms) {
                        System.out.println("  Room #" + room.getRoomNumber() +
                                " - Guests: " + room.getGuests().size() +
                                "/" + (type == RoomType.SINGLE ? 1 : 2) +
                                (room.isAvailable() ? " (Available)" : " (Full)"));
                    }
                    hasRooms = true;
                }
            }
        }

        if (!hasRooms) {
            System.out.println("No rooms have been booked yet.");
        }
    }
}