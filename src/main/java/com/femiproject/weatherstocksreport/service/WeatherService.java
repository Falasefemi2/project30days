package com.femiproject.weatherstocksreport.service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.Semaphore;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.femiproject.weatherstocksreport.model.WeatherResponse;
import io.github.cdimascio.dotenv.Dotenv;

public class WeatherService {
    private static final Dotenv dotenv = Dotenv.load();
    private static final String API_KEY = dotenv.get("WEATHER_API_KEY");
    private static final String BASE_URL = "https://api.openweathermap.org/data/2.5/weather";
    private static final Semaphore SEMAPHORE = new Semaphore(20);
    private static final HttpClient HTTP_CLIENT = HttpClient.newHttpClient();
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static WeatherResponse fetchWeather(double lat, double lon) {
        if (API_KEY == null || API_KEY.isEmpty()) {
            System.err.println("WEATHER_API_KEY not found in environment variables");
            return null;
        }

        String url = String.format(
                "%s?lat=%.4f&lon=%.4f&appid=%s&units=metric",
                BASE_URL, lat, lon, API_KEY);

        try {
            SEMAPHORE.acquire();
            try {
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(url))
                        .header("User-Agent", "WeatherStocksReport/1.0")
                        .GET()
                        .build();

                HttpResponse<String> response = HTTP_CLIENT.send(request,
                        HttpResponse.BodyHandlers.ofString());

                if (response.statusCode() != 200) {
                    System.err.println("Weather API returned status code: " + response.statusCode());
                    System.err.println("Response body: " + response.body());
                    return null;
                }

                return OBJECT_MAPPER.readValue(response.body(), WeatherResponse.class);

            } finally {
                SEMAPHORE.release();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Thread interrupted while waiting for semaphore: " + e.getMessage());
            return null;
        } catch (Exception e) {
            System.err.println("Failed to fetch weather: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}