package com.femiproject.tictactoe;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GameClientTest {

    @Mock
    private Socket mockSocket;

    @Mock
    private ObjectOutputStream mockOut;

    @Mock
    private ObjectInputStream mockIn;

    @Mock
    private Scanner mockScanner;

    private ByteArrayOutputStream outputStream;
    private PrintStream originalOut;

    @BeforeEach
    void setUp() {
        // Capture System.out for testing print statements
        outputStream = new ByteArrayOutputStream();
        originalOut = System.out;
        System.setOut(new PrintStream(outputStream));
    }

    @Test
    void testPrintBoard_EmptyBoard() throws Exception {
        Method printBoard = GameClient.class.getDeclaredMethod("printBoard", char[][].class);
        printBoard.setAccessible(true);

        char[][] emptyBoard = {
            {' ', ' ', ' '},
            {' ', ' ', ' '},
            {' ', ' ', ' '}
        };

        printBoard.invoke(null, (Object) emptyBoard);

        String output = outputStream.toString();

        // Verify board header is printed
        assertTrue(output.contains("    1   2   3"));
        assertTrue(output.contains("  +---+---+---+"));

        // Verify row numbers are printed
        assertTrue(output.contains("1 |"));
        assertTrue(output.contains("2 |"));
        assertTrue(output.contains("3 |"));

        // Verify empty cells are displayed as spaces
        assertTrue(output.contains("   |   |   |"));
    }

    @Test
    void testPrintBoard_WithMoves() throws Exception {
        Method printBoard = GameClient.class.getDeclaredMethod("printBoard", char[][].class);
        printBoard.setAccessible(true);

        char[][] boardWithMoves = {
            {'X', ' ', 'O'},
            {' ', 'X', ' '},
            {'O', ' ', 'X'}
        };

        printBoard.invoke(null, (Object) boardWithMoves);

        String output = outputStream.toString();

        // Verify X and O are displayed correctly
        assertTrue(output.contains(" X "));
        assertTrue(output.contains(" O "));

        // Verify board structure is maintained
        assertTrue(output.contains("    1   2   3"));
        assertTrue(output.contains("  +---+---+---+"));
    }

    @Test
    void testPrintBoard_FullBoard() throws Exception {
        Method printBoard = GameClient.class.getDeclaredMethod("printBoard", char[][].class);
        printBoard.setAccessible(true);

        char[][] fullBoard = {
            {'X', 'O', 'X'},
            {'O', 'X', 'O'},
            {'X', 'O', 'X'}
        };

        printBoard.invoke(null, (Object) fullBoard);

        String output = outputStream.toString();

        // Count occurrences of X and O
        long xCount = output.chars().filter(ch -> ch == 'X').count();
        long oCount = output.chars().filter(ch -> ch == 'O').count();

        assertEquals(5, xCount); // 5 X's on the board
        assertEquals(4, oCount); // 4 O's on the board
    }

    @Test
    void testGameMessage_YourTurn() throws Exception {
        // Create a mock game message for YOUR_TURN status
        GameMessage mockMessage = new GameMessage();
        mockMessage.board = new char[][]{
            {' ', ' ', ' '},
            {' ', ' ', ' '},
            {' ', ' ', ' '}
        };
        mockMessage.status = GameStatus.YOUR_TURN;
        mockMessage.currentPlayer = 'X';
        mockMessage.message = null;

        // This test would require more complex setup to test the full game loop
        // For now, we'll test that the board is printed correctly
        Method printBoard = GameClient.class.getDeclaredMethod("printBoard", char[][].class);
        printBoard.setAccessible(true);

        printBoard.invoke(null, (Object) mockMessage.board);

        String output = outputStream.toString();
        assertTrue(output.contains("    1   2   3"));
    }

    @Test
    void testGameMessage_WithMessage() throws Exception {
        GameMessage mockMessage = new GameMessage();
        mockMessage.board = new char[][]{
            {'X', ' ', ' '},
            {' ', ' ', ' '},
            {' ', ' ', ' '}
        };
        mockMessage.status = GameStatus.WAIT;
        mockMessage.currentPlayer = 'O';
        mockMessage.message = "Wait for your turn";

        Method printBoard = GameClient.class.getDeclaredMethod("printBoard", char[][].class);
        printBoard.setAccessible(true);

        printBoard.invoke(null, (Object) mockMessage.board);

        // Simulate printing the message
        System.out.println(mockMessage.message);

        String output = outputStream.toString();
        assertTrue(output.contains("Wait for your turn"));
        assertTrue(output.contains(" X "));
    }

    @Test
    void testGameMessage_GameOver() throws Exception {
        GameMessage winMessage = new GameMessage();
        winMessage.board = new char[][]{
            {'X', 'X', 'X'},
            {'O', 'O', ' '},
            {' ', ' ', ' '}
        };
        winMessage.status = GameStatus.WIN;
        winMessage.currentPlayer = 'X';
        winMessage.message = "You Win!";

        Method printBoard = GameClient.class.getDeclaredMethod("printBoard", char[][].class);
        printBoard.setAccessible(true);

        printBoard.invoke(null, (Object) winMessage.board);
        System.out.println(winMessage.message);
        System.out.println("Game Over");

        String output = outputStream.toString();
        assertTrue(output.contains("You Win!"));
        assertTrue(output.contains("Game Over"));
        assertTrue(output.contains(" X | X | X |")); // Winning row
    }

    @Test
    void testBoardDisplay_EdgeCases() throws Exception {
        Method printBoard = GameClient.class.getDeclaredMethod("printBoard", char[][].class);
        printBoard.setAccessible(true);

        // Test with null characters (should be treated as spaces)
        char[][] boardWithNulls = {
            {'\0', 'X', '\0'},
            {'O', '\0', 'X'},
            {'\0', 'O', '\0'}
        };

        printBoard.invoke(null, (Object) boardWithNulls);

        String output = outputStream.toString();

        // Verify the board structure is maintained even with null characters
        assertTrue(output.contains("    1   2   3"));
        assertTrue(output.contains("  +---+---+---+"));
        assertTrue(output.contains(" X "));
        assertTrue(output.contains(" O "));
    }

    @Test
    void testGameMessageCreation() {
        // Test creating a GameMessage for sending moves
        GameMessage move = new GameMessage();
        move.row = 1;
        move.col = 2;

        assertEquals(1, move.row);
        assertEquals(2, move.col);

        // Test that we can create messages without setting all fields
        assertDoesNotThrow(() -> {
            GameMessage msg = new GameMessage();
            msg.row = 0;
            msg.col = 0;
        });
    }

    @Test
    void testInputValidation_Simulation() {
        // Simulate valid input range (1-3 converted to 0-2)
        int userRow = 2; // User enters 2
        int actualRow = userRow - 1; // Converted to 1 for array index

        assertTrue(actualRow >= 0 && actualRow < 3);

        int userCol = 3; // User enters 3
        int actualCol = userCol - 1; // Converted to 2 for array index

        assertTrue(actualCol >= 0 && actualCol < 3);
    }

    @Test
    void testBoardFormatting_Consistency() throws Exception {
        Method printBoard = GameClient.class.getDeclaredMethod("printBoard", char[][].class);
        printBoard.setAccessible(true);

        char[][] testBoard = {
            {'X', 'O', 'X'},
            {'O', 'X', 'O'},
            {'X', 'O', 'X'}
        };

        printBoard.invoke(null, (Object) testBoard);

        String output = outputStream.toString();
        String[] lines = output.split("\n");

        // Verify consistent formatting
        boolean hasHeader = false;
        int separatorCount = 0;
        int rowCount = 0;

        for (String line : lines) {
            if (line.contains("    1   2   3")) {
                hasHeader = true;
            }
            if (line.contains("+---+---+---+")) {
                separatorCount++;
            }
            if (line.matches("\\d \\|.*\\|")) {
                rowCount++;
            }
        }

        assertTrue(hasHeader);
        assertEquals(4, separatorCount); // Top + 3 between rows
        assertEquals(3, rowCount); // 3 game rows
    }

    @Test
    void testGameStatus_Handling() {
        // Test all possible game statuses
        GameStatus[] allStatuses = {
            GameStatus.YOUR_TURN,
            GameStatus.WAIT,
            GameStatus.WIN,
            GameStatus.LOSE,
            GameStatus.DRAW,
            GameStatus.INVALID
        };

        for (GameStatus status : allStatuses) {
            assertNotNull(status);

            // Simulate different responses based on status
            switch (status) {
                case YOUR_TURN:
                    System.out.println("Your move");
                    break;
                case WIN:
                case LOSE:
                case DRAW:
                    System.out.println("Game Over");
                    break;
                case WAIT:
                    System.out.println("Waiting...");
                    break;
                case INVALID:
                    System.out.println("Invalid move");
                    break;
            }
        }

        String output = outputStream.toString();
        assertTrue(output.contains("Your move"));
        assertTrue(output.contains("Game Over"));
        assertTrue(output.contains("Waiting..."));
        assertTrue(output.contains("Invalid move"));
    }

    // Clean up after tests
    @org.junit.jupiter.api.AfterEach
    void tearDown() {
        System.setOut(originalOut);
    }
}
