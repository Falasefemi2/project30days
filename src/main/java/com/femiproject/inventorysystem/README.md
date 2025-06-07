# Inventory Management System

A command-line based inventory management system built in Java that helps track products, manage stock levels, and monitor inventory value.

## Features

- **Product Management**

  - Add new products with automatic ID generation
  - Update existing product details
  - Delete products
  - Search products by any field
  - View all products

- **Stock Management**

  - Track product quantities
  - Set minimum stock thresholds
  - Get low stock alerts
  - Restock products
  - Calculate total inventory value

- **Data Persistence**
  - Save inventory data to file
  - Load inventory data on startup

## Prerequisites

- Java 17 or higher
- Maven

## Installation

1. Clone the repository:

```bash
git clone https://github.com/Falasefemi2/project30days.git
cd inventory-system
```

2. Build the project:

```bash
mvn clean install
```

## Usage

Run the application:

```bash
java -jar target/inventory-system-1.0-SNAPSHOT.jar
```

### Menu Options

1. **View All Products**

   - Displays all products in a tabular format
   - Shows ID, name, price, quantity, and minimum threshold

2. **Add Product**

   - Automatically generates a unique ID
   - Enter product details:
     - Name
     - Price
     - Initial quantity
     - Minimum threshold

3. **Update Product**

   - Update by product ID
   - Modify any field:
     - Name
     - Price
     - Quantity
     - Minimum threshold
   - Press Enter to keep current values

4. **Delete Product**

   - Remove product by ID

5. **Search Products**

   - Search across all fields
   - Case-insensitive search
   - Partial matches supported

6. **Show Low Stock**

   - Lists products below minimum threshold
   - Shows current quantity vs required minimum

7. **Restock Product**

   - Add stock by product ID
   - Enter quantity to add

8. **Show Inventory Value**
   - Displays total value of all inventory

## Project Structure

```
src/
├── main/
│   └── java/
│       └── com/
│           └── femiproject/
│               └── inventorysystem/
│                   ├── InventoryCli.java
│                   ├── InventoryManager.java
│                   ├── Product.java
│                   └── InventoryInterface.java
└── test/
    └── java/
        └── com/
            └── femiproject/
                └── inventorysystem/
                    └── InventoryManagerTest.java
```

## Testing

Run tests:

```bash
mvn test
```

## Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## License

This project is licensed under the MIT License - see the LICENSE file for details.
