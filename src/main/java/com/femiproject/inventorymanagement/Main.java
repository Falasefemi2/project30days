package com.femiproject.inventorymanagement;

import java.util.Scanner;

public class Main {

    private ProductManager productManager;
    private SalesManager salesManager;
    private Scanner scanner;

    public Main() {
        this.productManager = new ProductManager();
        this.salesManager = new SalesManager();
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        System.out.println("=== Welcome to Femi Electronics ===");

        while (true) {
            printMenu();
            int choice = getIntInput("Choose an option: ");

            switch (choice) {
                case 1 -> addProduct();
                case 2 -> updateProduct();
                case 3 -> deleteProduct();
                case 4 -> listAllProduct();
                case 5 -> searchForProduct();
                case 6 -> sellProduct();
                case 7 -> viewSalesHistory();
                case 8 -> totalRevenue();
                case 0 -> {
                    System.out.println("Thank you for using the Electornics system. Goodbye!");
                    return;
                }
                default -> System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private void printMenu() {
        System.out.println("\n === Electornics System ===");
        System.out.println("1. Add Product");
        System.out.println("2. Update Product");
        System.out.println("3. Delete Product");
        System.out.println("4. List all Product");
        System.out.println("5. Search product by category");
        System.out.println("6. Sell Product");
        System.out.println("7. View sales history");
        System.out.println("8. View total revenue");
        System.out.println("0. Exit");
    }

    private int getIntInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    public void addProduct() {
        System.out.println("Enter product name: ");
        String name = scanner.nextLine();
        System.out.println("Enter brand: ");
        String brand = scanner.nextLine();
        System.out.println("Enter quantity: ");
        int quantity = scanner.nextInt();
        scanner.nextLine();
        System.out.println("Enter price: ");
        double price = scanner.nextDouble();
        scanner.nextLine();
        System.out.println("Enter category:");
        String category = scanner.nextLine();

        Product product = new Product(name, brand, quantity, price, category);
        productManager.addProduct(product);
    }

    public void updateProduct() {
        System.out.println("Enter product ID: ");
        String id = scanner.nextLine();
        Product product = productManager.findProductById(id);
        if (product == null) {
            System.out.println("Product not found.");
            return;
        }
        System.out.println("Enter new quantity: ");
        int quantity = scanner.nextInt();
        scanner.nextLine();
        System.out.println("Enter new price: ");
        double price = scanner.nextDouble();
        scanner.nextLine();
        productManager.updateProduct(product, quantity, price);
    }

    public void deleteProduct() {
        System.out.println("Enter product ID: ");
        String id = scanner.nextLine();
        productManager.deleteProduct(id);
    }

    public void listAllProduct() {
        var products = productManager.listAllProduct();
        if (products.isEmpty()) {
            System.out.println("No products found.");
        } else {
            products.forEach(System.out::println);
        }
    }

    public void searchForProduct() {
        System.out.println("Enter product to search: ");
        String word = scanner.nextLine();
        var products = productManager.searchProductsByCategory(word);
        if (products.isEmpty()) {
            System.out.println("No products found in this category.");
        } else {
            products.forEach(System.out::println);
        }
    }

    public void sellProduct() {
        System.out.println("Enter product ID: ");
        String id = scanner.nextLine();
        Product product = productManager.findProductById(id);
        salesManager.simulateSales(product, productManager, scanner);
    }

    public void viewSalesHistory() {
        var history = salesManager.salesHistory();
        if (history.isEmpty()) {
            System.out.println("No sales history found.");
        } else {
            history.forEach(System.out::println);
        }
    }

    public void totalRevenue() {
        double revenue = salesManager.totalRevenue();
        System.out.println("Total revenue: " + revenue);
    }

    public static void main(String[] args) {
        Main m = new Main();
        m.start();
    }
}
