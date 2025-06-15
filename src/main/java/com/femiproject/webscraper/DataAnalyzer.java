package com.femiproject.webscraper;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class DataAnalyzer {

    private JsonStorage jsonStorage;

    public DataAnalyzer() {
        this.jsonStorage = new JsonStorage();
    }

    public void analyzeData() {
        List<Article> articles = jsonStorage.loadArticles();

        if (articles.isEmpty()) {
            System.out.println("No articles to analyze. Please scrape some data first!");
            return;
        }

        System.out.println("\n=== DATA ANALYSIS REPORT ===");
        System.out.println("Total articles analyzed: " + articles.size());
        System.out.println();

        analyzeSourceDistribution(articles);
        analyzeCommonWords(articles);
        analyzeTitleLengths(articles);
        analyzeKeywordFrequency(articles);
        showRecentArticles(articles);
    }

    private void analyzeSourceDistribution(List<Article> articles) {
        System.out.println("=== ARTICLES BY SOURCE ===");

        Map<String, Integer> sourceCount = new HashMap<>();

        for (Article article : articles) {
            String source = article.getSource();
            sourceCount.put(source, sourceCount.getOrDefault(source, 0) + 1);
        }

        sourceCount.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .forEach(entry -> {
                    String source = entry.getKey();
                    int count = entry.getValue();
                    double percentage = (count * 100.0) / articles.size();
                    System.out.printf("%-15s: %3d articles (%.1f%%)%n", source, count, percentage);
                });
        System.out.println();
    }

    private void analyzeCommonWords(List<Article> articles) {
        System.out.println("=== MOST COMMON WORDS IN TITLES ===");

        Map<String, Integer> wordCount = new HashMap<>();

        Set<String> stopWords = new HashSet<>(Arrays.asList(
                "the", "a", "an", "and", "or", "but", "in", "on", "at", "to", "for", "of", "with", "by",
                "is", "are", "was", "were", "be", "been", "being", "have", "has", "had", "do", "does", "did",
                "will", "would", "could", "should", "may", "might", "can", "must", "shall", "this", "that",
                "these", "those", "i", "you", "he", "she", "it", "we", "they", "me", "him", "her", "us", "them"));

        for (Article article : articles) {
            String title = article.getTitle().toLowerCase();
            String[] words = title.split("[\\s\\p{Punct}]+");

            for (String word : words) {
                word = word.trim();

                if (word.length() > 2 && !stopWords.contains(word)) {
                    wordCount.put(word, wordCount.getOrDefault(word, 0) + 1);
                }
            }
        }

        wordCount.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(10)
                .forEach(e -> {
                    System.out.printf("%-15s: %d occurrences%n", e.getKey(), e.getValue());

                });
        System.out.println();
    }

    private void analyzeTitleLengths(List<Article> articles) {
        System.out.println("=== TITLE LENGTH ANALYSIS ===");

        List<Integer> lengths = articles.stream()
                .map(a -> a.getTitle().length())
                .toList();

        int minLength = lengths.stream().min(Integer::compareTo).orElse(0);
        int maxLength = lengths.stream().max(Integer::compareTo).orElse(0);
        double avgLength = lengths.stream().mapToInt(Integer::intValue).average().orElse(0.0);

        System.out.printf("Shortest title: %d characters%n", minLength);
        System.out.printf("Longest title: %d characters%n", maxLength);
        System.out.printf("Average title length: %.1f characters%n", avgLength);

        Map<String, Integer> lengthRanges = new HashMap<>();
        for (int length : lengths) {
            String range;
            if (length < 30) {
                range = "Short (< 30)";
            } else if (length < 60) {
                range = "Medium (30-60)";
            } else if (length < 90) {
                range = "Long (60-90)";
            } else {
                range = "Very Long (90+)";
            }
            lengthRanges.put(range, lengthRanges.getOrDefault(range, 0) + 1);
        }

        System.out.println("\nTitle Length Distribution:");
        lengthRanges.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .forEach(entry -> {
                    System.out.printf("%-20s: %d articles%n", entry.getKey(), entry.getValue());
                });
        System.out.println();
    }

    private void analyzeKeywordFrequency(List<Article> articles) {
        System.out.println("=== KEYWORD FREQUENCY ANALYSIS ===");

        String[] keywords = {
                "covid", "pandemic", "vaccine", "health",
                "economy", "economic", "market", "stock",
                "technology", "tech", "ai", "artificial intelligence",
                "climate", "environment", "green", "energy",
                "politics", "government", "election", "president",
                "war", "conflict", "ukraine", "russia",
                "china", "usa", "america", "europe"
        };

        Map<String, Integer> keywordCount = new HashMap<>();

        for (String keyword : keywords) {
            int count = 0;
            for (Article article : articles) {
                if (article.getTitle().equalsIgnoreCase(keyword)) {
                    count++;
                }
            }
            if (count > 0) {
                keywordCount.put(keyword, count);
            }
        }

        if (keywordCount.isEmpty()) {
            System.out.println("No specific keywords found in the current dataset.");
        } else {
            keywordCount.entrySet().stream()
                    .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                    .forEach(entry -> {
                        System.out.printf("%-20s: %d articles%n", entry.getKey(), entry.getValue());
                    });
        }
        System.out.println();
    }

    private void showRecentArticles(List<Article> articles) {
        System.out.println("=== RECENT ARTICLES (Latest 5) ===");

        List<Article> sortedArticles = articles.stream()
                .sorted((a1, a2) -> a2.getScrapedAt().compareTo(a1.getScrapedAt()))
                .toList();

        int limit = Math.min(5, sortedArticles.size());
        for (int i = 0; i < limit; i++) {
            Article article = sortedArticles.get(i);
            System.out.printf("%d. %s%n", i + 1, article.getTitle());
            System.out.printf("   Source: %s | Scraped: %s%n",
                    article.getSource(),
                    article.getScrapedAt().substring(0, 16));
            System.out.println();
        }
    }

    public void interactiveAnalysis() {
        Scanner scanner = new Scanner(System.in);
        List<Article> articles = jsonStorage.loadArticles();

        if (articles.isEmpty()) {
            System.out.println("No articles to analyze. Please scrape some data first!");
            return;
        }

        System.out.println("\n=== INTERACTIVE ANALYSIS ===");
        System.out.print("Enter a keyword to search for: ");
        String keyword = scanner.nextLine().trim();

        if (keyword.isEmpty()) {
            System.out.println("No keyword provided.");
            return;
        }

        List<Article> matchingArticles = articles.stream()
                .filter(article -> article.getTitle().equalsIgnoreCase(keyword)).toList();

        System.out.printf("%nFound %d articles containing '%s':%n", matchingArticles.size(), keyword);

        for (int i = 0; i < Math.min(matchingArticles.size(), 10); i++) {
            Article article = matchingArticles.get(i);
            System.out.printf("%d. %s%n", i + 1, article.getTitle());
            System.out.printf("   Source: %s%n", article.getSource());
        }

        if (matchingArticles.size() > 10) {
            System.out.printf("... and %d more articles%n", matchingArticles.size() - 10);
        }

    }
}
