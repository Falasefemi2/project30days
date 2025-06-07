package com.femiproject.inventorysystem;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class InventoryUtils implements InventoryInterface<Product> {
    private final ObjectMapper objectMapper;
    private final String inventoryFile = "inventory.json";

    public InventoryUtils() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    @Override
    public void save(List<Product> products) throws Exception {
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(inventoryFile), products);
        } catch (IOException e) {
            System.out.println("Error saving products: " + e.getMessage());
        }
    }

    @Override
    public List<Product> load() throws Exception {
        File file = new File(inventoryFile);
        if (!file.exists() || file.length() == 0) {
            return new ArrayList<>();
        }
        return objectMapper.readValue(file, new TypeReference<List<Product>>() {
        });
    }

}
