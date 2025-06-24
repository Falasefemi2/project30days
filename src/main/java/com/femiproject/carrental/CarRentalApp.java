package com.femiproject.carrental;

import java.util.Scanner;

public class CarRentalApp {

    public static void main(String[] args) {
        Admin admin = new Admin();
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("\n====== CAR RENTAL SYSTEM ======");
            System.out.println("1. Create User");
            System.out.println("2. Create Vehicle");
            System.out.println("3. Rent Vehicle");
            System.out.println("4. Return Vehicle");
            System.out.println("5. View All Rentals");
            System.out.println("6. View All Rentals From Users");
            System.out.println("7. View All Users");
            System.out.println("8. View All Vehicles");
            System.out.println("0. Exit");
            System.out.print("Enter choice: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1" -> admin.createUser();
                case "2" -> admin.createVehicle();
                case "3" -> {
                    System.out.print("Enter user ID: ");
                    String rentUserId = scanner.nextLine();
                    System.out.print("Enter vehicle ID: ");
                    String rentVehicleId = scanner.nextLine();
                    admin.rentVehicleForUser(rentUserId, rentVehicleId);
                }
                case "4" -> {
                    System.out.print("Enter user ID: ");
                    String returnUserId = scanner.nextLine();
                    System.out.print("Enter vehicle ID: ");
                    String returnVehicleId = scanner.nextLine();
                    admin.returnVehicleForUser(returnUserId, returnVehicleId);
                }
                case "5" -> admin.viewAllRentals();
                case "6" -> admin.viewAllRentalsFromUsers();
                case "7" -> admin.viewAllUsers();
                case "8" -> admin.viewAllVehicles();
                case "0" -> {
                    running = false;
                    System.out.println("Exiting... Goodbye!");
                }
                default -> System.out.println("Invalid choice. Try again.");
            }
        }
        scanner.close();
    }
}