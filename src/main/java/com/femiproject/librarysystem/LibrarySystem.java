package com.femiproject.librarysystem;

import java.util.List;

public class LibrarySystem {

    private final Librarian librarian;
    private final MemberManager memberManager;
    private static final int MAX_BORROW_LIMIT = 3;

    public LibrarySystem() {
        this.librarian = new Librarian();
        this.memberManager = new MemberManager();
    }

    public void borrowBook(String memberId, String bookId) {
        Member member = memberManager.getMemberById(memberId);
        if (member == null) {
            System.out.println("❌ Member not found with ID: " + memberId);
            return;
        }

        Book book = librarian.getBookByBookId(bookId);
        if (book == null) {
            System.out.println("❌ Book not found with ID: " + bookId);
            return;
        }

        if (!book.isAvailable()) {
            System.out.println("❌ Book is already borrowed: " + book.getTitle());
            return;
        }

        if (member.getBorrowedBooks().size() >= MAX_BORROW_LIMIT) {
            System.out.println("❌ Borrow limit reached. Maximum books allowed: " + MAX_BORROW_LIMIT);
            return;
        }

        member.getBorrowedBooks().add(book);
        book.setAvailable(false);

        memberManager.saveMembers();
        librarian.saveBooks();

        System.out.println("✅ Book borrowed successfully: " + book.getTitle() + " by " + member.getName());
    }

    public void returnBook(String memberId, String bookId) {
        Member member = memberManager.getMemberById(memberId);
        if (member == null) {
            System.out.println("❌ Member not found with ID: " + memberId);
            return;
        }

        List<Book> borrowedBooks = member.getBorrowedBooks();
        Book bookToReturn = borrowedBooks.stream()
                .filter(b -> b.getBookId().equals(bookId))
                .findFirst()
                .orElse(null);

        if (bookToReturn == null) {
            System.out.println("❌ Member did not borrow this book or book ID not found: " + bookId);
            return;
        }

        borrowedBooks.remove(bookToReturn);
        Book bookInCatalog = librarian.getBookByBookId(bookId);
        if (bookInCatalog != null) {
            bookInCatalog.setAvailable(true);
        }

        memberManager.saveMembers();
        librarian.saveBooks();

        System.out.println("✅ Book returned successfully: " + bookToReturn.getTitle() + " by " + member.getName());
    }

    public void registerMember(Member member) {
        if (member == null) {
            System.out.println("❌ Cannot register null member.");
            return;
        }
        memberManager.addMember(member);
        System.out.println("✅ Member registered successfully: " + member.getName());
    }

    public void addBook(Book book) {
        if (book == null) {
            System.out.println("❌ Cannot add null book.");
            return;
        }
        librarian.addBook(book);
        System.out.println("✅ Book added successfully: " + book.getTitle());
    }

    public void listAvailableBooks() {
        List<Book> availableBooks = librarian.getAvailableBooks();

        if (availableBooks.isEmpty()) {
            System.out.println("📚 No available books at the moment.");
            return;
        }

        System.out.println("\n📚 Available Books:");
        System.out.println("=".repeat(80));
        availableBooks.forEach(book -> {
            System.out.printf("📖 ID: %s | Title: %s | Author: %s | Genre: %s%n",
                    book.getBookId(), book.getTitle(), book.getAuthor(), book.getGenre());
        });
        System.out.println("=".repeat(80));
    }

    public void viewBorrowedBooks(String memberId) {
        Member member = memberManager.getMemberById(memberId);
        if (member == null) {
            System.out.println("❌ Member not found with ID: " + memberId);
            return;
        }

        List<Book> borrowed = member.getBorrowedBooks();
        if (borrowed.isEmpty()) {
            System.out.println("📚 " + member.getName() + " has no books borrowed.");
            return;
        }

        System.out.println("\n📚 Books borrowed by " + member.getName() + ":");
        System.out.println("=".repeat(80));
        borrowed.forEach(book -> {
            System.out.printf("📖 ID: %s | Title: %s | Author: %s | Genre: %s%n",
                    book.getBookId(), book.getTitle(), book.getAuthor(), book.getGenre());
        });
        System.out.println("=".repeat(80));
    }

    public void searchBook(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            System.out.println("❌ Please enter a valid search keyword.");
            return;
        }

        List<Book> foundBooks = librarian.searchForBook(keyword.toLowerCase());
        if (foundBooks.isEmpty()) {
            System.out.println("❌ No books found for keyword: " + keyword);
            return;
        }

        System.out.println("\n📚 Search Results for '" + keyword + "':");
        System.out.println("=".repeat(80));
        foundBooks.forEach(book -> {
            System.out.printf("📖 ID: %s | Title: %s | Author: %s | Genre: %s | Available: %s%n",
                    book.getBookId(), book.getTitle(), book.getAuthor(), book.getGenre(),
                    book.isAvailable() ? "✅ Yes" : "❌ No");
        });
        System.out.println("=".repeat(80));
    }

    public void listAllMembers() {
        List<Member> allMembers = memberManager.getAllMembers();
        if (allMembers.isEmpty()) {
            System.out.println("👥 No members registered yet.");
            return;
        }

        System.out.println("\n👥 Registered Members:");
        System.out.println("=".repeat(80));
        allMembers.forEach(member -> {
            System.out.printf("👤 ID: %s | Name: %s | Books Borrowed: %d%n",
                    member.getMemberId(), member.getName(), member.getBorrowedBooks().size());
        });
        System.out.println("=".repeat(80));
    }

    public void shutdown() {
        System.out.println("💾 Saving data before shutdown...");
        memberManager.saveMembers();
        librarian.saveBooks();
        System.out.println("✅ Data saved successfully. Goodbye!");
    }

    public Librarian getLibrarian() {
        return librarian;
    }

    public MemberManager getMemberManager() {
        return memberManager;
    }
}