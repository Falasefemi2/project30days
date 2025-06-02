package com.femiproject.tictactoe;

import java.io.Serializable;
import java.util.Arrays;

public class GameMessage implements Serializable {

    public int row, col;
    public char[][] board;
    public GameStatus status;
    public char currentPlayer;
    public String message;

    public GameMessage() {
        this.board = new char[3][3];
        for (int i = 0; i < 3; i++) {
            Arrays.fill(board[i], ' ');
        }
    }

    public GameMessage(char[][] board, GameStatus status, char currentPlayer, String message, int row, int col) {
        this.board = new char[3][3];
        for (int i = 0; i < 3; i++) {
            this.board[i] = Arrays.copyOf(board[i], 3);
        }
        this.status = status;
        this.currentPlayer = currentPlayer;
        this.message = message;
        this.row = row;
        this.col = col;
    }
}
