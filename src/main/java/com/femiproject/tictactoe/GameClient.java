package com.femiproject.tictactoe;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class GameClient {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        try (Socket socket = new Socket("localhost", 12345)) {
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            boolean gameRunning = true;
            while (gameRunning) {
                GameMessage msg = (GameMessage) in.readObject();
                printBoard(msg.board);
                if (msg.message != null) {
                    System.out.println(msg.message);
                }
                if (null != msg.status) {
                    switch (msg.status) {
                        case YOUR_TURN -> {
                            System.out.println("Your move (Player " + msg.currentPlayer + ")");
                            System.out.println("Enter row (1-3): ");
                            int row = scanner.nextInt() - 1;
                            System.out.println("Enter col (1-3): ");
                            int col = scanner.nextInt() - 1;
                            GameMessage move = new GameMessage();
                            move.row = row;
                            move.col = col;
                            out.writeObject(move);
                            out.flush();
                        }
                        case WIN, LOSE, DRAW -> {
                            System.out.println("Game Over");
                            gameRunning = false;
                        }
                        case INVALID -> {
                        }
                        default -> {
                        }
                    }
                    // Invalid move, prompt again without reading new message
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            scanner.close();
        }
    }

    private static void printBoard(char[][] board) {
        System.out.println("\n    1   2   3");
        System.out.println("  +---+---+---+");

        for (int i = 0; i < 3; i++) {
            System.out.print((i + 1) + " |");
            for (int j = 0; j < 3; j++) {
                char cell = board[i][j];
                String display = (cell == ' ') ? "   " : " " + cell + " ";
                System.out.print(display + "|");
            }
            System.out.println();
            System.out.println("  +---+---+---+");
        }
        System.out.println();
    }
}
