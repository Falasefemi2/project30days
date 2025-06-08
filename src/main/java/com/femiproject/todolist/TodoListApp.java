package com.femiproject.todolist;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

public class TodoListApp {

    private static TaskManager taskManager = new TaskManager();
    private static Scanner scanner = new Scanner(System.in);
    private static DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static void main(String[] args) {
        while (true) {
            displayMenu();
            String choice = scanner.nextLine();
            switch (choice) {
                case "1" -> addTask();
                case "2" -> updateTask();
                case "3" -> deleteTask();
                case "4" -> viewTasks("all");
                case "5" -> viewTasks("priority");
                case "6" -> viewTasks("deadline");
                case "7" -> {
                    System.out.println("Exiting...");
                    scanner.close();
                    return;
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void displayMenu() {
        System.out.println("\n=== To-Do List Manager ===");
        System.out.println("1. Add Task");
        System.out.println("2. Update Task");
        System.out.println("3. Delete Task");
        System.out.println("4. View All Tasks");
        System.out.println("5. View Tasks Sorted by Priority");
        System.out.println("6. View Tasks Sorted by Deadline");
        System.out.println("7. Exit");
        System.out.print("Enter your choice: ");
    }

    private static void addTask() {
        System.out.print("Enter task description: ");
        String description = scanner.nextLine();

        System.out.print("Enter priority (HIGH/MEDIUM/LOW): ");
        Priority priority;
        try {
            priority = Priority.valueOf(scanner.nextLine().toUpperCase());
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid priority. Using MEDIUM as default.");
            priority = Priority.MEDIUM;
        }

        System.out.print("Enter deadline (yyyy-MM-dd): ");
        LocalDate deadline;
        try {
            deadline = LocalDate.parse(scanner.nextLine(), dateFormatter);
        } catch (Exception e) {
            System.out.println("Invalid date format. Using today's date as deadline.");
            deadline = LocalDate.now();
        }

        taskManager.addTask(description, priority, deadline, "PENDING");
        System.out.println("Task added successfully!");
    }

    private static void updateTask() {
        System.out.print("Enter task ID to update: ");
        String id = scanner.nextLine();
        Task existingTask = taskManager.findTaskById(id);

        if (existingTask == null) {
            System.out.println("Task not found!");
            return;
        }

        System.out.print("Enter new description (press Enter to keep current): ");
        String description = scanner.nextLine();
        if (description.isEmpty()) {
            description = existingTask.getDescription();
        }

        System.out.print("Enter new priority (HIGH/MEDIUM/LOW) (press Enter to keep current): ");
        Priority priority = existingTask.getPriority();
        String priorityInput = scanner.nextLine();
        if (!priorityInput.isEmpty()) {
            try {
                priority = Priority.valueOf(priorityInput.toUpperCase());
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid priority. Keeping current priority.");
            }
        }

        System.out.print("Enter new deadline (yyyy-MM-dd) (press Enter to keep current): ");
        LocalDateTime deadline = existingTask.getDeadline();
        String deadlineInput = scanner.nextLine();
        if (!deadlineInput.isEmpty()) {
            try {
                LocalDate newDeadline = LocalDate.parse(deadlineInput, dateFormatter);
                deadline = LocalDateTime.of(newDeadline, LocalTime.of(0, 0));
            } catch (Exception e) {
                System.out.println("Invalid date format. Keeping current deadline.");
            }
        }

        System.out.print("Enter new status (press Enter to keep current): ");
        String status = scanner.nextLine();
        if (status.isEmpty()) {
            status = existingTask.getStatus();
        }

        Task updatedTask = new Task(id, description, priority, deadline, status);
        if (taskManager.updateTask(updatedTask)) {
            System.out.println("Task updated successfully!");
        } else {
            System.out.println("Failed to update task!");
        }
    }

    private static void deleteTask() {
        System.out.print("Enter task ID to delete: ");
        String id = scanner.nextLine();

        if (taskManager.deleteTask(id)) {
            System.out.println("Task deleted successfully!");
        } else {
            System.out.println("Task not found!");
        }
    }

    private static void viewTasks(String sortType) {
        List<Task> tasks;
        switch (sortType) {
            case "priority" -> tasks = taskManager.getTaskSortedByPriority();
            case "deadline" -> tasks = taskManager.getTasksSortedByDeadline();
            default -> tasks = taskManager.getAllTasks();
        }

        if (tasks.isEmpty()) {
            System.out.println("No tasks found!");
            return;
        }

        System.out.println("\n=== Tasks ===");
        for (Task task : tasks) {
            System.out.println(task);
        }
    }
}
