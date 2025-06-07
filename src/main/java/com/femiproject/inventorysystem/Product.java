package com.femiproject.inventorysystem;

public class Product {

    private String id;
    private String name;
    private double price;
    private int quantity;
    private int minThreshold;

    public Product() {
    }

    public Product(String id, String name, int quantity, int minThreshold) {
        this.id = id;
        this.name = name;
        this.price = 0.0;
        this.quantity = quantity;
        this.minThreshold = minThreshold;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getMinThreshold() {
        return minThreshold;
    }

    public void setMinThreshold(int minThreshold) {
        this.minThreshold = minThreshold;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Product{");
        sb.append("id=").append(id);
        sb.append(", name=").append(name);
        sb.append(", price=").append(price);
        sb.append(", quantity=").append(quantity);
        sb.append(", minThreshold=").append(minThreshold);
        sb.append('}');
        return sb.toString();
    }
}
