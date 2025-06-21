package com.femiproject.librarysystem;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Book {
    @JsonProperty
    private String bookId;

    @JsonProperty
    private String title;

    @JsonProperty
    private String author;

    @JsonProperty
    private String genre;

    @JsonProperty
    private String isbn;

    @JsonProperty
    private boolean available;

    @JsonCreator
    public Book(
            @JsonProperty String bookId,
            @JsonProperty String title,
            @JsonProperty String author,
            @JsonProperty String genre,
            @JsonProperty String isbn,
            @JsonProperty boolean available) {
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.isbn = isbn;
        this.available = available;
    }

    public Book(String title, String author, String genre, String isbn) {
        this.bookId = UUID.randomUUID().toString();
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.isbn = isbn;
        this.available = true;
    }

    public String getBookId() {
        return bookId;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getGenre() {
        return genre;
    }

    public String getIsbn() {
        return isbn;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    @Override
    public String toString() {
        return String.format("Book{id='%s', title='%s', author='%s', genre='%s', isbn='%s', available=%b}",
                bookId, title, author, genre, isbn, available);
    }
}
