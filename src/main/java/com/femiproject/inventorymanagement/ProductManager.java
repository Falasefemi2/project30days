package com.femiproject.inventorymanagement;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class ProductManager {

    private List<Product> products;
    private final String product_file = "new_products.json";
    private final ObjectMapper objectMapper;

    public ProductManager() {
        this.products = new ArrayList<>();
        this.objectMapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .enable(SerializationFeature.INDENT_OUTPUT);
        loadProducts();
    }

    private void loadProducts() {
        try {
            File file = new File(product_file);
            if (file.exists() && file.length() > 0) {
                List<Product> loadedProduct = objectMapper.readValue(file, new TypeReference<List<Product>>() {
                });
                products = loadedProduct;
                System.out.println("Product loaded successfully from " + product_file);
            }
        } catch (IOException e) {
            System.err.println("Error loading product: " + e.getMessage());
            products = new ArrayList<>();
        }
    }

    public void saveProducts() {
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(product_file), products);
            System.out.println("Product saved successfully to " + product_file);
        } catch (IOException e) {
            System.err.println("Error saving Products: " + e.getMessage());
        }
    }

    public void addProduct(Product product) {
        if (product != null) {
            products.add(product);
            saveProducts();
            System.out.println("Product " + product.getName() + " added successfully");
        }
    }

    public void updateProduct(Product product, Integer quantity, Double price) {
        if (quantity != null) {
            product.setQuantity(quantity);
            System.out.println("Product quantity updated: " + product.getQuantity());
        }
        if (price != null) {
            product.setPricePerUnit(price);
            System.out.println("Product price updated: " + product.getPricePerUnit());
        }
        saveProducts();
    }

    public Product findProductById(String productId) {
        return products.stream()
                .filter(p -> p.getProductId().equals(productId))
                .findFirst()
                .orElse(null);
    }

    public boolean deleteProduct(String productId) {
        boolean removed = products.removeIf(p -> p.getProductId().equals(productId));
        if (removed) {
            saveProducts();
            System.out.println("Product with ID " + productId + " deleted successfully.");
        } else {
            System.out.println("Product with ID " + productId + " not found.");
        }
        return removed;
    }

    public List<Product> listAllProduct() {
        return new ArrayList<>(products);
    }

    public List<Product> searchProductsByCategory(String category) {
        return products.stream()
                .filter(p -> p.getCategory().equalsIgnoreCase(category))
                .toList();
    }
}
