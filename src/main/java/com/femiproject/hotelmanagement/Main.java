package com.femiproject.hotelmanagement;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Hotel hotel = new Hotel();

        while (true) {
            showMenu();

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> handleCustomerDetails(hotel, scanner);
                case 2 -> handleBookRoom(hotel, scanner);
                case 3 -> handleAvailability(hotel, scanner);
                case 4 -> handleOrder(hotel, scanner);
                case 5 -> handleBill(hotel, scanner);
                case 6 -> handleDeallocate(hotel, scanner);
                case 7 -> handleFeatures(hotel, scanner);
                case 8 -> hotel.showAllRooms();
                case 0 -> {
                    System.out.println("Thank you for using the Hotel Management System.");
                    scanner.close();
                    return;
                }
                default -> System.out.println("Invalid choice. Try again.");
            }
        }
    }

    private static void showMenu() {
        System.out.println("\n===== Hotel Management System =====");
        System.out.println("1. Add Customer Details to Existing Room");
        System.out.println("2. Book New Room");
        System.out.println("3. Check Room Availability");
        System.out.println("4. Order Food");
        System.out.println("5. Generate Bill");
        System.out.println("6. Deallocate Room");
        System.out.println("7. Show Room Features");
        System.out.println("8. Show All Rooms");
        System.out.println("0. Exit");
        System.out.print("Enter your choice: ");
    }

    private static RoomType getRoomType(Scanner scanner) {
        System.out.println("Enter Room Type (1: SINGLE, 2: DOUBLE): ");
        int typeChoice = scanner.nextInt();
        scanner.nextLine();
        return typeChoice == 1 ? RoomType.SINGLE : RoomType.DOUBLE;
    }

    private static RoomCategory getRoomCategory(Scanner scanner) {
        System.out.println("Enter Room Category (1: LUXURY, 2: DELUXE): ");
        int categoryChoice = scanner.nextInt();
        scanner.nextLine();
        return categoryChoice == 1 ? RoomCategory.LUXURY : RoomCategory.DELUXEE;
    }

    private static int getRoomNumber(Scanner scanner) {
        System.out.print("Enter Room Number: ");
        return scanner.nextInt();
    }

    private static Guest inputGuestDetails(Scanner scanner, int guestNumber) {
        System.out.println("\nEnter details for Guest " + guestNumber + ":");
        System.out.print("Name: ");
        String name = scanner.nextLine();
        System.out.print("Contact: ");
        String contact = scanner.nextLine();
        System.out.print("Gender: ");
        String gender = scanner.nextLine();
        return new Guest(name, contact, gender);
    }

    private static void handleCustomerDetails(Hotel hotel, Scanner scanner) {
        RoomType type = getRoomType(scanner);
        RoomCategory category = getRoomCategory(scanner);
        int roomNumber = getRoomNumber(scanner);
        scanner.nextLine();

        hotel.customerDetails(type, category, roomNumber);
    }

    private static void handleBookRoom(Hotel hotel, Scanner scanner) {
        RoomType type = getRoomType(scanner);
        RoomCategory category = getRoomCategory(scanner);

        List<Guest> guests = new ArrayList<>();

        Guest guest1 = inputGuestDetails(scanner, 1);
        guests.add(guest1);

        if (type == RoomType.DOUBLE) {
            System.out.print("\nWould you like to add a second guest? (y/n): ");
            String response = scanner.nextLine().trim().toLowerCase();
            if (response.equals("y") || response.equals("yes")) {
                Guest guest2 = inputGuestDetails(scanner, 2);
                guests.add(guest2);
            }
        }

        hotel.bookRoom(type, category, guests);
    }

    private static void handleAvailability(Hotel hotel, Scanner scanner) {
        RoomType type = getRoomType(scanner);
        RoomCategory category = getRoomCategory(scanner);
        hotel.availability(type, category);
    }

    private static void handleOrder(Hotel hotel, Scanner scanner) {
        RoomType type = getRoomType(scanner);
        RoomCategory category = getRoomCategory(scanner);
        int roomNumber = getRoomNumber(scanner);
        scanner.nextLine();

        System.out.println("\nAvailable Food Items:");
        for (int i = 0; i < FoodType.values().length; i++) {
            FoodType ft = FoodType.values()[i];
            System.out.println((i + 1) + ". " + ft + " - ₦" + ft.getItemPrice());
        }

        System.out.print("Enter food choice (1-" + FoodType.values().length + "): ");
        int choice = Integer.parseInt(scanner.nextLine());

        if (choice < 1 || choice > FoodType.values().length) {
            System.out.println("Invalid food choice.");
            return;
        }

        FoodType selectedFood = FoodType.values()[choice - 1];

        System.out.print("Enter quantity: ");
        int quantity = Integer.parseInt(scanner.nextLine());

        if (quantity <= 0) {
            System.out.println("Invalid quantity.");
            return;
        }

        hotel.order(type, category, roomNumber, selectedFood, quantity);
    }

    private static void handleBill(Hotel hotel, Scanner scanner) {
        RoomType type = getRoomType(scanner);
        RoomCategory category = getRoomCategory(scanner);
        int roomNumber = getRoomNumber(scanner);
        scanner.nextLine();

        hotel.bill(type, category, roomNumber);
    }

    private static void handleDeallocate(Hotel hotel, Scanner scanner) {
        RoomType type = getRoomType(scanner);
        RoomCategory category = getRoomCategory(scanner);
        int roomNumber = getRoomNumber(scanner);
        scanner.nextLine();

        hotel.deallocate(type, category, roomNumber);
    }

    private static void handleFeatures(Hotel hotel, Scanner scanner) {
        RoomType type = getRoomType(scanner);
        RoomCategory category = getRoomCategory(scanner);

        hotel.features(type, category);
    }
}