package com.femiproject.todolist;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Task {

    @JsonProperty("id")
    private String id;

    @JsonProperty("description")
    private String description;

    @JsonProperty("priority")
    private Priority priority;

    @JsonProperty("deadline")
    private LocalDateTime deadline;

    @JsonProperty("status")
    private String status;

    public Task(String id, String description, Priority priority, LocalDateTime deadline, String status) {
        this.id = id;
        this.description = description;
        this.priority = priority;
        this.deadline = deadline;
        this.status = status;
    }

    public Task() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public LocalDateTime getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDateTime deadline) {
        this.deadline = deadline;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Task [id=" + id + ", description=" + description + ", priority=" + priority + ", deadline=" + deadline
                + ", status=" + status + "]";
    }
}
