package com.femiproject.todolist;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import java.time.format.DateTimeFormatter;

public class TaskManager {

    private List<Task> tasks;
    private final String filePath = "tasks.json";
    private final ObjectMapper objectMapper;

    public TaskManager() {
        tasks = new ArrayList<>();
        objectMapper = new ObjectMapper();
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addSerializer(LocalDateTime.class,
                new LocalDateTimeSerializer(DateTimeFormatter.ISO_DATE_TIME));
        objectMapper.registerModule(javaTimeModule);
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        loadTasksFromFile();
    }

    private void loadTasksFromFile() {
        File file = new File(filePath);
        if (file.exists()) {
            try {
                tasks = objectMapper.readValue(file, objectMapper.getTypeFactory()
                        .constructCollectionType(List.class, Task.class));
            } catch (IOException e) {
                System.out.println("Error loading tasks: " + e.getMessage());
                tasks = new ArrayList<>();
            }
        }
    }

    private void saveTasksToFile() {
        try {
            objectMapper.writeValue(new File(filePath), tasks);
        } catch (IOException e) {
            System.out.println("Error saving tasks: " + e.getMessage());
        }
    }

    public Task findTaskById(String id) {
        return tasks.stream()
                .filter(t -> t.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public void addTask(String description, Priority priority, LocalDate deadline, String status) {
        String id = UUID.randomUUID().toString();
        LocalDateTime deadlineDateTime = LocalDateTime.of(deadline, LocalTime.of(0, 0, 0));
        tasks.add(new Task(id, description, priority, deadlineDateTime, status));
        saveTasksToFile();
    }

    public boolean updateTask(Task t) {
        Task task = findTaskById(t.getId());

        if (task == null) {
            return false;
        }

        if (t.getDescription() != null) {
            task.setDescription(t.getDescription());
        }
        task.setDeadline(t.getDeadline());
        task.setPriority(t.getPriority());
        task.setStatus(t.getStatus());
        saveTasksToFile();
        return true;
    }

    public boolean deleteTask(String id) {
        Task task = findTaskById(id);

        if (task != null) {
            tasks.remove(task);
            saveTasksToFile();
            return true;
        }
        return false;
    }

    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks);
    }

    public List<Task> getTaskSortedByPriority() {
        return tasks.stream()
                .sorted((t1, t2) -> t1.getPriority().compareTo(t2.getPriority()))
                .toList();
    }

    public List<Task> getTasksSortedByDeadline() {
        return tasks.stream()
                .sorted((t1, t2) -> t1.getDeadline().compareTo(t2.getDeadline()))
                .toList();
    }

}
