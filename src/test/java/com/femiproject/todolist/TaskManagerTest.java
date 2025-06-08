package com.femiproject.todolist;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TaskManagerTest {

    private TaskManager taskManager;

    @BeforeEach
    void setUp() {
        // Clean up any existing file to ensure fresh state
        File file = new File("tasks.json");
        if (file.exists()) {
            file.delete();
        }

        taskManager = new TaskManager();
    }

    @Test
    void testAddTask() {
        taskManager.addTask("New Task", Priority.HIGH, LocalDate.now(), "PENDING");

        List<Task> tasks = taskManager.getAllTasks();
        assertEquals(1, tasks.size());

        Task task = tasks.get(0);
        assertEquals("New Task", task.getDescription());
        assertEquals(Priority.HIGH, task.getPriority());
        assertEquals("PENDING", task.getStatus());
    }

    @Test
    void testDeleteTask() {
        taskManager.addTask("Task to Delete", Priority.MEDIUM, LocalDate.now(), "PENDING");
        String taskId = taskManager.getAllTasks().get(0).getId();

        boolean result = taskManager.deleteTask(taskId);
        assertTrue(result);

        List<Task> tasks = taskManager.getAllTasks();
        assertTrue(tasks.isEmpty());
    }

    @Test
    void testFindTaskById() {
        taskManager.addTask("Find Me", Priority.LOW, LocalDate.now(), "PENDING");
        Task addedTask = taskManager.getAllTasks().get(0);

        Task found = taskManager.findTaskById(addedTask.getId());
        assertNotNull(found);
        assertEquals(addedTask.getId(), found.getId());

        // Negative case: invalid ID
        assertNull(taskManager.findTaskById("nonexistent-id"));
    }

    @Test
    void testGetAllTasks() {
        taskManager.addTask("Task A", Priority.LOW, LocalDate.now(), "PENDING");
        taskManager.addTask("Task B", Priority.HIGH, LocalDate.now(), "DONE");

        List<Task> tasks = taskManager.getAllTasks();
        assertEquals(2, tasks.size());

        // Ensure they are distinct
        assertNotEquals(tasks.get(0).getId(), tasks.get(1).getId());
    }

    @Test
    void testGetTasksSortedByPriority() {
        taskManager.addTask("Low priority task", Priority.LOW, LocalDate.now(), "PENDING");
        taskManager.addTask("High priority task", Priority.HIGH, LocalDate.now(), "PENDING");
        taskManager.addTask("Medium priority task", Priority.MEDIUM, LocalDate.now(), "PENDING");

        List<Task> sortedTasks = taskManager.getTaskSortedByPriority();

        // Verify order: HIGH -> MEDIUM -> LOW
        assertEquals(Priority.HIGH, sortedTasks.get(0).getPriority());
        assertEquals(Priority.MEDIUM, sortedTasks.get(1).getPriority());
        assertEquals(Priority.LOW, sortedTasks.get(2).getPriority());
    }

    @Test
    void testGetTasksSortedByDeadline() {
        LocalDate today = LocalDate.of(2025, 6, 8);
        LocalDate tomorrow = today.plusDays(1);
        LocalDate nextWeek = today.plusDays(7);

        taskManager.addTask("Next week task", Priority.MEDIUM, nextWeek, "PENDING");
        taskManager.addTask("Tomorrow task", Priority.MEDIUM, tomorrow, "PENDING");
        taskManager.addTask("Today task", Priority.MEDIUM, today, "PENDING");

        List<Task> sortedTasks = taskManager.getTasksSortedByDeadline();

        assertEquals(today, sortedTasks.get(0).getDeadline().toLocalDate());
        assertEquals(tomorrow, sortedTasks.get(1).getDeadline().toLocalDate());
        assertEquals(nextWeek, sortedTasks.get(2).getDeadline().toLocalDate());
    }

    @Test
    void testUpdateTask() {
        LocalDate fixedDate = LocalDate.of(2025, 6, 9);

        // Add and retrieve a task
        taskManager.addTask("Original", Priority.LOW, fixedDate, "PENDING");
        Task original = taskManager.getAllTasks().get(0);

        // Set the expected updated deadline
        LocalDate expectedUpdatedDeadline = fixedDate.plusDays(1);

        // Create updated task
        Task updated = new Task(
                original.getId(),
                "Updated Description",
                Priority.HIGH,
                LocalDateTime.of(expectedUpdatedDeadline, LocalTime.of(0, 0)),
                "DONE");

        boolean success = taskManager.updateTask(updated);
        assertTrue(success);

        Task result = taskManager.findTaskById(original.getId());
        assertEquals("Updated Description", result.getDescription());
        assertEquals(Priority.HIGH, result.getPriority());
        assertEquals("DONE", result.getStatus());
        assertEquals(LocalDateTime.of(expectedUpdatedDeadline, LocalTime.of(0, 0)), result.getDeadline());
    }

}
