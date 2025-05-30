package com.femiproject.expensetracker;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ExpenseTracker {

    private static int idCounter = 1;

    private int id;
    private String description;
    private double amount;
    private LocalDate date;

    public ExpenseTracker() {
    }

    public ExpenseTracker(String description, double amount) {

        if (description == null || description.isEmpty()) {
            throw new IllegalArgumentException("description cannot be null or empty");
        }

        this.id = idCounter++;
        this.description = description;
        this.amount = amount;
        this.date = LocalDate.now();
    }

    public ExpenseTracker(int id, String description, double amount, LocalDate date) {
        this.id = id;
        this.description = description;
        this.amount = amount;
        this.date = date;

        // Ensure idCounter is always ahead
        if (id >= idCounter) {
            idCounter = id + 1;
        }
    }

    public int getId() {
        return id;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void updateExpense(String description, double amount) {
        this.description = description;
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "ExpenseTracker [id=" + id + ", description=" + description + ", amount=" + amount + ", date=" + date + "]";
    }

}
