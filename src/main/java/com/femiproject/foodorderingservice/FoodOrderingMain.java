package com.femiproject.foodorderingservice;

import java.util.Scanner;

public class FoodOrderingMain {
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("=== Food Ordering Service ===");

        while (true) {
            User currentUser = UserManager.getCurrentUser();

            if (currentUser != null) {
                // User is logged in
                System.out.println("Welcome, " + currentUser.getName() + "!");
                displayLoggedInMenu();
            } else {
                // User is not logged in
                displayGuestMenu();
            }

            int choice = getIntInput("Enter your choice: ");

            if (currentUser != null) {
                handleLoggedInChoice(choice);
            } else {
                handleGuestChoice(choice);
            }

            System.out.println("\n" + "=".repeat(40) + "\n");
        }
    }

    private static void displayGuestMenu() {
        System.out.println("1. Create Account");
        System.out.println("2. Login");
        System.out.println("3. View Restaurants (Guest View)");
        System.out.println("4. Exit");
    }

    private static void displayLoggedInMenu() {
        System.out.println("1. View Restaurants");
        System.out.println("2. Place Order");
        System.out.println("3. Logout");
        System.out.println("4. Exit");
    }

    private static void handleGuestChoice(int choice) {
        switch (choice) {
            case 1:
                UserManager.createUser();
                break;
            case 2:
                System.out.print("Enter your email: ");
                String email = scanner.nextLine();
                UserManager.loginUser(email);
                break;
            case 3:
                UserManager.displayRestaurants();
                break;
            case 4:
                System.out.println("Thank you for using Food Ordering Service!");
                scanner.close();
                System.exit(0);
            default:
                System.out.println("Invalid choice. Please try again.");
        }
    }

    private static void handleLoggedInChoice(int choice) {
        switch (choice) {
            case 1:
                UserManager.displayRestaurants();
                break;
            case 2:
                UserManager.placeOrder();
                break;
            case 3:
                UserManager.logout();
                System.out.println("Logged out successfully.");
                break;
            case 4:
                System.out.println("Thank you for using Food Ordering Service!");
                scanner.close();
                System.exit(0);
            default:
                System.out.println("Invalid choice. Please try again.");
        }
    }

    private static int getIntInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }
}