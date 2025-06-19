package com.femiproject.foodorderingservice;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class OrderManager {
    private static final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .enable(SerializationFeature.INDENT_OUTPUT);
    private static final String DATA_FILE = "order_data.json";
    private static List<Order> orders = new ArrayList<>();
    private static final Object lock = new Object();

    static {
        loadOrders();
    }

    private static void loadOrders() {
        try {
            synchronized (lock) {
                File file = new File(DATA_FILE);
                if (file.exists() && file.length() > 0) {
                    List<Order> loadedOrders = objectMapper.readValue(file, new TypeReference<List<Order>>() {
                    });
                    orders = loadedOrders;
                    System.out.println("Orders loaded successfully from " + DATA_FILE);
                } else {
                    System.out.println("No existing order data found. Starting with empty list.");
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading orders from " + DATA_FILE + ": " + e.getMessage());
            orders = new ArrayList<>();
        }
    }

    private static void saveOrders() {
        try {
            synchronized (lock) {
                objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(DATA_FILE), orders);
                System.out.println("Orders saved successfully to " + DATA_FILE);
            }
        } catch (IOException e) {
            System.err.println("Error saving orders: " + e.getMessage());
        }
    }

    public static Order createOrder(String userId, String restaurantId, List<MenuItem> menuItems) {
        String orderId = UUID.randomUUID().toString();
        Order order = new Order(orderId, userId, restaurantId, Status.PENDING);

        for (MenuItem item : menuItems) {
            order.addMenuItem(item);
        }

        orders.add(order);
        saveOrders();
        return order;
    }

    public static List<Order> getOrdersByUserId(String userId) {
        return orders.stream()
                .filter(order -> order.getUserId().equals(userId))
                .toList();
    }

    public static Order getOrderById(String orderId) {
        return orders.stream()
                .filter(order -> order.getOrderId().equals(orderId))
                .findFirst()
                .orElse(null);
    }

    public static void updateOrderStatus(String orderId, Status status) {
        Order order = getOrderById(orderId);
        if (order != null) {
            order.setStatus(status);
            saveOrders();
        }
    }

    public static List<Order> getAllOrders() {
        return new ArrayList<>(orders);
    }
}