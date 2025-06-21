package com.femiproject.librarysystem;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Member {

    @JsonProperty
    private String memberId;

    @JsonProperty
    private String name;

    @JsonProperty
    private List<Book> borrowedBooks;

    @JsonCreator
    public Member(
            @JsonProperty("memberId") String memberId,
            @JsonProperty("name") String name,
            @JsonProperty("borrowedBooks") List<Book> borrowedBooks) {
        this.memberId = memberId;
        this.name = name;
        this.borrowedBooks = borrowedBooks != null ? borrowedBooks : new ArrayList<>();
    }

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

    public void setName(String name) {
        this.name = name;
    }

    public List<Book> getBorrowedBooks() {
        return borrowedBooks;
    }

    public void setBorrowedBooks(List<Book> borrowedBooks) {
        this.borrowedBooks = borrowedBooks != null ? borrowedBooks : new ArrayList<>();
    }

    @Override
    public String toString() {
        return String.format("Member{id='%s', name='%s', borrowedBooks=%d}",
                memberId, name, borrowedBooks.size());
    }
}