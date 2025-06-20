package com.femiproject.ecommerce;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class User {

    @JsonProperty("userId")
    private String userId;

    @JsonProperty("name")
    private String name;

    @JsonProperty("email")
    private String email;

    @JsonProperty("password")
    private String password;

    @JsonProperty("role")
    private UserRole role;

    @JsonProperty("products")
    private List<Product> products;

    @JsonProperty("cart")
    private List<CartItem> cart;

    @JsonProperty("orderHistory")
    private List<Order> orderHistory;

    @JsonCreator
    public User(
            @JsonProperty("userId") String userId,
            @JsonProperty("name") String name,
            @JsonProperty("email") String email,
            @JsonProperty("password") String password,
            @JsonProperty("role") UserRole role,
            @JsonProperty("products") List<Product> products,
            @JsonProperty("cart") List<CartItem> cart,
            @JsonProperty("orderHistory") List<Order> orderHistory) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
        this.products = products != null ? products : new ArrayList<>();
        this.cart = cart != null ? cart : new ArrayList<>();
        this.orderHistory = orderHistory != null ? orderHistory : new ArrayList<>();
    }

    public User(String name, String email, String password, UserRole role) {
        this.userId = generateUserId();
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
        this.products = new ArrayList<>();
        this.cart = new ArrayList<>();
        this.orderHistory = new ArrayList<>();
    }

    private String generateUserId() {
        return UUID.randomUUID().toString();
    }

    public void addProduct(Product product) {
        if (isSupplier()) {
            products.add(product);
            System.out.println("Product added successfully: " + product.getProductName());
        } else {
            System.out.println("Only suppliers can add products!");
        }
    }

    public void removeProduct(String productId) {
        if (isSupplier()) {
            products.removeIf(p -> p.getProductId().equals(productId));
            System.out.println("Product removed successfully");
        } else {
            System.out.println("Only suppliers can remove products!");
        }
    }

    public List<Product> getMyProducts() {
        if (isSupplier()) {
            return new ArrayList<>(products);
        }
        return new ArrayList<>();
    }

    public void addToCart(Product product, int quantity) {
        if (isBuyer()) {
            CartItem existingItem = cart.stream()
                    .filter(item -> item.getProduct().getProductId().equals(product.getProductId()))
                    .findFirst()
                    .orElse(null);

            if (existingItem != null) {
                existingItem.setQuantity(existingItem.getQuantity() + quantity);
            } else {
                cart.add(new CartItem(product, quantity));
            }
            System.out.println("Added to cart: " + product.getProductName());
        } else {
            System.out.println("Only buyers can add items to cart!");
        }
    }

    public void removeFromCart(String productId) {
        if (isBuyer()) {
            cart.removeIf(item -> item.getProduct().getProductId().equals(productId));
            System.out.println("Item removed from cart");
        } else {
            System.out.println("Only buyers can remove items from cart!");
        }
    }

    public List<CartItem> getCart() {
        if (isBuyer()) {
            return new ArrayList<>(cart);
        }
        return new ArrayList<>();
    }

    public double getCartTotal() {
        if (isBuyer()) {
            return cart.stream()
                    .mapToDouble(item -> item.getProduct().getPrice() * item.getQuantity())
                    .sum();
        }
        return 0.0;
    }

    public Order checkout() {
        if (isBuyer() && !cart.isEmpty()) {
            Order order = new Order(this.userId, new ArrayList<>(cart));
            orderHistory.add(order);
            cart.clear();
            System.out.println("Order placed successfully! Order ID: " + order.getOrderId());
            return order;
        } else if (!isBuyer()) {
            System.out.println("Only buyers can checkout!");
            return null;
        } else {
            System.out.println("Cart is empty!");
            return null;
        }
    }

    public String getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public UserRole getRole() {
        return role;
    }

    public List<Product> getProducts() {
        return products;
    }

    public List<Order> getOrderHistory() {
        return orderHistory;
    }

    public boolean isBuyer() {
        return this.role == UserRole.BUYER;
    }

    public boolean isSupplier() {
        return this.role == UserRole.SUPPLIER;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId='" + userId + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", role=" + role +
                '}';
    }
}
