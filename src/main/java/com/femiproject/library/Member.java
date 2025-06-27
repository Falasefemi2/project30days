package com.femiproject.library;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Member {
    @JsonProperty("memberId")
    private String memberId;
    @JsonProperty("name")
    private String name;
    @JsonProperty("borrowedBooks")
    private List<Book> borrowedBooks;

    @JsonCreator
    public Member(String name) {
        this.memberId = UUID.randomUUID().toString();
        this.name = name;
        this.borrowedBooks = new ArrayList<>();
    }

    public String getMemberId() {
        return memberId;
    }

    public String getName() {
        return name;
    }

    public List<Book> getBorrowedBooks() {
        return borrowedBooks;
    }

    public void borrowBook(Book book) {
        if (book != null && !book.isBorrowed()) {
            Book borrowedBook = new Book(book.title(), book.author(), book.ISBN(), book.genre(), true);
            this.borrowedBooks.add(borrowedBook);
        }
    }

    public void returnBook(Book book) {
        if (book != null && this.borrowedBooks.remove(book)) {
        }
    }

    @Override
    public String toString() {
        return "Member [memberId=" + memberId + ", name=" + name + ", borrowedBooks=" + borrowedBooks + "]";
    }
}
