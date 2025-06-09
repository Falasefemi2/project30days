package com.femiproject.passwordgenerator;

import java.util.Scanner;

public class PasswordGenerator {

    public Alphabet alphabet;
    public Scanner scanner;
    private final java.util.Random random = new java.util.Random();

    public PasswordGenerator(Scanner scanner) {
        this.scanner = scanner;
    }

    public PasswordGenerator(boolean upper, boolean lower, boolean numbers, boolean symbols) {
        alphabet = new Alphabet(upper, lower, numbers, symbols);
    }

    private void printMenu() {
        System.out.println();
        System.out.println("Enter 1 - Password Generator");
        System.out.println("Enter 2 - Password Strength Check");
        System.out.println("Enter 3 - Useful Information");
        System.out.println("Enter 4 - Quit");
        System.out.println("Choice:");
    }

    private void exitMessage() {
        System.out.println("Thanks for trying our product");
    }

    private void checkPassword() {
        String input;

        System.out.println("Enter password: ");

        input = scanner.nextLine().trim();

        Password password = new Password(input);
        System.out.println(password.calculatePassword());
    }

    private void errorMessage(String errorString) {
        if (!errorString.equalsIgnoreCase("yes") && !errorString.equalsIgnoreCase("no")) {
            System.out.println("You have entered something incorrect let's go over it again \n");
        }
    }

    private boolean isInclude(String input) {
        return input.equalsIgnoreCase("yes");
    }

    private boolean askYesNo(String prompt) {
        String input;

        do {
            System.out.println(prompt);
            input = scanner.nextLine().trim();
            errorMessage(input);
        } while (!input.equalsIgnoreCase("yes") && !input.equalsIgnoreCase("no"));
        return isInclude(input);
    }

    private Password generatePassword(int length) {
        final StringBuilder pass = new StringBuilder("");
        final int alphabetLength = alphabet.poolString().length();

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(alphabetLength);
            pass.append(alphabet.poolString().charAt(index));
        }
        return new Password(pass.toString());
    }

    private void requestPassword() {
        boolean includeUpper, includeLower, includeNumbers, includeSymbols;

        System.out.println("\nHello, welcome to Password Generator: ");
        System.out.println("Answer the following questions by yes or no:\n");

        while (true) {
            includeUpper = askYesNo("Do you want Uppercase letters \"ABCD...\" to be used?");
            includeLower = askYesNo("Do you want Lowercase letters \"abcd...\" to be used?");
            includeNumbers = askYesNo("Do you want Numbers \"1234...\" to be used?");
            includeSymbols = askYesNo("Do you want Symbols \"!@#$...\" to be used?");

            if (includeUpper || includeLower || includeNumbers || includeSymbols) {
                break;
            }

            System.out.println("\nYou must select at least one character type. Please try again.\n");
        }

        System.out.print("\nEnter desired password length: ");
        int length = scanner.nextInt();
        scanner.nextLine();

        PasswordGenerator passGenerator = new PasswordGenerator(includeUpper, includeLower, includeNumbers,
                includeSymbols);
        Password password = passGenerator.generatePassword(length);

        System.out.println("Your generated password -> " + password);
    }

    private void printUsefulInfo() {
        System.out.println();
        System.out.println("Use a minimum password length of 8 or more characters if permitted");
        System.out.println("Include lowercase and uppercase alphabetic characters, numbers and symbols if permitted");
        System.out.println("Generate passwords randomly where feasible");
        System.out.println(
                "Avoid using the same password twice (e.g., across multiple user accounts and/or software systems)");
        System.out.println(
                """
                        Avoid character repetition, keyboard patterns, dictionary words, letter or number sequences,
                        usernames, relative or pet names, romantic links (current or past) and biographical information (e.g., ID numbers, ancestors' names or dates).""");
        System.out.println("Avoid using information that the user's colleagues and/or "
                + "acquaintances might know to be associated with the user");
        System.out.println(
                "Do not use passwords which consist wholly of any simple combination of the aforementioned weak components");
    }

    public void mainLoop() {
        System.out.println("Welcome to femi password generator: ");
        printMenu();

        String userOption = "1";

        while (!userOption.equals("4")) {

            userOption = scanner.nextLine().trim();

            switch (userOption) {
                case "1" -> {
                    requestPassword();
                    printMenu();
                }
                case "2" -> {
                    checkPassword();
                    printMenu();
                }
                case "3" -> {
                    printUsefulInfo();
                    printMenu();
                }
                case "4" -> {
                    exitMessage();
                }
                default -> {
                    System.out.println();
                    System.out.println("Kindly select one of the available commands");
                    printMenu();
                }
            }
        }
    }
}
