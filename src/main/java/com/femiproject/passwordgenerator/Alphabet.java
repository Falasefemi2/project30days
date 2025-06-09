package com.femiproject.passwordgenerator;

public class Alphabet {

    private static final String UPPERCASE = "ABCDEFJHIJKLMNOPQRSTUVWXYZ";
    private static final String LOWERCASE = "abcdefghijklmnopqrstuvwxyz";
    private static final String NUMBERS = "1234567890";
    private static final String SYMBOLS = "!@#$%^&*()_-=+\\/`?";

    private final StringBuilder pool;

    public Alphabet(boolean upper, boolean lower, boolean numbers, boolean symbols) {
        pool = new StringBuilder();

        if (upper) {
            pool.append(UPPERCASE);
        }

        if (lower) {
            pool.append(LOWERCASE);
        }

        if (numbers) {
            pool.append(NUMBERS);
        }

        if (numbers) {
            pool.append(SYMBOLS);
        }
    }

    public String poolString() {
        return pool.toString();
    }
}
