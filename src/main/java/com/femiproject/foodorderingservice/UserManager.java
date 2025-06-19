package com.femiproject.foodorderingservice;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class UserManager {
    private static final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .enable(SerializationFeature.INDENT_OUTPUT);
    private static final String DATA_FILE = "user_data.json";
    private static Scanner scanner = new Scanner(System.in);
    private static List<User> users = new ArrayList<>();
    private static final Object lock = new Object();
    private static User currentUser = null;

    static {
        loadUsers();
    }

    private static void loadUsers() {
        try {
            synchronized (lock) {
                File file = new File(DATA_FILE);
                if (file.exists() && file.length() > 0) {
                    List<User> loadedUsers = objectMapper.readValue(file, new TypeReference<List<User>>() {
                    });
                    users = loadedUsers;
                    System.out.println("Users loaded successfully from " + DATA_FILE);
                } else {
                    System.out.println("No existing user data found. Starting with empty list.");
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading users from " + DATA_FILE + ": " + e.getMessage());
            users = new ArrayList<>();
        }
    }

    private static void saveUsers() {
        try {
            synchronized (lock) {
                objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(DATA_FILE), users);
                System.out.println("Users saved successfully to " + DATA_FILE);
            }
        } catch (IOException e) {
            System.err.println("Error saving users: " + e.getMessage());
        }
    }

    private static User getUserInput() {
        System.out.println("Enter your name: ");
        String name = scanner.nextLine();

        System.out.println("Enter your email: ");
        String email = scanner.nextLine();

        System.out.println("Enter your phone number: ");
        String phoneNumber = scanner.nextLine();

        System.out.println("Enter your address: ");
        String address = scanner.nextLine();

        System.out.println("Choose payment method:");
        System.out.println("1. Credit Card");
        System.out.println("2. Cash");
        System.out.print("Enter choice (1-2): ");

        int choice = scanner.nextInt();
        scanner.nextLine();

        PaymentMethod paymentMethod = (choice == 1) ? PaymentMethod.CREDIT_CARD : PaymentMethod.CASH;

        return new User(name, email, phoneNumber, address, paymentMethod);
    }

    private static boolean isEmailExists(String email) {
        return users.stream().anyMatch(user -> user.getEmail().equalsIgnoreCase(email));
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public static void createUser() {
        User newUser = getUserInput();

        if (isEmailExists(newUser.getEmail())) {
            System.out.println("User with email " + newUser.getEmail() + " already exists!");
            return;
        }

        users.add(newUser);
        saveUsers();
        System.out.println("User " + newUser.getName() + " created successfully");
    }

    public static User getUserByEmail(String email) {
        return users.stream()
                .filter(e -> e.getEmail().equals(email))
                .findFirst()
                .orElse(null);
    }

    public static boolean loginUser(String email) {
        if (email == null || email.trim().isEmpty()) {
            System.out.println("Email cannot be empty");
            return false;
        }

        User user = getUserByEmail(email);

        if (user != null) {
            currentUser = user;
            System.out.println("Login successful! Welcome " + user.getName());
            return true;
        } else {
            System.out.println("User with email " + email + " not found.");
            return false;
        }
    }

    public static void logout() {
        currentUser = null;
    }

    public static void displayRestaurants() {
        RestaurantManager.displayRestaurants();
    }

    public static void placeOrder() {
        if (currentUser == null) {
            System.out.println("Please login first.");
            return;
        }

        // Display available restaurants
        RestaurantManager.displayRestaurants();
        List<Restaurant> restaurants = RestaurantManager.getAllRestaurants();

        if (restaurants.isEmpty()) {
            System.out.println("No restaurants available for ordering.");
            return;
        }

        // Get restaurant selection
        System.out.print("Enter restaurant number: ");
        int restaurantChoice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        if (restaurantChoice < 1 || restaurantChoice > restaurants.size()) {
            System.out.println("Invalid restaurant selection.");
            return;
        }

        Restaurant selectedRestaurant = restaurants.get(restaurantChoice - 1);

        // Display restaurant menu
        RestaurantManager.displayRestaurantMenu(selectedRestaurant);
        List<MenuItem> menuItems = selectedRestaurant.getMenuItems();

        if (menuItems.isEmpty()) {
            System.out.println("No menu items available.");
            return;
        }

        // Collect order items
        List<MenuItem> orderItems = new ArrayList<>();
        boolean continueOrdering = true;

        while (continueOrdering) {
            System.out.print("Enter menu item number (or 0 to finish): ");
            int itemChoice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            if (itemChoice == 0) {
                continueOrdering = false;
            } else if (itemChoice >= 1 && itemChoice <= menuItems.size()) {
                MenuItem selectedItem = menuItems.get(itemChoice - 1);
                orderItems.add(selectedItem);
                System.out.println("Added: " + selectedItem.getName() + " - $" + selectedItem.getPrice());
            } else {
                System.out.println("Invalid menu item selection.");
            }
        }

        if (orderItems.isEmpty()) {
            System.out.println("No items selected. Order cancelled.");
            return;
        }

        // Create and save order
        Order order = OrderManager.createOrder(currentUser.getUserId(), selectedRestaurant.getRestuarantId(),
                orderItems);

        // Update user's order history
        currentUser.addOrderToHistory(order.getOrderId());
        saveUsers();

        // Display order summary
        System.out.println("\n=== Order Summary ===");
        System.out.println("Order ID: " + order.getOrderId());
        System.out.println("Restaurant: " + selectedRestaurant.getName());
        System.out.println("Items:");
        for (MenuItem item : orderItems) {
            System.out.println("  - " + item.getName() + " - $" + item.getPrice());
        }
        System.out.println("Total: $" + order.getTotalPrice());
        System.out.println("Status: " + order.getStatus());
        System.out.println("Payment Method: " + currentUser.getPaymentMethod());
        System.out.println("Order placed successfully!");
    }
}