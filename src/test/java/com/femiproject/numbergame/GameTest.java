package com.femiproject.numbergame;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Random;
import java.util.Scanner;

import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mockito;
import static org.mockito.Mockito.when;

public class GameTest {

    private final InputStream originalSystemIn = System.in;
    private final PrintStream originalSystemOut = System.out;
    private ByteArrayOutputStream consoleOutput;

    @BeforeEach
    void setUpStreams() {
        consoleOutput = new ByteArrayOutputStream();
        System.setOut(new PrintStream(consoleOutput));
    }

    @AfterEach
    void restoreStreams() {
        System.setIn(originalSystemIn);
        System.setOut(originalSystemOut);
    }

    private Game createGameWithMockedInputAndRandom(String input, int targetNumberToGenerate) {
        ByteArrayInputStream testIn = new ByteArrayInputStream(input.getBytes());
        Scanner testScanner = new Scanner(testIn);

        Random mockRandom = Mockito.mock(Random.class);
        when(mockRandom.nextInt(100)).thenReturn(targetNumberToGenerate - 1);

        return new Game(testScanner, mockRandom);
    }

    @Test
    void handleGuess_correctGuess_returnsTrueAndPrintsMessage() {
        Game game = new Game();
        assertTrue(game.handleGuess(50, 50));
        assertTrue(consoleOutput.toString().contains("🎉 Correct! You guessed the number!"));
    }

    @Test
    void handleGuess_lowGuess_returnsFalseAndPrintsMessage() {
        Game game = new Game();
        assertFalse(game.handleGuess(40, 50));
        assertTrue(consoleOutput.toString().contains("Too low. Try again."));
    }

    @Test
    void handleGuess_highGuess_returnsFalseAndPrintsMessage() {
        Game game = new Game();
        assertFalse(game.handleGuess(60, 50));
        assertTrue(consoleOutput.toString().contains("Too high. Try again."));
    }

    @Test
    void generateTargetNumber_usesInjectedRandomAndReturnsCorrectValue() {
        Random mockRandom = Mockito.mock(Random.class);
        when(mockRandom.nextInt(100)).thenReturn(74); // nextInt(100) returns 0-99
        // For this test, the scanner is not used by generateTargetNumber
        Game game = new Game(new Scanner(""), mockRandom); // Dummy scanner
        assertEquals(75, game.generateTargetNumber()); // 74 + 1
    }

