package com.femiproject.library;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Library {

    private static final String BOOKS_FILE = "new_books.json";
    private static final String MEMBERS_FILE = "new_members.json";
    private static final ObjectMapper objectMapper = new ObjectMapper()
            .enable(SerializationFeature.INDENT_OUTPUT);

    private List<Book> books;
    private List<Member> members;

    public Library() {
        this.books = new ArrayList<>();
        this.members = new ArrayList<>();
        loadData();
    }

    private void loadData() {
        try {
            File booksFile = new File(BOOKS_FILE);
            if (booksFile.exists() && booksFile.length() > 0) {
                books = objectMapper.readValue(booksFile, new TypeReference<List<Book>>() {
                });
                System.out.println("Loaded " + books.size() + " books from " + BOOKS_FILE);
            }

            File membersFile = new File(MEMBERS_FILE);
            if (membersFile.exists()) {
                members = objectMapper.readValue(membersFile, new TypeReference<List<Member>>() {
                });
                System.out.println("Loaded " + members.size() + " members from " + MEMBERS_FILE);
            }
        } catch (IOException e) {
            System.err.println("Error loading data: " + e.getMessage());
        }
    }

    public void saveData() {
        try {
            objectMapper.writeValue(new File(BOOKS_FILE), books);
            System.out.println("Saved " + books.size() + " books to " + BOOKS_FILE);

            objectMapper.writeValue(new File(MEMBERS_FILE), members);
            System.out.println("Saved " + members.size() + " members to " + MEMBERS_FILE);
        } catch (IOException e) {
            System.err.println("Error saving data: " + e.getMessage());
        }
    }

    public Optional<Book> findBookByIsbn(String isbn) {
        if (isbn == null || isbn.isEmpty()) {
            return Optional.empty();
        }

        return books.stream()
                .filter(b -> b.ISBN().equalsIgnoreCase(isbn.trim()))
                .findFirst();
    }

    public Optional<Member> findMemberById(String memberId) {
        if (memberId == null || memberId.trim().isEmpty()) {
            return Optional.empty();
        }

        return members.stream()
                .filter(m -> m.getMemberId().equals(memberId.trim()))
                .findFirst();
    }

    public void addBook(Book book) {
        if (book != null) {
            if (findBookByIsbn(book.ISBN()).isPresent()) {
                System.out.println("Error: Book with ISBN " + book.ISBN() + " already exists.");
            } else {
                books.add(book);
                saveData();
                System.out.println("Book added successfully: " + book.title());
            }
        } else {
            System.out.println("Error: Cannot add a null book.");
        }
    }

    public void removeBook(String isbn) {
        Optional<Book> bookOpt = findBookByIsbn(isbn);
        if (bookOpt.isEmpty()) {
            System.out.println("Error: Book with ISBN " + isbn + " is not available to remove.");
        } else {
            Book bookToRemove = bookOpt.get();
            if (bookToRemove.isBorrowed()) {
                System.out.println("Error: Book '" + bookToRemove.title()
                        + "' cannot be removed because it is currently borrowed.");
            } else {
                books.remove(bookToRemove);
                saveData();
                System.out.println("Book '" + bookToRemove.title() + "' removed successfully.");
            }
        }
    }

    public void addMember(Member member) {
        if (member != null) {
            if (members.stream().anyMatch(m -> m.getName().equalsIgnoreCase(member.getName()))) {
                System.out.println("Warning: Member with name '" + member.getName() + "' already exists.");
            }
            members.add(member);
            saveData();
            System.out
                    .println("Member added successfully: " + member.getName() + " (ID: " + member.getMemberId() + ")");
        } else {
            System.out.println("Error: Cannot add a null member.");
        }
    }

    public void borrowBook(String memberId, String isbn) {
        Optional<Member> memberOpt = findMemberById(memberId);
        Optional<Book> bookOpt = findBookByIsbn(isbn);

        if (memberOpt.isEmpty()) {
            System.out.println("Error: Member with ID " + memberId + " not found.");
            return;
        }
        if (bookOpt.isEmpty()) {
            System.out.println("Error: Book with ISBN " + isbn + " not found.");
            return;
        }

        Member member = memberOpt.get();
        Book book = bookOpt.get();

        if (book.isBorrowed()) {
            System.out.println("Error: Book '" + book.title() + "' is already borrowed.");
        } else {
            member.borrowBook(book);
            saveData();
            System.out.println("Book '" + book.title() + "' borrowed by " + member.getName() + " successfully.");
        }
    }

    public void returnBook(String memberId, String isbn) {
        Optional<Member> memberOpt = findMemberById(memberId);
        Optional<Book> bookOpt = findBookByIsbn(isbn);

        if (memberOpt.isEmpty()) {
            System.out.println("Error: Member with ID " + memberId + " not found.");
            return;
        }
        if (bookOpt.isEmpty()) {
            System.out.println("Error: Book with ISBN " + isbn + " not found.");
            return;
        }

        Member member = memberOpt.get();
        Book book = bookOpt.get();

        if (!book.isBorrowed()) {
            System.out.println("Error: Book '" + book.title() + "' was not borrowed.");
        } else if (!member.getBorrowedBooks().contains(book)) {
            System.out.println(
                    "Error: Book '" + book.title() + "' was not borrowed by member '" + member.getName() + "'.");
        } else {
            member.returnBook(book);
            saveData();
            System.out.println("Book '" + book.title() + "' returned by " + member.getName() + " successfully.");
        }
    }

    public Optional<Book> searchBookByTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            System.out.println("Invalid title for search.");
            return Optional.empty();
        }

        return books.stream()
                .filter(b -> b.title().equalsIgnoreCase(title.trim()))
                .findFirst();
    }

    public void displayAllBooks() {
        if (books.isEmpty()) {
            System.out.println("No books in the library yet.");
            return;
        }
        System.out.println("\n--- All Books in Library ---");
        books.forEach(System.out::println);
        System.out.println("---------------------------\n");
    }

    public void displayBorrowedBooks(String memberId) {
        Optional<Member> memberOpt = findMemberById(memberId);
        if (memberOpt.isEmpty()) {
            System.out.println("Error: Member with ID " + memberId + " not found.");
            return;
        }
        Member member = memberOpt.get();
        List<Book> borrowedBooks = member.getBorrowedBooks();
        if (borrowedBooks.isEmpty()) {
            System.out.println("Member " + member.getName() + " (ID: " + memberId + ") has no borrowed books.");
        } else {
            System.out.println("\n--- Books borrowed by " + member.getName() + " (ID: " + memberId + ") ---");
            borrowedBooks.forEach(System.out::println);
            System.out.println("-------------------------------------------\n");
        }
    }
}
