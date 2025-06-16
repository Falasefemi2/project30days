# QR Code Manager

A simple, menu-driven Java application for generating, viewing, and extracting information from QR codes using the ZXing library.

# Features

- Generate QR Codes: Create QR codes from custom text input
- View QR Codes: Display generated QR code files with file information
- Extract Information: Decode and read information from existing QR codes
- User-Friendly Interface: Interactive menu system with clear navigation
- Error Handling: Comprehensive error handling for various scenarios

# Prerequisites

Java 8 or higher
ZXing (Zebra Crossing) library dependencies:

core-3.x.x.jar
javase-3.x.x.jar

# Installation

- Clone or download the project
- Ensure you have Java 8+ installed
- Add the ZXing library dependencies to your project
- Compile the Java file

```bash
javac -cp ".:lib/*" com/femiproject/qrcodes/QRCodeManager.java
```

- Run the application:

```bash
java -cp ".:lib/*" com.femiproject.qrcodes.QRCodeManager
```

# Usage

When you run the application, you'll see a menu with the following options:

```bash
==================================================
           QR CODE MANAGER
==================================================
1. Generate QR Code
2. View QR Code
3. Extract information from QR Code
4. Exit
--------------------------------------------------
Enter an option:
--------------------------------------------------
```

# Option 1: Generate QR Code

- Enter text that you want to encode into a QR code
- Input multiple lines if needed
- Terminate input with a blank line
- The QR code will be saved as qr_code.png in the project directory

# Option 2: View QR Code

- Displays information about the generated QR code file
- Attempts to open the QR code image with your system's default image viewer
- Shows file size and location details

# Option 3: Extract Information from QR Code

- Reads and decodes the information from the generated QR code
- Displays the decoded text and format information
- Handles various QR code reading errors gracefully

# Option 4: Exit

- Safely exits the application

# Error Handling

- The application handles various error scenarios:
- File not found: When trying to view/read a QR code that doesn't exist
- Invalid input: When entering non-numeric menu choices
- Decoding errors: When QR code is corrupted or unreadable
- I/O errors: When file operations fail

# Technical Details

- QR Code Dimensions: 300x300 pixels
- Output Format: PNG
- Encoding: UTF-8 text encoding
- Library: Google ZXing (Zebra Crossing)

# Limitations

- Only supports PNG format for QR code generation
- Desktop file opening may not work on all systems
- Requires ZXing library dependencies to be properly configured

# Contributing

- Feel free to fork this project and submit pull requests for improvements. Some potential enhancements:

For additional support or questions, please refer to the ZXing documentation or create an issue in the project repository.
