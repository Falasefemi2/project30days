# Hotel Management System

A Java-based hotel management system that handles room bookings, guest management, and food orders.

## Features

- Room Management

  - Automatic room number generation
  - Support for Single and Double rooms
  - Luxury and Deluxe room categories
  - Room availability checking
  - Room deallocation

- Guest Management

  - Guest registration
  - Multiple guests per room (for double rooms)
  - Guest details tracking

- Food Service

  - Food ordering system
  - Multiple food items support
  - Order tracking per guest

- Billing System

  - Room charges
  - Food charges
  - Detailed bill generation

- Data Persistence
  - JSON-based data storage
  - Automatic data saving
  - State preservation between sessions

## Room Types and Categories

- Room Types:

  - Single (1 guest)
  - Double (2 guests)

- Room Categories:
  - Luxury
    - Single: ₦3000
    - Double: ₦4000
  - Deluxe
    - Single: ₦2000
    - Double: ₦3000

## Setup

1. Ensure Java 17 or higher is installed
2. Add Jackson dependency to your project:

```xml
<dependency>
    <groupId>com.fasterxml.jackson.core</groupId>
    <artifactId>jackson-databind</artifactId>
    <version>2.15.2</version>
</dependency>
```

## Usage

Run the `Main` class to start the application. The system provides a menu-driven interface for:

1. Adding customer details
2. Booking rooms
3. Checking room availability
4. Ordering food
5. Generating bills
6. Deallocating rooms
7. Viewing room features
8. Viewing all rooms

## Data Storage

The system automatically saves all data to `hotel_data.json` in the project directory. This includes:

- Room information
- Guest details
- Food orders
- Booking status
