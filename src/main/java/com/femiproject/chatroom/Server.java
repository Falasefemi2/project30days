package com.femiproject.chatroom;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

public class Server extends WebSocketServer {

    private final Map<String, Set<WebSocket>> rooms = new ConcurrentHashMap<>();
    private final Map<WebSocket, String> usersRooms = new ConcurrentHashMap<>();

    public Server(int port) {
        super(new InetSocketAddress(port));
    }

    private void joinRoom(WebSocket conn, String room) {
        rooms.computeIfAbsent(room, k -> ConcurrentHashMap.newKeySet()).add(conn);
        usersRooms.put(conn, room);
        conn.send("You joined room " + room);

        broadcastMessage(room, "A new user joined the room", conn);
    }

    private void broadcastMessage(String room, String message, WebSocket sender) {
        Set<WebSocket> clients = rooms.get(room);
        if (clients != null) {
            for (WebSocket client : clients) {
                if (!client.equals(sender) && client.isOpen()) {
                    client.send(message);
                }
            }
        }
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        System.out.println("Client connected: " + conn.getRemoteSocketAddress());
        conn.send("Welcome! Use /join <roomname> to join a chat room");
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        String room = usersRooms.remove(conn);
        if (room != null) {
            rooms.get(room).remove(conn);
            broadcastMessage(room, "A user left the room", conn);
        }

        System.out.println("Client disconnected: " + conn.getRemoteSocketAddress());
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        if (message.startsWith("/join")) {
            String roomName = message.substring(6).trim();
            joinRoom(conn, roomName);
            return;
        }

        String room = usersRooms.get(conn);
        if (room != null) {
            broadcastMessage(room, message, conn);
        } else {
            conn.send("Join a room first with /join <room>");
        }
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        System.out.println("Error: " + ex.getMessage());
    }

    @Override
    public void onStart() {
        System.out.println("Server started on: " + getPort());
    }
}
