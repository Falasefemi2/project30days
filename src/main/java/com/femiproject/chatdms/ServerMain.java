package com.femiproject.chatdms;

import java.util.Scanner;

public class ServerMain {

    public static void main(String[] args) {
        int port = 8080;

        if (args.length > 0) {
            try {
                port = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                System.out.println("Invalid port number provided. Falling back to default port 8080.");
            }
        }

        ChatServer chatServer = new ChatServer(port);
        chatServer.start();

        System.out.println("Group Chat Server is running on port " + port);
        System.out.println("Press Enter to shut down the server.");

        try (Scanner scanner = new Scanner(System.in)) {
            scanner.nextLine();
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("Shutting down the server...");
        try {
            chatServer.stop();
        } catch (Exception e) {
            System.out.println("Error while stopping the server: " + e.getMessage());
        }
    }

}
