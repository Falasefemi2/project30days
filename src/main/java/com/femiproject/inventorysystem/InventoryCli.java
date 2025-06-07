package com.femiproject.inventorysystem;

import java.util.List;
import java.util.Scanner;
import java.util.UUID;

public class InventoryCli {

    private Scanner scanner;
    private InventoryInterface<Product> product;
    private final InventoryManager manager;

    public InventoryCli() throws Exception {
        this.product = new InventoryUtils();
        this.manager = new InventoryManager(product);
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        while (true) {
            showMenu();
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1" -> viewAllProducts();
                case "2" -> addProduct();
                case "3" -> updateProduct();
                case "4" -> deleteProduct();
                case "5" -> searchProducts();
                case "6" -> showLowStock();
                case "7" -> restockProduct();
                case "8" -> showInventoryValue();
                case "9" -> {
                    System.out.println("Exiting. Goodbye!");
                    return;
                }
                default -> System.out.println("Invalid option. Try again.");
            }
        }
    }

    private void showMenu() {
        System.out.println("\n===== Inventory Management System =====");
        System.out.println("1. View all products");
        System.out.println("2. Add a product");
        System.out.println("3. Update a product");
        System.out.println("4. Delete a product");
        System.out.println("5. Search products");
        System.out.println("6. Check low stock products");
        System.out.println("7. Restock product");
        System.out.println("8. View total inventory value");
        System.out.println("9. Exit");
        System.out.print("Enter your choice: ");
    }

    public void viewAllProducts() {
        List<Product> products = manager.listAllProducts();
        if (products.isEmpty()) {
            System.out.println("No products in inventory.");
            return;
        }
        System.out.println("\nAll Products:");
        System.out.println("ID\tName\t\tPrice\tQuantity\tMin Threshold");
        System.out.println("------------------------------------------------");
        for (Product p : products) {
            System.out.printf("%s\t%-15s\t$%.2f\t%d\t\t%d%n",
                    p.getId(), p.getName(), p.getPrice(), p.getQuantity(), p.getMinThreshold());
        }
    }

    public void addProduct() {
        try {
            System.out.println("\nAdd New Product");
            System.out.println("----------------");

            String id = UUID.randomUUID().toString();

            System.out.print("Enter product name: ");
            String name = scanner.nextLine();

            System.out.print("Enter product price: ");
            double price = Double.parseDouble(scanner.nextLine());

            System.out.print("Enter initial quantity: ");
            int quantity = Integer.parseInt(scanner.nextLine());

            System.out.print("Enter minimum threshold: ");
            int minThreshold = Integer.parseInt(scanner.nextLine());

            Product product = new Product();
            product.setId(id);
            product.setName(name);
            product.setPrice(price);
            product.setQuantity(quantity);
            product.setMinThreshold(minThreshold);

            manager.addProducts(product);
            System.out.println("Product added successfully!");
        } catch (Exception e) {
            System.out.println("Error adding product: " + e.getMessage());
        }
    }

    public void updateProduct() {
        try {
            System.out.println("\nUpdate Product");
            System.out.println("--------------");

            System.out.print("Enter product ID to update: ");
            String id = scanner.nextLine();

            Product existing = manager.findProductById(id);
            if (existing == null) {
                System.out.println("Product not found!");
                return;
            }

            System.out.print("Enter new name (press Enter to keep current): ");
            String name = scanner.nextLine();
            if (!name.isEmpty()) {
                existing.setName(name);
            }

            System.out.print("Enter new price (press Enter to keep current): ");
            String priceStr = scanner.nextLine();
            if (!priceStr.isEmpty()) {
                existing.setPrice(Double.parseDouble(priceStr));
            }

            System.out.print("Enter new quantity (press Enter to keep current): ");
            String quantityStr = scanner.nextLine();
            if (!quantityStr.isEmpty()) {
                existing.setQuantity(Integer.parseInt(quantityStr));
            }

            System.out.print("Enter new minimum threshold (press Enter to keep current): ");
            String thresholdStr = scanner.nextLine();
            if (!thresholdStr.isEmpty()) {
                existing.setMinThreshold(Integer.parseInt(thresholdStr));
            }

            if (manager.updateProduct(existing)) {
                System.out.println("Product updated successfully!");
            } else {
                System.out.println("Failed to update product.");
            }
        } catch (Exception e) {
            System.out.println("Error updating product: " + e.getMessage());
        }
    }

    public void deleteProduct() {
        try {
            System.out.println("\nDelete Product");
            System.out.println("--------------");

            System.out.print("Enter product ID to delete: ");
            String id = scanner.nextLine();

            if (manager.deleteProduct(id)) {
                System.out.println("Product deleted successfully!");
            } else {
                System.out.println("Product not found!");
            }
        } catch (Exception e) {
            System.out.println("Error deleting product: " + e.getMessage());
        }
    }

    public void searchProducts() {
        System.out.println("\nSearch Products");
        System.out.println("--------------");

        System.out.print("Enter search term: ");
        String query = scanner.nextLine();

        List<Product> results = manager.searchProducts(query);
        if (results.isEmpty()) {
            System.out.println("No products found matching your search.");
            return;
        }

        System.out.println("\nSearch Results:");
        System.out.println("ID\tName\t\tPrice\tQuantity");
        System.out.println("----------------------------------------");
        for (Product p : results) {
            System.out.printf("%s\t%-15s\t$%.2f\t%d%n",
                    p.getId(), p.getName(), p.getPrice(), p.getQuantity());
        }
    }

    public void showLowStock() {
        List<Product> lowStock = manager.getLowStockProducts();
        if (lowStock.isEmpty()) {
            System.out.println("No products are low in stock.");
            return;
        }

        System.out.println("\nLow Stock Products:");
        System.out.println("ID\tName\t\tCurrent\tMin Required");
        System.out.println("----------------------------------------");
        for (Product p : lowStock) {
            System.out.printf("%s\t%-15s\t%d\t%d%n",
                    p.getId(), p.getName(), p.getQuantity(), p.getMinThreshold());
        }
    }

    public void restockProduct() {
        try {
            System.out.println("\nRestock Product");
            System.out.println("--------------");

            System.out.print("Enter product ID: ");
            String id = scanner.nextLine();

            System.out.print("Enter quantity to add: ");
            int quantity = Integer.parseInt(scanner.nextLine());

            if (manager.restockProduct(id, quantity)) {
                System.out.println("Product restocked successfully!");
            } else {
                System.out.println("Product not found!");
            }
        } catch (Exception e) {
            System.out.println("Error restocking product: " + e.getMessage());
        }
    }

    public void showInventoryValue() {
        double totalValue = manager.getTotalInventoryValue();
        System.out.printf("\nTotal Inventory Value: $%.2f%n", totalValue);
    }
}
