package com.femiproject.githubuser;

import static org.junit.jupiter.api.Assertions.*;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MainTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;

    @BeforeEach
    void setUpStreams() {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    @AfterEach
    void restoreStreams() {
        System.setOut(originalOut);
        System.setErr(originalErr);
    }

    @Test
    void testMain_NoArguments() {
        // Given
        String[] args = {};

        // When
        Main.main(args);

        // Then
        assertTrue(errContent.toString().contains("Usage: github-activity <username>"));
    }

    @Test
    void testMain_WithArgument_InvalidUser() {
        // Given
        String[] args = {"nonexistentuser123456789"};

        // When
        try {
            Main.main(args);
        } catch (RuntimeException e) {
            // TODO: handle exception
        }

        // Then
        String errorOutput = errContent.toString();
        assertTrue(errorOutput.contains("Error:") || errorOutput.contains("not found"));

    }
}
