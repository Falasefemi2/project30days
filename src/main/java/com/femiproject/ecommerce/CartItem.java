package com.femiproject.ecommerce;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CartItem {

    @JsonProperty("product")
    private Product product;

    @JsonProperty("quantity")
    private int quantity;

    @JsonCreator
    public CartItem(
            @JsonProperty("product") Product product,
            @JsonProperty("quantity") int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getTotalPrice() {
        return product.getPrice() * quantity;
    }

    @Override
    public String toString() {
        return "CartItem{" +
                "product=" + product.getProductName() +
                ", quantity=" + quantity +
                ", totalPrice=" + getTotalPrice() +
                '}';
    }
}
