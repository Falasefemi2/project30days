package com.femiproject.tasktracker;

import java.util.Scanner;

public class Main {

    private final TaskManager taskManager;
    private final Scanner scanner;

    public Main() {
        this.taskManager = new TaskManager();
        this.scanner = new Scanner(System.in);
    }

    public void run() {
        while (true) {
            System.out.println("\n=== Task Tracker Menu ===");
            System.out.println("1. Add New Task");
            System.out.println("2. Update Task");
            System.out.println("3. Delete Task");
            System.out.println("4. Mark Task Progress");
            System.out.println("5. List All Tasks");
            System.out.println("6. List Task by Status");
            System.out.println("7. Exit");
            System.out.print("Enter option: ");

            int choice = getIntInput();

            switch (choice) {
                case 1 ->
                    addTask();
                case 2 ->
                    updateTask();
                case 3 ->
                    deleteTask();
                case 4 ->
                    markTask();
                case 5 ->
                    listAllTask();
                case 6 ->
                    listTaskByStatus();
                case 7 -> {
                    System.out.println("Exiting...");
                    return; // or System.exit(0);
                }
                default ->
                    System.out.println("Invalid option. Try again.");
            }
        }
    }

    public void addTask() {
        System.out.println("Enter description: ");
        String description = scanner.nextLine();

        taskManager.addTask(new Task(description, StatusEnum.IN_PROGRESS));
    }

    public void updateTask() {
        System.out.println("Enter ID: ");
        String id = scanner.nextLine();
        System.out.println("Enter new desciption: ");
        String description = scanner.nextLine();

        taskManager.updateTask(id, description);
    }

    public void deleteTask() {
        System.out.println("Enter ID: ");
        String id = scanner.nextLine();

        taskManager.deleteTask(id);
    }

    public void markTask() {
        System.out.println("Enter ID: ");
        String id = scanner.nextLine();
        System.out.println("Enter status (e.g., TODO, IN_PROGRESS, DONE):");
        String statusInput = scanner.nextLine();

        try {
            StatusEnum status = StatusEnum.valueOf(statusInput.toUpperCase());
            taskManager.markTask(status, id);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid status. Please use TODO, IN_PROGRESS, or DONE.");
        }
    }

    public void listAllTask() {
        taskManager.listAllTask();
    }

    public void listTaskByStatus() {
        System.out.println("Enter status (e.g., TODO, IN_PROGRESS, DONE):");
        String statusInput = scanner.nextLine();

        try {
            StatusEnum status = StatusEnum.valueOf(statusInput.toUpperCase());
            taskManager.listTaskByStatus(status);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid status. Please use TODO, IN_PROGRESS, or DONE.");
        }
    }

    public int getIntInput() {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public static void main(String[] args) {
        new Main().run();
    }
}
