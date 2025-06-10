# Weather & Stocks Report

A Java application that concurrently monitors weather conditions and stock prices, providing real-time updates in a formatted console output.

## Features

- 🌍 Location detection
- 🌤️ Real-time weather data including:
  - Temperature and feels-like temperature
  - Weather conditions and description
  - Humidity and pressure
  - Wind speed and visibility
  - Cloud coverage
  - Sunrise and sunset times
- 📈 Stock price monitoring
- ⚡ Concurrent data fetching using virtual threads
- 🔄 Automatic updates every 60 seconds
- 🛑 Graceful shutdown handling

## Requirements

- Java 21 or higher (for virtual threads support)
- Internet connection
- API keys for:
  - Weather API
  - Stock API

## Setup

1. Clone the repository
2. Configure API keys in the respective service classes:
   - `WeatherService.java`
   - `StockService.java`
   - `LocationService.java`

## Usage

Run the main class to start monitoring:

```bash
java -cp target/classes com.femiproject.weatherstocksreport.Main
```

The application will:

1. Run for 10 update cycles
2. Display weather and stock information every 60 seconds
3. Automatically shut down after completion

## Project Structure

```
src/main/java/com/femiproject/weatherstocksreport/
├── Main.java                 # Main application entry point
├── model/                    # Data models
├── service/                  # Service implementations
│   ├── LocationService.java  # Location detection service
│   ├── WeatherService.java   # Weather data service
│   └── StockService.java     # Stock price service
└── README.md                 # This file
```

## Error Handling

The application includes comprehensive error handling for:

- API failures
- Network issues
- Data parsing errors
- Rate limiting
- Authentication issues
