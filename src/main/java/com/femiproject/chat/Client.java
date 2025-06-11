package com.femiproject.chat;

import java.net.URI;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Scanner;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

public class Client extends WebSocketClient {

    private final Scanner scanner = new Scanner(System.in);
    private final String username;
    private boolean isConnected = false;

    public Client(URI serverUri, String username) {
        super(serverUri);
        this.username = username;
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        isConnected = true;
        System.out.println("Connected to the server");
        System.out.println("Type your message (or 'quit' to exit)");

        send("User " + username + " has joined");

        startThreadInput();
    }

    @Override
    public void onMessage(String message) {
        System.out.println("[" + LocalTime.now().truncatedTo(ChronoUnit.SECONDS) + "] " + message);
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        isConnected = false;

        System.out.println("Disconnected from server: " + reason);

        if (remote) {
            System.out.println("Disconnected from server");
        } else {
            System.out.println("Disconnected from server locally");
        }
    }

    @Override
    public void onError(Exception ex) {
        System.out.println("Error occured: " + ex.getMessage());
        isConnected = false;
    }

    private void startThreadInput() {
        Thread thread = new Thread(() -> {
            while (isConnected && scanner.hasNextLine()) {
                String input = scanner.nextLine();

                if ("quit".equalsIgnoreCase(input)) {
                    System.out.println("Disconnecting...");
                    close();
                    break;
                }

                if (isConnected && !input.trim().isEmpty()) {
                    send(username + ": " + input);
                }
            }
        });

        thread.setDaemon(true);
        thread.start();
    }

    public boolean isConnected() {
        return isConnected && !isClosed();
    }

}
