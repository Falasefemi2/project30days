package com.femiproject.inventorymanagement;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Product {
    @JsonProperty
    private String productId;
    @JsonProperty
    private String name;
    @JsonProperty
    private String brand;
    @JsonProperty
    private int quantity;
    @JsonProperty
    private double pricePerUnit;
    @JsonProperty
    private String category;

    @JsonCreator
    public Product(
            @JsonProperty String productId,
            @JsonProperty String name,
            @JsonProperty String brand,
            @JsonProperty int quantity,
            @JsonProperty double pricePerUnit,
            @JsonProperty String category) {
        this.productId = productId;
        this.name = name;
        this.brand = brand;
        this.quantity = quantity;
        this.pricePerUnit = pricePerUnit;
        this.category = category;
    }

    public Product(String name, String brand, int quantity, double pricePerUnit, String category) {
        this.productId = UUID.randomUUID().toString();
        this.name = name;
        this.brand = brand;
        this.quantity = quantity;
        this.pricePerUnit = pricePerUnit;
        this.category = category;
    }

    public String getProductId() {
        return productId;
    }

    public String getName() {
        return name;
    }

    public String getBrand() {
        return brand;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getPricePerUnit() {
        return pricePerUnit;
    }

    public String getCategory() {
        return category;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setPricePerUnit(double pricePerUnit) {
        this.pricePerUnit = pricePerUnit;
    }

    @Override
    public String toString() {
        return "Product [productId=" + productId + ", name=" + name + ", brand=" + brand + ", quantity=" + quantity
                + ", pricePerUnit=" + pricePerUnit + ", category=" + category + "]";
    }

    public boolean isProductAvailable() {
        return quantity > 0;
    }
}
