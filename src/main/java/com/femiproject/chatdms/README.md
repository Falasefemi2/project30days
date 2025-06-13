# ChatDMS - WebSocket-based Chat Application

A real-time chat application with private messaging capabilities built using Java WebSocket.

## Features

- Real-time messaging using WebSocket
- User registration system
- Private direct messaging (DM) between users
- Online user listing
- Simple command-based interface

## Prerequisites

- Java 8 or higher
- Maven for dependency management

## Setup

1. Clone the repository
2. Build the project:
   ```bash
   mvn clean install
   ```

## Running the Application

1. Start the server:

   ```bash
   java -cp target/chatdms-1.0-SNAPSHOT.jar com.femiproject.chatdms.ServerMain
   ```

2. Start a client:
   ```bash
   java -cp target/chatdms-1.0-SNAPSHOT.jar com.femiproject.chatdms.ClientMain
   ```

## Available Commands

- `/register <username>` - Register with a username
- `/dm <username> <message>` - Send a private message to a user
- `/list` - List all online users
- `quit` - Exit the chat

## Architecture

- `ChatServer.java` - WebSocket server implementation
- `ChatClient.java` - WebSocket client implementation
- `ServerMain.java` - Server entry point
- `ClientMain.java` - Client entry point
