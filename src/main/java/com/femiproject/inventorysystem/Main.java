package com.femiproject.inventorysystem;

public class Main {

    public static void main(String[] args) {
        try {
            new InventoryCli().start();
        } catch (Exception e) {
            System.err.println("Failed to start Product CLI: " + e.getMessage());
        }
    }
}
