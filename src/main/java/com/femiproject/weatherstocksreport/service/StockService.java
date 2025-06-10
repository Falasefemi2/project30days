package com.femiproject.weatherstocksreport.service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.Semaphore;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.cdimascio.dotenv.Dotenv;

public class StockService {
    private static final Dotenv dotenv = Dotenv.load();
    private static final String API_KEY = dotenv.get("STOCK_API_KEY");
    private static final String BASE_URL = "https://api.twelvedata.com/price";
    private static final Semaphore SEMAPHORE = new Semaphore(5); // Lower limit for stock API
    private static final HttpClient HTTP_CLIENT = HttpClient.newHttpClient();
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final String[] SYMBOLS = { "AAPL", "TSLA", "GOOGL", "MSFT" };
    private static final int DELAY_BETWEEN_REQUESTS = 250; // 250ms delay

    public static Map<String, String> fetchStockPrices() {
        // Validate API key
        if (API_KEY == null || API_KEY.isEmpty()) {
            System.err.println("STOCK_API_KEY not found in environment variables");
            Map<String, String> errorMap = new LinkedHashMap<>();
            for (String symbol : SYMBOLS) {
                errorMap.put(symbol, "API Key Missing");
            }
            return errorMap;
        }

        Map<String, String> stockPrices = new LinkedHashMap<>();

        // Fetch each stock individually with proper semaphore handling
        for (String symbol : SYMBOLS) {
            String price = fetchSingleStockPrice(symbol);
            stockPrices.put(symbol, price);

            // Add delay between requests to avoid rate limiting
            if (!symbol.equals(SYMBOLS[SYMBOLS.length - 1])) { // Don't delay after last request
                try {
                    Thread.sleep(DELAY_BETWEEN_REQUESTS);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.err.println("Thread interrupted during delay");
                    break;
                }
            }
        }

        return stockPrices;
    }

    private static String fetchSingleStockPrice(String symbol) {
        try {
            SEMAPHORE.acquire();
            try {
                String url = String.format("%s?symbol=%s&apikey=%s", BASE_URL, symbol, API_KEY);

                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(url))
                        .header("Accept", "application/json")
                        .header("User-Agent", "WeatherStocksReport/1.0")
                        .timeout(Duration.ofSeconds(10))
                        .GET()
                        .build();

                HttpResponse<String> response = HTTP_CLIENT.send(request,
                        HttpResponse.BodyHandlers.ofString());

                switch (response.statusCode()) {
                    case 200 -> {
                        return parseStockResponse(response.body(), symbol);
                    }
                    case 429 -> {
                        System.err.println("Rate limit exceeded for " + symbol);
                        return "Rate Limited";
                    }
                    case 401 -> {
                        System.err.println("Unauthorized - check API key for " + symbol);
                        return "Unauthorized";
                    }
                    default -> {
                        System.err.println("HTTP Error " + response.statusCode() + " for " + symbol);
                        System.err.println("Response: " + response.body());
                        return "HTTP Error";
                    }
                }

            } finally {
                SEMAPHORE.release(); // Always release semaphore
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Thread interrupted while waiting for semaphore for " + symbol);
            return "Interrupted";
        } catch (Exception e) {
            System.err.println("Failed to fetch price for " + symbol + ": " + e.getMessage());
            e.printStackTrace();
            return "Error";
        }
    }

    private static String parseStockResponse(String responseBody, String symbol) {
        try {
            JsonNode root = OBJECT_MAPPER.readTree(responseBody);

            // Check for API error messages
            if (root.has("message")) {
                String message = root.get("message").asText();
                System.err.println("API Error for " + symbol + ": " + message);
                return "API Error";
            }

            if (root.has("status") && "error".equals(root.get("status").asText())) {
                String message = root.has("message") ? root.get("message").asText() : "Unknown error";
                System.err.println("API Status Error for " + symbol + ": " + message);
                return "API Error";
            }

            // Try to extract price from different possible response formats
            if (root.has("price")) {
                String price = root.get("price").asText();
                // Validate that price is a number
                try {
                    double priceValue = Double.parseDouble(price);
                    return String.format("$%.2f", priceValue);
                } catch (NumberFormatException e) {
                    return price; // Return as-is if not a number
                }
            } else if (root.isObject() && root.size() > 0) {
                // Sometimes the response might be nested differently
                JsonNode firstNode = root.elements().next();
                if (firstNode.has("price")) {
                    String price = firstNode.get("price").asText();
                    try {
                        double priceValue = Double.parseDouble(price);
                        return String.format("$%.2f", priceValue);
                    } catch (NumberFormatException e) {
                        return price;
                    }
                }
            }

            System.err.println("Unexpected response format for " + symbol + ": " + responseBody);
            return "Invalid Format";

        } catch (Exception e) {
            System.err.println("Error parsing response for " + symbol + ": " + e.getMessage());
            return "Parse Error";
        }
    }

    // Utility method to get available symbols
    public static String[] getSymbols() {
        return SYMBOLS.clone();
    }
}