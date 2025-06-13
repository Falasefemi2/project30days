package com.femiproject.chatdms;

import java.net.InetSocketAddress;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

public class ChatServer extends WebSocketServer {

    private final Map<String, WebSocket> userConnections = new ConcurrentHashMap<>();
    private final Map<WebSocket, String> connectionUsernames = new ConcurrentHashMap<>();

    public ChatServer(int port) {
        super(new InetSocketAddress(port));
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        System.out.println("Client connected: " + conn.getRemoteSocketAddress());
        conn.send("Welcome!! Use /register <name> to begin chat with freinds");
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        removeUser(conn);
        System.out.println("Connection closed: " + conn.getRemoteSocketAddress());
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        String sender = connectionUsernames.get(conn);

        if (message.startsWith("/register ")) {
            String[] parts = message.split(" ", 2);
            if (parts.length < 2 || parts[1].trim().isEmpty()) {
                conn.send("Usage: /register <username>");
                return;
            }

            String username = parts[1].trim();
            if (userConnections.containsKey(username)) {
                conn.send("Username already taken. Choose another one.");
            } else {
                registerUser(conn, username);
                conn.send("You are registered as: " + username);
            }
            return;
        }

        if (sender == null) {
            conn.send("You must register first. Use /register <username>");
            return;
        }

        if (message.startsWith("/dm ")) {
            String[] parts = message.split(" ", 3);
            if (parts.length < 3) {
                conn.send("Usage: /dm <username> <message>");
                return;
            }

            String target = parts[1].trim();
            String msg = parts[2].trim();
            sendPrivateMessage(sender, target, msg);
            return;
        }

        if (message.equals("/list")) {
            listOnlineUsers(conn);
            return;
        }

        conn.send("Unknown command. Use /register, /dm or /list.");
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        System.err.println("Error from " + conn + ": " + ex.getMessage());
    }

    @Override
    public void onStart() {
        System.out.println("WebSocket server started and ready to accept connections.");
        System.out.println("Private DM Chat Server started on port: " + getPort());
    }

    private void registerUser(WebSocket conn, String username) {
        if (username == null || username.isEmpty()) {
            conn.send("Username cannot be empty.");
            return;
        }

        if (userConnections.containsKey(username)) {
            conn.send("Username already taken. Try another.");
            return;
        }

        userConnections.put(username, conn);
        connectionUsernames.put(conn, username);
        System.out.println(username + " has registered.");
    }

    private void sendPrivateMessage(String sender, String target, String message) {
        if (sender.equalsIgnoreCase(target)) {
            WebSocket senderConn = userConnections.get(sender);
            if (senderConn != null) {
                senderConn.send("You cannot send a private message to yourself.");
            }
            return;
        }
        WebSocket targetConn = userConnections.get(target);
        if (targetConn != null) {
            targetConn.send("[DM from " + sender + "]: " + message);
        }
    }

    private void listOnlineUsers(WebSocket conn) {
        Set<String> onlineUsers = new HashSet<>(userConnections.keySet());
        conn.send("Online users: " + String.join(", ", onlineUsers));
    }

    private void removeUser(WebSocket conn) {
        String username = connectionUsernames.remove(conn);
        if (username != null) {
            userConnections.remove(username);
            System.out.println(username + " has disconnected.");
        }
    }

}
