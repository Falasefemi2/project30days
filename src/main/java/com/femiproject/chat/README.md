# 🔌 Java CLI Chat App using WebSockets

A lightweight command-line chat application built in Java using [java-websocket](org.java-websocket). This project demonstrates real-time communication using WebSocket protocol with a multiclient CLI interface.

## ✨ Features

- 📡 Real-time messaging via WebSocket
- 👥 Multiple clients can connect to the server
- 🧍 Personalized usernames per client
- 🔌 Graceful connection and disconnection
- 💬 Simple CLI-based interaction
- ⚙️ Configurable server address and port

---

## 📁 Project Structure

```bash
src/
├── com.femiproject.chat/
│   ├── Server.java         # WebSocket server logic
│   ├── ServerMain.java     # Entry point to start the server
│   ├── Client.java         # WebSocket client implementation
│   └── ClientMain.java     # Entry point to start a client
```

## 🧪 Example Usage

## Start the server

```bash
java com.femiproject.chat.ServerMain
Chat server running on port 8887
Press enter to stop.


## Start a client

java com.femiproject.chat.ClientMain
Enter your username:
Femi
Enter server address (default: localhost):
localhost
Enter server port (default: 8887):

Connecting to ws://localhost:8887....
Connected to the server
Type your message (or 'quit' to exit)
```
