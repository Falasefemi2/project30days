package com.femiproject.tasktracker;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class TaskManager {

    private List<Task> tasks;

    @JsonIgnore
    private final ObjectMapper objectMapper;
    private final String TASK_FILE = "task.json";

    public TaskManager() {
        this.tasks = new ArrayList<>();
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        loadTasks();
    }

    public void addTask(Task task) {
        tasks.add(task);
        saveTasks();
        System.out.println("Task added successfully (ID: " + task.getId() + ") ");
    }

    public Task findTaskByID(String id) {
        return tasks.stream()
                .filter(t -> t.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public boolean updateTask(String id, String description) {
        Task task = findTaskByID(id);
        if (task == null) {
            return false;
        }
        task.setDescription(description);
        saveTasks();
        return true;
    }

    public void deleteTask(String id) {
        Task task = findTaskByID(id);
        if (task == null) {
            System.out.println("Task not found.");
            return;
        }
        tasks.remove(task);
        saveTasks();
        System.out.println("Task deleted successfully.");
    }

    public boolean markTask(StatusEnum statusEnum, String id) {
        Task task = findTaskByID(id);
        if (task == null) {
            return false;
        }
        task.setStatus(statusEnum);
        saveTasks();
        return true;
    }

    public void listAllTask() {
        for (Task t : tasks) {
            if (t.getCreatedAt() == null || t.getUpdatedAt() == null) {
                System.out.println("Task not found.");
                continue;
            }
            System.out.println(t);
        }
    }

    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks);
    }

    public List<Task> getTasksByStatus(StatusEnum status) {
        return tasks.stream()
                .filter(t -> t.getStatus().equals(status))
                .collect(Collectors.toList());
    }

    public void listTaskByStatus(StatusEnum status) {
        List<Task> tasksByStatus = getTasksByStatus(status);
        if (tasksByStatus.isEmpty()) {
            System.out.println("No task found with status: " + status.name());
        } else {
            tasksByStatus.forEach(System.out::println);
        }
    }

    private void loadTasks() {
        try {
            File file = new File(TASK_FILE);
            if (file.exists() && file.length() > 0) {
                System.out.println("Loading existing tasks from file...");
                tasks = objectMapper.readValue(file, new TypeReference<List<Task>>() {
                });
                System.out.println("Loaded " + tasks.size() + " tasks.");
            }
        } catch (IOException e) {
            System.err.println("Error loading task: " + e.getMessage());
        }
    }

    private void saveTasks() {
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(TASK_FILE), tasks);
        } catch (IOException e) {
            System.out.println("Error saving task: " + e.getMessage());
        }
    }
}
