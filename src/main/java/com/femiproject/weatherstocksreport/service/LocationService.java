package com.femiproject.weatherstocksreport.service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.Semaphore;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.femiproject.weatherstocksreport.model.LocationResponse;

public class LocationService {

    private static final String URL = "http://ip-api.com/json";
    private static final Semaphore SEMAPHORE = new Semaphore(20);

    public static LocationResponse fetchLocation() {
        try {
            SEMAPHORE.acquire();
            try {
                HttpClient client = HttpClient.newHttpClient();
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(URL))
                        .GET()
                        .build();
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

                ObjectMapper mapper = new ObjectMapper();
                return mapper.readValue(response.body(), LocationResponse.class);
            } finally {
                SEMAPHORE.release();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Thread interrupted while waiting for semaphore");
            return null;
        } catch (Exception e) {
            System.out.println("Failed to fetch location: " + e.getMessage());
            return null;
        }
    }
}
