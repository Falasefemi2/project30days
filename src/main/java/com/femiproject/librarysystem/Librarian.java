package com.femiproject.librarysystem;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class Librarian {

    private List<Book> books;
    private final String library_file = "library.json";
    private final ObjectMapper objectMapper;

    public Librarian() {
        this.books = new ArrayList<>();
        this.objectMapper = new ObjectMapper()
                .enable(SerializationFeature.INDENT_OUTPUT);
        loadBooks();
        initializeDefaultBooks();
    }

    private void loadBooks() {
        try {
            File file = new File(library_file);
            if (file.exists() && file.length() > 0) {
                List<Book> loadedBooks = objectMapper.readValue(file, new TypeReference<List<Book>>() {
                });
                books = loadedBooks;
                System.out.println("Books loaded successfully from " + library_file);
            } else {
                System.out.println("No existing library file found. Starting with empty library.");
            }
        } catch (IOException e) {
            System.err.println("Error loading books: " + e.getMessage());
            books = new ArrayList<>();
        }
    }

    private void initializeDefaultBooks() {
        if (books.isEmpty()) {
            books.add(new Book("To Kill a Mockingbird", "Harper Lee", "Fiction", "978-0-06-112008-4"));
            books.add(new Book("1984", "George Orwell", "Dystopian Fiction", "978-0-452-28423-4"));
            books.add(new Book("Pride and Prejudice", "Jane Austen", "Romance", "978-0-14-143951-8"));
            books.add(new Book("The Great Gatsby", "F. Scott Fitzgerald", "Fiction", "978-0-7432-7356-5"));
            books.add(new Book("The Catcher in the Rye", "J.D. Salinger", "Fiction", "978-0-316-76948-0"));
            saveBooks();
            System.out.println("Default books added to library.");
        }
    }

    public void saveBooks() {
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(library_file), books);
            System.out.println("Books saved successfully to " + library_file);
        } catch (IOException e) {
            System.err.println("Error saving books: " + e.getMessage());
        }
    }

    public void addBook(Book book) {
        if (book != null) {
            books.add(book);
            saveBooks();
            System.out.println("Book added: " + book.getTitle());
        }
    }

    public void removeBook(String bookId) {
        boolean removed = books.removeIf(b -> b.getBookId().equals(bookId));
        if (removed) {
            saveBooks();
            System.out.println("Book removed successfully.");
        } else {
            System.out.println("Book not found.");
        }
    }

    public Book getBookByBookId(String bookId) {
        return books.stream()
                .filter(b -> b.getBookId().equals(bookId))
                .findFirst()
                .orElse(null);
    }

    public List<Book> searchForBook(String word) {
        if (word == null || word.trim().isEmpty()) {
            return new ArrayList<>();
        }
        String searchTerm = word.toLowerCase().trim();
        return books.stream()
                .filter(b -> b.getTitle().toLowerCase().contains(searchTerm) ||
                        b.getAuthor().toLowerCase().contains(searchTerm) ||
                        b.getGenre().toLowerCase().contains(searchTerm))
                .toList();
    }

    public List<Book> listAllBooks() {
        return new ArrayList<>(books);
    }

    public List<Book> getAvailableBooks() {
        return books.stream()
                .filter(Book::isAvailable)
                .toList();
    }
}