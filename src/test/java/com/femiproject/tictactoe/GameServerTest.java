package com.femiproject.tictactoe;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GameServerTest {

    @Mock
    private Socket mockSocket1;

    @Mock
    private Socket mockSocket2;

    @Mock
    private ObjectOutputStream mockOut1;

    @Mock
    private ObjectOutputStream mockOut2;

    @Mock
    private ObjectInputStream mockIn1;

    @Mock
    private ObjectInputStream mockIn2;

    @Mock
    private ServerSocket mockServerSocket;

    private GameServer gameServer;

    @BeforeEach
    void setUp() {
        gameServer = new GameServer();
    }

    @Test
    void testInitializeBoard() throws Exception {
        // Use reflection to access private method and field
        Method initializeBoard = GameServer.class.getDeclaredMethod("initializeBoard");
        initializeBoard.setAccessible(true);

        Field boardField = GameServer.class.getDeclaredField("board");
        boardField.setAccessible(true);

        initializeBoard.invoke(gameServer);
        char[][] board = (char[][]) boardField.get(gameServer);

        // Verify all cells are empty
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                assertEquals(' ', board[i][j]);
            }
        }
    }

    @Test
    void testIsValidMove() throws Exception {
        Method isValidMove = GameServer.class.getDeclaredMethod("isValidMove", int.class, int.class);
        isValidMove.setAccessible(true);

        Method initializeBoard = GameServer.class.getDeclaredMethod("initializeBoard");
        initializeBoard.setAccessible(true);
        initializeBoard.invoke(gameServer);

        // Test valid moves on empty board
        assertTrue((Boolean) isValidMove.invoke(gameServer, 0, 0));
        assertTrue((Boolean) isValidMove.invoke(gameServer, 1, 1));
        assertTrue((Boolean) isValidMove.invoke(gameServer, 2, 2));

        // Test invalid coordinates
        assertFalse((Boolean) isValidMove.invoke(gameServer, -1, 0));
        assertFalse((Boolean) isValidMove.invoke(gameServer, 0, -1));
        assertFalse((Boolean) isValidMove.invoke(gameServer, 3, 0));
        assertFalse((Boolean) isValidMove.invoke(gameServer, 0, 3));

        // Test occupied cell
        Field boardField = GameServer.class.getDeclaredField("board");
        boardField.setAccessible(true);
        char[][] board = (char[][]) boardField.get(gameServer);
        board[1][1] = 'X';

        assertFalse((Boolean) isValidMove.invoke(gameServer, 1, 1));
    }

    @Test
    void testIsBoardFull() throws Exception {
        Method isBoardFull = GameServer.class.getDeclaredMethod("isBoardFull");
        isBoardFull.setAccessible(true);

        Field boardField = GameServer.class.getDeclaredField("board");
        boardField.setAccessible(true);

        Method initializeBoard = GameServer.class.getDeclaredMethod("initializeBoard");
        initializeBoard.setAccessible(true);
        initializeBoard.invoke(gameServer);

        // Empty board should not be full
        assertFalse((Boolean) isBoardFull.invoke(gameServer));

        // Fill the board
        char[][] board = (char[][]) boardField.get(gameServer);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = (i + j) % 2 == 0 ? 'X' : 'O';
            }
        }

        // Full board should return true
        assertTrue((Boolean) isBoardFull.invoke(gameServer));
    }

    @Test
    void testCheckWin_Rows() throws Exception {
        Method checkWin = GameServer.class.getDeclaredMethod("checkWin");
        checkWin.setAccessible(true);

        Field boardField = GameServer.class.getDeclaredField("board");
        boardField.setAccessible(true);

        Method initializeBoard = GameServer.class.getDeclaredMethod("initializeBoard");
        initializeBoard.setAccessible(true);
        initializeBoard.invoke(gameServer);

        char[][] board = (char[][]) boardField.get(gameServer);

        // Test no win initially
        assertFalse((Boolean) checkWin.invoke(gameServer));

        // Test row wins
        for (int row = 0; row < 3; row++) {
            // Reset board
            initializeBoard.invoke(gameServer);

            // Fill a row with X
            for (int col = 0; col < 3; col++) {
                board[row][col] = 'X';
            }

            assertTrue((Boolean) checkWin.invoke(gameServer), "Row " + row + " should be a win");
        }
    }

    @Test
    void testCheckWin_Columns() throws Exception {
        Method checkWin = GameServer.class.getDeclaredMethod("checkWin");
        checkWin.setAccessible(true);

        Field boardField = GameServer.class.getDeclaredField("board");
        boardField.setAccessible(true);

        Method initializeBoard = GameServer.class.getDeclaredMethod("initializeBoard");
        initializeBoard.setAccessible(true);

        char[][] board = (char[][]) boardField.get(gameServer);

        // Test column wins
        for (int col = 0; col < 3; col++) {
            // Reset board
            initializeBoard.invoke(gameServer);

            // Fill a column with O
            for (int row = 0; row < 3; row++) {
                board[row][col] = 'O';
            }

            assertTrue((Boolean) checkWin.invoke(gameServer), "Column " + col + " should be a win");
        }
    }

    @Test
    void testCheckWin_Diagonals() throws Exception {
        Method checkWin = GameServer.class.getDeclaredMethod("checkWin");
        checkWin.setAccessible(true);

        Field boardField = GameServer.class.getDeclaredField("board");
        boardField.setAccessible(true);

        Method initializeBoard = GameServer.class.getDeclaredMethod("initializeBoard");
        initializeBoard.setAccessible(true);

        char[][] board = (char[][]) boardField.get(gameServer);

        // Test main diagonal (top-left to bottom-right)
        initializeBoard.invoke(gameServer);
        board[0][0] = 'X';
        board[1][1] = 'X';
        board[2][2] = 'X';
        assertTrue((Boolean) checkWin.invoke(gameServer), "Main diagonal should be a win");

        // Test anti-diagonal (top-right to bottom-left)
        initializeBoard.invoke(gameServer);
        board[0][2] = 'O';
        board[1][1] = 'O';
        board[2][0] = 'O';
        assertTrue((Boolean) checkWin.invoke(gameServer), "Anti-diagonal should be a win");
    }

    @Test
    void testSendBoard() throws Exception {
        // Setup mocks
        Field outsField = GameServer.class.getDeclaredField("outs");
        outsField.setAccessible(true);
        ObjectOutputStream[] outs = new ObjectOutputStream[2];
        outs[0] = mockOut1;
        outs[1] = mockOut2;
        outsField.set(gameServer, outs);

        Method initializeBoard = GameServer.class.getDeclaredMethod("initializeBoard");
        initializeBoard.setAccessible(true);
        initializeBoard.invoke(gameServer);

        Method sendBoard = GameServer.class.getDeclaredMethod("sendBoard", int.class, GameStatus.class, String.class);
        sendBoard.setAccessible(true);

        // Test sending to player 0
        sendBoard.invoke(gameServer, 0, GameStatus.YOUR_TURN, "Your turn");

        verify(mockOut1).writeObject(any(GameMessage.class));
        verify(mockOut1).flush();

        // Test sending to player 1
        sendBoard.invoke(gameServer, 1, GameStatus.WAIT, "Wait for your turn");

        verify(mockOut2).writeObject(any(GameMessage.class));
        verify(mockOut2).flush();
    }

    @Test
    void testGameLogic_WinScenario() throws Exception {
        // This is a more complex integration test that would require
        // setting up the entire game state and mocking network I/O
        // Here's a simplified version focusing on the win detection logic

        Field boardField = GameServer.class.getDeclaredField("board");
        boardField.setAccessible(true);

        Method initializeBoard = GameServer.class.getDeclaredMethod("initializeBoard");
        initializeBoard.setAccessible(true);
        initializeBoard.invoke(gameServer);

        Method checkWin = GameServer.class.getDeclaredMethod("checkWin");
        checkWin.setAccessible(true);

        Method isBoardFull = GameServer.class.getDeclaredMethod("isBoardFull");
        isBoardFull.setAccessible(true);

        char[][] board = (char[][]) boardField.get(gameServer);

        // Simulate a winning game sequence
        board[0][0] = 'X'; // Player 1 move
        assertFalse((Boolean) checkWin.invoke(gameServer));
        assertFalse((Boolean) isBoardFull.invoke(gameServer));

        board[1][0] = 'O'; // Player 2 move
        assertFalse((Boolean) checkWin.invoke(gameServer));

        board[0][1] = 'X'; // Player 1 move
        assertFalse((Boolean) checkWin.invoke(gameServer));

        board[1][1] = 'O'; // Player 2 move
        assertFalse((Boolean) checkWin.invoke(gameServer));

        board[0][2] = 'X'; // Player 1 move - should win (top row)
        assertTrue((Boolean) checkWin.invoke(gameServer));
    }

    @Test
    void testGameLogic_DrawScenario() throws Exception {
        Field boardField = GameServer.class.getDeclaredField("board");
        boardField.setAccessible(true);

        Method initializeBoard = GameServer.class.getDeclaredMethod("initializeBoard");
        initializeBoard.setAccessible(true);
        initializeBoard.invoke(gameServer);

        Method checkWin = GameServer.class.getDeclaredMethod("checkWin");
        checkWin.setAccessible(true);

        Method isBoardFull = GameServer.class.getDeclaredMethod("isBoardFull");
        isBoardFull.setAccessible(true);

        char[][] board = (char[][]) boardField.get(gameServer);

        // Create a draw scenario
        board[0][0] = 'X';
        board[0][1] = 'O';
        board[0][2] = 'X';
        board[1][0] = 'O';
        board[1][1] = 'X';
        board[1][2] = 'O';
        board[2][0] = 'O';
        board[2][1] = 'X';
        board[2][2] = 'O';

        assertFalse((Boolean) checkWin.invoke(gameServer));
        assertTrue((Boolean) isBoardFull.invoke(gameServer));
    }
}
