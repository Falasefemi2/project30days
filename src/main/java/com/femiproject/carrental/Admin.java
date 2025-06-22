package com.femiproject.carrental;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.File;
import java.io.IOException;

public class Admin {

    private List<User> users;
    private List<Vehicle> vehicles;
    private List<Rental> rentals;
    private Scanner scanner;
    private static final String USERS_FILE = "users.json";
    private static final String VEHICLES_FILE = "vehicles.json";
    private static final String RENTALS_FILE = "rentals.json";
    private final ObjectMapper objectMapper;

    public Admin() {
        this.users = new ArrayList<>();
        this.vehicles = new ArrayList<>();
        this.rentals = new ArrayList<>();
        this.scanner = new Scanner(System.in);
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        this.objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        loadData();
    }

    public void createUser() {
        System.out.println("Enter name: ");
        String name = scanner.nextLine();
        System.out.println("Enter phone: ");
        String phone = scanner.nextLine();

        User user = new User(name, phone);
        users.add(user);
        saveData();
        System.out.println("User created successfully " + user.getName());
    }

    public User getUserById(String userId) {
        return users.stream().filter(u -> u.getUserId().equals(userId)).findFirst().orElse(null);
    }

    public void createVehicle() {
        System.out.println("Enter name: ");
        String name = scanner.nextLine();
        System.out.println("Enter quantities: ");
        int quantity = scanner.nextInt();
        scanner.nextLine();
        System.out.println("Enter color: ");
        String color = scanner.nextLine();
        System.out.println("Enter price per day:");
        double price = scanner.nextDouble();
        scanner.nextLine();

        Vehicle vehicle = new Vehicle(name, quantity, color, price);
        vehicles.add(vehicle);
        saveData();
        System.out.println("Vehicle added to store: " + vehicle.getName());
    }

    public Vehicle getVehicleById(String vId) {
        return vehicles.stream().filter(v -> v.getVehicleId().equals(vId)).findFirst().orElse(null);
    }

    public void viewAllRentals() {
        rentals.forEach(System.out::println);
    }

    public void viewAllRentalsFromUsers() {
        for (User user : users) {
            for (Rental rental : user.getRentals()) {
                System.out.println("User: " + user.getName() + " - " + rental);
            }
        }
    }

    public void rentVehicleForUser(String userId, String vId) {
        User user = getUserById(userId);
        Vehicle vehicle = getVehicleById(vId);

        if (user == null) {
            System.out.println("User not found.");
            return;
        }
        if (vehicle == null) {
            System.out.println("Vehicle not found.");
            return;
        }

        if (vehicle.isAvailable()) {
            user.rentVehicle(vehicle);
            rentals.addAll(user.getRentals());
            saveData();
        }
    }

    public void returnVehicleForUser(String userId, String vId) {
        User user = getUserById(userId);
        Vehicle vehicle = getVehicleById(vId);
        if (user == null) {
            System.out.println("User not found.");
            return;
        }
        if (vehicle == null) {
            System.out.println("Vehicle not found.");
            return;
        }

        user.returnVehicle(vehicle);
        rentals.addAll(user.getRentals());
        saveData();
    }

    public void saveData() {
        try {
            objectMapper.writeValue(new File(USERS_FILE), users);
            objectMapper.writeValue(new File(VEHICLES_FILE), vehicles);
            objectMapper.writeValue(new File(RENTALS_FILE), rentals);
            System.out.println("Data saved to JSON files.");
        } catch (IOException e) {
            System.out.println("Error saving data: " + e.getMessage());
        }
    }

    public void loadData() {
        try {
            File usersFile = new File(USERS_FILE);
            File vehiclesFile = new File(VEHICLES_FILE);
            File rentalsFile = new File(RENTALS_FILE);
            if (usersFile.exists()) {
                users = objectMapper.readValue(usersFile, new TypeReference<List<User>>() {
                });
            }
            if (vehiclesFile.exists()) {
                vehicles = objectMapper.readValue(vehiclesFile, new TypeReference<List<Vehicle>>() {
                });
            }
            if (rentalsFile.exists()) {
                rentals = objectMapper.readValue(rentalsFile, new TypeReference<List<Rental>>() {
                });
            }
            System.out.println("Data loaded from JSON files.");
        } catch (IOException e) {
            System.out.println("Error loading data: " + e.getMessage());
        }
    }

}
