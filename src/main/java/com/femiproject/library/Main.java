package com.femiproject.library;

import java.util.Optional;
import java.util.Scanner;

public class Main {

    private Scanner scanner;
    private Library library;

    public Main() {
        this.scanner = new Scanner(System.in);
        this.library = new Library();
    }

    public void start() {
        int choice;
        do {
            printMenu();
            System.out.print("Enter your choice: ");
            while (!scanner.hasNextInt()) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.next();
                System.out.print("Enter your choice: ");
            }
            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> addBook();
                case 2 -> removeBook();
                case 3 -> addMember();
                case 4 -> borrowBook();
                case 5 -> returnBook();
                case 6 -> searchBook();
                case 7 -> displayAllBooks();
                case 8 -> displayBorrowedBooksForMember();
                case 0 -> System.out.println("Exiting application. Thanks for using our service!");
                default -> System.out.println("Invalid option. Please choose a number from the menu.");
            }
            System.out.println();

        } while (choice != 0);

        library.saveData();
        scanner.close();
    }

    public void printMenu() {
        System.out.println("--- Library Management System ---");
        System.out.println("1. Add Book");
        System.out.println("2. Remove Book");
        System.out.println("3. Add Member");
        System.out.println("4. Borrow Book");
        System.out.println("5. Return Book");
        System.out.println("6. Search Book by Title");
        System.out.println("7. Display All Books");
        System.out.println("8. Display Books Borrowed by a Member");
        System.out.println("0. Quit");
        System.out.println("---------------------------------");
    }

    public void addBook() {
        System.out.print("Enter book title: ");
        String title = scanner.nextLine();
        System.out.print("Enter book author: ");
        String author = scanner.nextLine();
        System.out.print("Enter book ISBN: ");
        String isbn = scanner.nextLine();
        System.out.print("Enter book genre: ");
        String genre = scanner.nextLine();

        library.addBook(new Book(title, author, isbn, genre));
    }

    public void removeBook() {
        System.out.print("Enter book ISBN to remove: ");
        String isbn = scanner.nextLine();
        library.removeBook(isbn);
    }

    public void addMember() {
        System.out.print("Enter member name: ");
        String name = scanner.nextLine();
        library.addMember(new Member(name));
    }

    public void borrowBook() {
        System.out.print("Enter member ID: ");
        String memberId = scanner.nextLine();
        System.out.print("Enter book ISBN: ");
        String bookIsbn = scanner.nextLine();

        library.borrowBook(memberId, bookIsbn);
    }

    public void returnBook() {
        System.out.print("Enter member ID: ");
        String memberId = scanner.nextLine();
        System.out.print("Enter book ISBN: ");
        String bookIsbn = scanner.nextLine();

        library.returnBook(memberId, bookIsbn);
    }

    public void searchBook() {
        System.out.print("Enter book title to search: ");
        String title = scanner.nextLine();

        Optional<Book> foundBookOpt = library.searchBookByTitle(title);
        if (foundBookOpt.isPresent()) {
            System.out.println("Book found: " + foundBookOpt.get());
        } else {
            System.out.println("Book with title '" + title + "' not found.");
        }
    }

    public void displayAllBooks() {
        library.displayAllBooks();
    }

    public void displayBorrowedBooksForMember() {
        System.out.print("Enter member ID to display borrowed books: ");
        String memberId = scanner.nextLine();
        library.displayBorrowedBooks(memberId);
    }

    public static void main(String[] args) {
        new Main().start();
    }
}
