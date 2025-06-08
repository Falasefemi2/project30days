# TodoList App CLI

A command-line based task management tool built in Java that helps you organize and track your tasks efficiently.

## Features

- Add new tasks with descriptions, priorities, and deadlines
- Update existing tasks
- Delete tasks
- View tasks with multiple sorting options:
  - All tasks
  - Sorted by priority
  - Sorted by deadline
- Task priorities: HIGH, MEDIUM, LOW
- Deadline tracking with date format (yyyy-MM-dd)
- Task status tracking

## Requirements

- Java 17 or higher
- Maven (for building)

## Building the Project

```bash
mvn clean package
```

## Running the Application

```bash
java -jar target/todolist-1.0-SNAPSHOT.jar
```

## Usage

1. Add Task: Create a new task with description, priority, and deadline
2. Update Task: Modify existing task details
3. Delete Task: Remove a task by its ID
4. View All Tasks: Display all tasks
5. View Tasks by Priority: Sort and display tasks by priority level
6. View Tasks by Deadline: Sort and display tasks by due date
7. Exit: Close the application

## Project Structure

- `TodoListApp.java`: Main application class with CLI interface
- `TaskManager.java`: Core task management functionality
- `Task.java`: Task model class
- `Priority.java`: Priority enum for task prioritization
