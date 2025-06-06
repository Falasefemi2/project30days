# Contact Book CLI

A simple Command Line Interface (CLI) application in Java for managing contacts.  
Supports adding, updating, deleting, and searching contacts, with the ability to persist data using JSON or CSV file storage.

---

## Features

- Add new contacts with details: ID, Name, Phone, Email, and Address
- Update existing contacts
- Delete contacts by ID
- Search contacts by any attribute (ID, name, phone, email)
- List all saved contacts
- Supports JSON storage (using Jackson library)
- Supports CSV storage (using OpenCSV library)
- Modular and extendable architecture with separate layers for model, service, storage, and CLI

---

## Technologies Used

- Java 11+
- [Jackson](https://github.com/FasterXML/jackson) for JSON serialization/deserialization
- [OpenCSV](http://opencsv.sourceforge.net/) for CSV handling
- Maven or Gradle (optional) for dependency management

---

## Usage

Once the app is running, you’ll be prompted with a menu:

Welcome to Contact Book CLI!
Choose storage format:

1. JSON
2. CSV

Menu:

1. Add Contact
2. Update Contact
3. Delete Contact
4. Search Contact
5. List All Contacts
6. Exit
