package com.femiproject.chatdms;

import java.net.URI;
import java.util.Scanner;

public class ClientMain {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter your username: ");
        String username = scanner.nextLine().trim();

        if (username.isEmpty()) {
            System.out.println("Username cannot be empty.");
            return;
        }

        System.out.print("Enter server address (default: localhost): ");
        String serverAddress = scanner.nextLine().trim();
        if (serverAddress.isEmpty()) {
            serverAddress = "localhost";
        }

        System.out.print("Enter port (default: 8080): ");
        String portInput = scanner.nextLine().trim();
        int port = 8080;
        if (!portInput.isEmpty()) {
            try {
                port = Integer.parseInt(portInput);
            } catch (NumberFormatException e) {
                System.out.println("Invalid port. Using default port 8080.");
            }
        }

        try {
            URI uri = new URI("ws://" + serverAddress + ":" + port);
            ChatClient client = new ChatClient(uri, username);

            System.out.println("Connecting to " + uri + "...");
            client.connectBlocking(); // blocking connection attempt

            // Wait while client is connected
            while (client.isOpen()) {
                Thread.sleep(1000);
            }

        } catch (Exception e) {
            System.err.println("Failed to connect: " + e.getMessage());
        }

    }
}
