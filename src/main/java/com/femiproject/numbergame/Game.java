package com.femiproject.numbergame;

import java.util.Random;
import java.util.Scanner;

public class Game {

    private final Scanner scanner;
    private final Random random;
    private DifficultyLevel difficultyLevel;

    public Game() {
        this.scanner = new Scanner(System.in);
        this.random = new Random();
    }

    public void start() {
        playRound();
    }

    public void playRound() {
        System.out.println("Welcome to the Number Guessing Game!");
        System.out.println("I'm thinking of a number between 1 and 100.");
        System.out.println("Please select the difficulty level: ");
        System.out.println("1. Easy (10 chances)");
        System.out.println("2. Medium (5 chances)");
        System.out.println("3. Hard (3 chances)");
        System.out.println("Enter choice: ");

        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1 ->
                difficultyLevel = DifficultyLevel.EASY;
            case 2 ->
                difficultyLevel = DifficultyLevel.MEDIUM;
            case 3 ->
                difficultyLevel = DifficultyLevel.HARD;
            default -> {
                System.out.println("Invalid choice. Defaulting to Medium difficulty.");
                difficultyLevel = DifficultyLevel.MEDIUM;
            }
        }
        System.out.println("Great! You selected " + difficultyLevel + " difficulty.");
        System.out.println("You have " + difficultyLevel.getChances() + " chances.");

        System.out.println("Let's start the game!\n");

        int targetNumber = generateTargetNumber();

        int attempts = 0;
        boolean hasWon = false;

        while (attempts < difficultyLevel.getChances()) {
            int guess = promptGuess();

            if (handleGuess(guess, targetNumber)) {
                hasWon = true;
                break;
            }

            attempts++;
            int chancesLeft = difficultyLevel.getChances() - attempts;
            if (chancesLeft > 0) {
                System.out.println("You have " + chancesLeft + " chances left.\n");
            }
        }

        if (!hasWon) {
            System.out.println("Sorry! You've used all your chances.");
            System.out.println("The correct number was: " + targetNumber);
        }

    }

    public int promptGuess() {
        int guess = -1;

        while (true) {
            System.out.print("Enter your guess (1–100): ");

            if (scanner.hasNextInt()) {
                guess = scanner.nextInt();

                scanner.nextLine();

                if (guess >= 1 && guess <= 100) {
                    return guess;
                } else {
                    System.out.println("Please enter a number between 1 and 100.");
                }
            } else {
                System.out.println("Invalid input. Please enter a valid number.");
                scanner.nextLine();
            }
        }
    }

    public boolean handleGuess(int guess, int target) {
        if (guess == target) {
            System.out.println("🎉 Correct! You guessed the number!");
            return true;
        } else if (guess < target) {
            System.out.println("Too low. Try again.");
        } else {
            System.out.println("Too high. Try again.");
        }
        return false;
    }

    public int generateTargetNumber() {
        return random.nextInt(100) + 1;
    }

}
