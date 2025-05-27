# Task Tracker

Task tracker is a project used to track and manage your tasks. In this task, you will build a simple command line interface (CLI) to track **what you need to do**, **what you have done**, and **what you are currently working on**. This project will help me practice my programming skills, including working with the filesystem, handling user inputs, and building a simple CLI application.

## Requirements

The application should run from the command line, accept user actions and inputs as arguments, and store the tasks in a JSON file. The user should be able to:

- Add, Update, and Delete tasks
- Mark a task as in progress or done
- List all tasks
- List all tasks that are done
- List all tasks that are not done
- List all tasks that are in progress

## Example

1. **Adding a new task**

- Buy groceries

2. **Updating and deleting tasks**

- update 1 "Buy groceries and cook dinner"
- delete 1
  3 **Marking a task as in progress or done**
- mark-in-progress 1
- mark-done 1
  4 **Listing all tasks**
- list
  5 **Listing tasks by status**
- list done
- list todo
- list in-progress

## Task Properties

Each task has the following properties:

- id: A unique identifier for the task
- description: A short description of the task
- status: The status of the task (todo, in-progress, done)
- createdAt: The date and time when the task was created
- updatedAt: The date and time when the task was last updated
