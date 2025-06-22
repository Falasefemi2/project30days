# Car Rental System

This is a simple console-based Car Rental System written in Java. It allows administrators to manage users, vehicles, and rentals, providing basic functionality for renting and returning vehicles.

## Features

- **User Management:** Create and manage users with unique IDs and contact information.
- **Vehicle Management:** Add vehicles with details such as name, quantity, color, and price per day.
- **Rental Management:** Rent and return vehicles, track rental status (ACTIVE, RETURNED, CANCELLED), and calculate rental costs based on duration.
- **Data Persistence:** User, vehicle, and rental data are saved to JSON files for persistence across sessions.
- **View Rentals:** Display all rentals or rentals by user.

## How to Use

1. Run `CarRentalApp.java`.
2. Use the menu to create users, add vehicles, rent or return vehicles, and view rental records.
3. Data is automatically saved and loaded from `users.json`, `vehicles.json`, and `rentals.json` in the project directory.

## Main Classes

- `Admin`: Handles all admin operations and data persistence.
- `User`: Represents a customer and their rentals.
- `Vehicle`: Represents a rentable vehicle.
- `Rental`: Represents a rental transaction.
- `Status`: Enum for rental status.

---

This project is for educational purposes and can be extended with more features as needed.
