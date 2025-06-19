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

public class RestaurantManager {
    private static final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .enable(SerializationFeature.INDENT_OUTPUT);
    private static final String DATA_FILE = "restaurant_data.json";
    private static Scanner scanner = new Scanner(System.in);
    private static List<Restaurant> restaurants = new ArrayList<>();
    private static final Object lock = new Object();

    static {
        loadRestaurants();
        if (restaurants.isEmpty()) {
            initializeSampleRestaurants();
        }
    }

    private static void loadRestaurants() {
        try {
            synchronized (lock) {
                File file = new File(DATA_FILE);
                if (file.exists() && file.length() > 0) {
                    List<Restaurant> loadedRestaurants = objectMapper.readValue(file,
                            new TypeReference<List<Restaurant>>() {
                            });
                    restaurants = loadedRestaurants;
                    System.out.println("Restaurants loaded successfully from " + DATA_FILE);
                } else {
                    System.out.println("No existing restaurant data found. Starting with sample data.");
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading restaurants from " + DATA_FILE + ": " + e.getMessage());
            restaurants = new ArrayList<>();
        }
    }

    private static void saveRestaurants() {
        try {
            synchronized (lock) {
                objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(DATA_FILE), restaurants);
                System.out.println("Restaurants saved successfully to " + DATA_FILE);
            }
        } catch (IOException e) {
            System.err.println("Error saving restaurants: " + e.getMessage());
        }
    }

    private static void initializeSampleRestaurants() {
        List<MenuItem> pizzaMenu = new ArrayList<>();
        pizzaMenu.add(new MenuItem("Margherita Pizza", 12.99, "Classic tomato sauce with mozzarella"));
        pizzaMenu.add(new MenuItem("Pepperoni Pizza", 14.99, "Spicy pepperoni with cheese"));
        pizzaMenu.add(new MenuItem("Hawaiian Pizza", 13.99, "Ham and pineapple"));

        List<MenuItem> burgerMenu = new ArrayList<>();
        burgerMenu.add(new MenuItem("Classic Burger", 8.99, "Beef patty with lettuce and tomato"));
        burgerMenu.add(new MenuItem("Cheese Burger", 9.99, "Beef patty with cheese"));
        burgerMenu.add(new MenuItem("Chicken Burger", 7.99, "Grilled chicken breast"));

        List<MenuItem> sushiMenu = new ArrayList<>();
        sushiMenu.add(new MenuItem("California Roll", 6.99, "Crab, avocado, cucumber"));
        sushiMenu.add(new MenuItem("Salmon Nigiri", 4.99, "Fresh salmon over rice"));
        sushiMenu.add(new MenuItem("Dragon Roll", 12.99, "Eel, avocado, cucumber"));

        restaurants.add(new Restaurant("Pizza Palace", "123 Main St", pizzaMenu));
        restaurants.add(new Restaurant("Burger House", "456 Oak Ave", burgerMenu));
        restaurants.add(new Restaurant("Sushi Express", "789 Pine Rd", sushiMenu));

        saveRestaurants();
    }

    public static List<Restaurant> getAllRestaurants() {
        return new ArrayList<>(restaurants);
    }

    public static Restaurant getRestaurantById(String restaurantId) {
        return restaurants.stream()
                .filter(r -> r.getRestuarantId().equals(restaurantId))
                .findFirst()
                .orElse(null);
    }

    public static Restaurant getRestaurantByIndex(int index) {
        if (index >= 0 && index < restaurants.size()) {
            return restaurants.get(index);
        }
        return null;
    }

    public static void displayRestaurants() {
        if (restaurants.isEmpty()) {
            System.out.println("No restaurants available.");
            return;
        }

        System.out.println("\n=== Available Restaurants ===");
        for (int i = 0; i < restaurants.size(); i++) {
            Restaurant restaurant = restaurants.get(i);
            System.out.println((i + 1) + ". " + restaurant.getName() + " - " + restaurant.getAddress());
        }
        System.out.println();
    }

    public static void displayRestaurantMenu(Restaurant restaurant) {
        if (restaurant == null) {
            System.out.println("Restaurant not found.");
            return;
        }

        System.out.println("\n=== " + restaurant.getName() + " Menu ===");
        List<MenuItem> menuItems = restaurant.getMenuItems();

        if (menuItems.isEmpty()) {
            System.out.println("No menu items available.");
            return;
        }

        for (int i = 0; i < menuItems.size(); i++) {
            MenuItem item = menuItems.get(i);
            System.out.printf("%d. %s - $%.2f\n", (i + 1), item.getName(), item.getPrice());
            System.out.println("   " + item.getDescription());
        }
        System.out.println();
    }
}
