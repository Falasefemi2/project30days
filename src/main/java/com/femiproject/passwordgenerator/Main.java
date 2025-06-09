package com.femiproject.passwordgenerator;

import java.util.Scanner;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        try (scanner) {
            PasswordGenerator pg = new PasswordGenerator(scanner);
            pg.mainLoop();
        }
    }
}
