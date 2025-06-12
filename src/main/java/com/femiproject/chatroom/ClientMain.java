package com.femiproject.chatroom;

import java.net.URI;
import java.util.Scanner;

public class ClientMain {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter your username: ");
        String username = scanner.nextLine();

        if (username.isEmpty()) {
            System.out.println("Username cannot be empty");
            return;
        }

        System.out.println("Enter server address  (default localhost): ");
        String serverAddress = scanner.nextLine().trim();
        if (serverAddress.isEmpty()) {
            serverAddress = "localhost";
        }

        System.out.print("Enter server port (default: 8887): ");
        String portInput = scanner.nextLine().trim();
        int port = 8080;

        if (!portInput.isEmpty()) {
            try {
                port = Integer.parseInt(portInput);
            } catch (NumberFormatException e) {
                System.out.println("Invalid port number, using default 8887.");
            }
        }

        try {
            URI uri = new URI("ws://" + serverAddress + ":" + port);
            Client client = new Client(uri, username);

            System.out.println("Connecting to " + uri + "....");
            client.connectBlocking();

            while (client.isOpen()) {
                Thread.sleep(1000);
            }

        } catch (Exception e) {
            System.err.println("Connection failed: " + e.getMessage());
        }
    }
}
