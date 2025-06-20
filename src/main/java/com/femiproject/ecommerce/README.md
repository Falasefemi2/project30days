# E-commerce System

A simple Java-based e-commerce platform supporting buyers and suppliers, with persistent data storage using JSON files.

## Features

- User registration and login (role-based: Buyer or Supplier)
- Secure authentication
- Product catalog management (add, update, remove, search)
- Shopping cart functionality
- Order processing and order history
- Stock management
- Supplier dashboard and buyer dashboard
- Data persistence using `admin_file.json` (users) and `products.json` (products)

## User Roles

- **Buyer**: Browse/search products, add to cart, checkout, view order history
- **Supplier**: Add/manage products, view orders for their products

## Main Classes

- `Main`: Entry point, handles main menu and navigation
- `Admin`: User management, authentication, dashboards
- `User`: Represents a user (buyer or supplier)
- `ProductManager`: Handles product CRUD and search
- `Product`: Product details
- `Order`: Order details and status
- `CartItem`: Item in a shopping cart
- `OrderStatus`, `UserRole`: Enums for order state and user type

## How to Run

1. Compile all Java files in the package.
2. Run `Main.java`.
3. Follow the on-screen menu to register, login, and use the system.

---

_This project is for educational/demo purposes and uses simple file-based storage._
