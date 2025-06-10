package com.femiproject.weatherstocksreport;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.femiproject.weatherstocksreport.model.LocationResponse;
import com.femiproject.weatherstocksreport.model.WeatherResponse;
import com.femiproject.weatherstocksreport.service.LocationService;
import com.femiproject.weatherstocksreport.service.StockService;
import com.femiproject.weatherstocksreport.service.WeatherService;

public class Main {
    private static final ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();

    public static void main(String[] args) {
        System.out.println("Starting Weather & Stock Monitor...\n");

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("\nShutting down executor...");
            executor.shutdown();
            try {
                if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                    executor.shutdownNow();
                }
            } catch (InterruptedException e) {
                executor.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }));

        int cycleCount = 0;
        final int MAX_CYCLES = 10;

        while (cycleCount < MAX_CYCLES) {
            cycleCount++;
            System.out.printf("=== Update Cycle %d ===\n", cycleCount);

            try {
                CompletableFuture<LocationResponse> locationFuture = CompletableFuture.supplyAsync(() -> {
                    System.out.println("Fetching location...");
                    return LocationService.fetchLocation();
                }, executor);

                CompletableFuture<Map<String, String>> stockFuture = CompletableFuture.supplyAsync(() -> {
                    System.out.println("Fetching stock prices...");
                    return StockService.fetchStockPrices();
                }, executor);

                CompletableFuture<WeatherResponse> weatherFuture = locationFuture.thenApplyAsync(location -> {
                    if (location != null) {
                        System.out.println("Fetching weather for " + location.city + ", " + location.country + "...");
                        return WeatherService.fetchWeather(location.lat, location.lon);
                    } else {
                        System.err.println("Cannot fetch weather - location is null");
                        return null;
                    }
                }, executor);

                // Wait for all tasks to complete and display results
                CompletableFuture<Void> allTasks = CompletableFuture.allOf(locationFuture, weatherFuture, stockFuture)
                        .thenRun(() -> {
                            try {
                                LocationResponse location = locationFuture.get(5, TimeUnit.SECONDS);
                                WeatherResponse weather = weatherFuture.get(5, TimeUnit.SECONDS);
                                Map<String, String> stocks = stockFuture.get(5, TimeUnit.SECONDS);

                                displayResults(location, weather, stocks);

                            } catch (Exception e) {
                                System.err.println("Error processing results: " + e.getMessage());
                                e.printStackTrace();
                            }
                        });

                allTasks.get(30, TimeUnit.SECONDS);

            } catch (Exception e) {
                System.err.println("Update cycle " + cycleCount + " failed: " + e.getMessage());
                e.printStackTrace();
            }

            // Sleep between cycles (except for the last one)
            if (cycleCount < MAX_CYCLES) {
                try {
                    System.out.println("Waiting 60 seconds for next update...\n");
                    Thread.sleep(60_000);
                } catch (InterruptedException e) {
                    System.out.println("Update loop interrupted.");
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }

        System.out.println("Monitoring completed. Shutting down...");
        executor.shutdown();
        try {
            if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    private static void displayResults(LocationResponse location, WeatherResponse weather, Map<String, String> stocks) {
        System.out.println("\n" + "=".repeat(60));
        System.out.printf("Update Time: %s%n",
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        System.out.println("=".repeat(60));

        // Display location
        if (location != null) {
            System.out.printf("📍 Location: %s, %s (lat: %.4f, lon: %.4f)%n%n",
                    location.city, location.country, location.lat, location.lon);
        } else {
            System.out.println("❌ Location: Unable to fetch location\n");
        }

        // Display weather
        if (weather != null) {
            displayWeather(weather);
        } else {
            System.out.println("❌ Weather: Unable to fetch weather data\n");
        }

        // Display stocks
        displayStocks(stocks);

        System.out.println("=".repeat(60) + "\n");
    }

    private static void displayWeather(WeatherResponse weather) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
            LocalDateTime sunrise = LocalDateTime.ofInstant(
                    Instant.ofEpochSecond(weather.sys.sunrise),
                    ZoneId.systemDefault());
            LocalDateTime sunset = LocalDateTime.ofInstant(
                    Instant.ofEpochSecond(weather.sys.sunset),
                    ZoneId.systemDefault());

            System.out.println("🌤️  Weather Report for " + weather.name + ", " + weather.sys.country);
            System.out.printf("   Current: %s (%s)%n",
                    weather.weather.get(0).main, weather.weather.get(0).description);

            System.out.printf("   Temperature: %.1f°C (Feels like: %.1f°C)%n",
                    weather.main.temp, weather.main.feels_like);

            System.out.printf("   Humidity: %d%%, Pressure: %d hPa%n",
                    weather.main.humidity, weather.main.pressure);
            System.out.printf("   Wind: %.1f m/s, Visibility: %d m%n",
                    weather.wind.speed, weather.visibility);
            System.out.printf("   Clouds: %d%%%n", weather.clouds.all);
            System.out.printf("   Sunrise: %s, Sunset: %s%n%n",
                    sunrise.format(formatter), sunset.format(formatter));
        } catch (Exception e) {
            System.err.println("Error displaying weather: " + e.getMessage());
            System.out.println("❌ Weather: Error formatting weather data\n");
        }
    }

    private static void displayStocks(Map<String, String> stocks) {
        System.out.println("📈 Stock Prices:");
        if (stocks != null && !stocks.isEmpty()) {
            stocks.forEach((symbol, price) -> {
                String status = isErrorPrice(price) ? "❌" : "💰";
                System.out.printf("   %s %s: %s%n", status, symbol, price);
            });
        } else {
            System.out.println("   ❌ Unable to fetch stock prices");
        }
        System.out.println();
    }

    private static boolean isErrorPrice(String price) {
        return price == null ||
                price.equals("N/A") ||
                price.equals("Error") ||
                price.equals("API Key Missing") ||
                price.equals("Rate Limited") ||
                price.equals("Unauthorized") ||
                price.equals("HTTP Error") ||
                price.equals("API Error") ||
                price.equals("Parse Error") ||
                price.equals("Interrupted");
    }
}