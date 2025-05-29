package com.femiproject.githubuser;

import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        if (args.length == 0) {
            System.err.println("Usage: github-activity <username>");
            return;
        }

        String username = args[0];

        GithubActivity githubActivity = new GithubActivity();

        try {
            githubActivity.fetchAndDisplayActivity(username);
        } catch (IOException | InterruptedException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
