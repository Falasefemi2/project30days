# Food Ordering Service

A simple Java-based food ordering system that allows users to browse restaurants, view menus, and place orders.

## Features

- **User Management**: Create accounts and login with email
- **Restaurant Browsing**: View available restaurants and their locations
- **Menu Display**: Browse restaurant menus with prices and descriptions
- **Order Placement**: Select items from menus and place orders
- **Order History**: Track user order history
- **Payment Methods**: Support for Credit Card and Cash payments
- **Data Persistence**: All data is saved to JSON files

## How to Run

1. **Compile the project**:

   ```bash
   mvn compile
   ```

2. **Run the application**:
   ```bash
   mvn exec:java -Dexec.mainClass="com.femiproject.foodorderingservice.FoodOrderingMain"
   ```

## Usage

### For Guests (Not Logged In)

1. **Create Account** - Register with name, email, phone, address, and payment method
2. **Login** - Sign in with your email
3. **View Restaurants** - Browse available restaurants (read-only)
4. **Exit** - Close the application

### For Logged In Users

1. **View Restaurants** - See all available restaurants
2. **Place Order** - Select restaurant, choose menu items, and complete order
3. **Logout** - Sign out of your account
4. **Exit** - Close the application

## Ordering Process

1. **Select Restaurant**: Choose from the list of available restaurants
2. **Browse Menu**: View menu items with prices and descriptions
3. **Add Items**: Enter item numbers to add to your order
4. **Complete Order**: Enter '0' to finish and place your order
5. **Order Summary**: Review your order details and total

## Project Structure

```
foodorderingservice/
├── UserManager.java          # User account management
├── RestaurantManager.java    # Restaurant and menu management
├── OrderManager.java         # Order creation and tracking
├── FoodOrderingMain.java     # Main application entry point
├── User.java                 # User entity
├── Restaurant.java           # Restaurant entity
├── MenuItem.java             # Menu item entity
├── Order.java                # Order entity
├── Status.java               # Order status enum
├── PaymentMethod.java        # Payment method enum
└── README.md                 # This file
```

## Data Files

The application creates and manages these JSON files:

- `user_data.json` - User accounts and profiles
- `restaurant_data.json` - Restaurant information and menus
- `order_data.json` - Order history and details

## Sample Data

The system comes with sample restaurants:

- **Pizza Palace** - Italian pizza and pasta
- **Burger House** - American burgers and fries
- **Sushi Express** - Japanese sushi and rolls

Each restaurant has a menu with items, prices, and descriptions.

## Technical Details

- **Language**: Java 21
- **Build Tool**: Maven
- **Data Format**: JSON
- **Dependencies**: Jackson (JSON processing)
- **Architecture**: Simple layered architecture with managers for different domains

## Future Enhancements

- Order tracking and delivery status
- Restaurant ratings and reviews
- Multiple payment processing
- Real-time order updates
