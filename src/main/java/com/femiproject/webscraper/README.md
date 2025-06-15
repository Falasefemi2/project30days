# Java Web Scraper

A flexible web scraping tool built in Java that allows you to extract articles from any website using custom CSS selectors.

## Features

- Scrape articles from any website using custom CSS selectors
- Built-in support for BBC News and Hacker News
- Save scraped articles to JSON storage
- Analyze scraped data
- User-friendly command-line interface

## Prerequisites

- Java 17 or higher
- Maven

## Dependencies

- JSoup (HTML parsing)
- Jackson (JSON handling)

## Setup

1. Clone the repository
2. Navigate to the project directory
3. Build the project:

```bash
mvn clean install
```

## Usage

Run the application:

```bash
java -jar target/webscraper-1.0-SNAPSHOT.jar
```

### Menu Options

1. **Scrape BBC News**: Extracts articles from BBC News
2. **Scrape Hacker News**: Extracts articles from Hacker News
3. **Scrape Custom Website**: Scrape any website using custom selectors
4. **View Saved Articles**: Display previously scraped articles
5. **Analyze Data**: View statistics about scraped articles
6. **Clear Saved Data**: Remove all saved articles
7. **Exit**: Close the application

### Scraping Custom Websites

When using option 3, you'll need to provide:

- Website URL
- CSS selector for article titles
- CSS selector for article links
- Source name

Example CSS selectors:

- Title: `h1`, `.article-title`, `#main-heading`
- Links: `a`, `.article-link`, `[href]`

## Project Structure

- `WebScraper.java`: Main application class
- `Article.java`: Article data model
- `JsonStorage.java`: Handles article storage
- `DataAnalyzer.java`: Analyzes scraped data

## Notes

- Some websites may block scraping attempts
- Always respect website terms of service and robots.txt
- Use appropriate delays between requests when scraping
- CSS selectors may need to be updated if website structure changes
