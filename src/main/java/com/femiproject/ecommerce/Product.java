package com.femiproject.ecommerce;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Product {

    @JsonProperty("productId")
    private String productId;

    @JsonProperty("productName")
    private String productName;

    @JsonProperty("price")
    private double price;

    @JsonProperty("supplierId")
    private String supplierId;

    @JsonProperty("description")
    private String description;

    @JsonProperty("category")
    private String category;

    @JsonProperty("stockQuantity")
    private int stockQuantity;

    @JsonCreator
    public Product(
            @JsonProperty("productId") String productId,
            @JsonProperty("productName") String productName,
            @JsonProperty("price") double price,
            @JsonProperty("supplierId") String supplierId,
            @JsonProperty("description") String description,
            @JsonProperty("category") String category,
            @JsonProperty("stockQuantity") int stockQuantity) {
        this.productId = productId;
        this.productName = productName;
        this.price = price;
        this.supplierId = supplierId;
        this.description = description;
        this.category = category;
        this.stockQuantity = stockQuantity;
    }

    private String generateProductId() {
        return UUID.randomUUID().toString();
    }

    public Product(String productName, double price, String supplierId, String description, String category,
            int stockQuantity) {
        this.productId = generateProductId();
        this.productName = productName;
        this.price = price;
        this.supplierId = supplierId;
        this.description = description;
        this.category = category;
        this.stockQuantity = stockQuantity;
    }

    public String getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public double getPrice() {
        return price;
    }

    public String getSupplierId() {
        return supplierId;
    }

    public String getDescription() {
        return description;
    }

    public String getCategory() {
        return category;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public boolean isInStock() {
        return stockQuantity > 0;
    }

    public boolean reduceStock(int quantity) {
        if (stockQuantity >= quantity) {
            stockQuantity -= quantity;
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "Product{" +
                "productId='" + productId + '\'' +
                ", productName='" + productName + '\'' +
                ", price=" + price +
                ", category='" + category + '\'' +
                ", stock=" + stockQuantity +
                '}';
    }
}
