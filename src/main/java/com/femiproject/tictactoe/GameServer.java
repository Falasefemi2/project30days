package com.femiproject.tictactoe;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class GameServer {

    private final char[][] board = new char[3][3];
    private final Socket[] players = new Socket[2];
    private final ObjectOutputStream[] outs = new ObjectOutputStream[2];
    private final ObjectInputStream[] ins = new ObjectInputStream[2];

    public static void main(String[] args) throws IOException {
        new GameServer().start();
    }

    public void start() throws IOException {
        try (ServerSocket serverSocket = new ServerSocket(12345)) {
            System.out.println("Server started!! Waiting for players....");

            for (int i = 0; i < 2; i++) {
                players[i] = serverSocket.accept();
                outs[i] = new ObjectOutputStream(players[i].getOutputStream());
                ins[i] = new ObjectInputStream(players[i].getInputStream());
                System.out.println("Player " + (i + 1) + " connected.");
            }

            initializeBoard();
            playGame();
        }
    }

    private void initializeBoard() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = ' ';
            }
        }
    }

    private void playGame() throws IOException {
        int currentPlayer = 0;
        boolean gameOver = false;

        sendBoard(0, GameStatus.YOUR_TURN, null);
        sendBoard(1, GameStatus.WAIT, null);

        while (!gameOver) {
            try {
                GameMessage msg = (GameMessage) ins[currentPlayer].readObject();

                if (isValidMove(msg.row, msg.col)) {
                    // Update board with player's mark
                    board[msg.row][msg.col] = currentPlayer == 0 ? 'X' : 'O';

                    // Check for win
                    if (checkWin()) {
                        sendBoard(currentPlayer, GameStatus.WIN, "You Win!");
                        sendBoard(1 - currentPlayer, GameStatus.LOSE, "You lose!");
                        gameOver = true;
                    } else if (isBoardFull()) {
                        sendBoard(0, GameStatus.DRAW, "It's a draw.");
                        sendBoard(1, GameStatus.DRAW, "It's a draw");
                        gameOver = true;
                    } else {
                        // Switch players and continue game
                        currentPlayer = 1 - currentPlayer;
                        sendBoard(currentPlayer, GameStatus.YOUR_TURN, null);
                        sendBoard(1 - currentPlayer, GameStatus.WAIT, null);
                    }
                } else {
                    sendBoard(currentPlayer, GameStatus.INVALID, "Invalid move! Try again.");
                }
            } catch (Exception e) {
                e.printStackTrace();
                break;
            }
        }
    }

    private void sendBoard(int playerindex, GameStatus status, String message) throws IOException {
        GameMessage msg = new GameMessage(board, status, playerindex == 0 ? 'X' : 'O', message, -1, -1);

        outs[playerindex].writeObject(msg);

        outs[playerindex].flush();
    }

    private boolean isValidMove(int row, int col) {
        return row >= 0 && row < 3 && col >= 0 && col < 3 && board[row][col] == ' ';
    }

    private boolean isBoardFull() {
        for (char[] cs : board) {
            for (char c : cs) {
                if (c == ' ') {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean checkWin() {
        // for rows
        for (int i = 0; i < 3; i++) {
            if (board[i][0] != ' ' && board[i][0] == board[i][1] && board[i][1] == board[i][2]) {
                return true;
            }
        }

        // for col
        for (int j = 0; j < 3; j++) {
            if (board[0][j] != ' ' && board[0][j] == board[1][j] && board[1][j] == board[2][j]) {
                return true;
            }
        }

        // Check diagonals
        return (board[0][0] != ' ' && board[0][0] == board[1][1] && board[1][1] == board[2][2])
                || (board[0][2] != ' ' && board[0][2] == board[1][1] && board[1][1] == board[2][0]);
    }
}
