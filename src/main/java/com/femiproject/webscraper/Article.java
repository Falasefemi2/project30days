package com.femiproject.webscraper;

public class Article {
    private String title;
    private String url;
    private String source;
    private String scrapedAt;

    public Article() {
    }

    public Article(String title, String url, String source, String scrapedAt) {
        this.title = title;
        this.url = url;
        this.source = source;
        this.scrapedAt = scrapedAt;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getScrapedAt() {
        return scrapedAt;
    }

    public void setScrapedAt(String scrapedAt) {
        this.scrapedAt = scrapedAt;
    }
}
