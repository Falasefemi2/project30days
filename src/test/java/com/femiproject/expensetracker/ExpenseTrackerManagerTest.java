package com.femiproject.expensetracker;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.Test;

import com.opencsv.exceptions.CsvValidationException;

public class ExpenseTrackerManagerTest {

    @Test
    void testDeleteTracker() throws CsvValidationException, NumberFormatException, IOException {
        ExpenseTrackerManager manager = new ExpenseTrackerManager();
        ExpenseTracker tracker = new ExpenseTracker("Test Desc", 100);
        manager.addExpenseTracker(tracker);

        int trackerId = tracker.getId();

        manager.deleteTracker(trackerId);

        ExpenseTracker result = manager.findTrackerById(trackerId);
        assertNull(result, "Tracker should be null after deketion!!");

    }

    @Test
    void testFindTrackerById() throws CsvValidationException, NumberFormatException, IOException {
        ExpenseTrackerManager manager = new ExpenseTrackerManager();
        ExpenseTracker tracker = new ExpenseTracker("Test Desc", 100);
        manager.addExpenseTracker(tracker);

        int trackerId = tracker.getId();

        ExpenseTracker result = manager.findTrackerById(trackerId);

        assertNotNull(result, "Tracker Id should not be null");
        assertEquals(trackerId, result.getId(), "Tracker Id should be the same");
        assertEquals("Test Desc", result.getDescription(), "Description should match");

    }

    @Test
    void testGetTotalExpense() throws CsvValidationException, NumberFormatException, IOException {
        ExpenseTrackerManager manager = new ExpenseTrackerManager();
        ExpenseTracker tracker = new ExpenseTracker("Testing Desc", 1000);
        manager.addExpenseTracker(tracker);

        assertEquals(2194.88, manager.getTotalExpense());
    }

    @Test
    void testShowMonthlySummary() throws CsvValidationException, NumberFormatException, IOException {

        ExpenseTrackerManager manager = new ExpenseTrackerManager();
        ExpenseTracker tracker = new ExpenseTracker("Testing Desc", 1000);
        manager.addExpenseTracker(tracker);

        manager.showMonthlySummary(5);
        assertEquals(4194.88, manager.getTotalExpense(), 0.01);
    }

    @Test
    void testShowYearlySummary() throws CsvValidationException, NumberFormatException, IOException {
        ExpenseTrackerManager manager = new ExpenseTrackerManager();

        manager.showYearlySummary(2025);
        assertEquals(4194.88, manager.getTotalExpense(), 0.01);
    }

    @Test
    void testUpdateExpenseTracker() throws CsvValidationException, NumberFormatException, IOException {
        ExpenseTrackerManager manager = new ExpenseTrackerManager();
        ExpenseTracker tracker = new ExpenseTracker("Original Desc", 200);
        manager.addExpenseTracker(tracker);

        int trackerId = tracker.getId();
        manager.updateExpenseTracker(trackerId, "Updated Desc", 300);

        ExpenseTracker updatedTracker = manager.findTrackerById(trackerId);
        assertNotNull(updatedTracker, "Updated tracker should not be null");
        assertEquals("Updated Desc", updatedTracker.getDescription(), "Description should be updated");
        assertEquals(300, updatedTracker.getAmount(), "Amount should be updated");
    }

    @Test
    void testViewAllExpenseTrackers() throws Exception {
        ExpenseTrackerManager manager = new ExpenseTrackerManager();

        ExpenseTracker tracker1 = new ExpenseTracker("Tracker 1", 100.0);
        ExpenseTracker tracker2 = new ExpenseTracker("Tracker 2", 200.0);

        manager.addExpenseTracker(tracker1);
        manager.addExpenseTracker(tracker2);

        // Redirect System.out
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        try {
            manager.viewAllExpenseTrackers(); // prints the output
        } finally {
            System.setOut(originalOut); // reset System.out
        }
        String expectedOutput = tracker1.toSummaryString() + "\n" + tracker2.toSummaryString();
        assertEquals(expectedOutput.trim(), expectedOutput);
    }
}
