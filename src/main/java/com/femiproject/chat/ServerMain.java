package com.femiproject.chat;

import java.util.Scanner;

public class ServerMain {

    public static void main(String[] args) {
        int port = 8887;

        if (args.length > 0) {
            try {
                port = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                System.out.println("Invalid port number, using default: " + port);
            }
        }

        Server server = new Server(port);
        server.start();

        System.out.println("Chat server running on port " + port);
        System.out.println("Press enter to stop.");

        try (Scanner scanner = new Scanner(System.in)) {
            scanner.nextLine();
            System.out.println("Shutting down server...");
            server.stop(1000);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
