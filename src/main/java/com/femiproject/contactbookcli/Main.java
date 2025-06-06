package com.femiproject.contactbookcli;

import com.femiproject.contactbookcli.cli.ContactBookCLI;

public class Main {

    public static void main(String[] args) {
        try {
            new ContactBookCLI().start();
        } catch (Exception e) {
            System.err.println("Failed to start Contact Book CLI: " + e.getMessage());
        }
    }
}
