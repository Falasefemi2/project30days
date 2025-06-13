package com.femiproject.chatroom;

import java.net.URI;
import java.util.Scanner;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

public class Client extends WebSocketClient {

    private String username;
    private Scanner scanner = new Scanner(System.in);
    private boolean isConnected = false;

    public Client(URI uri, String username) {
        super(uri);
        this.username = username;
    }

    private void startClientThread() {
        Thread thread = Thread.startVirtualThread(() -> {
            while (isConnected) {
                if (!scanner.hasNextLine())
                    continue;
                String input = scanner.nextLine();

                if (input.equalsIgnoreCase("quit")) {
                    close();
                    break;
                }

                if (!input.trim().isEmpty()) {
                    if (input.startsWith("/join")) {
                        send(input);
                    } else {
                        send(username + ": " + input);
                    }
                }
            }
        });

        thread.setDaemon(true);
        thread.start();
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        isConnected = true;
        System.out.println("Connected Server");
        System.out.println("Enter /join <room> to join a room chat");
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
