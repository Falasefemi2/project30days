package com.femiproject.tasktracker;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

public class TaskManagerTest {

    @Test
    void testDeleteTask() {
        TaskManager taskManager = new TaskManager();
        Task task = new Task("Buy game console", StatusEnum.IN_PROGRESS);
        taskManager.addTask(task);
        String taskId = task.getId();

        taskManager.deleteTask(taskId);

        Task result = taskManager.findTaskByID(taskId);
        assertNull(result, "Task should be null after deletion!!");
    }

    @Test
    void testFindTaskByID() {
        TaskManager taskManager = new TaskManager();
        Task task = new Task("Buy game console", StatusEnum.IN_PROGRESS);
        taskManager.addTask(task);
        String taskId = task.getId();

        Task result = taskManager.findTaskByID(taskId);

        assertNotNull(result, "Task should not be null");
        assertEquals(taskId, result.getId(), "TASK ID should match");
        assertEquals("Buy game console", result.getDescription(), "Description should match");
    }

    @Test
    void testListAllTask() {
        TaskManager taskManager = new TaskManager();

        Task task1 = new Task("Write report", StatusEnum.TODO);
        Task task2 = new Task("Submit form", StatusEnum.IN_PROGRESS);

        taskManager.addTask(task1);
        taskManager.addTask(task2);

        List<Task> allTasks = taskManager.getAllTasks();

        Assertions.assertTrue(allTasks.contains(task1));
        Assertions.assertTrue(allTasks.contains(task2));
    }

    @Test
    void testListTaskByStatus() {
        TaskManager taskManager = new TaskManager();

        Task task1 = new Task("Task 99", StatusEnum.TODO);
        Task task2 = new Task("Task 90", StatusEnum.DONE);
        Task task3 = new Task("Task 100", StatusEnum.TODO);

        taskManager.addTask(task1);
        taskManager.addTask(task2);
        taskManager.addTask(task3);

        List<Task> todoTasks = taskManager.getTasksByStatus(StatusEnum.TODO);

        Assertions.assertTrue(todoTasks.contains(task1));
        Assertions.assertTrue(todoTasks.contains(task3));
    }

    @Test
    void testMarkTask() {
        TaskManager taskManager = new TaskManager();
        Task task = new Task("Read a book", StatusEnum.TODO);
        taskManager.addTask(task);

        boolean result = taskManager.markTask(StatusEnum.DONE, task.getId());

        assertTrue(result);
        assertEquals(StatusEnum.DONE, task.getStatus());
    }

    @Test
    void testMarkTaskWithInvalidId() {
        TaskManager taskManager = new TaskManager();

        boolean result = taskManager.markTask(StatusEnum.DONE, "invalid-id");

        assertFalse(result);
    }

    @Test
    void testUpdateTask() {
        TaskManager taskManager = new TaskManager();
        Task task = new Task("Clean room", StatusEnum.TODO);
        taskManager.addTask(task);

        boolean result = taskManager.updateTask(task.getId(), "Clean the entire apartment");

        assertTrue(result);
        assertEquals("Clean the entire apartment", task.getDescription());
    }

    @Test
    void testUpdateTaskWithInvalidId() {
        TaskManager taskManager = new TaskManager();

        boolean result = taskManager.updateTask("non-existent-id", "Anything");

        assertFalse(result);
    }

}
