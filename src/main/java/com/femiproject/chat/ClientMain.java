package com.femiproject.chat;

import java.net.URI;
import java.util.Scanner;

public class ClientMain {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter your username: ");
        String username = scanner.nextLine();

        System.out.println("Enter server address (default: localhost): ");
        String serverAddress = scanner.nextLine();

        if (serverAddress.trim().isEmpty()) {
            serverAddress = "localhost";
        }

        System.out.print("Enter server port (default: 8887): ");
        String portStr = scanner.nextLine();
        int port = 8887;
        if (!portStr.trim().isEmpty()) {
            try {
                port = Integer.parseInt(portStr);
            } catch (NumberFormatException e) {
                System.out.println("Invalid port, using default: " + port);
            }
        }

        try {
            URI serveUri = new URI("ws://" + serverAddress + ":" + port);
            Client client = new Client(serveUri, username);

            System.out.println("Connecting to " + serveUri + "....");
            client.connect();

            while (client.isConnected()) {
                Thread.sleep(1000);
            }
        } catch (Exception e) {
            System.err.println("Failed to connect: " + e.getMessage());
        }
    }
}
