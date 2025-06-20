package com.femiproject.ecommerce;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class ProductManager {

    private List<Product> allProducts;
    private final String PRODUCTS_FILE = "products.json";
    private final ObjectMapper objectMapper;

    public ProductManager() {
        this.allProducts = new ArrayList<>();
        this.objectMapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .enable(SerializationFeature.INDENT_OUTPUT);
        loadProducts();
    }

    private void loadProducts() {
        try {
            File file = new File(PRODUCTS_FILE);
            if (file.exists() && file.length() > 0) {
                List<Product> loadedProducts = objectMapper.readValue(file, new TypeReference<List<Product>>() {
                });
                allProducts = loadedProducts;
                System.out.println("Products loaded successfully");
            }
        } catch (IOException e) {
            System.err.println("Error loading products: " + e.getMessage());
        }
    }

    private void saveProducts() {
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(PRODUCTS_FILE), allProducts);
        } catch (IOException e) {
            System.err.println("Error saving products: " + e.getMessage());
        }
    }

    public void addProduct(Product product) {
        allProducts.add(product);
        saveProducts();
    }

    public void removeProduct(String productId) {
        allProducts.removeIf(p -> p.getProductId().equals(productId));
        saveProducts();
    }

    public List<Product> getAllProducts() {
        return new ArrayList<>(allProducts);
    }

    public List<Product> getProductsBySupplier(String supplierId) {
        return allProducts.stream()
                .filter(p -> p.getSupplierId().equals(supplierId))
                .toList();
    }

    public Product getProductById(String productId) {
        return allProducts.stream()
                .filter(p -> p.getProductId().equals(productId))
                .findFirst()
                .orElse(null);
    }

    public List<Product> searchProducts(String keyword) {
        return allProducts.stream()
                .filter(p -> p.getProductName().toLowerCase().contains(keyword.toLowerCase()) ||
                        p.getCategory().toLowerCase().contains(keyword.toLowerCase()))
                .toList();
    }

}
