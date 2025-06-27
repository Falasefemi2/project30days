package com.femiproject.library;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Book(
        @JsonProperty("title") String title,
        @JsonProperty("author") String author,
        @JsonProperty("isbn") String ISBN,
        @JsonProperty("genre") String genre,
        @JsonProperty("isBorrowed") boolean isBorrowed) {

    public Book(String title, String author, String ISBN, String genre) {
        this(title, author, ISBN, genre, false);
    }
}
