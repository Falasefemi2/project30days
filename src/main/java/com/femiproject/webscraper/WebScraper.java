package com.femiproject.webscraper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class WebScraper {
    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36";
    private JsonStorage jsonStorage;
    private DataAnalyzer analyzer;

    public WebScraper() {
        this.jsonStorage = new JsonStorage();
        this.analyzer = new DataAnalyzer();
    }

    public List<Article> scrapeWebsite(String url, String titleSelector, String linkSelector, String sourceName) {
        List<Article> articles = new ArrayList<>();

        try {
            System.out.println("Connecting to: " + url);

            Document doc = Jsoup.connect(url)
                    .userAgent(USER_AGENT)
                    .timeout(10000)
                    .get();

            System.out.println("Successfully connected! Parsing articles...");

            Elements elements = doc.select(titleSelector);
            String currentTime = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);

            for (Element element : elements) {
                try {
                    String title = element.text().trim();

                    if (title.isEmpty())
                        continue;

                    String articleUrl = "";
                    Element linkElement = element.select(linkSelector).first();
                    if (linkElement != null) {
                        articleUrl = linkElement.attr("href");
                        if (articleUrl.startsWith("/")) {
                            articleUrl = url + articleUrl;
                        }
                    }

                    Article article = new Article(title, articleUrl, sourceName, currentTime);
                    articles.add(article);

                } catch (Exception e) {
                    System.out.println("Error processing element: " + e.getMessage());
                }
            }

            System.out.println("Found " + articles.size() + " articles");

        } catch (IOException e) {
            System.err.println("Error connecting to website: " + e.getMessage());
            System.err.println("This could be due to network issues or website changes");
        }

        return articles;
    }

    public void displayMenu() {
        System.out.println("\n=== Java Web Scraper ===");
        System.out.println("1. Scrape BBC News");
        System.out.println("2. Scrape Hacker News");
        System.out.println("3. Scrape Custom Website");
        System.out.println("4. View saved articles");
        System.out.println("5. Analyze data");
        System.out.println("6. Clear saved data");
        System.out.println("7. Exit");
        System.out.print("Choose an option: ");
    }

    public static void main(String[] args) {
        WebScraper scraper = new WebScraper();
        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to Java Web Scraper!");
        System.out.println("This tool will help you scrape and analyze news articles.");

        while (true) {
            scraper.displayMenu();

            try {
                int choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1 -> {
                        List<Article> bbcArticles = scraper.scrapeWebsite(
                                "https://www.bbc.com/news",
                                "h3[data-testid='card-headline']",
                                "a",
                                "BBC News");
                        if (!bbcArticles.isEmpty()) {
                            scraper.jsonStorage.saveArticles(bbcArticles);
                            System.out
                                    .println("Successfully scraped and saved " + bbcArticles.size() + " BBC articles!");
                        }
                    }

                    case 2 -> {
                        List<Article> hnArticles = scraper.scrapeWebsite(
                                "https://news.ycombinator.com",
                                "span.titleline > a",
                                "a",
                                "Hacker News");
                        if (!hnArticles.isEmpty()) {
                            scraper.jsonStorage.saveArticles(hnArticles);
                            System.out.println(
                                    "Successfully scraped and saved " + hnArticles.size() + " Hacker News articles!");
                        }
                    }

                    case 3 -> {
                        System.out.print("Enter website URL: ");
                        String url = scanner.nextLine();
                        System.out.print("Enter title selector (CSS selector): ");
                        String titleSelector = scanner.nextLine();
                        System.out.print("Enter link selector (CSS selector): ");
                        String linkSelector = scanner.nextLine();
                        System.out.print("Enter source name: ");
                        String sourceName = scanner.nextLine();

                        List<Article> customArticles = scraper.scrapeWebsite(url, titleSelector, linkSelector,
                                sourceName);
                        if (!customArticles.isEmpty()) {
                            scraper.jsonStorage.saveArticles(customArticles);
                            System.out
                                    .println("Successfully scraped and saved " + customArticles.size() + " articles!");
                        }
                    }

                    case 4 -> {
                        List<Article> savedArticles = scraper.jsonStorage.loadArticles();
                        if (savedArticles.isEmpty()) {
                            System.out.println("No articles found. Try scraping first!");
                        } else {
                            System.out.println("\n=== Saved Articles ===");
                            for (int i = 0; i < Math.min(savedArticles.size(), 10); i++) {
                                System.out.println((i + 1) + ". " + savedArticles.get(i).getTitle());
                                System.out.println("   Source: " + savedArticles.get(i).getSource());
                                System.out.println("   URL: " + savedArticles.get(i).getUrl());
                                System.out.println();
                            }
                            if (savedArticles.size() > 10) {
                                System.out.println("... and " + (savedArticles.size() - 10) + " more articles");
                            }
                        }
                    }

                    case 5 -> scraper.analyzer.analyzeData();

                    case 6 -> {
                        scraper.jsonStorage.clearData();
                        System.out.println("All saved data cleared!");
                    }

                    case 7 -> {
                        System.out.println("Thanks for using Java Web Scraper!");
                        return;
                    }

                    default -> System.out.println("Invalid option. Please try again.");
                }

            } catch (Exception e) {
                System.err.println("Error: " + e.getMessage());
                scanner.nextLine();
            }

            System.out.println("\nPress Enter to continue...");
            scanner.nextLine();
        }
    }
}