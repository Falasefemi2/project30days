# Library Management System

A Java-based library management system that allows users to manage books, members, and borrowing operations with persistent JSON storage.

## Features

### 📚 Book Management

- Add new books to the library
- Remove books (only if not currently borrowed)
- Search books by ISBN or title
- Display all books in the library
- Track book borrowing status

### 👥 Member Management

- Add new members to the system
- Generate unique member IDs automatically
- Track member borrowing history

### 🔄 Borrowing Operations

- Borrow books (updates book status to true)
- Return books (updates book status to false)
- View borrowed books for specific members
- Prevent borrowing of already borrowed books

### 💾 Data Persistence

- Automatic JSON file storage using Jackson
- Separate files for books (`new_books.json`) and members (`new_members.json`)
- Data automatically loads when system starts
- Automatic saving after each operation

## Project Structure

```
library/
├── Book.java          # Immutable record for book data
├── Member.java        # Member class with borrowing functionality
├── Library.java       # Main library management system
├── Main.java          # Application entry point and CLI
└── README.md          # This file
```

## Classes Overview

### Book (Record)

```java
public record Book(
    String title,
    String author,
    String ISBN,
    String genre,
    boolean isBorrowed
)
```

- Immutable data structure for book information
- JSON serialization support with Jackson annotations
- Constructor overload for creating available books

### Member

```java
public class Member {
    private String memberId;        // Auto-generated UUID
    private String name;            // Member name
    private List<Book> borrowedBooks; // Currently borrowed books
}
```

- Manages member information and borrowing history
- Methods: `borrowBook()`, `returnBook()`
- JSON serialization support

### Library

```java
public class Library {
    private List<Book> books;       // All books in library
    private List<Member> members;   // All registered members
}
```

- Core management system
- Handles all CRUD operations
- Manages data persistence
- Provides search and display functionality

## Usage Examples

### Adding Books

```java
Library library = new Library();
Book book = new Book("The Great Gatsby", "F. Scott Fitzgerald", "978-0743273565", "Fiction");
library.addBook(book);
```

### Adding Members

```java
Member member = new Member("John Doe");
library.addMember(member);
```

### Borrowing Books

```java
library.borrowBook("member-id", "978-0743273565");
```

### Returning Books

```java
library.returnBook("member-id", "978-0743273565");
```

### Searching Books

```java
Optional<Book> book = library.findBookByIsbn("978-0743273565");
Optional<Book> bookByTitle = library.searchBookByTitle("The Great Gatsby");
```

### Displaying Information

```java
library.displayAllBooks();
library.displayBorrowedBooks("member-id");
```

## Data Storage

The system uses JSON files for persistent storage:

- **Books**: `new_books.json`
- **Members**: `new_members.json`

Files are automatically created and updated when:

- The library system starts (loads existing data)
- Books are added or removed
- Members are added
- Books are borrowed or returned

## Dependencies

- **Jackson**: For JSON serialization/deserialization
- **Java 17+**: For record support

## Error Handling

The system includes comprehensive error handling:

- Validates input parameters
- Checks for duplicate books (by ISBN)
- Prevents borrowing of already borrowed books
- Prevents removal of borrowed books
- Validates member and book existence before operations
- Graceful handling of file I/O errors

## Running the Application

1. Ensure you have Java 17+ installed
2. Add Jackson dependency to your project
3. Run `Main.java` to start the library management system
4. Use the CLI interface to interact with the system

## File Format Examples

### Books JSON

```json
[
  {
    "title": "The Great Gatsby",
    "author": "F. Scott Fitzgerald",
    "isbn": "978-0743273565",
    "genre": "Fiction",
    "isBorrowed": false
  }
]
```

### Members JSON

```json
[
  {
    "memberId": "550e8400-e29b-41d4-a716-446655440000",
    "name": "John Doe",
    "borrowedBooks": []
  }
]
```
