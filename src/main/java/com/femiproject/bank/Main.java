package com.femiproject.bank;

import java.util.Scanner;

public class Main {

    private final BankSystem bankSystem;
    private final Scanner scanner;
    private User currentUser;
    private boolean running = true;

    public Main() {
        this.bankSystem = new BankSystem();
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        while (running) {
            if (currentUser == null) {
                showWelcomeMenu();
            } else {
                showBankingMenu();
            }
        }
    }

    public int getIntInput() {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public double getDoubleInput() {
        try {
            return Double.parseDouble(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public void promptCreateAccount() {
        System.out.println("\n🧾 Create a New Account");

        System.out.print("👤 Enter username: ");
        String username = scanner.nextLine();

        System.out.print("🔑 Enter password: ");
        String password = scanner.nextLine();

        bankSystem.createUser(username, password);
    }

    public void promptLogin() {
        System.out.println("\n🔐 Log in to your account");

        System.out.print("👤 Username: ");
        String username = scanner.nextLine();

        System.out.print("🔑 Password: ");
        String password = scanner.nextLine();

        User user = bankSystem.authUser(username, password);
        if (user != null) {
            currentUser = user;
            System.out.println("✅ Login successful. Welcome, " + currentUser.getUsername() + "!");
        } else {
            System.out.println("❌ Invalid username or password.");
        }
    }

    public void showWelcomeMenu() {
        System.out.println("""
                
                📟 === Welcome to CLI Bank ===
                1️⃣  Create Account
                2️⃣  Login
                3️⃣  Exit
                """);

        System.out.print("➡️  Choose an option: ");
        int choice = getIntInput();
        switch (choice) {
            case 1 ->
                promptCreateAccount();
            case 2 ->
                promptLogin();
            case 3 -> {
                System.out.println("👋 Thanks for banking with us. Goodbye!");
                running = false;
            }
            default ->
                System.out.println("⚠️  Invalid option. Please try again.");
        }
    }

    public void deposit() {
        System.out.println("\n💰 Deposit Funds");

        System.out.print("Enter amount to deposit: ");
        double amount = getDoubleInput();

        bankSystem.deposit(currentUser.getUsername(), amount);
    }

    public void withdraw() {
        System.out.println("\n🏧 Withdraw Funds");

        System.out.print("Enter amount to withdraw: ");
        double amount = getDoubleInput();

        bankSystem.withdraw(currentUser.getUsername(), amount);
    }

    public void transfer() {
        System.out.println("\n🔁 Transfer Funds");

        System.out.print("Enter receiver's username: ");
        String receiver = scanner.nextLine();

        System.out.print("Enter amount to transfer: ");
        double amount = getDoubleInput();

        bankSystem.transfer(currentUser.getUsername(), receiver, amount);
    }

    public void viewBalance() {
        System.out.println("\n📄 Account Balance");
        bankSystem.viewBalance(currentUser.getUsername());
    }

    public void transactionHistory() {
        System.out.println("\n📜 Transaction History");
        bankSystem.displayTransactionHistory(currentUser.getUsername());
    }

    public void logout() {
        System.out.println("\n🚪 Logging out...");
        currentUser = null;
    }

    public void showBankingMenu() {
        System.out.println("""
                
                🏦 === Banking Menu ===
                1️⃣  Deposit
                2️⃣  Withdraw
                3️⃣  Transfer
                4️⃣  View Balance
                5️⃣  Transaction History
                6️⃣  Logout
                """);

        System.out.print("➡️  Choose an option: ");
        int choice = getIntInput();

        switch (choice) {
            case 1 ->
                deposit();
            case 2 ->
                withdraw();
            case 3 ->
                transfer();
            case 4 ->
                viewBalance();
            case 5 ->
                transactionHistory();
            case 6 ->
                logout();
            default ->
                System.out.println("⚠️  Invalid option. Please select from 1–6.");
        }
    }

    public static void main(String[] args) {
        new Main().start();
    }
}
