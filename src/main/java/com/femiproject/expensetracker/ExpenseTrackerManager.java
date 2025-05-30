package com.femiproject.expensetracker;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ExpenseTrackerManager {

    private static final Logger LOGGER = Logger.getLogger(ExpenseTrackerManager.class.getName());
    private List<ExpenseTracker> expenseTrackers;
    private static final String DATA_FILE = "expenses.csv";

    public ExpenseTrackerManager() throws CsvValidationException, NumberFormatException {
        this.expenseTrackers = new ArrayList<>();
        loadFromCSV();
    }

    public ExpenseTracker findTrackerById(int id) {
        return expenseTrackers.stream()
                .filter(u -> u.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public void addExpenseTracker(ExpenseTracker tracker) throws IOException {
        expenseTrackers.add(tracker);
        System.out.println("Expense added successfully");
        saveToCSV();
    }

    public void updateExpenseTracker(int id, String description, double amount) throws IOException {
        ExpenseTracker expenseTracker = findTrackerById(id);
        if (expenseTracker == null) {
            System.out.println("Tracker id not found");
            return;
        }
        expenseTracker.updateExpense(description, amount);
        saveToCSV();
    }

    public void deleteTracker(int id) throws IOException {
        ExpenseTracker expenseTracker = findTrackerById(id);
        if (expenseTracker == null) {
            System.out.println("Tracker id not found");
            return;
        }

        expenseTrackers.remove(expenseTracker);
        System.out.println("Tracker deleted successfully");
        saveToCSV();
    }

    public void viewAllExpenseTrackers() {
        for (ExpenseTracker expenseTracker : expenseTrackers) {
            System.out.println(expenseTracker);
        }
    }

    public double getTotalExpense() {
        return expenseTrackers.stream()
                .mapToDouble(ExpenseTracker::getAmount)
                .sum();
    }

    public void showMonthlySummary(int month) {
        double total = expenseTrackers.stream()
                .filter(e -> e.getDate().getMonthValue() == month && e.getDate().getYear() == LocalDate.now().getYear())
                .mapToDouble(ExpenseTracker::getAmount)
                .sum();
        System.out.println("Total expense for month " + month + ": " + total);
    }

    public void showYearlySummary(int year) {
        double total = expenseTrackers.stream()
                .filter(e -> e.getDate().getYear() == year)
                .mapToDouble(ExpenseTracker::getAmount)
                .sum();
        System.out.println("Total expense for year " + year + ": " + total);
    }

    private void saveToCSV() throws IOException {
        try (CSVWriter writer = new CSVWriter(new FileWriter(DATA_FILE))) {
            String[] header = {"ID", "DESCRIPTION", "AMOUNT", "DATE"};
            writer.writeNext(header);

            for (ExpenseTracker expense : expenseTrackers) {
                String[] record = {
                    String.valueOf(expense.getId()),
                    expense.getDescription(),
                    String.valueOf(expense.getAmount()),
                    expense.getDate().format(DateTimeFormatter.ISO_DATE)
                };
                writer.writeNext(record);
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error saving to CSV", e);
            throw e;
        }
    }

    private void loadFromCSV() throws CsvValidationException, NumberFormatException {
        expenseTrackers = new ArrayList<>(); // Clear old list first

        try (CSVReader reader = new CSVReader(new FileReader(DATA_FILE))) {
            String[] nextLine;
            boolean isHeader = true;

            while ((nextLine = reader.readNext()) != null) {
                if (isHeader) {
                    isHeader = false; // Skip header
                    continue;
                }

                int id = Integer.parseInt(nextLine[0].trim().replaceAll("\"", ""));
                String description = nextLine[1].trim().replaceAll("\"", "");
                double amount = Double.parseDouble(nextLine[2].trim().replaceAll("\"", ""));
                LocalDate date = LocalDate.parse(nextLine[3].trim().replaceAll("\"", ""));

                ExpenseTracker tracker = new ExpenseTracker(id, description, amount, date);
                expenseTrackers.add(tracker);
            }

            System.out.println("Expenses loaded from file.");
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "CSV file not found or error reading it", e);
        }
    }

}
