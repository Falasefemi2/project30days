package com.femiproject.chatroom;

import java.util.Scanner;

public class ServerMain {

    public static void main(String[] args) {
        int port = 8080;

        if (args.length > 0) {
            try {
                port = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                System.out.println("Invalid port number defaulting back to default port  number " + port);
            }
        }

        Server server = new Server(port);
        server.start();

        System.out.println("Group Chat Server is running on port " + port);
        System.out.println("Press Enter to shut down the server.");

        try (Scanner scanner = new Scanner(System.in)) {
            scanner.nextLine();
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("Shutting down the server...");
        try {
            server.stop();
        } catch (Exception e) {
            System.out.println("Error while stopping the server: " + e.getMessage());
        }
    }
}
