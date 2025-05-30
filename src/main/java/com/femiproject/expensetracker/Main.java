package com.femiproject.expensetracker;

import java.io.IOException;
import java.util.Scanner;

import com.opencsv.exceptions.CsvValidationException;

public class Main {

    private ExpenseTrackerManager tManager;
    private Scanner scanner;

    public Main() throws CsvValidationException, NumberFormatException {
        this.tManager = new ExpenseTrackerManager();
        this.scanner = new Scanner(System.in);
    }

    public void run() throws IOException {
        while (true) {
            System.out.println("Welcome to your expense tracker");
            System.out.println("1. Add an expense");
            System.out.println("2. Update expense");
            System.out.println("3. Delete expense");
            System.out.println("4. View all expense");
            System.out.println("5. Total expense");
            System.out.println("6. View summary by month");
            System.out.println("7. View summary by year");
            System.out.println("8. Exit");

            System.out.println("Choose an option: ");

            int choice = getIntInput();

            switch (choice) {
                case 1 ->
                    addTracker();
                case 2 ->
                    updateTracker();
                case 3 ->
                    deleteTracker();
                case 4 ->
                    viewAllExpense();
                case 5 ->
                    totalExpense();
                case 6 ->
                    viewMonthlySummary();
                case 7 ->
                    viewYearlySummary();
                case 8 -> {
                    System.exit(0);
                }
                default -> {
                    System.out.println("Invalid options");
                }
            }
        }
    }

    public void addTracker() throws IOException {
        System.out.println("Enter description: ");
        String description = scanner.nextLine();
        System.out.println("Enter amount: ");
        double amount = getDoubleInput();

        tManager.addExpenseTracker(new ExpenseTracker(description, amount));
    }

    public void updateTracker() throws IOException {
        System.out.println("Enter ID: ");
        int id = scanner.nextInt();

        scanner.nextLine();
        System.out.println("Enter description: ");
        String description = scanner.nextLine();

        System.out.println("Enter amount: ");
        double amount = getDoubleInput();

        tManager.updateExpenseTracker(id, description, amount);
    }

    public void deleteTracker() throws IOException {
        System.out.println("Enter ID: ");
        int id = scanner.nextInt();

        tManager.deleteTracker(id);
    }

    public void viewAllExpense() {
        tManager.viewAllExpenseTrackers();
    }

    public void totalExpense() {
        System.out.println("Total expense: " + tManager.getTotalExpense());
    }

    public void viewMonthlySummary() {
        System.out.println("Enter month (1-12): ");
        int month = getIntInput();
        tManager.showMonthlySummary(month);
    }

    public void viewYearlySummary() {
        System.out.println("Enter year: ");
        int year = getIntInput();
        tManager.showYearlySummary(year);
    }

    public int getIntInput() {
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid number. Try again: ");
            }
        }
    }

    public double getDoubleInput() {
        try {
            return Double.parseDouble(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public static void main(String[] args) throws CsvValidationException, NumberFormatException, IOException {
        new Main().run();
    }
}
