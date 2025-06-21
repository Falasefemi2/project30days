package com.femiproject.ecommerce;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class Admin {
    private static final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .enable(SerializationFeature.INDENT_OUTPUT);
    private static final String DATA_FILE = "admin_file.json";
    private static Scanner scanner = new Scanner(System.in);
    private static List<User> users = new ArrayList<>();
    private static User currentUser = null;
    private static ProductManager productManager = new ProductManager();

    public Admin() {
        loadUsers();
    }

    private void loadUsers() {
        try {
            File file = new File(DATA_FILE);
            if (file.exists() && file.length() > 0) {
                List<User> loadedUsers = objectMapper.readValue(file, new TypeReference<List<User>>() {
                });
                users = loadedUsers;
                System.out.println("Users loaded successfully from " + DATA_FILE);
            } else {
                System.out.println("No existing user data found. Starting with empty list.");
            }
        } catch (IOException e) {
            System.err.println("Error loading users from " + DATA_FILE + ": " + e.getMessage());
            users = new ArrayList<>();
        }
    }

    private void saveUsers() {
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(DATA_FILE), users);
            System.out.println("Users saved successfully to " + DATA_FILE);

        } catch (IOException e) {
            System.err.println("Error saving users: " + e.getMessage());
        }
    }

    private User getUserInput() {
        System.out.println("Enter your name: ");
        String name = scanner.nextLine();

        System.out.println("Enter your email: ");
        String email = scanner.nextLine();

        System.out.println("Enter your password: ");
        String password = scanner.nextLine();

        System.out.println("Choose your type:");
        System.out.println("1. Buyer");
        System.out.println("2. Supplier");
        System.out.print("Enter choice (1-2): ");

        int choice = scanner.nextInt();
        scanner.nextLine();

        UserRole userRole = (choice == 1) ? UserRole.BUYER : UserRole.SUPPLIER;

        return new User(name, email, password, userRole);
    }

    private boolean isEmailExists(String email) {
        return users.stream().anyMatch(user -> user.getEmail().equalsIgnoreCase(email));
    }

    private User authenticateUser(String email, String password) {
        return users.stream()
                .filter(user -> user.getEmail().equalsIgnoreCase(email) &&
                        user.getPassword().equals(password))
                .findFirst()
                .orElse(null);
    }

    public void createUser() {
        User newUser = getUserInput();

        if (isEmailExists(newUser.getEmail())) {
            System.out.println("User with email " + newUser.getEmail() + " already exists!");
            return;
        }

        users.add(newUser);
        saveUsers();
        System.out.println("User " + newUser.getName() + " created successfully");
    }

    public boolean loginUser(String email, String password) {
        if (email == null || email.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            System.out.println("Email and password cannot be empty");
            return false;
        }

        User user = authenticateUser(email, password);
        if (user != null) {
            currentUser = user;
            System.out.println("Login successful! Welcome " + user.getName());
            showUserDashboard();
            return true;
        } else {
            System.out.println("Invalid email or password.");
            return false;
        }
    }

    private void showUserDashboard() {
        if (currentUser == null)
            return;

        while (true) {
            System.out.println("\n=== " + currentUser.getName() + "'s Dashboard ===");

            if (currentUser.isSupplier()) {
                showSupplierMenu();
            } else if (currentUser.isBuyer()) {
                showBuyerMenu();
            }

            System.out.println("0. Logout");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            if (choice == 0) {
                logout();
                break;
            }

            if (currentUser.isSupplier()) {
                handleSupplierChoice(choice);
            } else if (currentUser.isBuyer()) {
                handleBuyerChoice(choice);
            }
        }
    }

    private void showSupplierMenu() {
        System.out.println("1. Add New Product");
        System.out.println("2. View My Products");
        System.out.println("3. Update Product");
        System.out.println("4. Remove Product");
        System.out.println("5. View All Orders for My Products");
    }

    private void showBuyerMenu() {
        System.out.println("1. Browse All Products");
        System.out.println("2. Search Products");
        System.out.println("3. Add Product to Cart");
        System.out.println("4. View Cart");
        System.out.println("5. Remove from Cart");
        System.out.println("6. Checkout");
        System.out.println("7. View Order History");
    }

    private void handleSupplierChoice(int choice) {
        switch (choice) {
            case 1 -> addNewProduct();
            case 2 -> viewMyProducts();
            case 3 -> updateProduct();
            case 4 -> removeProduct();
            case 5 -> viewOrdersForMyProducts();
            default -> System.out.println("Invalid choice!");
        }
    }

    private void handleBuyerChoice(int choice) {
        switch (choice) {
            case 1 -> browseAllProducts();
            case 2 -> searchProducts();
            case 3 -> addToCart();
            case 4 -> viewCart();
            case 5 -> removeFromCart();
            case 6 -> checkout();
            case 7 -> viewOrderHistory();
            default -> System.out.println("Invalid choice!");
        }
    }

    private void addNewProduct() {
        System.out.print("Enter product name: ");
        String name = scanner.nextLine();

        System.out.print("Enter product price: ");
        double price = scanner.nextDouble();
        scanner.nextLine();

        System.out.print("Enter product description: ");
        String description = scanner.nextLine();

        System.out.print("Enter product category: ");
        String category = scanner.nextLine();

        System.out.print("Enter stock quantity: ");
        int stock = scanner.nextInt();
        scanner.nextLine();

        Product product = new Product(name, price, currentUser.getUserId(), description, category, stock);
        currentUser.addProduct(product);
        productManager.addProduct(product);
        saveUsers();
    }

    private void viewMyProducts() {
        List<Product> myProducts = currentUser.getMyProducts();
        if (myProducts.isEmpty()) {
            System.out.println("You haven't added any products yet.");
            return;
        }

        System.out.println("\n=== Your Products ===");
        for (int i = 0; i < myProducts.size(); i++) {
            System.out.println((i + 1) + ". " + myProducts.get(i));
        }
    }

    private void updateProduct() {
        viewMyProducts();
        List<Product> myProducts = currentUser.getMyProducts();
        if (myProducts.isEmpty())
            return;

        System.out.print("Enter product number to update: ");
        int index = scanner.nextInt() - 1;
        scanner.nextLine();

        if (index >= 0 && index < myProducts.size()) {
            Product product = myProducts.get(index);

            System.out.print("Enter new price (current: " + product.getPrice() + "): ");
            double newPrice = scanner.nextDouble();
            scanner.nextLine();

            System.out.print("Enter new stock quantity (current: " + product.getStockQuantity() + "): ");
            int newStock = scanner.nextInt();
            scanner.nextLine();

            product.setPrice(newPrice);
            product.setStockQuantity(newStock);

            saveUsers();
            System.out.println("Product updated successfully!");
        } else {
            System.out.println("Invalid product number!");
        }
    }

    private void removeProduct() {
        viewMyProducts();
        List<Product> myProducts = currentUser.getMyProducts();
        if (myProducts.isEmpty())
            return;

        System.out.print("Enter product number to remove: ");
        int index = scanner.nextInt() - 1;
        scanner.nextLine();

        if (index >= 0 && index < myProducts.size()) {
            Product product = myProducts.get(index);
            currentUser.removeProduct(product.getProductId());
            productManager.removeProduct(product.getProductId());
            saveUsers();
        } else {
            System.out.println("Invalid product number!");
        }
    }

    private void viewOrdersForMyProducts() {
        System.out.println("Order tracking feature coming soon!");
    }

    private void browseAllProducts() {
        List<Product> allProducts = productManager.getAllProducts();
        if (allProducts.isEmpty()) {
            System.out.println("No products available.");
            return;
        }

        System.out.println("\n=== All Products ===");
        for (int i = 0; i < allProducts.size(); i++) {
            Product product = allProducts.get(i);
            if (product.isInStock()) {
                System.out.println((i + 1) + ". " + product);
            }
        }
    }

    private void searchProducts() {
        System.out.print("Enter search keyword: ");
        String keyword = scanner.nextLine();

        List<Product> results = productManager.searchProducts(keyword);
        if (results.isEmpty()) {
            System.out.println("No products found matching '" + keyword + "'");
            return;
        }

        System.out.println("\n=== Search Results ===");
        for (int i = 0; i < results.size(); i++) {
            System.out.println((i + 1) + ". " + results.get(i));
        }
    }

    private void addToCart() {
        browseAllProducts();
        List<Product> allProducts = productManager.getAllProducts()
                .stream()
                .filter(Product::isInStock)
                .toList();

        if (allProducts.isEmpty())
            return;

        System.out.print("Enter product number to add to cart: ");
        int index = scanner.nextInt() - 1;
        scanner.nextLine();

        if (index >= 0 && index < allProducts.size()) {
            Product product = allProducts.get(index);

            System.out.print("Enter quantity: ");
            int quantity = scanner.nextInt();
            scanner.nextLine();

            if (quantity <= product.getStockQuantity()) {
                currentUser.addToCart(product, quantity);
                saveUsers();
            } else {
                System.out.println("Not enough stock! Available: " + product.getStockQuantity());
            }
        } else {
            System.out.println("Invalid product number!");
        }
    }

    private void viewCart() {
        List<CartItem> cart = currentUser.getCart();
        if (cart.isEmpty()) {
            System.out.println("Your cart is empty.");
            return;
        }

        System.out.println("\n=== Your Cart ===");
        for (int i = 0; i < cart.size(); i++) {
            System.out.println((i + 1) + ". " + cart.get(i));
        }
        System.out.println("Total: $" + String.format("%.2f", currentUser.getCartTotal()));
    }

    private void removeFromCart() {
        viewCart();
        List<CartItem> cart = currentUser.getCart();
        if (cart.isEmpty())
            return;

        System.out.print("Enter item number to remove: ");
        int index = scanner.nextInt() - 1;
        scanner.nextLine();

        if (index >= 0 && index < cart.size()) {
            CartItem item = cart.get(index);
            currentUser.removeFromCart(item.getProduct().getProductId());
            saveUsers();
        } else {
            System.out.println("Invalid item number!");
        }
    }

    private void checkout() {
        if (currentUser.getCart().isEmpty()) {
            System.out.println("Your cart is empty!");
            return;
        }

        viewCart();
        System.out.print("Proceed with checkout? (y/n): ");
        String confirm = scanner.nextLine();

        if (confirm.toLowerCase().startsWith("y")) {
            Order order = currentUser.checkout();
            if (order != null) {
                // Update stock quantities
                for (CartItem item : order.getItems()) {
                    Product product = productManager.getProductById(item.getProduct().getProductId());
                    if (product != null) {
                        product.reduceStock(item.getQuantity());
                    }
                }
                saveUsers();
            }
        }
    }

    private void viewOrderHistory() {
        List<Order> orders = currentUser.getOrderHistory();
        if (orders.isEmpty()) {
            System.out.println("No orders found.");
            return;
        }

        System.out.println("\n=== Your Order History ===");
        for (Order order : orders) {
            System.out.println(order);
        }
    }

    public static void logout() {
        currentUser = null;
    }

}
