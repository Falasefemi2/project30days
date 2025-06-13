package com.femiproject.chatdms;

import java.net.URI;
import java.util.Scanner;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

public class ChatClient extends WebSocketClient {
    private String username;
    private Scanner scanner = new Scanner(System.in);
    private boolean isConnected = false;

    public ChatClient(URI uri, String username) {
        super(uri);
        this.username = username;
    }

    private void startClientThread() {
        Thread thread = Thread.startVirtualThread(() -> {
            while (isConnected) {
                if (!scanner.hasNextLine()) {
                    continue;
                }

                String input = scanner.nextLine().trim();

                if (input.equalsIgnoreCase("quit")) {
                    close();
                    break;
                }

                if (input.startsWith("/register") || input.startsWith("/dm") || input.startsWith("/list")) {
                    send(input);
                } else if (!input.isEmpty()) {
                    send(username + ": " + input);
                }
            }
        });

        thread.setDaemon(true);
        thread.start();
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        isConnected = true;
        System.out.println("Connected To Server");
        System.out.println("Enter /register <username> to start chatting with freinds");
        System.out.println("Type 'quit' to exit room chat");

        send("[System] " + username + " has connected");
        startClientThread();
    }

    @Override
    public void onMessage(String message) {
        System.out.println(message);
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        isConnected = false;
        System.out.println("Server disconnected: " + reason);
    }

    @Override
    public void onError(Exception ex) {
        System.out.println("Error: " + ex.getMessage());
    }

}
