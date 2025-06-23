package com.femiproject.inventorymanagement;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Sales {
    @JsonProperty
    private LocalDateTime saleDate;
    @JsonProperty
    private List<Product> products;
    @JsonProperty
    private int quantitySold;
    @JsonProperty
    private double totalCost;

    @JsonCreator
    public Sales() {
        this.saleDate = LocalDateTime.now();
        this.products = new ArrayList<>();
        this.quantitySold = 0;
        this.totalCost = 0;
    }

    public LocalDateTime getSaleDate() {
        return saleDate;
    }

    public List<Product> getProducts() {
        return products;
    }

    public int getQuantitySold() {
        return quantitySold;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void setQuantitySold(int quantitySold) {
        this.quantitySold = quantitySold;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

    @Override
    public String toString() {
        return "Sales [saleDate=" + saleDate + ", products=" + products + ", quantitySold=" + quantitySold
                + ", totalCost=" + totalCost + "]";
    }
}
