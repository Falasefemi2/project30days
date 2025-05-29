package com.femiproject.githubuser;

public class Main {

    public static void main(String[] args) {
        if (args.length == 0) {
            System.err.println("Usage: github-activity <username>");
            System.exit(1);
        }

        String username = args[0];

        GithubActivity githubActivity = new GithubActivity();

        try {
            githubActivity.fetchAndDisplayActivity(username);
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            System.exit(1);
        }
    }
}
