# Inventory Management System

A simple, command-line-based inventory management system for Femi Electronics. This application allows for the management of products, sales, and revenue tracking through a text-based interface.

## Key Features

- **Product Management:** Add, update, delete, and view products in the inventory.
- **Sales Tracking:** Record sales transactions and maintain a history of all sales.
- **Revenue Reporting:** Calculate and display total revenue from all sales.
- **Data Persistence:** Product and sales data are saved to JSON files (`new_products.json`, `sales.json`).
- **Search Functionality:** Search for products by category.

## How to Run

1. **Compile the code:**
   ```bash
   mvn compile
   ```
2. **Run the application:**
   ```bash
   mvn exec:java -Dexec.mainClass="com.femiproject.inventorymanagement.Main"
   ```

## File Structure

- `Main.java`: The entry point of the application, handles user input and navigation.
- `Product.java`: The model class for a product.
- `ProductManager.java`: Manages the product list, including CRUD operations and data persistence.
- `Sales.java`: The model class for a sales transaction.
- `SalesManager.java`: Manages sales history and revenue calculation.
- `new_products.json`: The data file for storing the product inventory.
- `sales.json`: The data file for storing sales history.
