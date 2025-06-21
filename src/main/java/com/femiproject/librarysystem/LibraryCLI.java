package com.femiproject.librarysystem;

import java.util.Scanner;

public class LibraryCLI {

    private final LibrarySystem librarySystem;
    private final Scanner scanner;

    public LibraryCLI() {
        this.librarySystem = new LibrarySystem();
        this.scanner = new Scanner(System.in);
    }

    public void run() {
        System.out.println("🏛️  Welcome to the Library Management System!");
        System.out.println("=".repeat(50));

        boolean running = true;

        while (running) {
            printMenu();
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1" -> registerMember();
                case "2" -> addBook();
                case "3" -> borrowBook();
                case "4" -> returnBook();
                case "5" -> listAvailableBooks();
                case "6" -> viewBorrowedBooks();
                case "7" -> searchBook();
                case "8" -> listAllMembers();
                case "9" -> {
                    System.out.println("🚪 Exiting system...");
                    librarySystem.shutdown();
                    running = false;
                }
                default -> System.out.println("❌ Invalid choice. Please select 1-9.");
            }

            if (running) {
                System.out.println("\nPress Enter to continue...");
                scanner.nextLine();
            }
        }
        scanner.close();
    }

    private void printMenu() {
        System.out.println("\n📚 Library Management System - Main Menu");
        System.out.println("=".repeat(50));
        System.out.println("1. 👤 Register New Member");
        System.out.println("2. 📖 Add New Book");
        System.out.println("3. 📚 Borrow Book");
        System.out.println("4. 🔄 Return Book");
        System.out.println("5. 📖 List Available Books");
        System.out.println("6. 👤 View Member's Borrowed Books");
        System.out.println("7. 🔍 Search for Books");
        System.out.println("8. 👥 List All Members");
        System.out.println("9. 🚪 Exit System");
        System.out.println("=".repeat(50));
        System.out.print("Choose an option (1-9): ");
    }

    private void registerMember() {
        System.out.println("\n👤 Register New Member");
        System.out.println("-".repeat(25));
        System.out.print("Enter member name: ");
        String name = scanner.nextLine().trim();

        if (name.isEmpty()) {
            System.out.println("❌ Member name cannot be empty.");
            return;
        }

        Member member = new Member(name);
        librarySystem.registerMember(member);
        System.out.println("✅ Member registered with ID: " + member.getMemberId());
    }

    private void addBook() {
        System.out.println("\n📖 Add New Book");
        System.out.println("-".repeat(20));

        System.out.print("Enter book title: ");
        String title = scanner.nextLine().trim();
        if (title.isEmpty()) {
            System.out.println("❌ Book title cannot be empty.");
            return;
        }

        System.out.print("Enter author name: ");
        String author = scanner.nextLine().trim();
        if (author.isEmpty()) {
            System.out.println("❌ Author name cannot be empty.");
            return;
        }

        System.out.print("Enter genre: ");
        String genre = scanner.nextLine().trim();
        if (genre.isEmpty()) {
            genre = "Unknown";
        }

        System.out.print("Enter ISBN: ");
        String isbn = scanner.nextLine().trim();
        if (isbn.isEmpty()) {
            isbn = "N/A";
        }

        Book book = new Book(title, author, genre, isbn);
        librarySystem.addBook(book);
    }

    private void borrowBook() {
        System.out.println("\n📚 Borrow Book");
        System.out.println("-".repeat(15));

        System.out.print("Enter Member ID: ");
        String memberId = scanner.nextLine().trim();
        if (memberId.isEmpty()) {
            System.out.println("❌ Member ID cannot be empty.");
            return;
        }

        System.out.print("Enter Book ID: ");
        String bookId = scanner.nextLine().trim();
        if (bookId.isEmpty()) {
            System.out.println("❌ Book ID cannot be empty.");
            return;
        }

        librarySystem.borrowBook(memberId, bookId);
    }

    private void returnBook() {
        System.out.println("\n🔄 Return Book");
        System.out.println("-".repeat(15));

        System.out.print("Enter Member ID: ");
        String memberId = scanner.nextLine().trim();
        if (memberId.isEmpty()) {
            System.out.println("❌ Member ID cannot be empty.");
            return;
        }

        System.out.print("Enter Book ID: ");
        String bookId = scanner.nextLine().trim();
        if (bookId.isEmpty()) {
            System.out.println("❌ Book ID cannot be empty.");
            return;
        }

        librarySystem.returnBook(memberId, bookId);
    }

    private void listAvailableBooks() {
        librarySystem.listAvailableBooks();
    }

    private void viewBorrowedBooks() {
        System.out.println("\n👤 View Member's Borrowed Books");
        System.out.println("-".repeat(35));
        System.out.print("Enter Member ID: ");
        String memberId = scanner.nextLine().trim();

        if (memberId.isEmpty()) {
            System.out.println("❌ Member ID cannot be empty.");
            return;
        }

        librarySystem.viewBorrowedBooks(memberId);
    }

    private void searchBook() {
        System.out.println("\n🔍 Search for Books");
        System.out.println("-".repeat(20));
        System.out.print("Enter search keyword (title, author, or genre): ");
        String keyword = scanner.nextLine().trim();

        if (keyword.isEmpty()) {
            System.out.println("❌ Search keyword cannot be empty.");
            return;
        }

        librarySystem.searchBook(keyword);
    }

    private void listAllMembers() {
        librarySystem.listAllMembers();
    }

    public static void main(String[] args) {
        new LibraryCLI().run();
    }
}