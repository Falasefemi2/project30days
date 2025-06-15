package com.femiproject.webscraper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class JsonStorage {

    private static final String DATA_FILE = "data/scraped_data.json";
    private ObjectMapper objectMapper;

    public JsonStorage() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

        createFolder();
    }

    private void createFolder() {
        try {
            File dataDir = new File("data");
            if (!dataDir.exists()) {
                dataDir.mkdirs();
                System.out.println("Folder created");
            }
        } catch (Exception e) {
            System.err.println("Error creating data directory: " + e.getMessage());
        }
    }

    public void saveArticles(List<Article> newArticles) {
        try {
            List<Article> existingArticles = loadArticles();
            existingArticles.addAll(newArticles);
            List<Article> uniqueArticles = removeDuplicates(existingArticles);

            objectMapper.writeValue(new File(DATA_FILE), uniqueArticles);

            System.out.println("Saved " + newArticles.size() + " new articles");
            System.out.println("Total articles in database: " + uniqueArticles.size());

        } catch (IOException e) {
            System.err.println("Error saving articles: " + e.getMessage());
        }
    }

    public List<Article> loadArticles() {
        try {
            File file = new File(DATA_FILE);
            if (!file.exists()) {
                return new ArrayList<>();
            }

            TypeReference<List<Article>> typeRef = new TypeReference<List<Article>>() {
            };
            return objectMapper.readValue(file, typeRef);
        } catch (IOException e) {
            System.err.println("Error loading articles: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    private List<Article> removeDuplicates(List<Article> articles) {
        List<Article> uniqueArticles = new ArrayList<>();

        for (Article article : articles) {
            boolean isDuplicate = false;

            for (Article existing : uniqueArticles) {
                // Check if title and source match (case-insensitive)
                if (existing.getTitle().equalsIgnoreCase(article.getTitle()) &&
                        existing.getSource().equals(article.getSource())) {
                    isDuplicate = true;
                    break;
                }
            }
            if (!isDuplicate) {
                uniqueArticles.add(article);
            }
        }

        return uniqueArticles;
    }

    public void clearData() {
        try {
            File file = new File(DATA_FILE);
            if (file.exists()) {
                file.delete();
                System.out.println("Data file deleted successfully");
            }
        } catch (Exception e) {
            System.err.println("Error clearing data: " + e.getMessage());
        }
    }

    public int getArticleCount() {
        return loadArticles().size();
    }

    public List<Article> getArticlesBySource(String source) {
        List<Article> allArticles = loadArticles();
        List<Article> filteredArticles = new ArrayList<>();

        for (Article article : allArticles) {
            if (article.getSource().equalsIgnoreCase(source)) {
                filteredArticles.add(article);
            }
        }
        return filteredArticles;
    }

    public List<Article> searchArticles(String keyword) {
        List<Article> allArticles = loadArticles();
        List<Article> matchingArticles = new ArrayList<>();

        String lowerKeyword = keyword.toLowerCase();

        for (Article article : allArticles) {
            if (article.getTitle().equalsIgnoreCase(lowerKeyword)) {
                matchingArticles.add(article);
            }
        }
        return matchingArticles;
    }
}
