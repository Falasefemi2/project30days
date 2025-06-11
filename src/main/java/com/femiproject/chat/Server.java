package com.femiproject.chat;

import java.net.InetSocketAddress;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

public class Server extends WebSocketServer {

    private final Set<WebSocket> clients = ConcurrentHashMap.newKeySet();

    public Server(int port) {
        super(new InetSocketAddress(port));
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        clients.add(conn);

        System.out.println("New client connected: " + conn.getRemoteSocketAddress());
        System.out.println("Total clients: " + clients.size());

        conn.send("Welcome to the chat server!");

        broadcast("A new user has entered the chat", conn);
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        clients.remove(conn);

        System.out.println("Client disconnected: " + conn.getRemoteSocketAddress());
        System.out.println("Total clients: " + clients.size());

        broadcast("A user has left the chat", conn);
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        System.out.println("Received: " + message + " from " + conn.getRemoteSocketAddress());
        broadcast(message, conn);
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        System.out.println("Error occurred: " + ex.getMessage());
        ex.printStackTrace();

        if (conn != null) {
            clients.remove(conn);
        }
    }

    @Override
    public void onStart() {
        System.out.println("Chat server started on port: " + getPort());
        setConnectionLostTimeout(0);
        setConnectionLostTimeout(100);
    }

    private void broadcast(String message, WebSocket sender) {
        for (WebSocket webSocket : clients) {
            try {
                webSocket.send(message);
            } catch (Exception e) {
                System.out.println("Error sending message: " + e.getMessage());

                clients.remove(webSocket);
            }
        }
    }

    public void broadCastAll(String message) {
        for (WebSocket webSocket : clients) {
            if (webSocket.isOpen()) {
                try {
                    webSocket.send(message);
                } catch (Exception e) {
                    System.out.println("Error sending message: " + e.getMessage());
                    clients.remove(webSocket);
                }
            }
        }
    }
}
