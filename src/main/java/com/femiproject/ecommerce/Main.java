package com.femiproject.ecommerce;

import java.util.Scanner;

public class Main {
    private static Scanner scanner = new Scanner(System.in);
    private static Admin admin = new Admin();

    public static void main(String[] args) {
        System.out.println("===========================================");
        System.out.println("    Welcome to Femi's E-commerce System   ");
        System.out.println("===========================================");

        while (true) {
            showMainMenu();
            int choice = getUserChoice();

            switch (choice) {
                case 1 -> registerNewUser();
                case 2 -> loginUser();
                case 3 -> viewSystemStats();
                case 4 -> {
                    exitApplication();
                    return;
                }
                default -> System.out.println("Invalid choice! Please try again.");
            }
        }
    }

    private static void showMainMenu() {
        System.out.println("\n=== MAIN MENU ===");
        System.out.println("1. Register New User");
        System.out.println("2. Login");
        System.out.println("3. View System Statistics");
        System.out.println("4. Exit");
        System.out.print("Enter your choice (1-4): ");
    }

    private static int getUserChoice() {
        try {
            int choice = scanner.nextInt();
            scanner.nextLine();
            return choice;
        } catch (Exception e) {
            scanner.nextLine();
            return -1;
        }
    }

    private static void registerNewUser() {
        System.out.println("\n=== USER REGISTRATION ===");
        admin.createUser();

        // Ask if they want to login immediately
        System.out.print("\nWould you like to login now? (y/n): ");
        String response = scanner.nextLine();
        if (response.toLowerCase().startsWith("y")) {
            loginUser();
        }
    }

    private static void loginUser() {
        System.out.println("\n=== USER LOGIN ===");

        System.out.print("Enter your email: ");
        String email = scanner.nextLine();

        System.out.print("Enter your password: ");
        String password = scanner.nextLine();

        boolean loginSuccess = admin.loginUser(email, password);

        if (!loginSuccess) {
            System.out.println("\nLogin failed. Please try again or register a new account.");

            System.out.println("\nWhat would you like to do?");
            System.out.println("1. Try login again");
            System.out.println("2. Register new account");
            System.out.println("3. Return to main menu");
            System.out.print("Enter choice: ");

            int choice = getUserChoice();
            switch (choice) {
                case 1:
                    loginUser();
                    break;
                case 2:
                    registerNewUser();
                    break;
                case 3:
                default:
                    break;
            }
        }
    }

    private static void viewSystemStats() {
        System.out.println("\n=== SYSTEM STATISTICS ===");

        System.out.println("📊 System Overview:");
        System.out.println("• E-commerce platform for buyers and suppliers");
        System.out.println("• Suppliers can upload and manage products");
        System.out.println("• Buyers can browse, shop, and place orders");
        System.out.println("• Secure user authentication system");
        System.out.println("• Persistent data storage using JSON files");

        System.out.println("\n🔧 System Features:");
        System.out.println("• User registration and login");
        System.out.println("• Role-based access (Buyer/Supplier)");
        System.out.println("• Product catalog management");
        System.out.println("• Shopping cart functionality");
        System.out.println("• Order processing and history");
        System.out.println("• Stock management");

        System.out.println("\n💾 Data Storage:");
        System.out.println("• Users: admin_file.json");
        System.out.println("• Products: products.json");

        pauseForUser();
    }

    private static void exitApplication() {
        System.out.println("\n===========================================");
        System.out.println("   Thank you for using our E-commerce!    ");
        System.out.println("            See you again soon!           ");
        System.out.println("===========================================");

        scanner.close();
    }

    private static void pauseForUser() {
        System.out.print("\nPress Enter to continue...");
        scanner.nextLine();
    }
}