    @Test
    void promptGuess_validNumericInput_returnsNumber() {
        String simulatedInput = "42\n";
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));
        Game game = new Game(new Scanner(System.in), new Random());

        assertEquals(42, game.promptGuess());
        assertTrue(consoleOutput.toString().contains("Enter your guess (1–100):"));
    }

    @Test
    void promptGuess_invalidTextThenValidNumericInput_returnsNumber() {
        String simulatedInput = "abc\n33\n";
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));
        Game game = new Game(new Scanner(System.in), new Random());

        assertEquals(33, game.promptGuess());
        String output = consoleOutput.toString();
        assertTrue(output.contains("Invalid input. Please enter a valid number."));
        long promptCount = output.lines().filter(line -> line.contains("Enter your guess (1–100):")).count();
        assertEquals(2, promptCount, "Should prompt twice");
    }

    @Test
    void promptGuess_outOfRangeThenValidNumericInput_returnsNumber() {
        String simulatedInput = "0\n101\n77\n";
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));
        Game game = new Game(new Scanner(System.in), new Random());

        assertEquals(77, game.promptGuess());
        String output = consoleOutput.toString();
        assertTrue(output.contains("Please enter a number between 1 and 100."));
        long promptCount = output.lines().filter(line -> line.contains("Enter your guess (1–100):")).count();
        assertEquals(3, promptCount, "Should prompt three times");
    }

    @Test
    void promptGuess_noMoreInputAfterInvalid_returnsSentinel() {
        String simulatedInput = "xyz";
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));
        Scanner testScanner = new Scanner(System.in);
        Game game = new Game(testScanner, new Random());

        assertEquals(-1, game.promptGuess());
        assertTrue(consoleOutput.toString().contains("Invalid input. Please enter a valid number."));
    }

    @ParameterizedTest
    @CsvSource({
        "1, EASY, 10", // Valid choice Easy
        "2, MEDIUM, 5", // Valid choice Medium
        "3, HARD, 3", // Valid choice Hard
        "0, MEDIUM, 5", // Invalid numeric choice, defaults to Medium
        "99, MEDIUM, 5", // Invalid numeric choice, defaults to Medium
        "xyz, MEDIUM, 5" // Non-numeric choice, defaults to Medium
    })
    void playRound_difficultySelectionAndOutput(String difficultyInput, String expectedLevel, int expectedChances) {
        String fullInput = difficultyInput + "\n"
                + // Difficulty choice
                "50\n"; // A dummy guess to allow playRound to proceed minimally

        Game game = createGameWithMockedInputAndRandom(fullInput, 25); // Target num 25
        game.playRound(); // Execute the method

        String output = consoleOutput.toString();
        assertTrue(output.contains("Great! You selected " + expectedLevel + " difficulty."), "Output was: " + output);
        assertTrue(output.contains("You have " + expectedChances + " chances."), "Output was: " + output);

        if (!difficultyInput.equals("1") && !difficultyInput.equals("2") && !difficultyInput.equals("3")) {
            assertTrue(output.contains("Invalid choice or non-integer input. Defaulting to Medium difficulty."), "Output was: " + output);
        }
    }

    @Test
    void playRound_playerWinsOnEasy() {
        String input = "1\n"
                + // Easy difficulty
                "10\n"
                + // Guess 1
                "20\n"
                + // Guess 2
                "25\n";  // Correct guess
        int targetNumber = 25;
        Game game = createGameWithMockedInputAndRandom(input, targetNumber);

        game.playRound();
        String output = consoleOutput.toString();

        assertTrue(output.contains("Great! You selected EASY difficulty."));
        assertTrue(output.contains("You have 10 chances."));
        assertTrue(output.contains("Too low. Try again.")); // For 10
        assertTrue(output.contains("You have 9 chances left."));
        assertTrue(output.contains("Too low. Try again.")); // For 20
        assertTrue(output.contains("You have 8 chances left."));
        assertTrue(output.contains("🎉 Correct! You guessed the number!"));
        assertFalse(output.contains("Sorry! You've used all your chances."));
    }

    @Test
    void playRound_playerLosesOnHard() {
        String input = "3\n"
                + // Hard difficulty
                "10\n"
                + // Guess 1 (Low)
                "90\n"
                + // Guess 2 (High)
                "50\n";  // Guess 3 (Low)
        int targetNumber = 75; // Target
        Game game = createGameWithMockedInputAndRandom(input, targetNumber);

        game.playRound();
        String output = consoleOutput.toString();

        assertTrue(output.contains("Great! You selected HARD difficulty."));
        assertTrue(output.contains("You have 3 chances."));
        assertTrue(output.contains("Too low. Try again."));    // For 10
        assertTrue(output.contains("You have 2 chances left."));
        assertTrue(output.contains("Too high. Try again."));   // For 90
        assertTrue(output.contains("You have 1 chances left.")); // Grammar "1 chances" from original code
        assertTrue(output.contains("Too low. Try again."));    // For 50
        // No "chances left" after the last attempt
        assertTrue(output.contains("Sorry! You've used all your chances."));
        assertTrue(output.contains("The correct number was: " + targetNumber));
        assertFalse(output.contains("🎉 Correct! You guessed the number!"));
    }

    @Test
    void playRound_inputExhaustionDuringGuessing_endsRound() {
        String input = "2\n"
                + // Medium difficulty (5 chances)
                "10\n"
                + // Guess 1
                "xyz";   // Invalid input, then stream ends
        int targetNumber = 50;
        Game game = createGameWithMockedInputAndRandom(input, targetNumber);

        game.playRound();
        String output = consoleOutput.toString();

        assertTrue(output.contains("Great! You selected MEDIUM difficulty."));
        assertTrue(output.contains("You have 5 chances."));
        assertTrue(output.contains("Too low. Try again.")); // For 10
        assertTrue(output.contains("You have 4 chances left."));
        assertTrue(output.contains("Invalid input. Please enter a valid number.")); // From promptGuess
        assertTrue(output.contains("No valid guess provided. Ending round.")); // From playRound
        // Game should indicate loss because no correct guess was made
        assertTrue(output.contains("Sorry! You've used all your chances.") || output.contains("The correct number was: " + targetNumber));
    }
}
