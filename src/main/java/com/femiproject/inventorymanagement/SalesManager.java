package com.femiproject.inventorymanagement;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class SalesManager {
    private List<Sales> salesHistory;
    private final String sales_file = "sales.json";
    private final ObjectMapper objectMapper;

    public SalesManager() {
        this.salesHistory = new ArrayList<>();
        this.objectMapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .enable(SerializationFeature.INDENT_OUTPUT);
        loadSales();
    }

    private void loadSales() {
        try {
            File file = new File(sales_file);
            if (file.exists() && file.length() > 0) {
                List<Sales> loadedSales = objectMapper.readValue(file, new TypeReference<List<Sales>>() {
                });
                salesHistory = loadedSales;
                System.out.println("Sales loaded successfully from " + sales_file);
            }
        } catch (IOException e) {
            System.err.println("Error loading sales: " + e.getMessage());
            salesHistory = new ArrayList<>();
        }
    }

    private void saveSales() {
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(sales_file), salesHistory);
            System.out.println("Sales saved successfully to " + sales_file);
        } catch (IOException e) {
            System.err.println("Error saving Sales: " + e.getMessage());
        }
    }

    public void simulateSales(Product product, ProductManager productManager, Scanner scanner) {
        if (product == null) {
            System.out.println("Product not found.");
            return;
        }
        if (product.isProductAvailable()) {
            System.out.println("Enter quantity to be sold: ");
            int quantity = scanner.nextInt();
            scanner.nextLine();

            if (quantity > 0 && quantity <= product.getQuantity()) {
                int newQuantity = product.getQuantity() - quantity;
                product.setQuantity(newQuantity);
                productManager.saveProducts();
                Sales sale = new Sales();
                sale.getProducts().add(product);
                sale.setQuantitySold(quantity);
                double saleAmount = quantity * product.getPricePerUnit();
                sale.setTotalCost(saleAmount);
                salesHistory.add(sale);
                saveSales();
                System.out.println("Sale successful! " + quantity + " units of " + product.getName() + " sold.");
            } else {
                System.out.println("Invalid quantity. Sale aborted.");
            }
        } else {
            System.out.println("Product is not available for sale.");
        }
    }

    public List<Sales> salesHistory() {
        return new ArrayList<>(salesHistory);
    }

    public double totalRevenue() {
        return salesHistory.stream().mapToDouble(Sales::getTotalCost).sum();
    }
}
